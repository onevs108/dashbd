package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class TransReport {
	private Integer id;
	
    private Integer transactionId;

    private String agentKey;

    private Integer servicesId;
    
    private Integer serviceId;
    
    private Integer serviceAreaId;

    private Date sendDtm;

    private Integer resultCode;
    
    private String resultMsg;

    private Integer retryCount;
    
    private String sendMsg;
    
    private String recvMsg;

    private Date createdAt;

    private Date updatedAt;
    
    private String startDate;
    
    private String endDate;
    
    protected String resultCodeClause;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey == null ? null : agentKey.trim();
    }

    public Integer getServicesId() {
        return servicesId;
    }

    public void setServicesId(Integer servicesId) {
        this.servicesId = servicesId;
    }

    public Date getSendDtm() {
        return sendDtm;
    }

    public void setSendDtm(Date sendDtm) {
        this.sendDtm = sendDtm;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
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

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg == null ? null : resultMsg.trim();
    }

	public String getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}

	public String getRecvMsg() {
		return recvMsg;
	}

	public void setRecvMsg(String recvMsg) {
		this.recvMsg = recvMsg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getResultCodeClause() {
		return resultCodeClause;
	}

	public void setResultCodeClause(String resultCodeClause) {
		this.resultCodeClause = resultCodeClause;
	}

	public Integer getServiceAreaId() {
		return serviceAreaId;
	}

	public void setServiceAreaId(Integer serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}
}