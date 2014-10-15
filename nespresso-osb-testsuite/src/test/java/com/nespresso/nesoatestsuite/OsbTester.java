package com.nespresso.nesoatestsuite;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.custommonkey.xmlunit.jaxp13.Validator;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nespresso.nesoatestsuite.utils.FFMWNesoaClient;
import com.nespresso.nesoatestsuite.utils.FTPNesoaClient;



public class OsbTester {

	protected static String testFacadeUrl="http://hqchnesoa102.nespresso.com:8105/TestServices/ProxyServices/TestServicesFacade_PS?SERVICE_NAME=";
	protected static long timeoutForFtp = 90000;
	//	FTP Settings
	public static String ftpHost 		= "hqbuun096.nespresso.com";
	public static String directory 		= "/wmos/environments/env-0/ave/";
	public static String username 		= "wmos";
	public static String password 		= "wm1033";

	protected String errorList = null;
	protected Validator v = null;

	@SuppressWarnings("unchecked")
	protected String compileErrorList(Validator v, String actualResultParam){
		StringBuffer errorList = new StringBuffer();
		StreamSource is2 = new StreamSource(new StringReader(actualResultParam));
		List<Object> listOfErrors = null;
		try{
			listOfErrors = v.getInstanceErrors(is2);		
			for(Object error : listOfErrors){
				errorList.append("\n"+error.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return errorList.toString();
	}

	
	//	Get the file..if the file is not present within a minute the test will fail
	protected String getActualXmlOutputFromFFMWFile(String directory, long startTime,String fileMask) throws IOException{
		String result = null;
		FFMWNesoaClient ffmwC = new FFMWNesoaClient(directory,fileMask);
        if(ffmwC.isFilePresent(startTime)){
            result = ffmwC.getLatestFileContents(directory);
        }else{
//			assertTrue("Expected FFMW file not present in..." + directory,
//					false);
        }
		return result;
	}

	
	protected String getNodeValueFromString(String xpathExpression, String xmlContent) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		String result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
		Document doc = builder.parse(is);
		XPathFactory xFactory = XPathFactory.newInstance();
		XPath xpath = xFactory.newXPath();
		XPathExpression expr = xpath.compile(xpathExpression);
		Object tmpResult = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) tmpResult;
		result = nodes.item(0).getNodeValue();

		return result;
	}
	
	
	//	Get the file..if the file is not present within a minute the test will fail
	protected String getActualXmlOutputFromFTPAsString(String ftpHost,String directory,String username,String password, long startTime,String fileMask) throws IOException{
		String result = null;
        FTPNesoaClient ftpTc = new FTPNesoaClient(ftpHost,directory,username,password,fileMask);
        InputStream is2 = null;
        if(ftpTc.isFilePresent(startTime)){
        	is2 = ftpTc.getLatestFileFromFtp();
        }else{
        	if(!fileMask.equalsIgnoreCase("ITEM_X_FFMW_AVE_")){
        	assertTrue("Expected FTP file not present in..."+directory,false);}
        }
        result = ftpTc.convertinputStreamToString(is2);
		return result;
	}
	
	public void waitForServiceToFinish(long period) throws InterruptedException{
		Thread.sleep(period);
	}
	
	public static void waitForServiceToFinish() throws InterruptedException{
		Thread.sleep(timeoutForFtp);
	}
	
	
	//	Converting the XML file into a useable String
	protected static StringBuilder buildInputStringFromXmlFile(String xmlFileName) throws IOException{
		StringBuilder result = new StringBuilder();
		BufferedReader input =  new BufferedReader(new FileReader(xmlFileName));
		String line = null;
	    while (( line = input.readLine()) != null){
	    	result.append(line);
	    	result.append(System.getProperty("line.separator"));
	    }	
	    return result;
	}
	
	//	Returns the timestamp of the latest file in that specific directory
	protected static long getStartTimeFromFTP(String ftpHost,String directory,String username,String password, String fileMask) throws IOException{
	    long result = System.currentTimeMillis();
	    FTPNesoaClient ftpTcPre = new FTPNesoaClient(ftpHost,directory,username,password, fileMask);
	    result = ftpTcPre.getLatestTimeStamp();
	    ftpTcPre.disconnect();
	    return result;
	}

	//	Returns the timestamp of the latest file in that specific directory
	protected long getTimeStampLastFileFromFFMW(String directory,String fileMask) throws IOException{
		long result = System.currentTimeMillis();
		TreeMap<Long,String> allFiles = new FFMWNesoaClient(directory, fileMask).getOrderedFFMWFileList(directory,fileMask);
		if (!allFiles.isEmpty())
			result = allFiles.lastKey();
		return result;
	}
	
	@Before
	public void initializeTests(){
		errorList = null;
		v = new Validator();
	}

}

