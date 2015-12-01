package com.catenoid.dashbd.dao.model;

public class AdPostFileRepair {
    private Integer serviceId;

    private Integer cancelled;

    private Integer offsetTime;

    private Integer randomTime;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getCancelled() {
        return cancelled;
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
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