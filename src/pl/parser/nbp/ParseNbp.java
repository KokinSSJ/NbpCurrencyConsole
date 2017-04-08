package pl.parser.nbp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ParseNbp {
	
	private final static String nbpSite = "http://www.nbp.pl/kursy/xml/";
	
	private List<String> list = new LinkedList<>();//TODO mo¿ê tutaj dac i potem zwrociæ przez geter??
	
	public static List<String>  getDirSet(LocalDate dateStart, LocalDate dateStop){
		int year = dateStart.getYear();
		List<String> dirSet = new LinkedList<>(); 
		//do for all years
		while(year<=dateStop.getYear()){
			String dirFileName = getDirName(year);
			System.out.println("check getDirSet while" + dirFileName);
			dirSet.addAll(getDirSet(dateStart, dateStop, dirFileName));
			year++;
		}
		return dirSet;
	}
	
	//TODO -> work with efficiency -> many times openConnection!
	//getValue -> get selling value for specific date
	private static List<String> getDirSet(LocalDate dateStart, LocalDate dateStop, String dirFileName){
		List<String> dirSet = new LinkedList<>();
//		System.out.println("check getSellingRate");
		URL url = null;
		BufferedReader bReader = null;
		String line;
		
		int check = 0;
		try {
//			System.out.println("check1");
			url = new URL(nbpSite+dirFileName);
			bReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			while((line=bReader.readLine())!=null){

				//some files has different signs at the first line!
				if(line.length()!=11){
					if(line.length()>11){
						line = line.substring(line.indexOf("c"));
					}else{
//						throw new IllegalArgumentException("Unsupported XML file name");
						continue;
					}
				}
				if(isNecessarySave(dateStart, dateStop, line)){
					
					dirSet.add(line);
					check++;
				}
				
				
			}
			
//			System.out.println("check1");
			System.out.println("check  value " + check);
			return dirSet;
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		finally {
			
			try {
				bReader.close();
				return dirSet;
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			
		}
		return dirSet;
		
	}
	//return txt file name, which consist xml file names
	//for current year -> dir.txt 
	//for previous years -> dir+Year+.txt ->e.g. dir2002.txt
	private static String getDirName(int year){
		if(year==LocalDate.now().getYear()){
			return "dir.txt";
		}
		else{
			return "dir"+ year + ".txt";
		}
	}
	
	//check if string is this which we need for calculations
	private static boolean isNecessarySave(LocalDate dateStart, LocalDate dateStop, String string){
		
		if("".equals(string)) return false; //extra check
		
		if(string.matches("[c+].*")){
			if(isNecessaryDate(dateStart, dateStop, string)){
				return true;
			}
		} 
//		if(string.matches(".*[c+].*")){
//			
//			if (isNecessaryDate(dateStart, dateStop, string.substring(string.indexOf("c")))) {
//				return true;
//			}
//		}
		
		
		return false;
	}

private static boolean isNecessaryDate(LocalDate dateStart, LocalDate dateStop, String string){
	Integer year = Integer.parseInt(20+string.substring(5,7));
	Integer month = Integer.parseInt(string.substring(7,9));
	Integer day = Integer.parseInt(string.substring(9,11));
	LocalDate stringDate = LocalDate.of(year, month, day);

	//check if dateStop >= stringDate >= dateStart
	if(!stringDate.isBefore(dateStart) && !stringDate.isAfter(dateStop)){
		return true;
	}
	return false;
	}

}
