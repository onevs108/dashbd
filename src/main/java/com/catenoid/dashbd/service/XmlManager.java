package com.catenoid.dashbd.service;

import java.io.IOException;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Service;

@Service
public class XmlManager {
	public String testMaking(){

			Element message = new Element("message");
			Document doc = new Document(message);
			doc.setRootElement(message);

			Element transaction = new Element("transaction");
			transaction.setAttribute(new Attribute("id", "1"));
			transaction.addContent(new Element("agentKey").setText("dGVzdA=="));
			
			doc.getRootElement().addContent(transaction);

			Element parameters = new Element("parameters");
			Element services = new Element("services");
			Element service = new Element("service");
			service.setAttribute(new Attribute("serviceType", "fileDownload"));
			
			Element fileDownload = new Element("fileDownload");
			fileDownload.setAttribute(new Attribute("serviceId", "urn:3gpp:filedownload-0410172238-1"));
			
			Element name = new Element("name");
			name.setAttribute(new Attribute("id", "1"));
			name.setText("test");
			Element serviceLanguage = new Element("serviceLanguage");
			serviceLanguage.setText("EN");
			
			Element transferConfig = new Element("transferConfig");
			Element QoS = new Element("QoS");
			QoS.addContent( new Element("GBR").setText("3000000"));
			QoS.addContent( new Element("QCI").setText("149"));
			
			Element ARP = new Element("ARP");
			ARP.addContent( new Element("level").setText("9"));
			ARP.addContent( new Element("preEmptionCapability").setText("0"));
			ARP.addContent( new Element("preEmptionVulnerability").setText("0"));
			QoS.addContent(ARP);
			
			Element FEC = new Element("FEC");
			FEC.addContent( new Element("fecType").setText("NoFEC"));
			FEC.addContent( new Element("fecRatio").setText("0"));

			transferConfig.addContent(QoS);
			transferConfig.addContent(FEC);
			
			Element serviceArea = new Element("serviceArea");
			serviceArea.addContent( new Element("said").setText("10000"));
			
			Element schedule = new Element("schedule");
			schedule.setAttribute(new Attribute("index", "1"));
			schedule.setAttribute(new Attribute("cancelled", "false"));
			schedule.setAttribute(new Attribute("start", "2015-04-10T17:24:09.000+09:00"));
			schedule.setAttribute(new Attribute("stop", "2015-04-10T17:39:09.000+09:00"));
			
			Element content = new Element("content");
			content.setAttribute(new Attribute("contentId", "1"));
			content.setAttribute(new Attribute("contentType", "text/plain"));
			content.setAttribute(new Attribute("cancelled", "false"));
			content.setAttribute(new Attribute("changed", "false"));
			content.addContent( new Element("fileURI").setText("http://192.168.1.115:8088/data/100M-RQ.txt"));
			Element deliveryInfo = new Element("deliveryInfo");
			deliveryInfo.setAttribute(new Attribute("start", "2015-04-10T17:24:09.000+09:00"));
			deliveryInfo.setAttribute(new Attribute("stop", "2015-04-10T17:39:09.000+09:00"));
			content.addContent(deliveryInfo);
			schedule.addContent(content);
			Element associatedDelivery = new Element("associatedDelivery");
			Element postFileRepair = new Element("postFileRepair");
			postFileRepair.setAttribute(new Attribute("offsetTime", "5"));
			postFileRepair.setAttribute(new Attribute("randomTime", "300"));
			postFileRepair.setAttribute(new Attribute("cancelled", "false"));
			
			Element receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", "RAck"));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", "305"));
			receptionReport.setAttribute(new Attribute("randomTime", "300"));
			
			associatedDelivery.addContent(postFileRepair);
			associatedDelivery.addContent(receptionReport);
			
			fileDownload.addContent(name);
			fileDownload.addContent(serviceLanguage);
			fileDownload.addContent(transferConfig);
			fileDownload.addContent(serviceArea);
			fileDownload.addContent(schedule);
			fileDownload.addContent(associatedDelivery);
			
			service.addContent(fileDownload);
			
			services.addContent(service);
			parameters.addContent(services);
			
			doc.getRootElement().addContent(parameters);
			
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			return xmlOutput.outputString(doc);

	}
	
	public static void main( String[] args ) {
		System.out.println(new XmlManager().testMaking());
	}
}
