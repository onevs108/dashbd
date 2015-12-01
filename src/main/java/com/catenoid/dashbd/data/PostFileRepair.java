package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.gson.annotations.Expose;

public class PostFileRepair {
	@Expose
	private Integer offsetTime;
	@Expose
	private Integer randomTime;
	@Expose
	private Boolean cancelled;
	
	public int getOffsetTime() {
		return offsetTime;
	}
	@XmlAttribute(name="offsetTime")
	public void setOffsetTime(Integer offsetTime) {
		this.offsetTime = offsetTime;
	}
	public Integer getRandomTime() {
		return randomTime;
	}
	@XmlAttribute(name="randomTime")
	public void setRandomTime(Integer randomTime) {
		this.randomTime = randomTime;
	}
	public Boolean isCancelled() {
		return cancelled;
	}
	@XmlAttribute(name="cancelled")
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
}
