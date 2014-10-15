package com.nespresso.nesoatestsuite.interfaces;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.XMLUnitTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;

public class TestReqPackMat extends OsbTester{

	static String actualOutputForReqPackMat;
	
	
	/*
	Tests for GM_ReqPackMat
*/
	
	@Test
	public void testReqPackMatOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_ReqPackMat";
		String xmlFileName  	= "xsd/req_pack_mat/TKT_Input.xml";
        String expectedSchema 	= "xsd/req_pack_mat/PickticketBridge_3_2_1.xsd";
        String fileNameMask 	= "PKT_FFMW_AVE_";

		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        // getStartTime, needed to make sure that we take the last file
        long startTime = getStartTimeFromFTP(ftpHost,directory,username,password, fileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(serviceToTest,contents.toString());
        //	Wait for a while
        waitForServiceToFinish(70000);
        //	Get the file..if the file is not present within a minute the test will fail
        actualOutputForReqPackMat = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,startTime,fileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForReqPackMat));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForReqPackMat );
		}
		assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testReqPackMatXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//PickticketBridge/Pickticket/Company",actualOutputForReqPackMat);
		xmlunit.assertXpathExists("//PickticketBridge/Pickticket/Division",actualOutputForReqPackMat);
	}

	
	@Test
	public void testReqPackMatXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"001\"","//PickticketBridge/Pickticket/Company",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"001\"","//PickticketBridge/Pickticket/Division",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"AVE\"","//PickticketBridge/Pickticket/Warehouse",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"PCAVE\"","//PickticketBridge/Pickticket/PickticketHeaderFields/ShipTo",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"Production Centre Avenches\"","//PickticketBridge/Pickticket/PickticketHeaderFields/ShipToName",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"CONV\"","//PickticketBridge/Pickticket/PickticketHeaderFields/ShipVia",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"51\"","//PickticketBridge/Pickticket/PickticketHeaderFields/PrintCode",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"40\"","//PickticketBridge/Pickticket/PickticketHeaderFields/AutoInvoiceStatus",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"N\"","//PickticketBridge/Pickticket/PickticketHeaderFields/CustomerRouting",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"PM\"","//PickticketBridge/Pickticket/PickticketHeaderFields/PickticketType",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"10\"","//PickticketBridge/Pickticket/PickticketHeaderFields/StatusCode",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"002\"","//PickticketBridge/Pickticket/PickticketHeaderFields/PktProfileID",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"1\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/PktLineNbr",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"001\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/SKU/SKUDefinition/Company",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"001\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/SKU/SKUDefinition/Division",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"R\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/SubSKUFields/InventoryType",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"*\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/SubSKUFields/BatchNumber",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"1\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/PickticketDetailFields/OrigOrderQty",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"1\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/PickticketDetailFields/OrigPktQty",actualOutputForReqPackMat);
		xmlunit.assertXpathValuesEqual("\"002\"","//PickticketBridge/Pickticket/ListOfPickticketDetails/PickticketDetail/PickticketDetailFields/PktProfileID",actualOutputForReqPackMat);		
	}

}