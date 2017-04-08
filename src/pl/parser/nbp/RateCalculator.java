package pl.parser.nbp;

import java.io.BufferedReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RateCalculator {
	
	private Double average = 0.0;
	private Double standardDeviation = 0.0;
	
	private  List<Double> buyList = new LinkedList<>();
	private  List<Double> sellList = new LinkedList<>();
	
	
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

	public  void  readValues(String xmlName, Currency currency){
		long start, stop;
		URL url = null;
		BufferedReader bReader = null;
		String line;
		try {
			
			url = new URL(ParseNbp.nbpSite+xmlName+".xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			//disable dtd
			dbf.setValidating(false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			start = System.currentTimeMillis();
			Document document = dBuilder.parse(url.openStream(), "UTF-8"); // very time consuming!!!
			stop = System.currentTimeMillis();
			NodeList nodeList = document.getElementsByTagName("kod_waluty");
			
			long time1 = stop-start;
			System.out.println("odczyt danych: " + time1);
			start = System.currentTimeMillis();
			
			
			
			for(int i=0; i<nodeList.getLength();i++ ){
				if(nodeList.item(i).getTextContent().equals(currency.name())){
//					System.out.println(i + "  dzia³a");
					String buyString = document.getElementsByTagName("kurs_kupna").item(i).getTextContent();
					String sellString = document.getElementsByTagName("kurs_sprzedazy").item(i).getTextContent();
					buyList.add(Double.parseDouble(buyString.replace(",", ".")));
					sellList.add(Double.parseDouble(sellString.replace(",", ".")));
					System.out.println("buString " + buyString + " sellString " + sellString);

					break;
				}
				else{
//					System.out.println("nie dzia³a");
				}
			}
			stop = System.currentTimeMillis();
			long time2 = stop-start;
			System.out.println("petla for " + time2);

//			System.out.println(nodeList.getLength());
			
//			Document document = dBuilder.parse(inputStream);
//			Element element = document.getDocumentElement();
////			System.out.println("checking");
//
//			NodeList nodeList = document.getElementsByTagName("pozycja");
//			Node node = nodeList.item(0);
//			Element part = (Element) node;
//			part.getParentNode();
//			
////			document.ge
//			
//			System.out.println("kod waluty " + node.getNodeType() + " " + node.getNodeValue() + " " +node.getNodeName());
//			System.out.println("second line " + element.getElementsByTagName("kod_waluty").item(0).getTextContent());
//			System.out.println(element.getElementsByTagName("kod_waluty").item(0).getParentNode().getNodeType());
//			System.out.println(document.getElementsByTagName("kod_waluty").item(8).getTextContent());
//			
//			System.out.println(document.getElementsByTagName("kurs_kupna").item(8).getTextContent());
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		finally {
			
		}
		
	}

	public Double getAverage() {
		return average;
	}

	public Double getStandardDeviation() {
		return standardDeviation;
	}

	public List<Double> getBuyList() {
		return buyList;
	}

	public List<Double> getSellList() {
		return sellList;
	}


	
	

}
