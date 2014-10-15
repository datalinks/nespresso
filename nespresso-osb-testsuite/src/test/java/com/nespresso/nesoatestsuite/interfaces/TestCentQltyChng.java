/**
 * 
 */
package com.nespresso.nesoatestsuite.interfaces;

/**
 * @author amohamma
 *
 */

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.XMLUnitTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;

public class TestCentQltyChng extends OsbTester{

	static String actualOutputForCentQltyChng;
	
	
	/*
	Tests for GM_CentQltyChng
*/
	
	@Test
	public void testCentQltyChngOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_CentQltyChng";
		String xmlFileName  	= "xsd/cent_qlty_chng/CQC_AVE_from_FFMW.xml";
        String expectedSchema 	= "xsd/cent_qlty_chng/BatchMasterBridge_2_3.xsd";
        String fileNameMask 	= "CQC_FFMW_AVE_";
        
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        // getStartTime, needed to make sure that we take the last file
        long startTime = getStartTimeFromFTP(ftpHost,directory,username,password, fileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(serviceToTest,contents.toString());
        //	Wait for a while
        waitForServiceToFinish(70000);
        //	Get the file..if the file is not present within a minute the test will fail
        actualOutputForCentQltyChng = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,startTime,fileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForCentQltyChng));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForCentQltyChng );
		}
		assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testCentQltyChngXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/BatchNbr",actualOutputForCentQltyChng);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/Company",actualOutputForCentQltyChng);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/Division",actualOutputForCentQltyChng);
		xmlunit.assertXpathExists("//BatchMasterBridge/BatchMaster/SKUDefinition/SizeDesc",actualOutputForCentQltyChng);
	}

	
	@Test
	public void testCentQltyChngXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"CQC\"","//BatchMasterBridge/BatchMaster/BatchNbr",actualOutputForCentQltyChng);
		xmlunit.assertXpathValuesEqual("\"001\"","//BatchMasterBridge/BatchMaster/SKUDefinition/Company",actualOutputForCentQltyChng);
		xmlunit.assertXpathValuesEqual("\"001\"","//BatchMasterBridge/BatchMaster/SKUDefinition/Division",actualOutputForCentQltyChng);
		
	}
}

