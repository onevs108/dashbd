package com.catenoid.dashbd.dao.model;

public class ServiceAreaPermissionAp {

	private Integer rownum;
	private Integer permissionId;
    private String permissionName;
	private Integer permissionCount;
	private Integer totalCount;
	
	public Integer getRownum() {
		return rownum;
	}
	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}
	public Integer getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public Integer getPermissionCount() {
		return permissionCount;
	}
	public void setPermissionCount(Integer permissionCount) {
		this.permissionCount = permissionCount;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}