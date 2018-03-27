# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

from scrapy import Item, Field
from enum import Enum

class Details(Item):
    title = Field()
    street = Field()
    zipCode = Field()
    city = Field()
    state = Field()
    geoLat = Field()
    geoLon = Field()
    telephoneNumber = Field()
    email = Field()
    url = Field()
    specialities = Field()
    hours = Field()
    srcUrl = Field()

class Hours(Item):
    weekday = Field() # Day of the week; MON, TUE, WED, THU, FRI, SAT, SUN
    from_time = Field()
    to_time = Field()

class Weekdays(Enum):
    MON = 'Montag'
    TUE = 'Dienstag'
    WED = 'Mittwoch'
    THU = 'Donnerstag'
    FRI = 'Freitag'
    SAT = 'Samstag'
    SUN = 'Sonntag'
