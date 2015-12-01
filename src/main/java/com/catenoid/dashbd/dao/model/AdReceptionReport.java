package com.catenoid.dashbd.dao.model;

public class AdReceptionReport {
    private Integer serviceId;

    private String reportType;

    private Integer cancelled;

    private Integer samplePercentage;

    private Integer offsetTime;

    private Integer randomTime;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType == null ? null : reportType.trim();
    }

    public Integer getCancelled() {
        return cancelled;
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
    }

    public Integer getSamplePercentage() {
        return samplePercentage;
    }

    public void setSamplePercentage(Integer samplePercentage) {
        this.samplePercentage = samplePercentage;
    }

    public Integer getOffsetTime() {
        return offsetTime;
    }

    public void setOffsetTime(Integer offsetTime) {
        this.offsetTime = offsetTime;
    }

    public Integer getRandomTime() {
        return randomTime;
    }

    public void setRandomTime(Integer randomTime) {
        this.randomTime = randomTime;
    }
}