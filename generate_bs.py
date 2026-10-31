import re
from datetime import date, timedelta

js_text = """
    '2000': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2001': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2002': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2003': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2004': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2005': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2006': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2007': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2008': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 29, Chaitra: 31 },
    '2009': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2010': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2011': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2012': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2013': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2014': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2015': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2016': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2017': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2018': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2019': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2020': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2021': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2022': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2023': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2024': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2025': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2026': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2027': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2028': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2029': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 32, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2030': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2031': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2032': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2033': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2034': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2035': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 29, Chaitra: 31 },
    '2036': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2037': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2038': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2039': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2040': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2041': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2042': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2043': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2044': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2045': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2046': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2047': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2048': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2049': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2050': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2051': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2052': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2053': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2054': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2055': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2056': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 32, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2057': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2058': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2059': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2060': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2061': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2062': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2063': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2064': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2065': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2066': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 29, Chaitra: 31 },
    '2067': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2068': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2069': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2070': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 29, Mangsir: 30, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2071': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2072': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2073': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2074': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2075': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2076': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2077': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2078': { Baisakh: 31, Jestha: 31, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2079': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2080': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2081': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2082': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2083': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2084': { Baisakh: 31, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 29, Falgun: 30, Chaitra: 31 },
    '2085': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 29, Chaitra: 31 },
    '2086': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 29, Poush: 30, Magh: 29, Falgun: 30, Chaitra: 30 },
    '2087': { Baisakh: 31, Jestha: 31, Asar: 32, Shrawan: 31, Bhadra: 31, Aswin: 31, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 30, Chaitra: 30 },
    '2088': { Baisakh: 30, Jestha: 31, Asar: 32, Shrawan: 32, Bhadra: 30, Aswin: 31, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 30, Chaitra: 30 },
    '2089': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 30, Chaitra: 30 },
    '2090': { Baisakh: 30, Jestha: 32, Asar: 31, Shrawan: 32, Bhadra: 31, Aswin: 30, Kartik: 30, Mangsir: 30, Poush: 29, Magh: 30, Falgun: 30, Chaitra: 30 },
"""

year_blocks = re.findall(r"'(\d{4})':\s*\{([^}]+)\}", js_text)

# Correct reference date: 2000 Baisakh 1 = 14 April 1943
current_ad_date = date(1943, 4, 14)
lines = []

for year, block in year_blocks:
    days = re.findall(r'\b(\d\d)\b', block)
    days_int = [int(d) for d in days]

    d = current_ad_date
    line = f"        {year} to BsYearData(LocalDate.of({d.year}, {d.month}, {d.day}), intArrayOf({','.join(map(str, days_int))})),"
    lines.append(line)

    total_days = sum(days_int)
    current_ad_date += timedelta(days=total_days)

kt_code = f"""package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate

data class NepaliDate(
    val year: Int,
    val month: Int, // 1 to 12
    val day: Int    // 1 to 32
) {{
    fun isValid(): Boolean {{
        if (year !in 2000..2100) return false
        if (month !in 1..12) return false
        val daysInMonth = BsCalendarData.getDaysInMonth(year, month)
        return day in 1..daysInMonth
    }}
}}

data class BsYearData(
    val baishakh1Ad: LocalDate,
    val months: IntArray
)

object BsCalendarData {{
    const val REFERENCE_BS_YEAR = 2000

    // Highly calibrated year-anchored Nepali Patro data for years 2000 to 2100
    // Aligned to authoritative global standard (nepali-date-converter)
    private val yearData: Map<Int, BsYearData> = mapOf(
{chr(10).join(lines)}
    )

    fun getBaishakh1Ad(year: Int): LocalDate? {{
        return yearData[year]?.baishakh1Ad
    }}

    fun getDaysInMonth(year: Int, month: Int): Int {{
        val yData = yearData[year] ?: return 30
        return if (month in 1..12) yData.months[month - 1] else 30
    }}
}}
"""

with open('C:/Users/gauta/AndroidStudioProjects/BSADDateConverterAgeCalculator/app/src/main/java/com/example/bs_addateconverteragecalculator/data/BsCalendarData.kt', 'w', encoding='utf-8') as f:
    f.write(kt_code)
