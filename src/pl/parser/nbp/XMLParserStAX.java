package pl.parser.nbp;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class XMLParserStAX implements XMLParser {
	
	private  List<Double> buyList = new LinkedList<>();
	private  List<Double> sellList = new LinkedList<>();
	
	public XMLParserStAX(List<String> xmlList, Currency currency) {
		readAll(xmlList, currency);
	}
	
	public void readAll (List<String> xmlList, Currency currency){
		for(String s: xmlList){
			readValuesStAX(s, currency);
		}
	}
	
//special parser for this exercise! -> 30% faster than standard DOM solution
	private  void  readValuesStAX(String xmlName, Currency currency){
		URL url = null;
		try {
			
			url = new URL(nbpSite+xmlName+".xml");
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
						buyList.add(buyTemp);
						sellList.add(sellTemp);
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
