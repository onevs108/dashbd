package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Transaction {
	
	@Expose
	private Integer id;

	public Integer getId() {
		return id;
	}

	@XmlAttribute(name="id")
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Expose
	private AgentKey agentKey;

	public AgentKey getAgentKey() {
		return agentKey;
	}

	@XmlElement(name="agentKey")
	public void setAgentKey(AgentKey agentKey) {
		this.agentKey = agentKey;
	}
}
