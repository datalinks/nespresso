package com.nespresso.nesoatestsuite.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.log4j.Logger;

public class FTPNesoaClient extends NesoaClients {
	  
   static Logger logger = Logger.getLogger(FTPNesoaClient.class);  
   HashMap<Long, String> filesInDir = new HashMap<Long, String>();
   FTPClient f = null;

   
   public FTPNesoaClient(String ftpHost, String directory, String username, String password, String fileMask){		  
		  initialize(ftpHost,directory,username,password, fileMask);
   }
	
   public void disconnect(){
		try {
			f.disconnect();
		} catch (IOException e) {
			logger.error("IOException occured in disconnect: "+e.getMessage());
		}
   }
   
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize(String ftpHost, String directory, String username, String password, String fileMask){
		f = connectToFtpServer(ftpHost,username,password);
		try{				
			FTPListParseEngine engine = f.initiateListParsing(directory);
		    while (engine.hasNext()) {
			       FTPFile[] files = engine.getNext(100);
			       for(FTPFile file : files){
			    	    if (file.isFile()){
			    	    	String fileName = file.getName();
			    	    	if(fileName.startsWith(fileMask)){
					       		filesInDir.put(file.getTimestamp().getTimeInMillis(),directory+fileName);		    	    	
			    	    	}
			    	    }
			       }
			       ValueComparator bvc =  new ValueComparator(filesInDir);
			       sorted_map = new TreeMap(bvc);
			       sorted_map.putAll(filesInDir);
		    }
		} catch (IOException e) {
			logger.error("IOException occured in initialize: "+e.getMessage());
		}
	  }
	  
	  private FTPClient connectToFtpServer(String ftpHost, String username, String password){
		  FTPClient f = new FTPClient();
		  try{
			    f.connect(ftpHost);
			    f.login(username,password);			  
		  }catch(SocketException sex){
			  System.err.println("Socket Exception occured "+sex.getMessage());
		  }catch(IOException iox){
			  System.err.println("IO Exception occured "+iox.getMessage());
		  }
		  return f;
	  }
		
	  
	  public long getLatestTimeStamp() throws IOException{
		    return sorted_map.lastKey();
	   }

	  public InputStream getLatestFileFromFtp() throws IOException{
		    String lastFile = sorted_map.get(sorted_map.lastKey());
		    return f.retrieveFileStream(lastFile);
	   }


	  public void persistLatestFileFromFtp(String outputFile) throws IOException{
		  FileOutputStream fos = new FileOutputStream(outputFile);  
		  String lastFile = sorted_map.get(sorted_map.lastKey());
		  f.retrieveFile(lastFile,fos);
	   }

	  
	  public String convertinputStreamToString(InputStream ists) throws IOException {
          StringBuilder sb = new StringBuilder();
		  if (ists != null) {
	            String line;
	            try {
	                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
	                while ((line = r1.readLine()) != null) {
	                    sb.append(line).append("\n");
	                }
	            } finally {
	                ists.close();
	            }
	        } 
            return sb.toString();
	  }
	  
		@SuppressWarnings("rawtypes")
		class ValueComparator implements Comparator {
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

