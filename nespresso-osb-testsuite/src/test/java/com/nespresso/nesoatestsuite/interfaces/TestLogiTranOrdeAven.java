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

public class TestLogiTranOrdeAven extends OsbTester{

	static String actualOutputForLogiTranOrdeAven;
	
	
	/*
	Tests for GM_LogiTranOrdeAve
*/
	
	@Test
	public void testLogiTranOrdeAveOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_LogiTranOrdeAve";
		String xmlFileName  	= "xsd/logi_tran_order/TST_LTO.xml";
        String expectedSchema 	= "xsd/logi_tran_order/BatchMasterBridge_2_3.xsd";
        String fileNameMask 	= "LTO_FFMW_AVE_";

		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        // getStartTime, needed to make sure that we take the last file
        long startTime = getStartTimeFromFTP(ftpHost,directory,username,password, fileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(serviceToTest,contents.toString());
        //	Wait for a while
        waitForServiceToFinish(70000);
        //	Get the file..if the file is not present within a minute the test will fail
        actualOutputForLogiTranOrdeAven = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,startTime,fileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForLogiTranOrdeAven));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForLogiTranOrdeAven );
		}
		assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testLogiTransOrderXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/BatchNbr", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/Company", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/Division", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc", actualOutputForLogiTranOrdeAven);
	}

	
	@Test
	public void testLogiTransOrderXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"LTO\"", "//BatchMasterBridge/BatchMaster/BatchNbr", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//BatchMasterBridge/BatchMaster/SKUDefinition/Company", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//BatchMasterBridge/BatchMaster/SKUDefinition/Division", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"43205155\"", "//BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"43205155\"", "//BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"476401092900000165\"", "//BatchMasterBridge/BatchMaster[1]/BatchMasterFields/MiscField1", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"476401092900000127\"", "//BatchMasterBridge/BatchMaster[2]/BatchMasterFields/MiscField1", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"UNRESTRICT\"", "//BatchMasterBridge/BatchMaster[1]/BatchMasterFields/MiscField2", actualOutputForLogiTranOrdeAven);
		xmlunit.assertXpathValuesEqual("\"UNRESTRICT\"", "//BatchMasterBridge/BatchMaster[2]/BatchMasterFields/MiscField2", actualOutputForLogiTranOrdeAven);
		
	}


	/*@Test
	public void testLogiTransOrderCheckStringLength() throws Exception{
		String valueSizeDesc = getNodeValueFromString("//BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc/text()", actualOutputForLogiTranOrdeAven);
		assertTrue("TEST failed: Expected //BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc/ smaller or EQ 15, actual "+valueSizeDesc, new String(valueSizeDesc).length()<=15);
		String valueFirstMiscField1 = getNodeValueFromString("//BatchMasterBridge/BatchMaster[1]/BatchMasterFields/MiscField1/text()", actualOutputForLogiTranOrdeAven);
		assertTrue("TEST failed: Expected //BatchMasterBridge/BatchMaster[1]/BatchMasterFields/MiscField1/ smaller or EQ 20, actual "+valueFirstMiscField1, new String(valueFirstMiscField1).length()<=20);
		String valueSecondMiscField1 = getNodeValueFromString("//BatchMasterBridge/BatchMaster[2]/BatchMasterFields/MiscField1/text()", actualOutputForLogiTranOrdeAven);
		assertTrue("TEST failed: Expected //BatchMasterBridge/BatchMaster[2]/BatchMasterFields/MiscField1/ smaller or EQ 20, actual "+valueSecondMiscField1, new String(valueSecondMiscField1).length()<=20);
		String valueMiscField2 = getNodeValueFromString("//BatchMasterBridge/BatchMaster/BatchMasterFields/MiscField2/text()", actualOutputForLogiTranOrdeAven);
		assertTrue("TEST failed: Expected //BatchMasterBridge/BatchMaster/BatchMasterFields/MiscField2/ smaller or EQ 20, actual "+valueMiscField2, new String(valueMiscField2).length()<=20);
	}	
*/
}

