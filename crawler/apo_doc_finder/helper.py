from scrapy.conf import settings
import requests
from apo_doc_finder.items import Insurance 

class Geocoder():
    apikey = None
    currentApiKeyNr = 0

    def getLatLng(self, street, zipCode, city, state):
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

        # simple retry mechanism if it fails
        for x in range(0, 3):
            self.currentApiKeyNr += 1
            self.apikey = settings.get('GOOGLE_API_KEY' + str(self.currentApiKeyNr), None)

            if self.apikey is None:
                self.currentApiKeyNr = 1
                self.apikey = settings.get('GOOGLE_API_KEY' + str(self.currentApiKeyNr), None)

            url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' + address
            
            if self.apikey is not None:
                url += '&key=' + self.apikey
            
            responseGeo = requests.get(url)
            resp_json_payload = responseGeo.json()

            if(resp_json_payload['status'] == 'OK'):
                lat = resp_json_payload['results'][0]['geometry']['location']['lat']
                lng = resp_json_payload['results'][0]['geometry']['location']['lng']
                return lat, lng

        return None, None

class TextHelper():
    def removeUnneccessarySpacesAndNewLines(self, rawText):
        splits = rawText.split()
        
        return ' '.join(splits)

    def getNrFromString(self, text):
        nr = None
        try:
            nr = int(text)
        except ValueError:
            pass
        return nr

class InsuranceHelper():
    def getInsuranceByCode(self, code):
        try:
            if code == 'SVAGW': # KTN
                code = 'SVA'
            if code.startswith('KFA'): # STMK
                code = 'KFA'
            if code == 'SV': # STMK
                code = 'SVB'
            if code == 'VA': # WIEN
                code = 'VAEB'

            insurance = Insurance[code]
            return insurance
        except KeyError:
            return None
