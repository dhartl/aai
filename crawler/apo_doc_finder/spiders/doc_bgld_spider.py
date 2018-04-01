import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper

class ApoSpider(scrapy.Spider):
    name = "doc_bgld"
    baseurl = 'http://www.aekbgld.at/arztsuche/-/arztsuchebgd/arzt/'
    textHelper = TextHelper()
    geocoder = Geocoder()

    def start_requests(self):
        url = self.baseurl
        # Didn't found a fast and good solution for doing the post for filtering
        # Therefore only get the detail pages per number
        # Saw a max of 3003
        for x in range(1, 3100):
            url = self.baseurl + str(x)
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        details = response.xpath('//div[@id="medsearch"]/div[@class="details"]')

        detailMessage = details.css('div::text').extract_first()

        if detailMessage is not None and 'nicht gefunden' not in detailMessage:
            title = details.xpath('./h2').css('h2::text').extract_first().strip()
            
            specialities = []
            specialitiesRaw = details.xpath('./div[@class="subjects"]').css('div::text').extract_first().split('/')
            for sr in specialitiesRaw:
                s = self.textHelper.removeUnneccessarySpacesAndNewLines(sr)
                specialities.append(s)

            detailTable = details.xpath('./table[@class="detailTable"]/tr[1]/td[2]')

            street = detailTable.xpath('./div[1]').css('div::text').extract_first()
            line2 = detailTable.xpath('./div[2]').css('div::text').extract_first()
            zipCode = line2.split(' ')[0]
            city = ' '.join(line2.split(' ')[1::])

            state = 'Burgenland'
            
            lat, lng = self.geocoder.getLatLng(street, zipCode, city, state)

            ordInfoTable = detailTable.xpath('./table[@class="ordInfo"]/tr')
            telNr = None
            website = None
            for tr in ordInfoTable:
                if 'Telefon:' in tr.xpath('./td[1]').css('td::text').extract_first():
                    telNr = tr.xpath('./td[2]').css('td::text').extract_first()
                if 'Homepage:' in tr.xpath('./td[1]').css('td::text').extract_first():
                    website = tr.xpath('./td[2]/a').css('a::attr(href)').extract_first()

            hoursTable = detailTable.xpath('./table[@class="consHoursTable"]/tbody/tr')
            hours = self.getHours(hoursTable)

            yield Details(
                title = title,
                street = street,
                zipCode = zipCode,
                city = city,
                state = state,
                geoLat = lat,
                geoLon = lng,
                telephoneNumber = telNr,
                email = None, # couldn't find any doctor with an email as contact for Burgenland
                url = website,
                specialities = specialities,
                hours = hours,
                srcUrl = response.url
            )
    
    def getHours(self, trs):
        hours = []
        for tr in trs:
            try:
                wd = Weekdays(tr.xpath('./td[1]').css('td::text').extract_first())
            except ValueError:
                continue

            time = Hours(
                weekday = wd.name,
                from_time = tr.xpath('./td[2]').css('td::text').extract_first(),
                to_time = tr.xpath('./td[3]').css('td::text').extract_first()
            )
            hours.append(time)

        return hours
