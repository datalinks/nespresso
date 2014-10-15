package com.nespresso.nesoatestsuite.interfaces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.XMLUnitTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;


public class TestWmosToFFMWInterfaces extends OsbTester{

	static String actualOutputForPackMateReqCancConf;
	static String actualOutputForPackMateOrdStat;
	static String actualOutputForPurcOrdeGoodRcpt;

	/*
		Title:			Tests for GM_PackMateReqCancConf 15.03
		Description:	From WMOS (through WMOS Filter), using the TestFacade, service=GM_PackMateReqCancConf 
						Then it will be handled by the GM_PackMateReqCancConf services, the output will be
						writen to the FFMW folder /data/ffmw/Avenches/Outbound/tpitro02
	*/
	
	
		@Test
		public void testPackMateReqCancConfOutputValidSchema() throws Exception{
			//	Initialize, needed for this test
			String serviceToTest 	= "GM_PackMateReqCancConf";
			String xmlFileName  	= "xsd/wmos_to_ffmw/TST_CANCCONF.xml";
	        String expectedSchema 	= "xsd/wmos_to_ffmw/B2MML-V02-TransferSchedule_Globe-V01_Schema.xsd";
	        String fileNameMask 	= "RC_WMS_AVE_";
	        String directory 		= "/data/ffmw/Avenches/Outbound/tpitro02/";

			// get Usable String content from XMLFile
			StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
	        // getStartTime, needed to make sure that we take the last file
	        long startTime = getTimeStampLastFileFromFFMW(directory, fileNameMask);
	        //	Call the ServiceFacade
	        HttpNesoaClient.callService(serviceToTest,contents.toString());
	        //	Wait for a while, 5 seconds is enough...I guess
	        waitForServiceToFinish(5000);
	        //	Get the file..if the file is not present within a minute the test will fail
	        actualOutputForPackMateReqCancConf = getActualXmlOutputFromFFMWFile(directory, startTime, fileNameMask);
			//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
			v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
			StreamSource is = new StreamSource(new StringReader(actualOutputForPackMateReqCancConf));
			boolean isValid = v.isInstanceValid(is);
			if(!isValid){
				errorList = compileErrorList(v, actualOutputForPackMateReqCancConf );
			}
			assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
		}

