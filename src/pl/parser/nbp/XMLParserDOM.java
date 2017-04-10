package pl.parser.nbp;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLParserDOM implements XMLParser{

	private  List<Double> buyList = new LinkedList<>();
	private  List<Double> sellList = new LinkedList<>();
	
	public XMLParserDOM(List<String> xmlList, Currency currency) {
		readAll(xmlList, currency);
	}

	public void readAll (List<String> xmlList, Currency currency){
		
		for(String s: xmlList){
			readValues(s, currency);
		}
		
	}
	
	private  void  readValues(String xmlName, Currency currency){

		URL url = null;
		DocumentBuilderFactory dbf = null;
		try {
			
			url = new URL(nbpSite+xmlName+".xml");
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
	
	@Override
	public List<Double> getBuyList() {
		return buyList;
	}
	
	@Override
	public List<Double> getSellList() {
		return sellList;
	}
	
}
