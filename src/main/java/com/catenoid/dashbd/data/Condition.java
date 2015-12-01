package com.catenoid.dashbd.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Condition {
	@Expose
	private ArrayList<String> serviceId;

	public ArrayList<String> getServiceId() {
		return serviceId;
	}

	@XmlElement(name="serviceId")
	public void setServiceId(ArrayList<String> serviceId) {
		this.serviceId = serviceId;
	}
}
