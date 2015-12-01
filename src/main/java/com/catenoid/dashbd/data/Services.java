package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Services {
	
	@Expose
	private Service service;

	public Service getService() {
		return service;
	}

	@XmlElement(name="service")
	public void setService(Service service) {
		this.service = service;
	}
}
