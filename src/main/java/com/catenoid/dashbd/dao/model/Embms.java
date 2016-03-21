package com.catenoid.dashbd.dao.model;

public class Embms {
	Integer id;
	Integer bmscId;
	String serverName = "";
	String protocol = "";
	String IPAddress = "";
	String loginId = "";
	String password = "";
	String command = "";
	String session = "";
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	
    
	public Integer getBmscId() {
		return bmscId;
	}

	public void setBmscId(Integer bmscId) {
		this.bmscId = bmscId;
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	
}
