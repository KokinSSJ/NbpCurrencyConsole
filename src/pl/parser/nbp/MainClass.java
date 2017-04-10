package pl.parser.nbp;

import java.time.LocalDate;
/**
 *  Calculate average buying rate and standard deviation for selling rate
 *  Used NBP web -> http://www.nbp.pl/home.aspx?f=/kursy/instrukcja_pobierania_kursow_walut.html
 *  without using NBP API
 *  
 *  
 * 		//Input format 'Currency' 'DateStart' 'DateStop'
 *		//ex. EUR 2013-01-28 2013-01-31
 *		OUTPUT -> 4.1505 0.0125
 *
 * @author Dominik Bogacki
 *
 */
public class MainClass {
	
	public static void main(String[] args) {
		//Input format 'Currency' 'DateStart' 'DateStop'
		//ex. EUR 2013-01-28 2013-01-31
		
		Currency currency = Currency.valueOf(args[0]);
		LocalDate dateStart = LocalDate.parse(args[1]);
		LocalDate dateStop = LocalDate.parse(args[2]);
		checkDates(dateStart, dateStop);

		RateCalculator rateCalculator = new RateCalculator(currency, dateStart, dateStop);

		System.out.format("%.4f\n", rateCalculator.getAverageBuy());
		System.out.format("%.4f\n",rateCalculator.getStandardDeviationSell());

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
		
		//if dateStart is older than 2002-01-01
		if(dateStart.isBefore(dateBase)){
			throw new IllegalArgumentException("DateStart has been change to " + dateBase + " -> min value");
		}
		
	}
	
}
