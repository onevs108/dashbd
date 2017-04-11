package com.catenoid.dashbd.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.HttpNetAgent;

public class CheckCRSInfoCron extends QuartzJobBean{
	
	private SqlSession sqlSession;
	
	@Value("#{config['b2.interface.url']}")
	private String b2InterfefaceURL;
	
	@Value("#{config['b2.bmsc.ip']}")
	private String bmscIp;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map<String, Object> modeLimit = mapper.selectCrsLimit();
		List<Map<String, Object>> moodRequestInfo = mapper.selectMoodRequestInfo(modeLimit);
		List<String> moodServiceId = mapper.selectMoodServiceId(modeLimit);
		int uniMax = (Integer) modeLimit.get("unicast");
		List<String> removeSaIdList = new ArrayList<String>();
		List<HashMap<String, List<String>>> serviceIdList = new ArrayList<HashMap<String, List<String>>>();
		
		for (int i = 0; i < moodServiceId.size(); i++) {
			modeLimit.put("serviceId", moodServiceId.get(i));
			List<String> bcSaIdList = mapper.getSendBcSaid(modeLimit);
			HashMap<String, List<String>> param = new HashMap<String, List<String>>();
			param.put(moodServiceId.get(i), bcSaIdList);
			serviceIdList.add(param);
		}
		
		for (int i = 0; i < serviceIdList.size(); i++) {
			String[] rtvs = new String[2];
			String respBody = "SUCCESS";
			String reqBody = "";
			String agentKey = Base64Coder.encode("127.0.0.1");
			
			try {
				reqBody = makeModityXml(serviceIdList.get(i), agentKey);
				Iterator<String> it = serviceIdList.get(i).keySet().iterator();
				String tempSvId = it.next();
				String crsIp = mapper.getCrsInfo(tempSvId);
				respBody = new HttpNetAgent().execute("http://" + crsIp + ":8080/dashbd/B2interface.do", "", reqBody, false);
			} catch (Exception e) {
				System.out.println(e);
			}
			rtvs[0] = respBody;
			rtvs[1] = reqBody;		
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public String makeModityXml(HashMap<String, List<String>> svId, String agentKey) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map<String, String> bcParam = new HashMap<String, String>();
		List<Map<String, String>> contentList = null;
		Iterator<String> it = svId.keySet().iterator();
		String tempSvId = it.next();
		bcParam.put("serviceId", tempSvId);
		bcParam.put("BCID", mapper.getBcIdFromServiceId(bcParam));
		Map<String, String> params = mapper.selectBroadcast(bcParam);
		bcParam.put("id", mapper.getScheduleIdFromBCID(bcParam));
		if(bcParam.get("BCID") != null){
			contentList = mapper.selectSchduleContentList(bcParam);
//			return outString(doc);
		}
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		//common- depth one
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(agentKey));					//key ??
		
		doc.getRootElement().addContent(transaction);

		Element parameters = new Element("parameters");
		Element services = new Element("services");
		Element service = new Element("service");
		
		Element name = new Element("name");
		name.setAttribute(new Attribute("lang", "en"));										//??
		name.setText(params.get("name"));
		Element serviceLanguage = new Element("serviceLanguage");
		serviceLanguage.setText(params.get("serviceLanguage"));
		
		Element transferConfig = new Element("transferConfig");
		Element QoS = new Element("QoS");
		QoS.addContent( new Element("GBR").setText(params.get("GBR")));
		QoS.addContent( new Element("QCI").setText(params.get("QCI")));
		
		Element ARP = new Element("ARP");
		ARP.addContent( new Element("level").setText(params.get("level")));
		String flag = "on".equals(params.get("preEmptionCapabiity"))? "0" : "1";
		ARP.addContent( new Element("preEmptionCapability").setText(flag));
		flag = "on".equals(params.get("preEmptionVulnerability"))? "0" : "1";
		ARP.addContent( new Element("preEmptionVulnerability").setText(flag));
		QoS.addContent(ARP);
		
		Element FEC = new Element("FEC");
		FEC.addContent( new Element("fecType").setText(params.get("fecType")));
		String ratio = "0";
		
		if (null != params.get("fecRatio") && !"".equals(params.get("fecRatio"))){
			ratio = params.get("fecRatio");
		}
		
		FEC.addContent( new Element("fecRatio").setText(ratio));

		transferConfig.addContent(QoS);
		transferConfig.addContent(FEC);
		//schedule 은 배열이 될수도 있음. 일딴 한개만 처리
		Element receptionReport = null;
		Element associatedDelivery = new Element("associatedDelivery");
		
		if ("on".equals(params.get("receptionReport"))){
			receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", params.get("reportType")));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
			receptionReport.setAttribute(new Attribute("randomTime", params.get("randomTime")));			
		}
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element serviceType = null;
		service.setAttribute(new Attribute("serviceType", "streaming"));
		
		Element streaming = new Element("streaming");
		streaming.setAttribute(new Attribute("serviceId", serviceId));
		if ( null != params.get("serviceClass") && !"".equals(params.get("serviceClass"))){
			streaming.setAttribute(new Attribute("serviceClass", params.get("serviceClass")));
		}
		streaming.setAttribute(new Attribute("serviceMode", params.get("serviceMode")));
		
