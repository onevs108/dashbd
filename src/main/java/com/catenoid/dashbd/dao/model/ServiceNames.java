package com.catenoid.dashbd.dao.model;

public class ServiceNames {
	private Integer id;
	
    private Integer serviceId;

    private String name;

    private String nameLang;

    private String serviceLanguage;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameLang() {
        return nameLang;
    }

    public void setNameLang(String nameLang) {
        this.nameLang = nameLang == null ? null : nameLang.trim();
    }

    public String getServiceLanguage() {
        return serviceLanguage;
    }

    public void setServiceLanguage(String serviceLanguage) {
        this.serviceLanguage = serviceLanguage == null ? null : serviceLanguage.trim();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}