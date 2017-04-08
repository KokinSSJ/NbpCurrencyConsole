package pl.parser.nbp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainClass {
	
	private static LocalDate dateStart;
	private static LocalDate dateStop;

	public static void main(String[] args) {
		//Input format 'Currency' 'DateStart' 'DateStop'
		//ex. EUR 2013-01-28 2013-01-31
		
		long start, stop;
		start = System.currentTimeMillis();
		Currency currency = getCurrency(args[0]);
		dateStart = LocalDate.parse(args[1]);
		dateStop = LocalDate.parse(args[2]);
		
		checkDates(dateStart, dateStop);
		
		List<String> dirList = ParseNbp.getDirSet(dateStart, dateStop);
		stop = System.currentTimeMillis();
		long time = stop - start;
		System.out.println("--------------------------");
		RateCalculator rateCalculator = new RateCalculator();
//		rateCalculator.readAll(dirList, currency);
//		rateCalculator.readValues("c034z020218", currency);
		start = System.currentTimeMillis();
		rateCalculator.readAll(dirList, currency);
		stop = System.currentTimeMillis();
		long time2 = stop-start;
		rateCalculator.getAverage();
		System.out.println(rateCalculator.getBuyList());
		System.out.println(rateCalculator.getSellList());
//		RateCalculator.readValues(dirList.get(0), currency);
//		RateCalculator.readValues("c034z020218", currency);
		
		System.out.println("Time download dir: " + time);
		System.out.println("Time retrieve xml: " + time2);
		
		System.out.println("dirset" + dirList);
		System.out.println("ilosc parsow " +  dirList.size());
		System.out.println(currency.valueOf("EUR") + " " + LocalDateTime.now());
		System.out.println("dateStart: " + dateStart);
		System.out.println("dateStop: " + dateStop);
		
	}
	
	
	private static Currency getCurrency(String string){
		switch (string) {
		case "EUR":
			return Currency.EUR;
		case "USD":
			return Currency.USD;
		case "GBP":
			return Currency.GBP;
		case "CHF":
			return Currency.CHF;
		default:
			throw new IllegalArgumentException("Illegal Currency - not recognized");
		}
	}
	
	private static void checkDates(LocalDate dateStart, LocalDate dateStop){
		
		//check if dateStart is greater than dateStop
		if(dateStart.isAfter(dateStop)){
			System.out.println("DateStart is after dateStop");
			throw new IllegalArgumentException("DateStart is after dateStop");
		}
		
		//check if dateStop is not greater than today!
		//TODO check if you can get exchange rate from "today" if yes -> use not negeted isAfter!
		if(!dateStop.isBefore(LocalDate.now())){
			System.out.println("DateStop is after or today -> can't get exchange rate");
			throw new IllegalArgumentException("DateStop is after today -> can't get exchange rate");
		}
		
		//if end time is older than 2002-01-01 -> first archived currency rate
		LocalDate dateBase = LocalDate.of(2002, 1, 1); //date from when NBP archiving data!
		if(dateStop.isBefore(dateBase)){
			throw new IllegalArgumentException("DateStop is to far, NBP has data from " + dateBase);
		}
		
		//set to 2002-01-01 if someone enter older dateStart!
		if(dateStart.isBefore(dateBase)){
			System.out.println("DateStart has been change to " + dateBase + " -> min value");
			MainClass.dateStart = dateBase;
		}
		
		//NBP support XML from
	}
	
}
