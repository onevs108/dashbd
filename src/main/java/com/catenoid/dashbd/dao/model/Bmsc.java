package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Bmsc {
    private Integer id;
    
    private Integer operatorId;
    
    private String name;

    private String ipaddress;
    
    private String circle;

    private Date createdAt;

    private Date updatedAt;
    
    private Integer totalCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle == null ? null : circle.trim();
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
    
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
    	JSONObject jsonResult = new JSONObject();
    	jsonResult.put("id", id);
    	jsonResult.put("operatorId", operatorId);
    	jsonResult.put("name", name);
    	jsonResult.put("ipaddress", ipaddress);
    	jsonResult.put("circle", circle);
    	jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
    	jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
    	return jsonResult;
    }

	@Override
	public String toString() {
		return "Bmsc [id=" + id + ", operatorId=" + operatorId + ", name=" + name + ", ipaddress=" + ipaddress
				+ ", circle=" + circle + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}