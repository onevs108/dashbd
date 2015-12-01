package com.catenoid.dashbd.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class ServiceArea {
	
	@Expose
	private ArrayList<Integer> said;

	public ArrayList<Integer> getSaid() {
		return said;
	}

	@XmlElement(name="said")
	public void setSaid(ArrayList<Integer> said) {
		this.said = said;
	}
}
