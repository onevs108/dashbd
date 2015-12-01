package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class Message {
	
	public interface NAME {
		public final String SERVER_CREATE 	= "SERVER.CREATE";
		public final String SERVER_UPDATE 	= "SERVER.UPDATE";
		public final String SERVER_DELETE 	= "SERVER.DELETE";
		public final String SERVER_ABORT 	= "SERVER.ABORT";
		public final String SERVER_RETRIEVE = "SERVER.RETRIEVE";
		public final String STATUS_NOTIFY 	= "STATUS.NOTIFY";
	}
	
	public interface TYPE {
		public final String REQUEST = "REQUEST";
		public final String RESPONSE = "RESPONSE";
	}
	
	@Expose
	private String name;

	public String getName() {
		return name;
	}

	@XmlAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@Expose
	private String type;

	public String getType() {
		return type;
	}

	@XmlAttribute(name="type")
	public void setType(String type) {
		this.type = type;
	}

	@Expose
	private Transaction transaction;
	
	public Transaction getTransaction() {
		return transaction;
	}

	@XmlElement(name="transaction")
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@XmlElement(name="parameters")
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	@Expose
	private Parameters parameters;
}
