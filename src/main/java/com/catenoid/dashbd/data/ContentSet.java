package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentSet {
	
	@Expose
	private Integer contentSetId;
	
	@Expose
	private Boolean cancelled;
	
	@Expose
	private ServiceArea serviceArea;
	
	@Expose
	@SerializedName("mpd")
	private Mpd mpd;
	
	public Integer getContentSetId() {
		return contentSetId;
	}
	
	@XmlAttribute(name="contentSetId")
	public void setContentSetId(Integer contentSetId) {
		this.contentSetId = contentSetId;
	}
	public Boolean isCancelled() {
		return cancelled;
	}
	
	@XmlAttribute(name="cancelled")
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public ServiceArea getServiceArea() {
		return serviceArea;
	}
	
	@XmlElement(name="serviceArea")
	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
	}
	public Mpd getMpd() {
		return mpd;
	}
	
	@XmlElement(name="mpd")
	public void setMpd(Mpd mpd) {
		this.mpd = mpd;
	}
}
