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

public class TestMateMastAven extends OsbTester{
	
static String actualOutputForItemMaster;
static String actualOutputForCrossRef;
static String outputValue;
	/*
	Tests for GM_MateMastAven
	*/
	// Test cases for Item master file
	@Test
	public void testMateMastAvenOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_MateMastAven";
		String xmlFileName  	= "xsd/mate_mast_ave/Tst_Mate_Mast123.txt";
        String expectedItemMasterSchema 	= "xsd/mate_mast_ave/ItemMasterBridge_2_3_1.xsd";
        String expectedCrossRefSchema 	= "xsd/mate_mast_ave/CrossReferenceBridge_2_3_1.xsd";
        String ItemMasterfileNameMask 	= "ITEM_C_FFMW_AVE_";
        String CrossReffileNameMask 	= "ITEM_X_FFMW_AVE_";
        

		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        // getStartTime, needed to make sure that we take the last file
        long ItemMasterstartTime = getStartTimeFromFTP(ftpHost,directory,username,password, ItemMasterfileNameMask);
        long CrossRefstartTime = getStartTimeFromFTP(ftpHost,directory,username,password, CrossReffileNameMask);
        //	Call the ServiceFacade
        HttpNesoaClient.callService(serviceToTest,contents.toString());
        //	Wait for a while
        waitForServiceToFinish(70000);
        //	Get the file..if the file is not present within a minute the test will fail
        actualOutputForItemMaster = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,ItemMasterstartTime,ItemMasterfileNameMask);
        actualOutputForCrossRef = getActualXmlOutputFromFTPAsString(ftpHost,directory,username,password,CrossRefstartTime,CrossReffileNameMask);
		//	Validate Item Master XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedItemMasterSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForItemMaster));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForItemMaster );
		}
		assertTrue(serviceToTest+" service output according to "+expectedItemMasterSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	
//		Validate Cross Reference XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		
		v.addSchemaSource(new StreamSource(new File(expectedCrossRefSchema)));      
        StreamSource isCrossRef = new StreamSource(new StringReader(actualOutputForCrossRef));
        outputValue=isCrossRef.toString();
        StringBuffer sb=new StringBuffer("CrossReferenceBridge");
        
       
        if(!outputValue.toString().contains("<CrossReferenceBridge"))
    {
              
    }
        else{
        boolean isCrossRefValid = v.isInstanceValid(isCrossRef);
        if(!isCrossRefValid){
              errorList = compileErrorList(v, actualOutputForCrossRef );
              
              
        }
        assertTrue(serviceToTest+" service output according to "+expectedCrossRefSchema+" validation is NOT VALID, errors are: "+errorList,isCrossRefValid);
     // Test cases for Cross Reference file
    	
    	
    	//@Test
    	
       // testMateMastAvenCrossRefXPathValuesOutput();
        }
  }
	

	
	@Test
	public void testMateMastAvenItemMasterXPathValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"001\"", "//ItemMasterBridge/ItemMaster/Item/SKUDefinition/Company", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"001\"", "//ItemMasterBridge/ItemMaster/Item/SKUDefinition/Division", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"RM\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/ProductGroup", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"U\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/StockingUM", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"N\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/PromptPackQty", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"0\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/SerialNbrReqYN", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"0\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/AcceptManufacDate", actualOutputForItemMaster);
	    xmlunit.assertXpathValuesEqual("\"0\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/AcceptExpirationDate", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"0\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/StatusCode", actualOutputForItemMaster); 
		xmlunit.assertXpathValuesEqual("\"1\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/TrackBatchNbr", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"02\"", "//ItemMasterBridge/ItemMaster/Item/ItemMasterFields/SkuProfileID", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"AVE\"", "//ItemMasterBridge/ItemMaster/ListOfItemWarehouses/ItemWarehouse/Warehouse", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"0\"", "//ItemMasterBridge/ItemMaster/ListOfItemWarehouses/ItemWarehouse/ItemWarehouseFields/StatusCode", actualOutputForItemMaster);
		xmlunit.assertXpathValuesEqual("\"02\"", "//ItemMasterBridge/ItemMaster/ListOfItemWarehouses/ItemWarehouse/ItemWarehouseFields/SkuProfileID", actualOutputForItemMaster);
		
	}
	
	
	       
    @Test
	public void testMateMastAvenCrossRefXPathValuesOutput() throws Exception{
		if(outputValue.toString().contains("<CrossReferenceBridge"))
		{ 
			XMLUnitTester xmlunit = new XMLUnitTester();
			xmlunit.assertXpathValuesEqual("\"001\"", "//CrossReferenceBridge/CrossRef/Company", actualOutputForCrossRef);
			xmlunit.assertXpathValuesEqual("\"001\"", "//CrossReferenceBridge/CrossRef/Division", actualOutputForCrossRef);
			xmlunit.assertXpathValuesEqual("\"1\"", "//CrossReferenceBridge/CrossRef/CrossReferenceFields/ScanQty", actualOutputForCrossRef);
			xmlunit.assertXpathValuesEqual("\"0\"", "//CrossReferenceBridge/CrossRef/StatusCode", actualOutputForCrossRef);
		}
	}
	
	}
	
	

	
	
	

