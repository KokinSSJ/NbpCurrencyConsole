package pl.parser.nbp;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class RateCalculator {
//	TODO zmien nazwy klas ¿eby lepiej pasowaly
	private Double averageBuy = 0.0;
	private Double standardDeviationSell = 0.0;
	
	private  List<Double> buyRateList = new LinkedList<>();
	private  List<Double> sellRateList = new LinkedList<>();
	
	private List<String> dirList = new LinkedList<>();
	private Currency currency;
	private LocalDate dateStart;
	private LocalDate dateStop;

	
	public RateCalculator(Currency currency, LocalDate dateStart, LocalDate dateStop) {
		this.currency = currency;
		this.dateStart = dateStart;
		this.dateStop = dateStop;
		
		this.dirList = NbpDirCollector.getDirList(dateStart, dateStop);
		XMLParser parser = new XMLParserStAX(this.dirList, currency);
		this.buyRateList = parser.getBuyList();
		this.sellRateList = parser.getSellList();
				
		this.averageBuy = calculateAverage(buyRateList);
		this.standardDeviationSell = caclulateStandardDeviation(sellRateList);
		
	}
	
	private static Double calculateAverage(List<Double> list){
		if(list.isEmpty()){
			return 0.0;
		}
		Double result =0.0;
		for(Double value : list){
			result+=value;
		}
		return result/list.size();
	}



	//standard deviation for population 
	private static Double caclulateStandardDeviation(List<Double> list) {
		if(list.isEmpty()){
			return 0.0;
		}
		Double averageSell = calculateAverage(list); 
		Double powerSum = 0.0;
		for(Double sell : list){
			powerSum += Math.pow(sell-averageSell, 2);
		}

		return Math.sqrt(powerSum/(list.size()));
	}

	public Double getAverageBuy() {
		return averageBuy;
	}

	public Double getStandardDeviationSell() {
		return standardDeviationSell;
	}

	public List<Double> getBuyRateList() {
		return buyRateList;
	}

	public List<Double> getSellRateList() {
		return sellRateList;
	}

	public List<String> getDirList() {
		return dirList;
	}

	public Currency getCurrency() {
		return currency;
	}

	public LocalDate getDateStart() {
		return dateStart;
	}

	public LocalDate getDateStop() {
		return dateStop;
	}



}
