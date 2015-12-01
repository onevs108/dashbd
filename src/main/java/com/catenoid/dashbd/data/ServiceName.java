package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import com.google.gson.annotations.Expose;

public class ServiceName {
	
	@Expose
	private String lang;

	@Expose
	private String name;
	
	public String getLang() {
		return lang;
	}	
	@XmlAttribute(name="lang")
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getName() {
		return name;
	}
	
	@XmlValue
	public void setName(String name) {
		this.name = name;
	}
}
