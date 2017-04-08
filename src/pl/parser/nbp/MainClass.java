package pl.parser.nbp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MainClass {

	public static void main(String[] args) {
		//Input format 'Currency' 'DateStart' 'DateStop'
		//ex. EUR 2013-01-28 2013-01-31
		
		long start, stop;
		start = System.currentTimeMillis();
		Currency currency = getCurrency(args[0]);
		LocalDate dateStart = LocalDate.parse(args[1]);
		LocalDate dateStop = LocalDate.parse(args[2]);
		
		checkDates(dateStart, dateStop);
		
		List<String> dirSet = ParseNbp.getDirSet(dateStart, dateStop);
		stop = System.currentTimeMillis();
		long time = stop - start;
		System.out.println("Time: " + time);
		
		System.out.println("dirset" + dirSet);
		System.out.println("ilosc parsow " +  dirSet.size());
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
//		LocalDate dataBase
//		if(dateStop.isBefore(other))
		
		//NBP support XML from
	}
	
}