		transferConfig.addContent(new Element("SegmentAvailableOffset").setText(params.get("segmentAvailableOffset")));
		
		if ("on".equals(params.get("reportType"))){
			receptionReport.setAttribute(new Attribute("samplePercentage", params.get("samplePercentage")));
			associatedDelivery.addContent(receptionReport);
		}
		
		if ( null != params.get("name") && !"".equals(params.get("name")))
			streaming.addContent(name);
		
		if ( null != params.get("serviceLanguage") && !"".equals(params.get("serviceLanguage")))
			streaming.addContent(serviceLanguage);
	 	
		streaming.addContent(transferConfig);
		Element consumptionReport = null;
		if ("MooD".equals(params.get("serviceMode"))){
			consumptionReport = new Element("consumptionReport");
			consumptionReport.setAttribute(new Attribute("location", params.get("moodLocation")));
			if ("on".equals(params.get("reportClientId"))){
				consumptionReport.setAttribute(new Attribute("reportClientId", "true"));
				consumptionReport.setAttribute(new Attribute("reportInterval", params.get("moodReportInterval")));
				consumptionReport.setAttribute(new Attribute("offsetTime", params.get("moodOffsetTime")));
				consumptionReport.setAttribute(new Attribute("randomTime", params.get("moodRandomTimePeriod")));			
				consumptionReport.setAttribute(new Attribute("samplePercentage", params.get("moodSamplePercentage")));
			}else{
				consumptionReport.setAttribute(new Attribute("reportClientId", "false"));
			}
			associatedDelivery.addContent(consumptionReport);
		}
		serviceType = streaming;
		
		Element schedule = null;
		schedule = new Element("schedule");
		schedule.setAttribute(new Attribute("index", "1"));
		schedule.setAttribute(new Attribute("cancelled", "false"));
		//time format ex) 2015-04-10T17:24:09.000+09:00 
		schedule.setAttribute(new Attribute("start", convertDateFormatNew(params.get("schedule_start"))));
		schedule.setAttribute(new Attribute("stop", convertDateFormatNew(params.get("schedule_stop"))));
		
		Element contentSet = new Element("contentSet");
		contentSet.setAttribute(new Attribute("contentSetId", String.valueOf(contentList.get(0).get("content_id")))); 					
		contentSet.setAttribute(new Attribute("cancelled", "false"));				
							
		Element serviceArea = new Element("serviceArea");
		serviceArea.addContent( new Element("said").setText(params.get("said")));
		
//		r12mpdURI
//		bcBasePattern
		
		Element mpd = new Element("mpd");
		mpd.setAttribute(new Attribute("changed", "false"));									
		mpd.addContent(new Element("mpdURI").setText(contentList.get(0).get("mpd")));

		Element mood = new Element("mood");
		Element r12MpdURI = new Element("r12MpdURI");
		r12MpdURI.setText(params.get("r12mpdURI"));
		r12MpdURI.setAttribute(new Attribute("changed", "false"));				
		mood.addContent(r12MpdURI);
		
		String[] pattern = contentList.get(0).get("bcBasePattern").split(",");
		for (int k = 0; k < pattern.length; k++) {
			mood.addContent(new Element("bcBasePattern").setText(pattern[k]));
		}
		
		//핵심부분
		Element bcServiceArea = new Element("bcServiceArea");
		for (int j = 0; j < svId.get(tempSvId).size(); j++) {
			bcServiceArea.addContent(new Element("said").setText(svId.get(tempSvId).get(j)));
		}
		mood.addContent(bcServiceArea);
		
		contentSet.addContent(serviceArea);
		contentSet.addContent(mpd);
		contentSet.addContent(mood);
		serviceType.addContent(schedule);
		serviceType.addContent(contentSet);
		if ("MooD".equals(params.get("serviceMode")) || "on".equals(params.get("receptionReport"))){
			serviceType.addContent(associatedDelivery);
		} 
		
		service.addContent(serviceType);
		services.addContent(service);
		parameters.addContent(services);
		
		doc.getRootElement().addContent(parameters);
//		System.out.println(outString(doc));
		return outString(doc);
	}
	
	public void setInit(SqlSession ss){
		
		if (sqlSession != null)
			return;
		
		sqlSession = ss;
	}
	
	private String convertDateFormatNew(String dateTime){
		String retStr = "";
		
		retStr = dateTime.replace(" ", "T");
		retStr += ".000+09:00";
				
		return retStr;
		
	}

	private String outString(Document doc){
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}
	
	/*for (int j = 0; j < moodRequestInfo.size(); j++) {
		if(moodServiceId.get(i).equals(moodRequestInfo.get(j).get("serviceId").toString())){
			int bro = (Integer) moodRequestInfo.get(j).get("countBC");
			//UNI캐스트 일때
			if(bro == 0) {
				if(uniMax > (Integer) moodRequestInfo.get(j).get("countUC"))
				{
					
				}
				else 
				{
					saIdList.add(moodRequestInfo.get(j).get("said").toString());
				}
			}
			//BRO캐스트 일때
			else // if(uni == 0)
			{
				if(uniMax > (Integer) moodRequestInfo.get(j).get("countBC")) 
				{
					
				}
				else 
				{
					saIdList.add(moodRequestInfo.get(j).get("said").toString());
				}
			}
		}dffffffffffffasdfs 
	}*/
}
