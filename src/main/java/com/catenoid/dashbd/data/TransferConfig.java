package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferConfig {
	
	@Expose
	@SerializedName("QoS")
	private QoS qos;

	public QoS getQos() {
		return qos;
	}

	@XmlElement(name="QoS")
	public void setQos(QoS qos) {
		this.qos = qos;
	}
		
	@Expose
	@SerializedName("FEC")
	private FEC fec;
	
	public FEC getFec() {
		return fec;
	}

	@XmlElement(name="FEC")
	public void setFec(FEC fec) {
		this.fec = fec;
	}

	@Expose
	private int segmentAvailableOffset;

	public int getSegmentAvailableOffset() {
		return segmentAvailableOffset;
	}

	@XmlElement(name="SegmentAvailableOffset")
	public void setSegmentAvailableOffset(int Offset) {
		segmentAvailableOffset = Offset;
	}

}

