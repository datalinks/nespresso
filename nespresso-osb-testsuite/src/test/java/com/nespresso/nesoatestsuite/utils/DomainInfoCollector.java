package com.nespresso.nesoatestsuite.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.j2ee.SessionBeanMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;

public class DomainInfoCollector {
	
    protected final Log logger = LogFactory.getLog(getClass());
	
    
	DomainMBean domainMBean = null;
	DomainRuntimeServiceMBean domainRuntimeServiceMBean = null;
	SessionBeanMBean sessionManagementMBean = null;
	
	public DomainInfoCollector(DomainHolder[] domains){
		for(DomainHolder domain : domains){
			try {
				domain.setConnection(setConnection(domain));
			} catch (MalformedURLException e) {
				logger.error("Error MalformedURLException setting connection for domain "+domain.getHost());
			} catch (IOException e) {
				logger.error("Error IOException setting connection for domain "+domain.getHost());
			}
		}
		
	}
	
	
	private MBeanServerConnection setConnection(DomainHolder domainParam) throws IOException,MalformedURLException {
		JMXServiceURL serviceURL = new JMXServiceURL("t3", domainParam.getHost(), domainParam.getPort(), "/jndi/" + DomainRuntimeServiceMBean.MBEANSERVER_JNDI_NAME);
		Hashtable<String,Object> h = new Hashtable<String,Object>();
		h.put(Context.SECURITY_PRINCIPAL, domainParam.getUsername());
		h.put(Context.SECURITY_CREDENTIALS, domainParam.getPassword());
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
		return JMXConnectorFactory.connect(serviceURL, h).getMBeanServerConnection();
	}	


	
	@SuppressWarnings("unused")
	private void closeConnection(JMXConnector jmxConn) {
		try {
			jmxConn.close();
		} catch (IOException ex) {
			logger.error("Exception in closeing connection: "+ex.getMessage());
		}finally{
			try{
				jmxConn.close();
			} catch (IOException ex) {
				logger.error("Exception in final stage of closeing connection: "+ex.getMessage());
			}		
		}
	}
	
	public Map<String,String> collectDistributedQueueInfo(DomainHolder domainParam){
		Map<String, String> result = new HashMap<String, String>();
		try {
			DomainRuntimeServiceMBean domainRuntimeServiceMBean = (DomainRuntimeServiceMBean) MBeanServerInvocationHandler.newProxyInstance(domainParam.getConnection(), new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));		
			DomainMBean dem = domainRuntimeServiceMBean.getDomainConfiguration();
			JMSSystemResourceMBean[] jmsSRs = dem.getJMSSystemResources();
			
			for (JMSSystemResourceMBean jmsSR : jmsSRs){
				DistributedQueueBean[] dQbs = jmsSR.getJMSResource().getDistributedQueues();
				for (DistributedQueueBean dQb : dQbs){
					result.put("DISTR_QUEUE_"+dQb.getName(),dQb.getJNDIName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	public Map<String,String> collectQueueInfo(DomainHolder domainParam){
		Map<String, String> result = new HashMap<String, String>();
		try {
			DomainRuntimeServiceMBean domainRuntimeServiceMBean = (DomainRuntimeServiceMBean) MBeanServerInvocationHandler.newProxyInstance(domainParam.getConnection(), new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));		
			DomainMBean dem = domainRuntimeServiceMBean.getDomainConfiguration();
			JMSSystemResourceMBean[] jmsSRs = dem.getJMSSystemResources();
			
			for (JMSSystemResourceMBean jmsSR : jmsSRs){
				QueueBean[] qbs = jmsSR.getJMSResource().getQueues();
				for (QueueBean qb : qbs){
					result.put("QUEUE_"+qb.getName(),qb.getJNDIName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
