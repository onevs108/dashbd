package com.catenoid.dashbd.data;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.catenoid.dashbd.util.Utils;
import com.google.gson.annotations.Expose;

public class Schedule {
	
	@Expose
	private Integer index;
	
	@Expose
	private Boolean cancelled;
	
	@Expose
	private Date start;
	
	@Expose
	private Date stop;
	
	@Expose
	private String start_8601;
	
	@Expose
	private String stop_8601;
	
	public Integer getIndex() {
		return index;
	}
	
	@XmlAttribute(name="index")
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Boolean isCancelled() {
		return cancelled;
	}
	
	@XmlAttribute(name="cancelled")
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public Date getStart() {
		return start;
	}
	
	@XmlAttribute(name="start")
	public void setStart(Date start) {
		this.start = start;
		setStart_8601(Utils.parseISO8601(start));
	}
	public Date getStop() {
		return stop;
	}
	
	@XmlAttribute(name="stop")
	public void setStop(Date stop) {
		this.stop = stop;
		setStop_8601(Utils.parseISO8601(stop));
	}
	
	public ArrayList<Content> getContent() {
		return content;
	}

	@XmlElement(name="content")
	public void setContent(ArrayList<Content> content) {
		this.content = content;
	}

	@Expose
	private ArrayList<Content> content;

	public String getStart_8601() {
		return start_8601;
	}

	@XmlTransient
	private void setStart_8601(String start_8601) {
		this.start_8601 = start_8601;
	}

	public String getStop_8601() {
		return stop_8601;
	}

	@XmlTransient
	private void setStop_8601(String stop_8601) {
		this.stop_8601 = stop_8601;
	}	
	
}
