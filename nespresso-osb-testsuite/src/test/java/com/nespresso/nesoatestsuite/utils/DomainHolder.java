package com.nespresso.nesoatestsuite.utils;

import javax.management.MBeanServerConnection;

public class DomainHolder {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	MBeanServerConnection connection;
	
	public MBeanServerConnection getConnection() {
		return connection;
	}

	public void setConnection(MBeanServerConnection connection) {
		this.connection = connection;
	}

	private String host;
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private int port;
	private String username;
	private String password;
	
	public DomainHolder(String nameParam,String hostParam, int portParam, String usernameParam, String passwordParam){
		this.name = nameParam;
		this.host = hostParam;
		this.port = portParam;
		this.username = usernameParam;
		this.password = passwordParam;
	}
}
