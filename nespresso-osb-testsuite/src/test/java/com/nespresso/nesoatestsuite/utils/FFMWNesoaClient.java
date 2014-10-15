package com.nespresso.nesoatestsuite.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class FFMWNesoaClient extends NesoaClients {
	
    HashMap<Long, String> filesInDir = new HashMap<Long, String>();

    public FFMWNesoaClient(String directory, String fileMask){
		sorted_map = getOrderedFFMWFileList(directory, fileMask);
    }

    
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TreeMap<Long,String> getOrderedFFMWFileList(String directory, String fileMask){
		HashMap<Long, String> tmpResult = new HashMap<Long, String>();
		File file = new File(directory);
		File[] files = file.listFiles();
		for (int fileInList = 0; fileInList < files.length; fileInList++){
			 File tmpFile = files[fileInList];
			 if(tmpFile.getName().indexOf(fileMask)>0){
				 tmpResult.put(tmpFile.lastModified(), tmpFile.getName());
			 }
		}
        ValueComparator bvc =  new ValueComparator(tmpResult);
        TreeMap sorted_map = new TreeMap(bvc);
        sorted_map.putAll(tmpResult);		
		return sorted_map;
	}
	
	
	public String getLatestFileContents(String directory) throws IOException{
		String fileName = sorted_map.get(sorted_map.lastKey());
		StringBuilder result = new StringBuilder();
		BufferedReader input =  new BufferedReader(new FileReader(directory+fileName));
		String line = null;
	    while (( line = input.readLine()) != null){
	    	result.append(line);
	    	result.append(System.getProperty("line.separator"));
	    }	
	    return result.toString();

		
	}
	
	@SuppressWarnings("rawtypes")
	static class ValueComparator implements Comparator {
		  Map base;
		  public ValueComparator(Map base) {
		      this.base = base;
		  }

		  public int compare(Object a, Object b) {
			  Long date1 = (Long)a;
			  Long date2 = (Long)b;
			  return date1.compareTo(date2);
		  }
	}

}
