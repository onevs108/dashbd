package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class AssociatedDelivery {
	@Expose
	private ReceptionReport receptionReport;

	public ReceptionReport getReceptionReport() {
		return receptionReport;
	}

	@XmlElement(name="receptionReport")
	public void setReceptionReport(ReceptionReport receptionReport) {
		this.receptionReport = receptionReport;
	}
}
