package com.catenoid.dashbd.dao.model;
import java.util.Date;

public class SystemDatabaseBackup {
	
	private int rownum;
	private int backupId;
	private String backupFileName;
	private String backupFilePath;
	private String backupCreatedId;
	private Date backupCreatedAt;
	private String backupType;
	private Integer totalCount;
	
	public String getBackupType() {
		return backupType;
	}
	public void setBackupType(String backupType) {
		this.backupType = backupType;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public int getBackupId() {
		return backupId;
	}
	public void setBackupId(int backupId) {
		this.backupId = backupId;
	}
	public String getBackupFileName() {
		return backupFileName;
	}
	public void setBackupFileName(String backupFileName) {
		this.backupFileName = backupFileName;
	}
	public String getBackupFilePath() {
		return backupFilePath;
	}
	public void setBackupFilePath(String backupFilePath) {
		this.backupFilePath = backupFilePath;
	}
	public String getBackupCreatedId() {
		return backupCreatedId;
	}
	public void setBackupCreatedId(String backupCreatedId) {
		this.backupCreatedId = backupCreatedId;
	}
	public Date getBackupCreatedAt() {
		return backupCreatedAt;
	}
	public void setBackupCreatedAt(Date backupCreatedAt) {
		this.backupCreatedAt = backupCreatedAt;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}
