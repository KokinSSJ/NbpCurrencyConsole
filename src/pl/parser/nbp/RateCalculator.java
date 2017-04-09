package pl.parser.nbp;

import java.io.BufferedReader;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RateCalculator {
	
	private Double average = 0.0;
	private Double standardDeviation = 0.0;
	
	private  List<Double> buyList = new LinkedList<>();
	private  List<Double> sellList = new LinkedList<>();
	private  List<Double> buyList2 = new LinkedList<>();
	private  List<Double> sellList2 = new LinkedList<>();
	
	
	public void readAll (List<String> xmlList, Currency currency){
		
		for(String s: xmlList){
			long start = System.currentTimeMillis();
			readValues(s, currency);
			long stop = System.currentTimeMillis();
			long time2 = stop-start;
			System.out.println("jeden read " + time2);
			System.out.println("----");
		}
		
	}
public void readAllStAX (List<String> xmlList, Currency currency){
		
		for(String s: xmlList){
			long start = System.currentTimeMillis();
			readValuesStAX(s, currency);
			long stop = System.currentTimeMillis();
			long time2 = stop-start;
			System.out.println("jeden read " + time2);
			System.out.println("----");
		}
		
	}

	public  void  readValues(String xmlName, Currency currency){

		URL url = null;
		DocumentBuilderFactory dbf = null;
		try {
			
			url = new URL(ParseNbp.nbpSite+xmlName+".xml");
			dbf = DocumentBuilderFactory.newInstance();
			
			//disable dtd
			dbf.setValidating(false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			Document document = dBuilder.parse(url.openStream(), "UTF-8"); // very time consuming!!!
			NodeList nodeList = document.getElementsByTagName("kod_waluty");
			
			//search all nodeList
			for(int i=0; i<nodeList.getLength();i++ ){
				//if node with necessary Currency value
				if(nodeList.item(i).getTextContent().equals(currency.name())){

					String buyString = document.getElementsByTagName("kurs_kupna").item(i).getTextContent();
					String sellString = document.getElementsByTagName("kurs_sprzedazy").item(i).getTextContent();
					buyList.add(Double.parseDouble(buyString.replace(",", ".")));
					sellList.add(Double.parseDouble(sellString.replace(",", ".")));
					break;
				}
			}
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	
	//special parser for this exercise! -> 30% faster than standard DOM solution
	public  void  readValuesStAX(String xmlName, Currency currency){
		URL url = null;
		try {
			
			url = new URL(ParseNbp.nbpSite+xmlName+".xml");
			XMLInputFactory factory = XMLInputFactory.newInstance();
		    XMLStreamReader reader = factory.createXMLStreamReader(url.openStream());

		    String content = ""; 
		    Double buyTemp = null; //zeruj
	    	Double sellTemp = null;
	    	Currency currTemp = null;

		    	while(reader.hasNext()){
		    		
		    		int event = reader.next();
		    		
		    		if(XMLStreamConstants.END_ELEMENT==event && "kod_waluty".equals(reader.getLocalName().toLowerCase())){
						
    						if(content.equals(currency.name())){
    							currTemp = Currency.valueOf(content);
    						}
					}
					else if(XMLStreamConstants.END_ELEMENT==event && "kurs_kupna".equals(reader.getLocalName().toLowerCase())){
						
    						buyTemp = Double.parseDouble(content.replace(",", "."));
					}
					else if(XMLStreamConstants.END_ELEMENT==event && "kurs_sprzedazy".equals(reader.getLocalName().toLowerCase())){

    						sellTemp = Double.parseDouble(content.replace(",", "."));
					}
					else if(XMLStreamConstants.CHARACTERS==event){
						content = reader.getText().trim();
//						if(content==null){
//							content = "";
//						}
						
					}
					//new "pozycja" element -> new node for searching
					else if(XMLStreamConstants.START_ELEMENT == event && "pozycja".equals(reader.getLocalName().toLowerCase())){
						 buyTemp = null; //zeruj
				    	 sellTemp = null;
				    	 currTemp = null;
						continue;
					}
		    		//if all necessary is collected go to next!
					if(buyTemp!=null && sellTemp!=null &&currTemp!=null){
						buyList2.add(buyTemp);
						sellList2.add(sellTemp);
						break;
						
					}
		    	}

			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		finally {
			
		}
		
	}
	
	private Double calculateAverage(List<Double> list){
		if(list.isEmpty()){
			return 0.0;
		}
		Double result =0.0;
		for(Double value : list){
			result+=value;
		}
		return result/list.size();
	}

	public List<Double> getBuyList2() {
		return buyList2;
	}

	public List<Double> getSellList2() {
		return sellList2;
	}

	public Double getAverage() {
		this.average = calculateAverage(buyList);
		return this.average;
	}

	public Double getStandardDeviation() {
		Double averageSell = calculateAverage(sellList); 
		Double powerSum = 0.0;
		for(Double sell : sellList){
			powerSum += Math.pow(sell-averageSell, 2);
		}
		standardDeviation = Math.sqrt(powerSum/(sellList.size()));
		
		return standardDeviation;
	}

	public List<Double> getBuyList() {
		return buyList;
	}

	public List<Double> getSellList() {
		return sellList;
	}


	
	

}
