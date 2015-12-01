package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.annotations.Expose;

public class AgentKey {

	@Expose
	private String decodeValue;
	
	@Expose
	private String value;

	public String getValue() {
		return Base64.encodeBase64String(decodeValue.getBytes());
	}

	/**
	 * base64 인코딩된 결과를 설정합니다.
	 * @param value
	 */
	@XmlValue
	public void setValue(String value) {
		this.value = value;
		setDecodeValue(new String(Base64.decodeBase64(value)));
	}

	public String getDecodeValue() {
		return decodeValue;
	}

	@XmlTransient
	public void setDecodeValue(String decodeValue) {
		this.decodeValue = decodeValue;
		this.value = Base64.encodeBase64String(decodeValue.getBytes());
	}
}
