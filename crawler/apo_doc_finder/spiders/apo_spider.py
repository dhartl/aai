import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays
from scrapy.conf import settings

class ApoSpider(scrapy.Spider):
    name = "apo"
    apikey = None

    def start_requests(self):
        url = 'https://www.apotheker.at/internet/oeak/Apotheken.nsf/formWebname?OpenForm'
        self.apikey = settings.get('GOOGLE_API_KEY', None)
        yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        detailLinks = response.xpath('//div[@id="contentApothekensuche"]/table[2]/tr[1]/td[1]/table[1]/tr/td[2]/b/font/a')

        items = []
        
        for detailLink in detailLinks:
            detail_page = detailLink.css('a::attr(href)').extract_first()
            if detail_page is not None:
                detail_page = response.urljoin(detail_page)
                items.append(scrapy.Request(detail_page, callback=self.parseDetail))

        return items

    def parseDetail(self, response):
        details = response.xpath('//td[@id="content_container"]/table[1]/tr[2]/td[2]')
    
        addressDetails = response.xpath('//font[text()="Adresse:"]').xpath('../..')

        street = addressDetails.xpath('./td[2]/font[1]').css('font::text').extract_first()
        zipCode = addressDetails.xpath('./td[2]/font[2]').css('font::text').extract_first()
        city = addressDetails.xpath('./td[2]/font[4]').css('font::text').extract_first()
        state = response.xpath('//font[text()="Bundesland:"]').xpath('../../td[2]/font').css('font::text').extract_first()

        address = ""

        if street is not None:
            address += street

        if zipCode is not None:
            address += ',' + zipCode
        
        if city is not None:
            if zipCode is None:
                address += ','
            address += city

        if state is not None:
            address += ',' + state

        lat, lng = self.getLatLng(address)

        item = Details(
            title = details.xpath('./span[1]').css('span::text').extract_first(),
            street = street,
            zipCode = zipCode,
            city = city,
            state = state,
            geoLat = lat,
            geoLon = lng,
            telephoneNumber = response.xpath('//font[text()="Tel.:"]').xpath('../../td[2]/font/a').css('a::attr(href)').extract_first(),
            email = response.xpath('//font[text()="EMail:"]').xpath('../../td[2]/font/a').css('a::text').extract_first(),
            url = response.xpath('//font[text()="Homepage:"]').xpath('../../td[2]/a').css('a::attr(href)').extract_first(),
            specialities = [], # empty for apo's
            hours = self.getHours(response),
            srcUrl = response.url
        )

        return item 

    def getLatLng(self, address):
        # simple retry mechanism if it fails
        for x in range(0, 3):
            url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' + address
            if self.apikey is not None:
                url += '&key=' + self.apikey
            
            responseGeo = requests.get(url)
            resp_json_payload = responseGeo.json()

            if(resp_json_payload['status'] == 'OK'):
                lat = resp_json_payload['results'][0]['geometry']['location']['lat']
                lng = resp_json_payload['results'][0]['geometry']['location']['lng']
                return lat, lng

        return 0,0

    def getHours(self, response):
        hours = []
        for x in Weekdays:
            weekday = response.xpath('//font[text()="' + x.value + '"]').xpath('../../td[2]/font').css('font::text').extract_first()
            if weekday is not None:
                times = weekday.split(', ')

                for t in times:
                    time_split = t.split(' - ')
                    time = Hours(
                        weekday = x.name,
                        from_time = time_split[0],
                        to_time = time_split[1]
                    )
                    hours.append(time)
        return hours
