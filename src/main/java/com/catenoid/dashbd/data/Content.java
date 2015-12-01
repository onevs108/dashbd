package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Content {
	@Expose
	private Integer contentId;
	@Expose
	private String contentType;
	@Expose
	private Boolean canclled;
	@Expose
	private Boolean changed;
	@Expose
	private String fileURI;
	@Expose
	private DeliveryInfo deliveryInfo;
	
	public Integer getContentId() {
		return contentId;
	}
	
	@XmlAttribute(name="contentId")
	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}
	public String getContentType() {
		return contentType;
	}
	@XmlAttribute(name="contentType")
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Boolean isCanclled() {
		return canclled;
	}
	@XmlAttribute(name="canclled")
	public void setCanclled(Boolean canclled) {
		this.canclled = canclled;
	}
	public Boolean isChanged() {
		return changed;
	}
	@XmlAttribute(name="changed")
	public void setChanged(Boolean changed) {
		this.changed = changed;
	}
	public String getFileURI() {
		return fileURI;
	}
	@XmlElement(name="fileURI")
	public void setFileURI(String fileURI) {
		this.fileURI = fileURI;
	}
	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}
	@XmlElement(name="deliveryInfo")
	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
}
