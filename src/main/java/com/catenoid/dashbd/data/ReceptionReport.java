package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.gson.annotations.Expose;

public class ReceptionReport {
	
	public interface REPORT_TYPE {
		public final String RACK = "RAck";
		public final String STAR_ALL = "StaR-all";
		public final String STAR_ONLY = "StaR-only";
	}
	
	@Expose
	private String reportType;
	
	@Expose
	private Boolean cancelled;
	
	@Expose
	private Integer offsetTime;
	
	@Expose
	private Integer randomTime;
	
	@Expose
	private Integer samplePercentage;
	
	public String getReportType() {
		return reportType;
	}
	
	@XmlAttribute(name="reportType")
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public Boolean isCancelled() {
		return cancelled;
	}
	@XmlAttribute(name="cancelled")
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public Integer getOffsetTime() {
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
	public Integer getSamplePercentage() {
		return samplePercentage;
	}
	@XmlAttribute(name="samplePercentage")
	public void setSamplePercentage(Integer samplePercentage) {
		this.samplePercentage = samplePercentage;
	}
}
