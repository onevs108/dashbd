package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Service {
	
	public interface SERVICE_TYPE {
		public final String FILE_DOWNLOAD = "fileDownload";
		public final String STREAMING = "streaming";
		public final String CAROUSEL_MULTIPLEFILES = "carousel-MultipleFiles";
		public final String CAROUSEL_SINGLEFILE = "carousel-SingleFile";
	}

	@Expose
	private String serviceType;

	public String getServiceType() {
		return serviceType;
	}

	@XmlAttribute
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Expose
	private ServiceType fileDownload;
	
	@Expose
	private ServiceType streaming;
	
	@Expose
	private ServiceType carouselMultipleFiles;
	
	@Expose
	private ServiceType carouselSingleFile;

	public ServiceType getFileDownload() {
		return fileDownload;
	}

	@XmlElement(name="fileDownload")
	public void setFileDownload(ServiceType sc) {
		this.fileDownload = sc;
	}

	public ServiceType getStreaming() {
		return streaming;
	}

	@XmlElement(name="streaming")
	public void setStreaming(ServiceType sc) {
		this.streaming = sc;
	}

	public ServiceType getCarouselMultipleFiles() {
		return carouselMultipleFiles;
	}

	@XmlElement(name="carousel-MultipleFiles")
	public void setCarouselMultipleFiles(ServiceType sc) {
		this.carouselMultipleFiles = sc;
	}

	public ServiceType getCarouselSingleFile() {
		return carouselSingleFile;
	}

	@XmlElement(name="carousel-SingleFile")
	public void setCarouselSingleFile(ServiceType sc) {
		this.carouselSingleFile = sc;
	}
}
