package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlValue;

import com.google.gson.annotations.Expose;

public class ServiceLanguage {
	@Expose
	private String language;
	
	public String getLanguage() {
		return language;
	}
	
	@XmlValue
	public void setLanguage(String language) {
		this.language = language;
	}
}
