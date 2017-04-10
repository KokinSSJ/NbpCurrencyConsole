package pl.parser.nbp;

import java.util.List;

public interface XMLParser {
	
	final static String nbpSite = "http://www.nbp.pl/kursy/xml/";
	
	void readAll (List<String> xmlList, Currency currency);

	public List<Double> getBuyList();

	public List<Double> getSellList();
}
