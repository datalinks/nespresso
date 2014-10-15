package com.nespresso.nesoatestsuite.utils;

import java.util.Date;
import java.util.TreeMap;

public class NesoaClients {

	TreeMap<Long,String> sorted_map = null; 

	
	  /*	
	   * 	Check if the file is Present based on a startTime (BEFORE THE SLEEP)
	   *	The Logic is:
	   *	the startTime input Parameter contains already a date which contains the last
	   *	file of a certain directory
	   *	
	   *	that date is compared to the timestamp of the last file of the current List (AFTER THE SLEEP)
	   *
	   * 	If the input parameter is BEFORE (meaning) older than the current sorted file
	   * 	than it exists TRUE
	   * 
	  */
	  public boolean isFilePresent(long startTime){
		  System.err.println("Last file "+sorted_map.get(startTime));
		  System.err.println("ACTUAL Last file "+sorted_map.get(sorted_map.lastKey()));
		  boolean result = false;
		  Date startDate = new Date(startTime);
		  Date lastFileTimeL = new Date(sorted_map.lastKey());
		  if(startDate.before(lastFileTimeL)){
			  result = true;
		  }
		  return result;
	  }

}
