package com.catenoid.dashbd.dao.model;

public class TransferConfig {
    private Integer serviceId;

    private Integer qosGbr;

    private Integer qosQci;

    private Integer arpLevel;

    private Integer arpPreEmptionCapability;

    private Integer arpPreEmptionVulnerability;

    private String fecType;

    private Integer fecRatio;

    private Integer segmentAvailableOffset;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getQosGbr() {
        return qosGbr;
    }

    public void setQosGbr(Integer qosGbr) {
        this.qosGbr = qosGbr;
    }

    public Integer getQosQci() {
        return qosQci;
    }

    public void setQosQci(Integer qosQci) {
        this.qosQci = qosQci;
    }

    public Integer getArpLevel() {
        return arpLevel;
    }

    public void setArpLevel(Integer arpLevel) {
        this.arpLevel = arpLevel;
    }

    public Integer getArpPreEmptionCapability() {
        return arpPreEmptionCapability;
    }

    public void setArpPreEmptionCapability(Integer arpPreEmptionCapability) {
        this.arpPreEmptionCapability = arpPreEmptionCapability;
    }

    public Integer getArpPreEmptionVulnerability() {
        return arpPreEmptionVulnerability;
    }

    public void setArpPreEmptionVulnerability(Integer arpPreEmptionVulnerability) {
        this.arpPreEmptionVulnerability = arpPreEmptionVulnerability;
    }

    public String getFecType() {
        return fecType;
    }

    public void setFecType(String fecType) {
        this.fecType = fecType == null ? null : fecType.trim();
    }

    public Integer getFecRatio() {
        return fecRatio;
    }

    public void setFecRatio(Integer fecRatio) {
        this.fecRatio = fecRatio;
    }

    public Integer getSegmentAvailableOffset() {
        return segmentAvailableOffset;
    }

    public void setSegmentAvailableOffset(Integer segmentAvailableOffset) {
        this.segmentAvailableOffset = segmentAvailableOffset;
    }
}