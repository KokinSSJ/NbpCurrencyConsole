package pl.parser.nbp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/** 
 * 
 * get list of all XML files 
 * @author Dominik Bogacki
 *
 */
public class NbpDirCollector {
	
	
	// public -> get list with files name
	public static List<String>  getDirList(LocalDate dateStart, LocalDate dateStop){
		int year = dateStart.getYear();
		List<String> dirList = new LinkedList<>(); 
		
		//do for all years
		while(year<=dateStop.getYear()){
			String dirFileName = getDirName(year);
			dirList.addAll(getXmlList(dateStart, dateStop, dirFileName));
			year++;
		}
		
		return dirList;
	}
	
	// private! -> get list with files name for specific year
	private static List<String> getXmlList(LocalDate dateStart, LocalDate dateStop, String dirFileName){
		List<String> dirList = new LinkedList<>();
		URL url = null;
		BufferedReader bReader = null;
		String line;
		
		try {
			url = new URL(XMLParser.nbpSite+dirFileName);
			bReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			while((line=bReader.readLine())!=null){

				//some files has different signs at the first line!
				if(line.length()!=11){
					if(line.length()>11 && line.contains("c")){
						line = line.substring(line.indexOf("c"));
						if(line.length()!=11) continue;
					}else{
//						throw new IllegalArgumentException("Unsupported XML file name");
						continue;
					}
				}
				if(isNecessarySave(dateStart, dateStop, line)){
					dirList.add(line);
				}
			}
			return dirList;
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		finally {
			if(bReader!=null){
				try {
					bReader.close();
					return dirList;
				} catch (IOException e) {
					e.printStackTrace();				
				}
			}
			
		}
		return dirList;
		
	}
	
	
	//return txt file name, which consist xml files name
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
		
		if("".equals(string) || string == null) return false; //extra check
		
		if(string.matches("[c+].*")){
			if(isNecessaryDate(dateStart, dateStop, string)){
				return true;
			}
		} 
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
