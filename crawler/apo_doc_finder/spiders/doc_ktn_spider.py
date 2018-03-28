import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder

class ApoSpider(scrapy.Spider):
    name = "doc_ktn"
    baseurl = 'http://api.aekktn.at/service/arztsuche/'
    cookies = None

    def start_requests(self):
        url = self.baseurl + '?arztsuche=1'
        yield scrapy.Request(url=url, callback=self.post_request)

    def post_request(self, response):
        form_data = {
            'vorname' : '',
            'nachname' : '',
            'ordinationszeiten' : '0',
            'ordinationszeit_1_type' : '0',
            'ordinationszeit_1_time1' : '000',
            'ordinationszeit_1_time2' : '100',
            'ordinationszeit_2_type' : '0',
            'ordinationszeit_2_time1' : '000',
            'ordinationszeit_2_time2' : '100',
            'ordinationszeit_3_type' : '0',
            'ordinationszeit_3_time1' : '000',
            'ordinationszeit_3_time2' : '100',
            'ordinationszeit_4_type' : '0',
            'ordinationszeit_4_time1' : '000',
            'ordinationszeit_4_time2' : '100',
            'ordinationszeit_5_type' : '0',
            'ordinationszeit_5_time1' : '000',
            'ordinationszeit_5_time2' : '100',
            'ordinationszeit_6_type' : '0',
            'ordinationszeit_6_time1' : '000',
            'ordinationszeit_6_time2' : '100',
            'ordinationszeit_7_type' : '0',
            'ordinationszeit_7_time1' : '000',
            'ordinationszeit_7_time2' : '100',
            'vorsorgeuntersuchung' : '0',
            'sort' : '0',
            'suchen' : 'Suche starten'
        }

        ssid = None

        responseCookies = response.headers.getlist("Set-Cookie")
        for cookie in responseCookies:
            cookie = str(cookie)
            if ssid is not None:
                break
            parts = cookie.split('; ')
            for part in parts:
                if 'SSID' in part:
                    ssid = part.split('=')[1]
                    break

        self.cookies = {
            'SSID' : ssid
        }

        url = self.baseurl + '?arztsuche=1'
        yield scrapy.FormRequest(url=url, formdata=form_data, cookies=self.cookies, callback=self.parse)

    def parse(self, response):
        detailLinks = response.xpath('//div[@id="arztsuche_search_result"]/table[1]/tr/td[1]/a')

        items = []
        
        for detailLink in detailLinks[:5:]:
            detail_page = detailLink.css('a::attr(href)').extract_first()
            if detail_page is not None:
                detail_page = response.urljoin(detail_page)
                items.append(scrapy.Request(detail_page, callback=self.parseDetail))

        return items

    def parseDetail(self, response):
        title = response.xpath('//h1[1]').css('h1::innerText').extract_first()
        print(title) # TODO: text is with line breaks and multiple spaces -> make it nice

        #geocoder = Geocoder()
        #lat, lng = geocoder.getLatLng(address)
        
        #item = Details(
        #    title = details.xpath('./span[1]').css('span::text').extract_first(),
        #    street = street,
        #    zipCode = zipCode,
        #    city = city,
        #    state = state,
        #    geoLat = lat,
        #    geoLon = lng,
        #    telephoneNumber = response.xpath('//font[text()="Tel.:"]').xpath('../../td[2]/font/a').css('a::attr(href)').extract_first(),
        #    email = response.xpath('//font[text()="EMail:"]').xpath('../../td[2]/font/a').css('a::text').extract_first(),
        #    url = response.xpath('//font[text()="Homepage:"]').xpath('../../td[2]/a').css('a::attr(href)').extract_first(),
        #    specialities = [], # empty for apo's
        #    hours = self.getHours(response),
        #    srcUrl = response.url
        #)

        return title#item
