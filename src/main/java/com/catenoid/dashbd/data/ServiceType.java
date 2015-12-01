package com.catenoid.dashbd.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class ServiceType {

	@Expose
	private String serviceId;
	
	@Expose
	private String serviceClass;

	public String getServiceClass() {
		return serviceClass;
	}

	@XmlAttribute(name="serviceClass")
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceId() {
		return serviceId;
	}

	@XmlAttribute(name="serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Expose
	private TransferConfig transferConfig;

	public TransferConfig getTransferConfig() {
		return transferConfig;
	}
	
	@XmlElement(name="transferConfig")
	public void setTransferConfig(TransferConfig transferConfig) {
		this.transferConfig = transferConfig;
	}
	
	public ArrayList<Schedule> getSchedule() {
		return schedule;
	}

	@XmlElement(name="schedule")
	public void setSchedule(ArrayList<Schedule> schedule) {
		this.schedule = schedule;
	}

	public ArrayList<ContentSet> getContentSet() {
		return contentSet;
	}

	public void setContentSet(ArrayList<ContentSet> contentSet) {
		this.contentSet = contentSet;
	}

	public AssociatedDelivery getAssociatedDelivery() {
		return associatedDelivery;
	}

	public void setAssociatedDelivery(AssociatedDelivery associatedDelivery) {
		this.associatedDelivery = associatedDelivery;
	}

	public ArrayList<ServiceName> getName() {
		return name;
	}

	@XmlElement(name="name")
	public void setName(ArrayList<ServiceName> name) {
		this.name = name;
	}

	public ArrayList<String> getServiceLanguage() {
		return serviceLanguage;
	}

	@XmlElement(name="serviceLanguage")
	public void setServiceLanguage(ArrayList<String> serviceLanguage) {
		this.serviceLanguage = serviceLanguage;
	}

	public ServiceArea getServiceArea() {
		return serviceArea;
	}

	@XmlElement(name="serviceArea")
	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
	}

	public int getRetrieveInterval() {
		return retrieveInterval;
	}

	@XmlAttribute(name="retrieveInterval")
	public void setRetrieveInterval(int retrieveInterval) {
		this.retrieveInterval = retrieveInterval;
	}

	@Expose
	private ArrayList<Schedule> schedule;
	
	@Expose
	private ArrayList<ContentSet> contentSet;
	
	@Expose
	private AssociatedDelivery associatedDelivery;

	@Expose
	private ArrayList<ServiceName> name;
	
	@Expose
	private ArrayList<String> serviceLanguage;
	
	@Expose
	private ServiceArea serviceArea;
	
	@Expose
	private int retrieveInterval;
}
