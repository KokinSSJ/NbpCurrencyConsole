# NBP Currency Console 

NBP => the central bank of Poland

1. get selling rate
2. get buying rate
3. calculate average buying rate
4. calculate standard deviation from selling rate

!without NBP API

----------------- RECRUITMENT TASK -----------------------

// Program start (INPUT):

-> currency mark (USD, EUR, CHF, GBP)

-> time interval - start date and stop date

// Program answer (OUTPUT):

-> average buying rate

-> standard deviation from selling rate

//Additional terms:

-> program starts with command prompt: 'java pl.parser.nbp.MainClass EUR 2013-01-28 2013-01-31'

![cmd start](https://github.com/KokinSSJ/NbpCurrencyConsole/blob/master/2017-04-10__Working_program.JPG)

-> program answer are delivered to standard output (console / command prompt)

-> files in package: pl.parser.nbp

-> 'main' method in MainClass class

----------------------------------

->> any number of classes

->> necessary information - How to download rates ['NBP currency instruction'](http://www.nbp.pl/home.aspx?f=/kursy/instrukcja_pobierania_kursow_walut.html)

->> don't use 'NBP Web API'

->> XML example ['NBP XML example'](http://www.nbp.pl/kursy/xml/c073z070413.xml)

->> any XML parsing method allowed

->> date means publication date in XML file ('data_publikacji')

->> currency rate from start date and stop date have to be also included into calculations

->> necessary maven project if you use external libraries

-----------------------------------------------
Example:
'java pl.parser.nbp.MainClass EUR 2013-01-28 2013-01-31'

expected output for example above: 4.1505 0.0125

-----------------------------------------------
EUR -> currency 

2013-01-28 -> start date 

2013-01-31 -> stop date 

4.1505 -> average buying rate 

0.0125 -> standard deviation from selling rate

@DB
