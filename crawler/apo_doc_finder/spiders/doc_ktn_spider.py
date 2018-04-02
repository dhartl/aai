import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper

class ApoSpider(scrapy.Spider):
    name = "doc_ktn"
    baseurl = 'http://api.aekktn.at/service/arztsuche/'
    cookies = None
    textHelper = TextHelper()
    geocoder = Geocoder()
    insuranceHelper = InsuranceHelper()

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
        
        for detailLink in detailLinks:
            detail_page = detailLink.css('a::attr(href)').extract_first()
            if detail_page is not None:
                detail_page = response.urljoin(detail_page)
                items.append(scrapy.Request(detail_page, callback=self.parseDetail))

        return items

    def parseDetail(self, response):
        titleRaw = response.xpath('//h1[1]').css('h1::text').extract_first()
        title = self.textHelper.removeUnneccessarySpacesAndNewLines(titleRaw)

        labels = response.xpath('//div[@class="result_label"]')
        specialities = []
        street = None
        city = None
        zipCode = None
        telNr = None
        website = None
        hours = []
        insurances = []

        for l in labels:
            labelTextRaw = l.css('div::text').extract_first()
            if labelTextRaw is not None:
                labelText = labelTextRaw.strip()
                contents = l.xpath('../div[@class="result_content"]/div')
                if labelText == 'Fach:':
                    for c in contents:
                        specialityRaw = self.textHelper.removeUnneccessarySpacesAndNewLines(c.css('div::text').extract_first())
                        splits = specialityRaw.split(' - ')
                        specialities.append(' '.join(splits[:len(splits)-1]))
                        insurancesRaw = splits[len(splits)-1]
                        for i in insurancesRaw.split(', '):
                            insurance = self.insuranceHelper.getInsuranceByCode(i)
                            if insurance is not None:
                                if insurance.name not in insurances:
                                    insurances.append(insurance.name)

                if labelText == 'Adresse:':
                    line2 = contents[1].css('div::text').extract_first()
                    zipCode = line2.split(' ')[0]
                    city = ' '.join(line2.split(' ')[1::])
                    streetRaw = contents[0].css('div::text').extract_first().split(' (')[0]
                    street = self.textHelper.removeUnneccessarySpacesAndNewLines(streetRaw)
                if labelText == 'Erreichbarkeiten:':
                    childs = contents.xpath('./div[@class="result_day_field"]')
                    for c in childs:
                        childLabel = c.css('div::text').extract_first()
                        texts = None
                        if (childLabel == 'Telefon:') or (childLabel == 'Mobiltelefon:' and telNr is None):
                            texts = c.xpath('..').css('div::text').extract()
                            telNr = texts[len(texts) - 1].strip().split(' ')[1]
                        if childLabel == 'Webseite:':
                            website = c.xpath('../a[1]').css('a::attr(href)').extract_first()
                if labelText == 'Ordinationszeiten:':
                    childs = contents.xpath('./div[@class="result_day_field"]')
                    hours = self.getHours(childs)

        state = 'Kärnten'
        lat, lng = self.geocoder.getLatLng(street, zipCode, city, state)
        
        if len(insurances) == 0:
            insurances.append(Insurance['WA'].name)

        item = Details(
            title = title,
            street = street,
            zipCode = zipCode,
            city = city,
            state = state,
            geoLat = lat,
            geoLon = lng,
            telephoneNumber = telNr,
            email = None, # couldn't find any doctor with an email as contact for Kärnten
            url = website,
            specialities = specialities,
            hours = hours,
            srcUrl = response.url,
            insurances = insurances
        )

        return item

    def getHours(self, divs):
        hours = []
        for div in divs:
            try:
                wd = Weekdays(div.css('div::text').extract_first())
            except ValueError:
                continue

            contents = div.xpath('..').css('div::text').extract()
            times = contents[len(contents) - 1].strip()[2:].split(', ')
            
            for t in times:
                time_split = t.split(' - ')
                time = Hours(
                    weekday = wd.name,
                    from_time = time_split[0],
                    to_time = time_split[1]
                )
                hours.append(time)
        return hours
