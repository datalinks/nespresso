package com.nespresso.nesoatestsuite.tests;

import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import oracle.security.jps.service.idm.asserter.AsserterException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nespresso.nesoatestsuite.utils.DomainHolder;
import com.nespresso.nesoatestsuite.utils.DomainInfoCollector;

public class TestDomainExpectations {
	static HashMap<DomainHolder,Map<String,String>> domainQueues = new HashMap<DomainHolder, Map<String,String>>();
	static HashMap<DomainHolder,Map<String,String>> distrDomainQueues = new HashMap<DomainHolder, Map<String,String>>();
	static DomainHolder dev0Domain;
	static DomainHolder devDomain;
	static DomainHolder tstDomain;
	static DomainHolder preProdDomain;
	static DomainHolder prodDomain;
	
	@BeforeClass
	public static void init(){
		dev0Domain = new DomainHolder("DEV","hqchnesoa102.nespresso.com",7101,"weblogic","weblogic1");
		devDomain = new DomainHolder("DEV FUT","hqchnesoa102.nespresso.com",7001,"weblogic","weblogic1");
		tstDomain = new DomainHolder("TEST","hqchnesoa104.nespresso.com",7001,"weblogic","weblogic1");
	    preProdDomain = new DomainHolder("PRE_PROD","hqchnesoa105.nespresso.com",7001,"weblogic","weblogic1");
		prodDomain = new DomainHolder("PROD","hqchnesoa107.nespresso.com",7001,"weblogic","weblogic1");
		DomainHolder[] domains = { dev0Domain,devDomain,tstDomain,preProdDomain,prodDomain };
		DomainInfoCollector dic = new DomainInfoCollector(domains);
		
		domainQueues.put(dev0Domain, dic.collectQueueInfo(dev0Domain));
		domainQueues.put(devDomain, dic.collectQueueInfo(devDomain));
		domainQueues.put(tstDomain, dic.collectQueueInfo(tstDomain));
		domainQueues.put(preProdDomain, dic.collectQueueInfo(preProdDomain));
		domainQueues.put(prodDomain, dic.collectQueueInfo(prodDomain));
		
		distrDomainQueues.put(dev0Domain, dic.collectQueueInfo(dev0Domain));
		distrDomainQueues.put(devDomain, dic.collectQueueInfo(devDomain));
		distrDomainQueues.put(tstDomain, dic.collectQueueInfo(tstDomain));
		distrDomainQueues.put(preProdDomain, dic.collectQueueInfo(preProdDomain));
		distrDomainQueues.put(prodDomain, dic.collectQueueInfo(prodDomain));
	}
	
	@Test
	public void testExpectedQueuesOnTest() throws Exception{
		Map<String, String> devQueues = domainQueues.get(devDomain);
		Map<String, String> tstQueues = domainQueues.get(tstDomain);
		Iterator<String> devJndiNames = devQueues.values().iterator();
		boolean errorFound = false;
		StringBuffer message = new StringBuffer();
		while(devJndiNames.hasNext()){
			String devJndiName = devJndiNames.next();
			if(!tstQueues.values().contains(devJndiName)){
				errorFound = true;
				message.append("TEST, check if Queue on "+devDomain.getName()+" with queuename: "+devJndiName+" existst in "+tstDomain.getName()+" failed!!!\n");
			}
		}
		assertFalse(message.toString(),errorFound);
	}

	@Test
	public void testExpectedDistributedQueuesOnTest() throws Exception{
		Map<String, String> distrDevQueues = distrDomainQueues.get(devDomain);
		Map<String, String> distrTstQueues = distrDomainQueues.get(tstDomain);
		Iterator<String> devJndiNames = distrDevQueues.values().iterator();
		boolean errorFound = false;
		StringBuffer message = new StringBuffer();
		while(devJndiNames.hasNext()){
			String devJndiName = devJndiNames.next();
			if(!distrTstQueues.values().contains(devJndiName)){
				errorFound = true;
				message.append("TEST, check if distr Queue on "+devDomain.getName()+" with distr queuename: "+devJndiName+" existst in "+tstDomain.getName()+" failed!!!\n");
			}
		}
		assertFalse(message.toString(),errorFound);
	}


	
	@Test
	public void testExpectedQueuesOnDev0() throws Exception{
		Map<String, String> devQueues = domainQueues.get(devDomain);
		Map<String, String> dev0Queues = domainQueues.get(dev0Domain);
		Iterator<String> devJndiNames = devQueues.values().iterator();
		boolean errorFound = false;
		StringBuffer message = new StringBuffer();
		while(devJndiNames.hasNext()){
			String devJndiName = devJndiNames.next();
			if(!dev0Queues.values().contains(devJndiName)){
				errorFound = true;
				message.append("TEST, check if Queue on "+devDomain.getName()+" with queuename: "+devJndiName+" existst in "+dev0Domain.getName()+" failed!!!\n");
			}
		}
		assertFalse(message.toString(),errorFound);
	}

	@Test
	public void testExpectedDistributedQueuesOnDev0() throws Exception{
		Map<String, String> distrDevQueues = distrDomainQueues.get(devDomain);
		Map<String, String> distrDev0Queues = distrDomainQueues.get(devDomain);
		Iterator<String> devJndiNames = distrDevQueues.values().iterator();
		boolean errorFound = false;
		StringBuffer message = new StringBuffer();
		while(devJndiNames.hasNext()){
			String devJndiName = devJndiNames.next();
			if(!distrDev0Queues.values().contains(devJndiName)){
				errorFound = true;
				message.append("TEST, check if distr Queue on "+devDomain.getName()+" with distr queuename: "+devJndiName+" existst in "+dev0Domain.getName()+" failed!!!\n");
			}
		}
		assertFalse(message.toString(),errorFound);
	}

	
	
	
}
