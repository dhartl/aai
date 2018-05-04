import requests
import scrapy
from apo_doc_finder.items import Details, Hours, Weekdays, Insurance
from scrapy.conf import settings
from apo_doc_finder.helper import Geocoder, TextHelper, InsuranceHelper

class DocWienSpider(scrapy.Spider):
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
                    url = response.urljoin(href)
                    yield scrapy.Request(
                        url=url,
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
        specialities = []
        insurances = []
        hours = []
        street = None
        zipCode = None
        city = None
        state = 'Wien'
        telNr = None
        website = None
        email = None

        titleRaw = response.xpath('//div[@class="body_text"]/h1[1]')
        if titleRaw:
            title = titleRaw.css('h1::text').extract_first().strip()

        headers = response.xpath('//table/tr/td/font/strong')
        if headers:
            for h in headers:
                if 'Fachberechtigung' in h.css('strong::text').extract_first():
                    specialitiesRaw = h.xpath('../../../td[2]')
                    if specialitiesRaw:
                        specialitiesList = specialitiesRaw.css('td::text').extract()
                        for s in specialitiesList:
                            s = self.textHelper.removeUnneccessarySpacesAndNewLines(s)
                            if s:
                                specialities.append(s)
                if 'Adresse' in h.css('strong::text').extract_first():
                    addressRaw = h.xpath('../../../td[2]')
                    if addressRaw:
                        addressList = addressRaw.css('td::text').extract()
                        for a in addressList:
                            a = self.textHelper.removeUnneccessarySpacesAndNewLines(a)
                        if len(addressList) > 1:
                            if addressList[0]:
                                street = addressList[0]
                            if addressList[1]:
                                zipCode = addressList[1].split()[0]
                                city = ' '.join(addressList[1].split()[1:])
                if 'Erreichbarkeit' in h.css('strong::text').extract_first():
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
                                            email = a.css('a::text').extract_first()
                                        else:
                                            website = href

                                if 'Telefon' in c:
                                    telNr = ' '.join(c.split()[1:])
                                if 'obil' in c and not telNr:
                                    telNr = ' '.join(c.split()[1:])

        headers2 = response.xpath('//table/tr/td')
        for td in headers2:
            texts = td.css('td::text').extract()
            if texts:
                for t in texts:
                    if 'Krankenkassen' in t:
                        insurancesRaw = td.xpath('../td[2]')
                        if insurancesRaw:
                            if 'KEINE' in insurancesRaw:
                                break
                            if 'ALLE' in insurancesRaw:
                                for i in self.allInsurances:
                                    insurance = self.insuranceHelper.getInsuranceByCode(i)
                                    if insurance:
                                        insurances.append(insurance.name)
                                break
                            insurancesList = insurancesRaw.css('td::text').extract()
                            for i in insurancesList:
                                i = self.textHelper.removeUnneccessarySpacesAndNewLines(i)
                                insurance = self.insuranceHelper.getInsuranceByCode(i)
                                if insurance:
                                    insurances.append(insurance.name)

        if len(insurances) == 0:
            insurances.append(Insurance['WA'].name)

        timesTable = response.xpath('//div[@class="body_text"]/table')
        if timesTable:
            for table in timesTable:
                if table.xpath('./tr[1]/td[1]').css('td::text').extract_first() == 'MO':
                    times = table.xpath('./tr[1]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'MON',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[2]/td[1]').css('td::text').extract_first() == 'DI':
                    times = table.xpath('./tr[2]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'TUE',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[3]/td[1]').css('td::text').extract_first() == 'MI':
                    times = table.xpath('./tr[3]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'WED',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[4]/td[1]').css('td::text').extract_first() == 'DO':
                    times = table.xpath('./tr[4]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'THU',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[5]/td[1]').css('td::text').extract_first() == 'FR':
                    times = table.xpath('./tr[5]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'FRI',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[6]/td[1]').css('td::text').extract_first() == 'SA':
                    times = table.xpath('./tr[6]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'SAT',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)
                if table.xpath('./tr[7]/td[1]').css('td::text').extract_first() == 'SO':
                    times = table.xpath('./tr[7]/td[3]').css('td::text').extract()
                    for time in times:
                        if time != '-':
                            hour = Hours(
                                        weekday = 'SUN',
                                        from_time = time.split('-')[0],
                                        to_time = time.split('-')[1]
                                    )
                            hours.append(hour)

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
