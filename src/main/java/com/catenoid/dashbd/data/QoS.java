package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QoS {
	
	@Expose
	@SerializedName("GBR")
	private Integer gbr;
	
	@Expose
	@SerializedName("QCI")
	private Integer qci;
	
	@Expose
	@SerializedName("ARP")
	private ARP	arp;
	
	public Integer getGbr() {
		return gbr;
	}
	@XmlElement(name="GBR")
	public void setGbr(Integer gbr) {
		this.gbr = gbr;
	}
	public Integer getQci() {
		return qci;
	}
	@XmlElement(name="QCI")
	public void setQci(Integer qci) {
		this.qci = qci;
	}
	public ARP getArp() {
		return arp;
	}
	@XmlElement(name="ARP")
	public void setArp(ARP arp) {
		this.arp = arp;
	}
}
