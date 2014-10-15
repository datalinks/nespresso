package com.nespresso.nesoatestsuite;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.XMLUnitTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;

public class PocWebServiceSayHello extends OsbTester{

	static String actualOutputForWs;
	
	@Test
	public void testIfExpectedAnswerIsBeingReturnedWithAddress1() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "SSR_POC_SAYHELLO";
		String xmlFileName  	= "xsd/poc/poc_ws_sayHello-1.xml";
		String expectedResult 	= "some address with nr 1 in it...";        
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        /*	
         * Call the ServiceFacade
         * Please NOTE THAt THIS TIME .......YOU GET AN ANSWER (from the WS) == SYNC CALL
         * A lookup of the actual return value is needed therefore use the getNodeValeFromString method
		*/
		String tmpResult = HttpNesoaClient.callWebService(serviceToTest,contents.toString());
		actualOutputForWs = getNodeValueFromString("//return//text()",tmpResult);
		assertTrue("Test failed, expected "+expectedResult+" actual result: "+actualOutputForWs,actualOutputForWs.equalsIgnoreCase(expectedResult));
	}

	
	@Test
	public void testIfExpectedAnswerIsBeingReturnedWithAddress3() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "SSR_POC_SAYHELLO";
		String xmlFileName  	= "xsd/poc/poc_ws_sayHello-3.xml";
		String expectedResult 	= "someOther addres with id: 3";        
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
		String tmpResult = HttpNesoaClient.callWebService(serviceToTest,contents.toString());
		actualOutputForWs = getNodeValueFromString("//return//text()",tmpResult);
		assertTrue("Test failed, expected "+expectedResult+" actual result: "+actualOutputForWs,actualOutputForWs.equalsIgnoreCase(expectedResult));
	}
	
}

