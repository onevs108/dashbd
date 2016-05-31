package com.catenoid.dashbd.dao.model;
import java.util.Date;

public class SystemBroadCastContents {
	
	private int rownum;
	private int schId;
	private int bmscid;
    private String bmscName;
    private int serviceAreaId;
    private String serviceAreaName;
    private int contentId;
    private String contentName;
    private int operatorId;
    private String operatorName;
    private String serviceCategory;
    private String fileType;
    private String fileFormat;
    private String serviceType;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Date updatedAt;
    private Integer totalCount;
    
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public int getSchId() {
		return schId;
	}
	public void setSchId(int schId) {
		this.schId = schId;
	}
	public int getBmscid() {
		return bmscid;
	}
	public void setBmscid(int bmscid) {
		this.bmscid = bmscid;
	}
	public String getBmscName() {
		return bmscName;
	}
	public void setBmscName(String bmscName) {
		this.bmscName = bmscName;
	}
	public int getServiceAreaId() {
		return serviceAreaId;
	}
	public void setServiceAreaId(int serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}
	public String getServiceAreaName() {
		return serviceAreaName;
	}
	public void setServiceAreaName(String serviceAreaName) {
		this.serviceAreaName = serviceAreaName;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}