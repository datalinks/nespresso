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

public class TestProdDeclInterfaces extends OsbTester{

	//	Var for ProdDeclAven
	static String actualOutputForProdDeclAven;
	static String pdaServiceToTest 	= "GM_ProdDeclAven";
	static String pdaFileNameMask 		= "ASN_H_FFMW_AVE";
	static String pdaXmlFileName  		= "xsd/prod_decl/TST_PDA.xml";
	static long pdaStartTime;
	
	//	Var for ProdDeclOrbe
	static String actualOutputForProdDeclOrbe;
	static String pdoServiceToTest 	= "GM_ProdDeclOrbe";
	static String pdoXmlFileName  		= "xsd/prod_decl/TST_PDO.xml";
	static String pdoFileNameMask 		= "ASN_H_FFMW_ORB";
	static long pdoStartTime;
	
	
	@BeforeClass
	public static void init() throws Exception{
		initProdDeclAven();
		initProdDeclOrbe();
		waitForServiceToFinish();
	}
	
	private static void initProdDeclAven() throws Exception{
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(pdaXmlFileName);
        // getStartTime, needed to make sure that we take the last file
		pdaStartTime = getStartTimeFromFTP(ftpHost,directory,username,password, pdaFileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(pdaServiceToTest,contents.toString());
	}
	

	private static void initProdDeclOrbe() throws Exception{
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(pdoXmlFileName);
        // getStartTime, needed to make sure that we take the last file
		pdoStartTime = getStartTimeFromFTP(ftpHost,directory,username,password, pdoFileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(pdoServiceToTest,contents.toString());
	}
	
	
	/*
		Tests for GM_ProdDeclAven
	*/
	@Test
	public void testProdDeclAvenOutputValidSchema() throws Exception{
        String expectedSchema 	= "xsd/prod_decl/WMOS_InboundASNBridge_2_3_1_Schema.xsd";
        actualOutputForProdDeclAven = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,pdaStartTime,pdaFileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForProdDeclAven));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForProdDeclAven );
		}
		assertTrue(pdaServiceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testProdDeclAvenXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//InboundASN/ToLocation", actualOutputForProdDeclAven);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclAven);
		xmlunit.assertXpathExists("//InboundASN/BatchCtlNbr", actualOutputForProdDeclAven);		
	}

	@Test
	public void testProdDeclAvenXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"M_376401092900001554\"","//InboundASN/ShipmentNbr", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//InboundASN/ToLocation", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"2011-12-20T15:31:07\"","//InboundASN/ASNHeaderFields/ShippedDate", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"2011-12-20T15:31:07\"","//InboundASN/ASNHeaderFields/SchedStartRcvDate", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"10\"", "//InboundASN/ASNHeaderFields/StatusCode", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"0\"", "//InboundASN/ASNHeaderFields/ScheduledAppt", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"M\"", "//InboundASN/ASNHeaderFields/ASNOriginalType", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"MES\"", "//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclAven);
		
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNDetails/ASNDetail/SKUDefinition/Company", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNDetails/ASNDetail/SKUDefinition/Division", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"7810.10\"", "//InboundASN/ListOfASNDetails/ASNDetail/SKUDefinition/SizeDesc", actualOutputForProdDeclAven);
		
		
		
		xmlunit.assertXpathValuesEqual("\"1\"", "//InboundASN/ListOfASNDetails/ASNDetail/SequenceNbr", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"35200\"", "//InboundASN/ListOfASNDetails/ASNDetail/UnitsShipped", actualOutputForProdDeclAven);
		
		xmlunit.assertXpathValuesEqual("\"F\"", "//InboundASN/ListOfASNDetails/ASNDetail/SubSKUFields/InventoryType", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"13543786\"", "//InboundASN/ListOfASNDetails/ASNDetail/SubSKUFields/BatchNumber", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"MES\"", "//InboundASN/ListOfASNDetails/ASNDetail/PONbr", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"0\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/CasesShipped", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"Y\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/ProcessImmdNeeds", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"2011-12-20T00:00:00\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/ManufacturingDate", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"0\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/StatusCode", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"7640109290037\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/VendorID", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"2012-11-30T00:00:00\"", "//InboundASN/ListOfASNDetails/ASNDetail/ASNDetailFields/ExpireDate", actualOutputForProdDeclAven);
		
			
		xmlunit.assertXpathValuesEqual("\"376401092900001554\"", "//InboundASN/ListOfASNCases/ASNCase/CaseNbr", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/Warehouse", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/Company", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/Division", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"MES\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/PONbr", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"M_376401092900001554\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/OriginalShipmentNbr", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"2011-12-20T00:00:00\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/ManufacturingDate", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"2012-11-30T00:00:00\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/ExpireDate", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"Y\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/ProcessImmdNeeds", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"0\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/StatusCode", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"Y\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/SingleSkuCase", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"7640109290037\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/VendorID", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"P\"", "//InboundASN/ListOfASNCases/ASNCase/ASNCaseHeaderFields/PhysicalEntityCode", actualOutputForProdDeclAven);
		
		
		
		xmlunit.assertXpathValuesEqual("\"1\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SkuSequenceNbr", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SKUDefinition/Company", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"001\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SKUDefinition/Division", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"7810.10\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SKUDefinition/SizeDesc", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"35200\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/ShippedAsnQuantity", actualOutputForProdDeclAven);
		
		
		xmlunit.assertXpathValuesEqual("\"F\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SubSKUFields/InventoryType", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"13543786\"", "//InboundASN/ListOfASNCases/ASNCase/ListOfASNCaseDetails/ASNCaseDetail/SubSKUFields/BatchNumber", actualOutputForProdDeclAven);
		
		
		// BUG ID...BatchCtlNbr, generated number should not be larger than 10 characters
		xmlunit.assertXpathEvaluatesTo("10", "string-length(//InboundASN/BatchCtlNbr)",actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"7810.10\"", "//InboundASN/ListOfASNDetails/ASNDetail/SKUDefinition/SizeDesc", actualOutputForProdDeclAven);
		xmlunit.assertXpathValuesEqual("\"35200\"", "//InboundASN/ListOfASNDetails/ASNDetail/UnitsShipped", actualOutputForProdDeclAven);
	}


	
	/*
		Tests for GM_ProdDeclOrbe
	 */
	@Test
	public void testProdDeclOrbeOutputValidSchema() throws Exception{
	    String expectedSchema 	= "xsd/prod_decl/WMOS_InboundASNBridge_2_3_1_Schema.xsd";
	    //	Get the file..if the file is not present within a minute the test will fail
	    actualOutputForProdDeclOrbe = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,pdoStartTime,pdoFileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForProdDeclOrbe));
		//System.out.println("actualOutputForProdDeclOrbe"+actualOutputForProdDeclOrbe);
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForProdDeclOrbe );
		}
		assertTrue(pdoServiceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}
	
	@Test
	public void testProdDeclOrbeXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathExists("//InboundASN/ToLocation", actualOutputForProdDeclOrbe);
		xmlunit.assertXpathExists("//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclOrbe);
	}
	
	@Test
	public void testProdDeclOrbeXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//InboundASN/ToLocation", actualOutputForProdDeclOrbe);
		xmlunit.assertXpathValuesEqual("\"ORB\"", "//InboundASN/ASNHeaderFields/ShipmentType", actualOutputForProdDeclOrbe);
		xmlunit.assertXpathEvaluatesTo("10", "string-length(//InboundASN/BatchCtlNbr)",actualOutputForProdDeclOrbe);
		xmlunit.assertXpathValuesEqual("\"7431.10\"", "//InboundASN/ListOfASNDetails/ASNDetail/SKUDefinition/SizeDesc", actualOutputForProdDeclOrbe);
		xmlunit.assertXpathValuesEqual("\"2200\"", "//InboundASN/ListOfASNDetails/ASNDetail/UnitsShipped", actualOutputForProdDeclOrbe);
	}
	
}

