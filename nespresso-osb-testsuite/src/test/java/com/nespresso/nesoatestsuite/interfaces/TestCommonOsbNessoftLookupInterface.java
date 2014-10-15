package com.nespresso.nesoatestsuite.interfaces;

import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;
import com.nespresso.nesoatestsuite.utils.HttpNesoaClient;

public class TestCommonOsbNessoftLookupInterface extends OsbTester{

	static String actualOutputMaterialLookup;
	
	@Test
	public void testNessoftMaterialLookupOutput() throws Exception{
		//	Initialize, needed for this test
		String serviceToTest 	= "CommonNessoftLookup_PS";
		String xmlFileName  	= "xsd/common/Nessoft_MaterialLookup.xml";

		// get Usable String content from XMLFile
		StringBuilder contents = buildInputStringFromXmlFile(xmlFileName);
        String result = HttpNesoaClient.callService(serviceToTest,contents.toString());
       	System.out.println(result);
	}

}

