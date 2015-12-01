package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class ARP {
	
	@Expose
	private Integer level;
	
	@Expose
	private Integer preEmptionCapability;
	
	@Expose
	private Integer preEmptionVulnerability;

	public Integer getLevel() {
		return level;
	}
	
	@XmlElement(name="level")
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer isPreEmptionCapability() {
		return preEmptionCapability;
	}
	@XmlElement(name="preEmptionCapability")
	public void setPreEmptionCapability(Integer preEmptionCapability) {
		this.preEmptionCapability = preEmptionCapability;
	}
	public Integer isPreEmptionVulnerability() {
		return preEmptionVulnerability;
	}
	@XmlElement(name="preEmptionVulnerability")
	public void setPreEmptionVulnerability(Integer preEmptionVulnerability) {
		this.preEmptionVulnerability = preEmptionVulnerability;
	}
}
