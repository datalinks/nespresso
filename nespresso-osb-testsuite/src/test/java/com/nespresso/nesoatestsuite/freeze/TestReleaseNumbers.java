package com.nespresso.nesoatestsuite.freeze;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.nespresso.nesoatestsuite.OsbTester;

/*
 * 	This test is for checking if people are obeying the Freeze period :)
 * 	which means that people are working on the trunk...while they should not 
 * 
 */
public class TestReleaseNumbers extends OsbTester{

	static String versionFile 	= "/home/soa/software/scripts/trunk_controller/versions.txt";
	static String versionResult = "/home/soa/software/scripts/trunk_controller/trunk_result.txt";
	static String versionPage 	= "/home/soa/software/apache-tomcat-6.0.33/webapps/NesOAv2Documentation/version.html";

	

    @Test
    public void testIsVersionFilePresent()throws Exception{
            File verFile = null;
            try{
               verFile = new File(versionFile);
            }catch(Exception ex){
               assertTrue("Versionfile: "+versionFile+" does not exist",false);
            }
            assertTrue("Versionfile: "+versionFile+" is not readable",verFile.canRead());
    }
	
	@Test
	public void testIsVersionPagePresent()throws Exception{
		File verFile = new File(versionPage);
		assertTrue("Versionfile: "+versionFile+" does not exist",verFile.exists());
	}


    @Test
    public void testIsCodeFrozen()throws Exception{
            Runtime.getRuntime().exec("/home/soa/software/scripts/trunk_controller/check.sh");

            try{
            	@SuppressWarnings("unused")
				File resultFile = new File(versionResult);
            }catch(Exception ex){
               assertTrue("RESULT: "+versionResult+" does not exist",false);
            }
            Thread.sleep(60000);
            StringBuilder results = buildInputStringFromXmlFile(versionResult);
            String result = results.toString();
            assertTrue("There are differences: \n\n"+result,result.length()<2);

    }
}
