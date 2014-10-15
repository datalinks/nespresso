package com.nespresso.nesoatestsuite.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.nespresso.nesoatestsuite.OsbTester;


public class HttpNesoaClient extends OsbTester{
	  
	  static Logger logger = Logger.getLogger(HttpNesoaClient.class);  
	
	  
	  public static String callService(String serviceName) throws Exception {
		String result = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(testFacadeUrl+serviceName);
        HttpResponse response = null;
		response = httpclient.execute(httppost);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new Exception("Exception calling "+serviceName+" statusCode "+response.getStatusLine().getStatusCode());
		}
		HttpEntity entity = response.getEntity();
    	if (entity != null) {
    	    long len = entity.getContentLength();
    	    if (len != -1 && len < 2048) {
				result = EntityUtils.toString(entity);
			}
    	}
    	return result;
	  }
	  
	  public static String callService(String serviceName, String body) throws Exception {
			String result = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(testFacadeUrl+serviceName);
	        StringEntity entityIn = new StringEntity(body);
	        httppost.setEntity(entityIn);
	        HttpResponse response = null;
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()!=200){
				throw new Exception("Exception calling "+serviceName+" statusCode "+response.getStatusLine().getStatusCode());
			}
	        HttpEntity entity = response.getEntity();
	    	if (entity != null) {
	    	    long len = entity.getContentLength();
	    	    if (len != -1 && len < 2048) {
					result = EntityUtils.toString(entity);
				}
	    	}
	    	return result;
		  }


	  public static String callWebService(String serviceName, String body) throws Exception {
			StringBuffer sb = new StringBuffer();
			DefaultHttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(testFacadeUrl+serviceName);
	        StringEntity entityIn = new StringEntity(body);
	        httppost.setEntity(entityIn);
	        HttpResponse response = null;
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()!=200){
				throw new Exception("Exception calling "+serviceName+" statusCode "+response.getStatusLine().getStatusCode());
			}
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line = null;
	    	while ((line = rd.readLine()) != null) {
	    		sb.append(line);
	    	}
	        return sb.toString();
		  }

	  

	  
/*	  
	  private static String stripMessageFromHeaderAndFooter(String message){
		  int stopHeader = message.indexOf("xml-fragment xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">")+71;
		  int startFooter = message.indexOf("</xml-fragment>");
		  String result = message.substring(stopHeader, startFooter);
		  return result;
	  }
	  
	  private static String stripClobMessageFromHeaderAndFooter(String message){
		  int stopHeader = message.indexOf("<![CDATA[")+9;
		  int startFooter = message.indexOf("]]>");
		  String result = message.substring(stopHeader, startFooter);
		  return result;
	  }
*/
}
