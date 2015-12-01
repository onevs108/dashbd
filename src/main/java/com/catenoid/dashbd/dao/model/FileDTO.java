package com.catenoid.dashbd.dao.model;

import org.springframework.web.multipart.MultipartFile;

public class FileDTO {
    private String contentid, title, type, fileName;
    private MultipartFile uploadfile; 
     
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
  
    public String getFileName() {
        return fileName;
    }
 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    public MultipartFile getUploadfile() {
        return uploadfile;
    }
 
    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