		@Test
		public void testPackMateReqCancConfOutput() throws Exception{
			XMLUnitTester xmlunit = new XMLUnitTester();
			xmlunit.assertXpathExists("//TransferSchedule/ID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/PublishedDate",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/ID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/ID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialDefinitionID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialLotID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialSubLotID", actualOutputForPackMateReqCancConf);
            xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentID",actualOutputForPackMateReqCancConf);
            xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
            xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialDefinitionID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialLotID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialSubLotID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/QuantityString",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/DataType",actualOutputForPackMateReqCancConf);
    		xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateReqCancConf);
			
		}

		@Test
		public void testPackMateReqCancConfValuesOutput() throws Exception{
			XMLUnitTester xmlunit = new XMLUnitTester();
			xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"2010-04-09T16:56:31\"","//TransferSchedule/PublishedDate",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"TO0000000346\"","//TransferSchedule/TransferRequest/ID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/ID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"ac08\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialDefinitionID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"10092510\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialLotID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"376101000205579800\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialSubLotID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"StorageZone\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"DSBin\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"25920.00\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"Unrestricted\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/globe_StockType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/globe_StorageUnitType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"ac08\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialDefinitionID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"10092510\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialLotID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"376101000205579800\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialSubLotID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"10H\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"StorageZone\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/ EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"WMS\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentID",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"25920.00\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/QuantityString",actualOutputForPackMateReqCancConf);
			xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/DataType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"Unrestricted\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/globe_StockType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/globe_StorageUnitType",actualOutputForPackMateReqCancConf);
	        xmlunit.assertXpathValuesEqual("\"2011-02-28T00:00:00\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/globe_ExpirationDate",actualOutputForPackMateReqCancConf);
	        
	        
		}
		
		@Test
		public void testPackMateReqCancConfXPathOutput() throws Exception{
			Document xmlDoc = XMLUnit.buildControlDocument(actualOutputForPackMateReqCancConf);
			String xpath; 
			String expectedValue;
			HashMap<String,String> m = new HashMap<String,String>();
		    //m.put("b2m", "http://www.wbf.org/xml/b2mml-v02");
		    NamespaceContext ctx = new SimpleNamespaceContext(m);
		    XpathEngine engine = XMLUnit.newXpathEngine();
		    engine.setNamespaceContext(ctx);
			
		    xpath = "//ID";
		    NodeList l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertFalse(xpath+" was NULL :(",l.item(0)==null);
		    assertTrue(xpath+" value is empty: "+l.item(0).getTextContent(),l.getLength()>0);
		    expectedValue = "NESOA_010297_";
		    assertTrue(xpath+" did not start with "+expectedValue,l.item(0).getTextContent().startsWith(expectedValue));
		    
		    
		    xpath = "//Location";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertFalse(xpath+" was NULL :(",l.item(0)==null);
		    assertTrue(xpath+" value is empty: "+l.item(0).getTextContent(),l.getLength()>0);

		    xpath="//Location/EquipmentID";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertFalse(xpath+" was NULL :(",l.item(0)==null);
		    assertTrue(xpath+" value is empty: ",l.getLength()>0);

		    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialDefinitionID";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertTrue(xpath+" value is empty: ",l.getLength()>0);
		    
		    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertTrue(xpath+" value is empty: ",l.getLength()>0);
		    
		    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertTrue(xpath+" value is empty: ",l.getLength()>0);
		    
		    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/UnitOfMeasure";
		    l = engine.getMatchingNodes(xpath, xmlDoc);
		    assertTrue(xpath+" value is empty: ",l.getLength()>0);
		}
		

	
	
	/*
	Title:			Tests for GM_PackMateReqOrdStat 15.02
	Description:	From WMOS (through WMOS Filter), using the TestFacade, service=GM_PackMateReqCancConf 
					Then it will be handled by the GM_PackMateReqCancConf services, the output will be
					writen to the FFMW folder /data/ffmw/Avenches/Outbound/tpitro02
*/
	
	@Test
	public void testPackMateReqOrdStatOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_PackMateReqOrdStat";
		String xmlFileName  	= "xsd/wmos_to_ffmw/TST_ORDSTAT.xml";
		String expectedSchema 	= "xsd/wmos_to_ffmw/B2MML-V02-TransferSchedule_Globe-V01_Schema.xsd";
	    String fileNameMask 	= "RC_WMS_AVE_";
	    String directory 		= "/data/ffmw/Avenches/Outbound/tpitro02/";
	
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
	    // getStartTime, needed to make sure that we take the last file
	    long startTime = getTimeStampLastFileFromFFMW(directory, fileNameMask);
	    //	Call the ServiceFacade
	    HttpNesoaClient.callService(serviceToTest,contents.toString());
	    //	Wait for a while, 5 seconds is enough...I guess
	    waitForServiceToFinish(5000);
	    //	Get the file..if the file is not present within a minute the test will fail
	    actualOutputForPackMateOrdStat = getActualXmlOutputFromFFMWFile(directory, startTime, fileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForPackMateOrdStat));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForPackMateOrdStat );
		}
		assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testPackMateOrdStatXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		    xmlunit.assertXpathExists("//TransferSchedule/ID",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/PublishedDate",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/ID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/ID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialDefinitionID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialLotID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialSubLotID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/ EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialDefinitionID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialLotID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialSubLotID",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateOrdStat);
		    xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/QuantityString",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/DataType",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathExists("//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateOrdStat);
	}

	@Test
	public void testPackMateOrdStatValuesOutput() throws Exception{
				XMLUnitTester xmlunit = new XMLUnitTester();
	
	        xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"2008-12-19T14:21:18\"","//TransferSchedule/PublishedDate",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/ID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"99999999999999999999\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/ MaterialSubLotID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/ EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"StorageZone\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/EquipmentElementLevel", actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/EquipmentElementLevel", actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel", actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Location/Globe_StorageLocation", actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"0\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"EA\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"Unrestricted\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/globe_StockType",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialProducedRequirement/globe_StorageUnitType",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"99999999999999999999\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/MaterialSubLotID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"3786\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"Site\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"Area\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"10H\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/ EquipmentID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"StorageZone\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/EquipmentElementLevel",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"WMS\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentID",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"StorageModule\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Location/Location/Location/Location/EquipmentElementLevel", actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"0001\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Location/Globe_StorageLocation",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"0\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/QuantityString",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/DataType",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"EA\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/Quantity/UnitOfMeasure",actualOutputForPackMateOrdStat);
	        xmlunit.assertXpathValuesEqual("\"Unrestricted\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/globe_StockType",actualOutputForPackMateOrdStat);
			xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferSchedule/TransferRequest/SegmentRequirement/MaterialConsumedRequirement/globe_StorageUnitType",actualOutputForPackMateOrdStat);
	}	

	@Test
	public void testPackMateReqOrdStatXPathOutput() throws Exception{
		Document xmlDoc = XMLUnit.buildControlDocument(actualOutputForPackMateOrdStat);
		String xpath; 
		String expectedValue;
		HashMap<String,String> m = new HashMap<String,String>();
	    //m.put("b2m", "http://www.wbf.org/xml/b2mml-v02");
	    NamespaceContext ctx = new SimpleNamespaceContext(m);
	    XpathEngine engine = XMLUnit.newXpathEngine();
	    engine.setNamespaceContext(ctx);
		
	    xpath = "//ID";
	    NodeList l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertFalse(xpath+" was NULL :(",l.item(0)==null);
	    assertTrue(xpath+" value is empty: "+l.item(0).getTextContent(),l.getLength()>0);
	    expectedValue = "NESOA_010297_";
	    assertTrue(xpath+" did not start with "+expectedValue,l.item(0).getTextContent().startsWith(expectedValue));
	    
	    
	    xpath = "//Location";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertFalse(xpath+" was NULL :(",l.item(0)==null);
	    assertTrue(xpath+" value is empty: "+l.item(0).getTextContent(),l.getLength()>0);
	
	    xpath="//Location/EquipmentID";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertFalse(xpath+" was NULL :(",l.item(0)==null);
	    assertTrue(xpath+" value is empty: ",l.getLength()>0);
	
	    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/MaterialDefinitionID";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertTrue(xpath+" value is empty: ",l.getLength()>0);
	    
	    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/QuantityString";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertTrue(xpath+" value is empty: ",l.getLength()>0);
	    
	    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/DataType";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertTrue(xpath+" value is empty: ",l.getLength()>0);
	    
	    xpath="//TransferRequest/SegmentRequirement/MaterialProducedRequirement/Quantity/UnitOfMeasure";
	    l = engine.getMatchingNodes(xpath, xmlDoc);
	    assertTrue(xpath+" value is empty: ",l.getLength()>0);
	}
	@Test
	public void testPackMateOrdStatConditionalOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		xmlunit.assertXpathValuesEqual("\"3004545974\"","//TransferSchedule/TransferRequest/ID",actualOutputForPackMateOrdStat);
		
	}


    @Test
	public void testPurcOrdeGoodRcptOutputValidSchema() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "GM_PurcOrdeGoodRcpt";
		String xmlFileName  	= "xsd/wmos_to_ffmw/TST_GOODRCPT.xml";
	    String expectedSchema 	= "xsd/wmos_to_ffmw/B2MML-V02-TransferPerformance_Globe-V01_Schema.xsd";
	    String fileNameMask 	= "R_WMS_AVE";
	    String directory 		= "/data/ffmw/Avenches/Outbound/tpigpu01/";
	
		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
	    // getStartTime, needed to make sure that we take the last file
	    long startTime = getTimeStampLastFileFromFFMW(directory, fileNameMask);
	    //	Call the ServiceFacade
	    HttpNesoaClient.callService(serviceToTest,contents.toString());
	    //	Wait for a while, 5 seconds is enough...I guess
	    waitForServiceToFinish(5000);
	    //	Get the file..if the file is not present within a minute the test will fail
	    actualOutputForPurcOrdeGoodRcpt = getActualXmlOutputFromFFMWFile(directory, startTime, fileNameMask);
		//	Validate XML with schema; Actual output against the XSD schema defined in the expectedSchema variable
		v.addSchemaSource(new StreamSource(new File(expectedSchema)));		
		StreamSource is = new StreamSource(new StringReader(actualOutputForPurcOrdeGoodRcpt));
		boolean isValid = v.isInstanceValid(is);
		if(!isValid){
			errorList = compileErrorList(v, actualOutputForPurcOrdeGoodRcpt );
		}
		assertTrue(serviceToTest+" service output according to "+expectedSchema+" validation is NOT VALID, errors are: "+errorList,isValid);
	}

	@Test
	public void testPurcOrdeGoodRcptXPathOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		    
		    xmlunit.assertXpathExists("//TransferPerformance/PublishedDate",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/ID",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/ID",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/MaterialDefinitionID",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/MaterialLotID",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/MaterialSubLotID",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Location/Globe_StorageLocation",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Quantity/QuantityString",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Quantity/DataType",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/Quantity/UnitOfMeasure",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/globe_StorageUnitType",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/globe_Item",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/globe_ExpirationDate",actualOutputForPurcOrdeGoodRcpt);
	        xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/globe_PostingDate",actualOutputForPurcOrdeGoodRcpt);
		    xmlunit.assertXpathExists("//TransferPerformance/TransferResponse/SegmentResponse/MaterialProducedActual/globe_ProductionDate",actualOutputForPurcOrdeGoodRcpt);
	}
	
	@Test
	public void testPurcOrdeGoodRcptValuesOutput() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
	xmlunit.assertXpathValuesEqual("\"2009-03-10T16:30:44\"","//TransferPerformance/PublishedDate",actualOutputForPurcOrdeGoodRcpt);
    xmlunit.assertXpathValuesEqual("\"5.62252\"","//TransferPerformance/TransferResponse[1]/ID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"5.62252\"","//TransferPerformance/TransferResponse[2]/ID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"4\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/ID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"5\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/ID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"7443\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/MaterialDefinitionID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"7810\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/MaterialDefinitionID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"SLTIN-P3\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/MaterialLotID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"SLTIN-P3\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/MaterialLotID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"3786\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"3786\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"Site\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"Site\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"CH2\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentID",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"Area\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"Area\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Location/Location/EquipmentElementLevel",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"0001\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Location/Globe_StorageLocation",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"0001\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Location/Globe_StorageLocation",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"35200.00000\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Quantity/QuantityString",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"35200.00000\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Quantity/QuantityString",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Quantity/DataType",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"decimal\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Quantity/DataType",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/globe_StorageUnitType",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"E2C\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/globe_StorageUnitType",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2009-11-22T00:00:00\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/globe_ExpirationDate",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2009-11-22T00:00:00\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/globe_ExpirationDate",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"0\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/globe_Item",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"0\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/globe_Item",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2009-03-10T16:18:42\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/globe_PostingDate",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2009-03-10T16:18:45\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/globe_PostingDate",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2008-11-20T00:00:00\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/globe_ProductionDate",actualOutputForPurcOrdeGoodRcpt);
	xmlunit.assertXpathValuesEqual("\"2008-11-20T00:00:00\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/globe_ProductionDate",actualOutputForPurcOrdeGoodRcpt);
    xmlunit.assertXpathValuesEqual("\"WMS\"","//TransferPerformance/globe_UserName",actualOutputForPurcOrdeGoodRcpt);
	
	}
	public void testPurcOrdeGoodRcptConditionalOutput1() throws Exception{
		XMLUnitTester xmlunit = new XMLUnitTester();
		
		xmlunit.assertXpathValuesEqual("\"EA\"","//TransferPerformance/TransferResponse[1]/SegmentResponse/MaterialProducedActual/Quantity/UnitOfMeasure",actualOutputForPurcOrdeGoodRcpt);
		xmlunit.assertXpathValuesEqual("\"EA\"","//TransferPerformance/TransferResponse[2]/SegmentResponse/MaterialProducedActual/Quantity/UnitOfMeasure",actualOutputForPurcOrdeGoodRcpt);
		
	}
	

}


