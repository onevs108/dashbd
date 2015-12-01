package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class FEC {
	
	public interface FEC_TYPE {
		public final String NO_FEC = "NoFEC";
		public final String RAPTOR = "Raptor";
		public final String RAPTOR_Q = "RaptorQ";
		public final String RSLDPC = "RSLDPC";
	}
	
	@Expose
	private String fecType;
	
	@Expose
	private Integer fecRatio;
	
	public String getFecType() {
		return fecType;
	}
	
	@XmlElement(name="fecType")
	public void setFecType(String fecType) {
		this.fecType = fecType;
	}
	public Integer getFecRatio() {
		return fecRatio;
	}
	
	@XmlElement(name="fecRatio")
	public void setFecRatio(Integer fecRatio) {
		this.fecRatio = fecRatio;
	}
}
