package com.catenoid.dashbd.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.gson.annotations.Expose;

public class Parameters {
	
	@Expose
	private ArrayList<Service> services;

	public ArrayList<Service> getServices() {
		return services;
	}

	@XmlElementWrapper(name="services")
	@XmlElement(name="service")
	public void setServices(ArrayList<Service> services) {
		this.services = services;
	}
	
	public ServiceQuery getServiceQuery() {
		return serviceQuery;
	}

	@XmlElement(name="serviceQuery")
	public void setServiceQuery(ServiceQuery serviceQuery) {
		this.serviceQuery = serviceQuery;
	}

	@Expose
	private ServiceQuery serviceQuery;
	
	@Expose
	private Notify notify;

	public Notify getNotify() {
		return notify;
	}

	@XmlElement(name="notify")
	public void setNotify(Notify notify) {
		this.notify = notify;
	}
}
