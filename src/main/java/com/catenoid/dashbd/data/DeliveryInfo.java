package com.catenoid.dashbd.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import com.catenoid.dashbd.util.Utils;
import com.google.gson.annotations.Expose;

public class DeliveryInfo {
	
	@Expose
	private Date start;
	
	@Expose
	private Date end;
	
	@Expose
	private String start_8601;
	
	@Expose
	private String end_8601;
	
	public String getStart_8601() {
		return start_8601;
	}
	
	@XmlTransient
	private void setStart_8601(String start_8601) {
		this.start_8601 = start_8601;
	}
	
	public String getEnd_8601() {
		return end_8601;
	}
	
	@XmlTransient
	private void setEnd_8601(String end_8601) {
		this.end_8601 = end_8601;
	}
	public Date getEnd() {
		return end;
	}
	
	@XmlAttribute(name="end")
	public void setEnd(Date end) {
		this.end = end;
		setEnd_8601(Utils.parseISO8601(end));
	}
	public Date getStart() {
		return start;
	}
	@XmlAttribute(name="start")
	public void setStart(Date start) {
		this.start = start;
		setStart_8601(Utils.parseISO8601(start));
	}
	
	
}
