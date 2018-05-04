# Austrian Pharmacy and Doctor's Crawler

This respository contains crawler implemented with [scrapy](https://scrapy.org/) in order to get information like opening hours, contact information and so on from all pharmacies and doctor's in austria.

The information is crawled from following sites:
* [www.apotheker.at](https://www.apotheker.at/internet/oeak/Apotheken.nsf/formWebname?OpenForm)
* [www.aerztekammer.at](http://www.aerztekammer.at/arztsuche)
	* [Burgendland](http://www.aekbgld.at/web/arztekammer-fur-burgenland/arztsuche)
	* [Kärnten](http://www.aekktn.at/web/arztekammer-fur-karnten/arztsuche)
	* [Niederösterreich](https://sso.arztnoe.at/arztsuche/search.jsf)
	* [Oberösterreich](http://arztsuche.aekooe.at/)(*)
	* [Salzburg](http://www.aeksbg.at/web/arztekammer-fur-salzburg/arztsuche)(*)
	* [Steiermark](https://www.aekstmk.or.at/46)
	* [Tirol](http://www.aektirol.at/arztsuche)(*)
	* [Vorarlberg](https://www.medicus-online.at//aek/dist/medicus.html)(*)
	* [Wien](http://www.praxisplan.at/)

(all with (*) marked states have no crawler implemented yet)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

To be able to run the crawler, following requirements are needed:

* Python (download from [here](https://www.python.org/downloads/))
* Scrapy (download from [here](https://scrapy.org/download/)) or use ``pip install scrapy`` if you have ``pip`` installed

### Crawling Data

To start crawling use following command:

``scrapy crawl <NAME> -o <OUTPUT>.json -t json``

whereas:

``<OUTPUT>`` = Filename (including the Filepath) e.g. ".\out"
``<NAME>`` can be one of the following:
* "apo"
* "doc_ktn"
* "doc_bgld"
* "doc_stmk"
* "doc_noe"
* "doc_wien"

Example call:
``scrapy crawl apo -o ".\out.json" -t json``

### Important Notes

#### Geocoding
In order to be able to get the geolocation information of the pharmacies and doctors you also need to set the API keys for the [Google Maps Geocoding API](https://developers.google.com/maps/documentation/geocoding/start?hl=de).
This can be down by calling the crawler with following additional parameters:
* ``-s <PARAMNAME>=<PARAMVALUE>``

Whereas ``<PARAMNAME>`` has to be ``GOOGLE_API_KEY`` followed by a number, e.g. ``GOOGLE_API_KEY1``
Because with the free API key you are only allowed to do 2500 calls per day, the crawler take all API keys available starting with 1 and counting up till the setting is not found and starting again with the first API-Key.
Example: ``scrapy crawl apo -o ".\out.json" -t json -s GOOGLE_API_KEY1=SomeValueQrs -s GOOGLE_API_KEY2=SomeValueUvw -s GOOGLE_API_KEY3=SomeValueXyz``
1. call -> GOOGLE_API_KEY1
2. call -> GOOGLE_API_KEY2
3. call -> GOOGLE_API_KEY3
4. call -> GOOGLE_API_KEY1
5. call -> GOOGLE_API_KEY2
6. call -> GOOGLE_API_KEY3
7. call -> GOOGLE_API_KEY1
8. ...

#### Doctor Crawler for Vienna
The crawler for vienna (``"doc_wien"``) has an additional parameter because it only gets the information based on the gender of the doctors. To get all doctors of vienna you have to run it twice (once per gender).
In addition, the cralwer is only able to do the crawling with ``CONCURRENT_REQUESTS`` is set to ``1``.

Sample calls:
* ``scrapy crawl doc_wien -a sex=M -o ".\docs_vienna_male.json" -t json -s CONCURRENT_REQUESTS=1``
* ``scrapy crawl doc_wien -a sex=W -o ".\docs_vienna_female.json" -t json -s CONCURRENT_REQUESTS=1``