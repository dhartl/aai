import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper


class DocWienSpider(scrapy.Spider):
    alreadyKnownItems = []
    name = "doc_wien"
    textHelper = TextHelper()
    insuranceHelper = InsuranceHelper()
    geocoder = Geocoder()
    cookies = None
    sex = 'W'
    allInsurances = ['GKK', 'BVA', 'KFA', 'SVA', 'VAEB']

    def __init__(self, sex='', **kwargs):
        if sex and (sex == 'W' or sex == 'M'):
            self.sex = sex
    
        super().__init__(**kwargs)

    def start_requests(self):
        yield scrapy.Request(
            url='http://www.praxisplan.at/',
            callback=self.post_request,
            dont_filter=True
        )

    def post_request(self, response):
        form_data = {
            'Page' : '1',
            'id' : '-1',
            'level' : '',
            'id_dep' : '-1',
            'pp_last' : '',
            'pp_first' : '',
            'pp_zip' : '',
            'pp_gender' : self.sex,
            'pp_id_field' : '-1',
            'pp_id_additional_field' : '-1',
            'pp_id_special_field' : '-1',
            'pp_id_kk' : '-1',
            'pp_from' : '-1',
            'pp_till' : '-1',
            'pp_id_lingo' : '-1',
            'submit' : 'Suchen'
        }

        phpsessid = None

        responseCookies = response.headers.getlist("Set-Cookie")
        for cookie in responseCookies:
            cookie = str(cookie)
            if phpsessid is not None:
                break
            parts = cookie.split('; ')
            for part in parts:
                if 'PHPSESSID' in part:
                    phpsessid = part.split('=')[1]
                    break

        self.cookies = {
            'PHPSESSID' : phpsessid
        }

        yield scrapy.FormRequest(
            url='http://www.praxisplan.at/doctor_lists.php',
            formdata=form_data,
            cookies=self.cookies,
            callback=self.parsePage,
            dont_filter=True
        )

    def parsePage(self, response):
        details = response.xpath('//a[@class="td-links-green"]')
        if details:
            for a in details:
                href = a.css('a::attr(href)').extract_first()
                if href:
                    yield scrapy.Request(
                        url=response.urljoin(href),
                        callback=self.parseDetail,
                        dont_filter=True
                    )

        currentPage = response.xpath('//div[@class="pagination"][1]/strong[1]')
        if currentPage:
            currentPageNr = self.textHelper.getNrFromString(currentPage.css('strong::text').extract_first().replace('|', '').strip())

            pages = response.xpath('//div[@class="pagination"][1]/a')
            if pages:
                for a in pages:
                    p = a.css('a::text').extract_first()
                    if p:
                        nr = self.textHelper.getNrFromString(p)
                        if nr == currentPageNr + 1:
                            href = a.css('a::attr(href)').extract_first()
                            if href:
                                url = response.urljoin(href)
                                yield scrapy.Request(
                                    url=url,
                                    callback=self.parsePage,
                                    dont_filter=True
                                )

    def parseDetail(self, response):
        title = None
        state = 'Wien'

        titleRaw = response.xpath('//div[@class="body_text"]/h1[1]')
        if titleRaw:
            title = titleRaw.css('h1::text').extract_first().strip()

        headers = response.xpath('//table/tr/td/font/strong')

        practises = response.xpath('//strong[contains(text(), "Praktiziert als")]')
        cntDetails = len(practises)
        if(cntDetails == 0):
            cntDetails = 1

        details = []

        for i in range(0, cntDetails):
            specialitiesTemp = []
            if i < len(practises):
                specialitiesTempRaw = practises[i].xpath('../../../td[2]')
                if specialitiesTempRaw:
                    specialitiesTempList = specialitiesTempRaw.css('td::text').extract()
                    for s in specialitiesTempList:
                        s = self.textHelper.removeUnneccessarySpacesAndNewLines(s)
                        if s:
                            specialitiesTemp.append(s)
            
            item = Details(
                title = title,
                specialities = specialitiesTemp,
                state = state,
                street = None,
                zipCode = None,
                city = None,
                geoLat = None,
                geoLon = None,
                telephoneNumber = None,
                email = None,
                url = None,
                hours = [],
                srcUrl = None,
                insurances = []
            )
            details.append(item)

        specialitiesTop = []
        adresses = []
        contacts = []
        insurancesDetail = []
        openingTimes = []

        if headers:
            for h in headers:
                if 'Fachberechtigung' in h.css('strong::text').extract_first():
                    specialitiesRaw = h.xpath('../../../td[2]')
                    if specialitiesRaw:
                        specialitiesList = specialitiesRaw.css('td::text').extract()
                        for s in specialitiesList:
                            s = self.textHelper.removeUnneccessarySpacesAndNewLines(s)
                            if s:
                                specialitiesTop.append(s)
                if 'Adresse' in h.css('strong::text').extract_first():
                    address = {}
                    addressRaw = h.xpath('../../../td[2]')
                    if addressRaw:
                        addressList = addressRaw.css('td::text').extract()
                        for a in addressList:
                            a = self.textHelper.removeUnneccessarySpacesAndNewLines(a)
                        if len(addressList) > 1:
                            if addressList[0]:
                                address['street'] = addressList[0]
                            if addressList[1]:
                                address['zipCode'] = addressList[1].split()[0]
                                address['city'] = ' '.join(addressList[1].split()[1:])
                            adresses.append(address)
                if 'Erreichbarkeit' in h.css('strong::text').extract_first():
                    contact = {}
                    contactRaw = h.xpath('../../../td[2]')
                    if contactRaw:
                        contactList = contactRaw.css('td::text').extract()
                        for c in contactList:
                            c = self.textHelper.removeUnneccessarySpacesAndNewLines(c)
                            if c:
                                aList = contactRaw.xpath('./a')
                                if aList:
                                    for a in aList:
                                        href = a.css('a::attr(href)').extract_first()
                                        if 'mailto' in href:
                                            contact['email'] = a.css('a::text').extract_first()
                                        else:
                                            contact['website'] = href

                                if 'Telefon' in c:
                                    contact['telNr'] = ' '.join(c.split()[1:])
                                if 'obil' in c and not contact.get('telNr'):
                                    contact['telNr'] = ' '.join(c.split()[1:])
                        contacts.append(contact)

        headers2 = response.xpath('//table/tr/td')
        for td in headers2:
            texts = td.css('td::text').extract()
            if texts:
                for t in texts:
                    if 'Krankenkassen' in t:
                        insuranceTemp = []
                        insurancesRaw = td.xpath('../td[2]')
                        if insurancesRaw:
                            if 'KEINE' in insurancesRaw:
                                break
                            if 'ALLE' in insurancesRaw:
                                for i in self.allInsurances:
                                    insurance = self.insuranceHelper.getInsuranceByCode(i)
                                    if insurance:
                                        if not insurance.name in insuranceTemp:
                                            insuranceTemp.append(insurance.name)
                                break
                            insurancesList = insurancesRaw.css('td::text').extract()
                            for i in insurancesList:
                                i = self.textHelper.removeUnneccessarySpacesAndNewLines(i)
                                insurance = self.insuranceHelper.getInsuranceByCode(i)
                                if insurance:
                                    if not insurance.name in insuranceTemp:
                                        insuranceTemp.append(insurance.name)
                            insurancesDetail.append(insuranceTemp)

        timesTable = response.xpath('//div[@class="body_text"]/table')
        if timesTable:
            for table in timesTable:
                openingTime = []
                if table.xpath('./tr[1]/td[1]').css('td::text').extract_first() == 'MO':
                    times = table.xpath('./tr[1]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'MON',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[2]/td[1]').css('td::text').extract_first() == 'DI':
                    times = table.xpath('./tr[2]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'TUE',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[3]/td[1]').css('td::text').extract_first() == 'MI':
                    times = table.xpath('./tr[3]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'WED',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[4]/td[1]').css('td::text').extract_first() == 'DO':
                    times = table.xpath('./tr[4]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'THU',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[5]/td[1]').css('td::text').extract_first() == 'FR':
                    times = table.xpath('./tr[5]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'FRI',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[6]/td[1]').css('td::text').extract_first() == 'SA':
                    times = table.xpath('./tr[6]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'SAT',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)
                if table.xpath('./tr[7]/td[1]').css('td::text').extract_first() == 'SO':
                    times = table.xpath('./tr[7]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'SUN',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            if not hour in openingTime:
                                openingTime.append(hour)

                openingTimes.append(openingTime)

        for i in range(0, len(details)):
            item = details[i]

            item['srcUrl'] = response.url

            if i < len(adresses):
                item['street'] = adresses[i].get('street')
                item['zipCode'] = adresses[i].get('zipCode')
                item['city'] = adresses[i].get('city')

            if i < len(contacts):
                item['telephoneNumber'] = contacts[i].get('telNr')
                item['url'] = contacts[i].get('website')
                item['email'] = contacts[i].get('email')

            if i < len(openingTimes):
                item['hours'] = openingTimes[i]

            if item['specialities'] is None:
                item['specialities'] = specialitiesTop

            if i < len(insurancesDetail):
                item['insurances'] = insurancesDetail[i]

            if len(item['insurances']) == 0:
                item['insurances'].append(Insurance['WA'].name)

            itemStr = str(item['title'])+str(item['street'])+str(item['zipCode'])+str(item['city'])+str(item['state'])+str(item['telephoneNumber'])+str(item['email'])+str(item['url'])+str(item['specialities'])+str(item['hours'])+str(item['insurances'])

            if itemStr not in self.alreadyKnownItems:
                lat, lng = self.geocoder.getLatLng(item['street'],
                            item['zipCode'],
                            item['city'],
                            item['state'])

                item['geoLat'] = lat
                item['geoLon'] = lng

                self.alreadyKnownItems.append(itemStr)
                yield item
