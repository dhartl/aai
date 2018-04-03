import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper

class DocNoeSpider(scrapy.Spider):
    name = "doc_noe"
    page = 1
    entry = 16
    #items = []
    sex = 'f'
    textHelper = TextHelper()
    
    def __init__(self, sex='', **kwargs):
        if sex:
            self.sex = sex
        super().__init__(**kwargs)

    def start_requests(self):
        yield scrapy.Request(
            url='https://sso.arztnoe.at/arztsuche/search.jsf',
            callback=self.post_request,
            dont_filter=True)

    def post_request(self, response):
        form_data = {
            'searchForm:_id9' : '',
            'searchForm:_id14' : self.sex,
            'searchForm:_id20' : '_IGNORE',
            'searchForm:_id36' : '_IGNORE',
            'searchForm:_id42' : '_IGNORE',
            'searchForm:_id48' : '_IGNORE',
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

        max = 20
        print('#####MAX: ' + str(max))
        print('#####NEXT: ' + str(next))
        print('#####SEX: ' + self.sex)
        if next >= (max):
            return# self.items

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
            
            email = None
            telNr = None
            website = None
            specialities = []
            for x in range(0, len(fields)):
                f = fields[x]
                colHeader = f.css('div::text').extract_first()
                if colHeader:
                    if 'Kontakt' in colHeader:
                        contact = fields[x + 1].xpath('./div/div')
                        for c in contact:
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
                    if 'Berufsbezeichnung' in colHeader:
                        specialitiesList = fields[x + 1].xpath('./li')
                        for li in specialitiesList:
                            specialities.append(li.css('li::text').extract_first())

            for x in range(0,2):
                item = Details(
                    title = title,
                    street = '',#street,
                    zipCode = '',#zipCode,
                    city = '',#city,
                    state = '',#state,
                    geoLat = '',#lat,
                    geoLon = '',#lng,
                    telephoneNumber = telNr,
                    email = email,
                    url = website,
                    specialities = specialities,
                    hours = '',#hours,
                    srcUrl = response.url,
                    insurances = '',#insurances
                )

                yield item
            #self.items.append(item)

            print('#########OBI#########: ' + header)
            print(str(self.page) + ':' + str(self.entry))

            #DO get all data!!!

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