package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Mpd {
	@Expose
	private String mpdURI;

	public String getMpdURI() {
		return mpdURI;
	}

	@XmlElement(name="mpdURI")
	public void setMpdURI(String mpdURI) {
		this.mpdURI = mpdURI;
	}
	
	public Boolean isChanged() {
		return changed;
	}

	@XmlAttribute(name="changed")
	public void setChanged(Boolean changed) {
		this.changed = changed;
	}

	@Expose
	private Boolean changed;
}
