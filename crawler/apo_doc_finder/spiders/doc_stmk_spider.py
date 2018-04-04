import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper

class DocStmkSpider(scrapy.Spider):
    name = "doc_stmk"
    textHelper = TextHelper()
    geocoder = Geocoder()
    insuranceHelper = InsuranceHelper()
    cookies = None

    def start_requests(self):
        yield scrapy.Request(url='https://www.aekstmk.or.at/46', callback=self.submit)

    def submit(self, response):
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

        yield scrapy.Request(url='https://www.aekstmk.or.at/46?spam=5ac2092d3c79b&referer=%2F46&search=search&pageName=46&fachgruppe=0&bezirk=0&arztname=&kassa=0&diplom=0&zusfach=0&geschlecht=&M01=0&M02=0&M03=0&M04=0&M05=0&M06=0&vu=0&park=0&roll=0&aufz=0&dmp=0&sprache=0', callback=self.parse, cookies=self.cookies)

    def parse(self, response):
        counties = response.xpath('//div[@class="bezirk"]')
        for c in counties:
            href = c.xpath('./a').css('a::attr(href)').extract_first()
            url = response.urljoin(href)
            yield scrapy.Request(url=url, callback=self.parseCounty, cookies=self.cookies)

    def parseCounty(self, response):
        detailLinks = response.xpath('//div[@class="bez_content vis "]/div/span/a')
        for d in detailLinks:
            href = d.css('a::attr(href)').extract_first()
            url = response.urljoin(href)
            yield scrapy.Request(url=url, callback=self.parseDetail, cookies=self.cookies)
    
    def parseDetail(self, response):
        title = response.xpath('//div[@id="ordi_container"]/h2').css('h2::text').extract_first()
        specialitiesRaw = response.xpath('//div[@id="ordi_container"]/p').css('p::text').extract()
        specialities = []
        for s in specialitiesRaw:
            snew = self.textHelper.removeUnneccessarySpacesAndNewLines(s)
            if snew:
                specialities.append(snew)
        
        street = None
        zipCode = None
        city = None
        telNr = None
        email = None
        website = None
        insurances = []
        hours = []

        headers = response.xpath('//div[@id="ordi_container"]/div/h3')
        headersH4 = response.xpath('//div[@id="ordi_container"]/div/table/tr/td/h4')
        for h in headers:
            hText = h.css('h3::text').extract_first()
            if 'Adresse' in hText:
                addressTableEntries = h.xpath('../table/tr')
                for tr in addressTableEntries:
                    rowName = tr.xpath('./td[1]').css('td::text').extract_first()
                    value = tr.xpath('./td[2]').css('td::text').extract_first()
                    if 'Strasse' in rowName:
                        street = value
                    if 'Plz' in rowName:
                        zipCode = value
                    if 'Ort' in rowName:
                        city = value
            if 'Kontakt' in hText:
                contactTableEntries = h.xpath('../table/tr')
                for tr in contactTableEntries:
                    rowName = tr.xpath('./td[1]').css('td::text').extract_first()
                    value = tr.xpath('./td[2]/a').css('a::text').extract_first()
                    if 'Tel.' in rowName:
                        telNr = value
                    if 'Mobil' in rowName and telNr is None:
                        telNr = value
                    if 'Email' in rowName:
                        email = value[2:]
                    if 'Homepage' in rowName:
                        website = value[2:]
            if 'Ordinationszeiten' in hText:
                timeTableEntries = h.xpath('../table/tr')
                hours = self.getHours(timeTableEntries)


        for h4 in headersH4:
            h4Text = h4.css('h4::text').extract_first()
            if 'Kassen' in h4Text:
                insuranceTableEntries = h4.xpath('../../../tr/td[2]')
                for i in insuranceTableEntries:
                    insuranceRaw = i.css('td::text').extract_first()
                    insuranceText = insuranceRaw.split(' ')[0]
                    if insuranceText:
                        insurance = self.insuranceHelper.getInsuranceByCode(insuranceText)
                        if insurance is not None:
                            if insurance.name not in insurances:
                                insurances.append(insurance.name)


        if len(insurances) == 0:
            insurances.append(Insurance['WA'].name)

        state = 'Steiermark'
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

        return item

    def getHours(self, trs):
        hours = []
        for tr in trs:
            try:
                title = tr.xpath('./td[1]').css('td::text').extract_first()
                if not title:
                    continue
                wd = Weekdays(title[:len(title)-1])
            except ValueError:
                continue

            hoursRaw = tr.xpath('./td[2]').css('td::text').extract_first()
            hoursString = hoursRaw.replace(' ', '').replace('und', '').split('von')

            for h1 in hoursString:
                if h1:
                    time = Hours(
                        weekday = wd.name,
                        from_time = h1.split('bis')[0],
                        to_time = h1.split('bis')[1]
                    )
                    hours.append(time)

        return hours