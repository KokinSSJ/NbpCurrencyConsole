package pl.parser.nbp;

import java.io.BufferedReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.parsers.SAXParser;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import org.omg.CosNaming._BindingIteratorImplBase;
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
			
//			dBuilder.
			stop = System.currentTimeMillis();
			long time1 = stop-start;
			System.out.println("odczyt danych: " + time1);
			//////////////////////////////
//			SAXParserFactory factory = SAXParserFactory.newInstance();
//			  factory.setValidating(false);
//			  factory.setNamespaceAware(true);
//			  factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
//				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//			  SAXParser parser = factory.newSAXParser();
////			  XMLReader reader = parser.getXMLReader();
////			  reader.setEntityResolver(new EntityResolver() {
////			   public InputSource resolveEntity(String pid, String sid) throws SAXException {
////			    return new InputSource(new StringReader(""));
////			   }
////			  });
//			  SAXHandler handler = new SAXHandler();
//
//			parser.parse(url.openStream(), handler);
//			System.out.println(parser);
			/////////////////////
			/////---------------
			start = System.currentTimeMillis();
			XMLInputFactory factory = XMLInputFactory.newInstance();
		    XMLStreamReader reader = factory.createXMLStreamReader(url.openStream());
			
		    Position position = null;
		    String content = ""; 
//		    while(reader.hasNext()){
//		    	
//		    	int event = reader.next();
//		    	
//		    	switch (event) {
//				case XMLStreamConstants.START_ELEMENT:
//					if("pozycja".equals(reader.getLocalName())){
//						position = new Position();
//					}
//					
//					
//					break;
//				case XMLStreamConstants.CHARACTERS:
//			          content = reader.getText().trim();
//			          break;
//				case XMLStreamConstants.END_ELEMENT:
//			          switch(reader.getLocalName()){
//			            case "kod_waluty":
////			            	System.out.println("start kod waluty");
////			            	System.out.println(currency);
//			            	System.out.println(reader.getLocalName());
////			            	System.out.println(content);
//			            	if(content.equals(currency.toString())){
//								position.setCurrency(currency);
//								System.out.println("waluta ok");
//							}
//							else {
//								continue;
//							}
//			              break;
//			            case "kurs_kupna":
//			            	if(position.getCurrency()!=null){
//			            		 position.setBuyRate(content);
//					              buyList2.add(Double.parseDouble(content.replace(",", ".")));
//			            	}
//			             
//			              break;
//			            case "kurs_sprzedazy":
//			            	if(position.getCurrency()!=null){
//			              position.setSellRate(content);
//			              sellList2.add(Double.parseDouble(content.replace(",", ".")));
//			              System.out.println("content" + content);
//			            	}
//			              break;
//			            
//			          }
//			          break;
//				}
//		    }
		    
		    	outerloop:
		    	while(reader.hasNext()){
		    		
		    		int event = reader.next();
		    		
		    		if(XMLStreamConstants.START_ELEMENT == event && "pozycja".equals(reader.getLocalName().toLowerCase())){
		    			Double buyTemp = null; //zeruj
				    	Double sellTemp = null;
				    	Currency currTemp = null;
		    				do{
		    					event=reader.next();
		    					
		    					if(XMLStreamConstants.START_ELEMENT==event && "kod_waluty".equals(reader.getLocalName().toLowerCase())){
		    						event=reader.next();
		    						if(XMLStreamConstants.CHARACTERS==event){
			    						content = reader.getText().trim();
			    						if(content.equals(currency.name())){
			    							System.out.println("currency dziala");
			    							currTemp = Currency.valueOf(content);
//			    							System.out.println(currTemp);
			    						}
			    					}
		    					}
		    					else if(XMLStreamConstants.START_ELEMENT==event && "kurs_kupna".equals(reader.getLocalName().toLowerCase())){
		    						event=reader.next();
		    						if(XMLStreamConstants.CHARACTERS==event){
			    						content = reader.getText().trim();
			    						buyTemp = Double.parseDouble(content.replace(",", "."));
			    					}
		    					}
		    					else if(XMLStreamConstants.START_ELEMENT==event && "kurs_sprzedazy".equals(reader.getLocalName().toLowerCase())){
		    						event=reader.next();
		    						if(XMLStreamConstants.CHARACTERS==event){
			    						content = reader.getText().trim();
			    						sellTemp = Double.parseDouble(content.replace(",", "."));
//			    						System.out.println(sellTemp);
			    					}
		    					}
		    					
		    					else if(XMLStreamConstants.END_ELEMENT == event && "pozycja".equals(reader.getLocalName().toLowerCase())){
		    						 buyTemp = null; //zeruj
		    				    	 sellTemp = null;
		    				    	 currTemp = null;
		    						break;
		    					}
		    					if(buyTemp!=null && sellTemp!=null &&currTemp!=null){
//		    						System.out.println(currTemp);
		    						buyList2.add(buyTemp);
		    						sellList2.add(sellTemp);
		    						break outerloop;
		    						
		    					}
		    				}while(reader.hasNext());
		    			
		    		}
		    		else{
		    			continue;
		    		}
		    	}
		    
			
		    stop = System.currentTimeMillis();
			time1 = stop-start;
			System.out.println("odczyt danychz MOJ: " + time1);
			/////-----------------
			
			NodeList nodeList = document.getElementsByTagName("kod_waluty");
			
			
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

	public List<Double> getBuyList2() {
		return buyList2;
	}

	public List<Double> getSellList2() {
		return sellList2;
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
