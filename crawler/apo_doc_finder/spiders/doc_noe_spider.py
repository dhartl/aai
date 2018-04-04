import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper

class DocNoeSpider(scrapy.Spider):
    name = "doc_noe"
    page = 1
    entry = 0
    #sex = 'f'
    textHelper = TextHelper()
    insuranceHelper = InsuranceHelper()
    geocoder = Geocoder()
    
    #def __init__(self, sex='', **kwargs):
    #    if sex:
    #        self.sex = sex
    #    super().__init__(**kwargs)

    def start_requests(self):
        yield scrapy.Request(
            url='https://sso.arztnoe.at/arztsuche/search.jsf',
            callback=self.post_request,
            dont_filter=True)

    def post_request(self, response):
        form_data = {
            'searchForm:_id9' : '',
            'searchForm:_id14' : '_IGNORE',#self.sex,
            'searchForm:_id20' : '_IGNORE',
            'searchForm:_id36' : '_IGNORE',
            'searchForm:_id42' : '_IGNORE',
            'searchForm:_id48' : 'true',#'_IGNORE',
            'searchForm:_id53' : '_IGNORE',
            'searchForm:startSearch2' : 'Suche starten',
            'searchForm_SUBMIT' : '1',
            'autoScroll' : '0,0',
            'searchForm:_link_hidden_' : '',
        }

        yield scrapy.FormRequest.from_response(
            response,
            formdata=form_data,
            callback=self.parse,
            dont_click = True,
            dont_filter=True
        )

    def parse(self, response):
        form_data = {
            '_id1_SUBMIT' : '1',
            'autoScroll' : '0,0',
            '_id1:queryResultDataTable:queryResultDataScroller' : 'idx' + str(self.page),
            '_id1:_link_hidden_' : '_id1:queryResultDataTable:queryResultDataScrolleridx' + str(self.page)
        }

        yield scrapy.FormRequest.from_response(
            response,
            formdata=form_data,
            callback=self.parsePages,
            dont_click = True,
            dont_filter=True
        )
    
    def parsePages(self, response):
        next = ((self.page - 1) * 20) + self.entry

        max = 0
        maxRaw = response.xpath('//span[@class="dataScroller-headerText"]').css('span::text').extract_first()
        if maxRaw:
            splits = maxRaw.split(' ')
            try:
                max = int(splits[len(splits) - 1].replace('.', ''))
            except ValueError:
                pass

        if next >= (max):
            return

        form_data = {
            '_id1_SUBMIT' : '1',
            'autoScroll' : '0,0',
            '_id1:queryResultDataTable:queryResultDataScroller' : '',
            '_id1:_link_hidden_' : '_id1:queryResultDataTable_' + str(next) + ':displayQueryResultDocument'
        }

        yield scrapy.FormRequest.from_response(
            response,
            formdata=form_data,
            callback=self.parseDetail,
            dont_click = True,
            dont_filter=True
        )

    def parseDetail(self, response):
        header = str(response.xpath('//div[@class="pageHeader"]').css('div::text').extract_first())
        if 'Suchergebnisse' not in header and 'Ärztesuche' not in header:
            title = header

            details = response.xpath('//div[@class="queryResult"]')

            fields = details.xpath('./div[@class="fields container"]/*')
            
            generalEmail = None
            generaltTelNr = None
            generalWebsite = None
            generalSpecialities = []
            for x in range(0, len(fields)):
                f = fields[x]
                colHeader = f.css('div::text').extract_first()
                if colHeader:
                    if 'Kontakt' in colHeader:
                        contact = fields[x + 1].xpath('./div/div')
                        generaltTelNr, generalEmail, generalWebsite = self.getContactInformation(contact)
                    if 'Berufsbezeichnung' in colHeader:
                        specialitiesList = fields[x + 1].xpath('./li')
                        for li in specialitiesList:
                            generalSpecialities.append(li.css('li::text').extract_first())

            services = details.xpath('./div[@class="serviceDomain container"]')

            for service in services:
                hours = []
                openingHours = service.xpath('./div[@class="openingHours"]/ul/li')
                if openingHours:
                    hours = self.getHours(openingHours)
                
                telNr = None
                email = None
                website = None
                contactDivs = service.xpath('./div[2]/div[@class="contact"]/div')
                if contactDivs:
                    telNr, email, website = self.getContactInformation(contactDivs)
                
                if not telNr:
                    telNr = generaltTelNr
                if not email:
                    email = generalEmail
                if not website:
                    website = generalWebsite
                    
                specialities = []
                specialitiesDiv = service.xpath('./div[2]/div[@class="serviceDomainFields"]')
                if specialitiesDiv:
                    speciality = specialitiesDiv.css('div::text').extract_first().split(':')
                    if speciality[1].strip():
                        speciality = self.textHelper.removeUnneccessarySpacesAndNewLines(speciality[1])
                        specialities.append(speciality)
                    else:
                        specialityList = specialitiesDiv.xpath('./ul/li')
                        if specialityList:
                            for li in specialityList:
                                speciality = li.css('li::text').extract_first()
                                if speciality not in specialities:
                                    speciality = self.textHelper.removeUnneccessarySpacesAndNewLines(speciality)
                                    specialities.append(speciality)

                if not specialities:
                    specialities = generalSpecialities        
                
                insurances = []
                insurancesList = service.xpath('./div[2]/div[@class="insurances"]/ul/li')
                if insurancesList:
                    for li in insurancesList:
                        text = li.css('li::text').extract_first()
                        if text:
                            insurance = self.insuranceHelper.getInsuranceByCode(text.split()[0])
                            if insurance and insurance.name not in insurances:
                                insurances.append(insurance.name)

                if len(insurances) == 0:
                    insurances.append(Insurance['WA'].name)

                street = None
                zipCode = None
                city = None
                state = 'Niederösterreich'
                addressDivs = service.xpath('./div[2]/div[@class="address"]/div')
                if addressDivs:
                    firstLine = addressDivs[0].css('div::text').extract_first()
                    zipCode = firstLine.split()[0].split('-')[1]
                    city = ' '.join(firstLine.split()[1:])
                    street = addressDivs[1].css('div::text').extract_first()

                lat, lng = self.geocoder.getLatLng(street, zipCode, city, state)

                item = Details(
                    title = title,
                    street = street,
                    zipCode = zipCode,
                    city = city,
                    state = state,
                    geoLat = lat,
                    geoLon = lng,
                    telephoneNumber = telNr,
                    email = email,
                    url = website,
                    specialities = specialities,
                    hours = hours,
                    srcUrl = response.url,
                    insurances = insurances
                )

                yield item

        form_data = {
            '_id1_SUBMIT' : '1',
            'autoScroll' : '0,0',
            '_id1:resultList' : 'Zurück zur Ergebnisliste',
            '_id1:_link_hidden_' : ''
        }

        self.entry += 1
        if self.entry == 20:
            self.entry = 0
            self.page += 1

        yield scrapy.FormRequest(
            url='https://sso.arztnoe.at/arztsuche/displayQueryResult.jsf',
            formdata=form_data,
            callback=self.parse,
            dont_filter=True)

    def getHours(self, lis):
        hours = []
        for li in lis:
            try:
                rawText = li.css('li::text').extract_first().replace(u'\xa0', ' ')
                text = self.textHelper.removeUnneccessarySpacesAndNewLines(rawText)
                day = text.split(' ')[0]
                times = ''.join(text.split(' ')[1:]).split('und')
                wd = Weekdays(day)
            except ValueError:
                continue

            for time in times:
                hour = Hours(
                    weekday = wd.name,
                    from_time = time.split('-')[0],
                    to_time = time.split('-')[1]
                )
                if hour not in hours:
                    hours.append(hour)

        return hours

    def getContactInformation(self, contactDivs):
        email = None
        telNr = None
        website = None
        if contactDivs:
            for c in contactDivs:
                textRaw = ' '.join(c.css('div::text').extract())
                text = self.textHelper.removeUnneccessarySpacesAndNewLines(textRaw)
                if 'Mail' in text:
                    emailRaw = c.xpath('./a')
                    if emailRaw is not None:
                        email = emailRaw.css('a::text').extract_first()
                if 'Tel' in text:
                    telNr = ''.join(text.split(' ')[1:])
                if 'Mobil' in text and telNr is None:
                    telNr = ''.join(text.split(' ')[1:])
                if 'Web' in text:
                    websiteRaw = c.xpath('./a')
                    if websiteRaw is not None:
                        website = websiteRaw.css('a::text').extract_first()
        
        return telNr, email, website