package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class ServiceAreaSearchParam {
    private Integer id;
    
    private Integer bandwidth;
    
    private String name;

    private String city;

    private Date createdAt;

    private Date updatedAt;
    
    private Integer page;
    
    private Integer perPage;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
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

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getPage() {
        return page;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

	public Integer getPerPage() {
		return perPage;
	}

}