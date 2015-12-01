package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class NotifyParams {
	@Expose
	private String serviceId;
	@Expose
	private String bmsc;
	@Expose
	private String sailist;
	
	public String getServiceId() {
		return serviceId;
	}
	@XmlElement(name="serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getBmsc() {
		return bmsc;
	}
	@XmlElement(name="bmsc")
	public void setBmsc(String bmsc) {
		this.bmsc = bmsc;
	}
	public String getSailist() {
		return sailist;
	}
	@XmlElement(name="sailist")
	public void setSailist(String sailist) {
		this.sailist = sailist;
	}
}
