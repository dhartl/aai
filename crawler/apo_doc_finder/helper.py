from scrapy.conf import settings
import requests

class Geocoder():
    apikey = None

    def getLatLng(self, address):
        # simple retry mechanism if it fails
        for x in range(0, 3):
            self.apikey = settings.get('GOOGLE_API_KEY', None)
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