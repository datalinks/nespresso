package com.nespresso.nesoatestsuite.interfaces;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.XMLUnitTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;

public class TestProdDeclReversalInterfaces extends OsbTester{

	//	Var for ProdDeclAven Reversal
	static String actualOutputForProdDeclAvenReversal;
	static String pdaRevServiceToTest 	= "GM_ProdDeclAven";
	static String pdaRevFileNameMask 	= "ASN_H_FFMW_AVE";
	static String pdaRevXmlFileName 	= "xsd/prod_decl/TST_PDA_REV.xml";
	static long pdaRevStartTime;
	
	//	Var for ProdDeclOrbe Reversal
	static String actualOutputForProdDeclOrbeReversal;
	static String pdoRevServiceToTest 	= "GM_ProdDeclOrbe";
	static String pdoRevXmlFileName  	= "xsd/prod_decl/TST_PDO_REV.xml";
	static String pdoRevFileNameMask 	= "ASN_H_FFMW_ORB";
	static long pdoRevStartTime;

	
	@BeforeClass
	public static void init() throws Exception{
		initProdDeclAvenReversal();
		initProdDeclOrbeReversal();
		waitForServiceToFinish();
	}
	
	private static void initProdDeclAvenReversal() throws Exception{
		String directory 	= "/wmos/environments/env-0/ave/";
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(pdaRevXmlFileName);
        // getStartTime, needed to make sure that we take the last file
		pdaRevStartTime = getStartTimeFromFTP(ftpHost,directory,username,password, pdaRevFileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(pdaRevServiceToTest,contents.toString());
	}

	private static void initProdDeclOrbeReversal() throws Exception{
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(pdoRevXmlFileName);
        // getStartTime, needed to make sure that we take the last file
		pdoRevStartTime = getStartTimeFromFTP(ftpHost,directory,username,password, pdoRevFileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(pdoRevServiceToTest,contents.toString());
	}

	
	

	/*
		Tests for GM_ProdDeclAvenReversal
	 */
	@Test
	public void testProdDeclAvenReversalOutputValidSchema() throws Exception{
        String expectedSchema 	= "xsd/prod_decl/WMOS_InboundASNBridge_2_3_1_Schema.xsd";
        actualOutputForProdDeclAvenReversal = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,pdaRevStartTime,pdaRevFileNameMask);
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForProdDeclAvenReversal));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForProdDeclAvenReversal );
		}
		assertTrue(pdaRevServiceToTest+" Reversal service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}
	
	@Test
	public void testProdDeclAvenReversalXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//InboundASN/ToLocation", actualOutputForProdDeclAvenReversal);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/MiscInstrCode1", actualOutputForProdDeclAvenReversal);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclAvenReversal);
	}

	
	@Test
	public void testProdDeclAvenReversalXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//InboundASN/ToLocation", actualOutputForProdDeclAvenReversal);
		xmlunit.assertXpathValuesEqual("\"CB\"", "//InboundASN/ASNHeaderFields/MiscInstrCode1", actualOutputForProdDeclAvenReversal);
		xmlunit.assertXpathValuesEqual("\"MES\"", "//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclAvenReversal);		
		xmlunit.assertXpathEvaluatesTo("10", "string-length(//InboundASN/BatchCtlNbr)",actualOutputForProdDeclAvenReversal);
	}

	
	
	/*
		Tests for GM_ProdDeclOrbeReversal
	 */
/*	@Test
	public void testProdDeclOrbeReversalOutputValidSchema() throws Exception{
	    String expectedSchema 	= "xsd/prod_decl/WMOS_InboundASNBridge_2_3_1_Schema.xsd";
	    actualOutputForProdDeclOrbeReversal = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,pdoRevStartTime,pdoRevFileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForProdDeclOrbeReversal));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForProdDeclOrbeReversal );
		}
		assertTrue(pdoRevServiceToTest+" reversal service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}
	
	@Test
	public void testProdDeclOrbeReversalXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//InboundASN/ToLocation", actualOutputForProdDeclOrbeReversal);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/MiscInstrCode1", actualOutputForProdDeclOrbeReversal);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclOrbeReversal);
	}
	
	
	@Test
	public void testProdDeclOrbeReversalXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//InboundASN/ToLocation", actualOutputForProdDeclOrbeReversal);
		xmlunit.assertXpathValuesEqual("\"CA\"", "//InboundASN/ASNHeaderFields/MiscInstrCode1", actualOutputForProdDeclOrbeReversal);
		xmlunit.assertXpathValuesEqual("\"ORB\"", "//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclOrbeReversal);		
		xmlunit.assertXpathEvaluatesTo("10", "string-length(//InboundASN/BatchCtlNbr)",actualOutputForProdDeclOrbeReversal);		
	}*/

	
}

