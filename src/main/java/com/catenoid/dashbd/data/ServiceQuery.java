package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class ServiceQuery {
	@Expose
	private Condition condition;
	public Condition getCondition() {
		return condition;
	}

	@XmlElement(name="condition")
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
