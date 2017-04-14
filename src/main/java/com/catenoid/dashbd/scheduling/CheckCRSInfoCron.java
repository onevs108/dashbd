package com.catenoid.dashbd.scheduling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.HttpNetAgent;

public class CheckCRSInfoCron extends QuartzJobBean{
	
	private SqlSession sqlSession;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
		Map<String, Object> modeLimit = mapper.selectCrsLimit();
		List<String> moodServiceId = mapper.selectMoodServiceId(modeLimit);
		List<HashMap<String, List<HashMap<String,String>>>> serviceIdList = new ArrayList<HashMap<String, List<HashMap<String,String>>>>();
		
		for (int i = 0; i < moodServiceId.size(); i++) {
			modeLimit.put("serviceId", moodServiceId.get(i));
			List<HashMap<String,String>> bcSaIdList = mapper.getSendBcSaid(modeLimit);
			for (int j = 0; j < bcSaIdList.size(); j++) {
				System.out.println("bcSaIdList ================== "+bcSaIdList.get(j)+ " ================== ");
			}
			if(bcSaIdList.size() > 0){
				HashMap<String, List<HashMap<String,String>>> param = new HashMap<String, List<HashMap<String,String>>>();
				param.put(moodServiceId.get(i), bcSaIdList);
				serviceIdList.add(param);
			}
		}
		
		System.out.println("serviceIdList ================== "+serviceIdList.size()+ " ================== ");
		for (int i = 0; i < serviceIdList.size(); i++) {
			String[] rtvs = new String[2];
			String respBody = "SUCCESS";
			String reqBody = "";
			String reqBodyCrs = "";
			try {
				Bmsc bmsc = bmscMapper.selectBmsc(793);
				System.out.println(" ================== BMSC Mood Update Start ================== ");
				String agentKey = Base64Coder.encode(bmsc.getIpaddress()); 
				reqBody = makeModityXml(serviceIdList.get(i), agentKey);
				respBody = new HttpNetAgent().execute("http://" + bmsc.getIpaddress() + bmsc.getCircle(), "", reqBody, false);
				System.out.println(" ================== BMSC Mood Update End ================== ");
				
				
				HashMap<String,String> crsParam = new HashMap<String, String>(); 
				Iterator<String> it = serviceIdList.get(i).keySet().iterator();
				String tempSvId = it.next();
				crsParam.put("serviceId", tempSvId);
				List<HashMap<String,String>> crsSaidList = serviceIdList.get(i).get(tempSvId);
				for (int j = 0; j < crsSaidList.size(); j++) {
					String said = crsSaidList.get(j).get("said");
					System.out.println(" ================== (said = "+said+") CRS Mood Update Start ================== ");
					crsParam.put("said", said);
					HashMap<String,String> crsInfo = mapper.getCrsInfo(crsParam);
					String agentKeyCRS = Base64Coder.encode(crsInfo.get("ip"));
					reqBodyCrs = makeModityXmlCRS(tempSvId, crsInfo, agentKeyCRS, said);
					new HttpNetAgent().execute("http://" + crsInfo.get("ip") + crsInfo.get("updateUrl"), "", reqBodyCrs, false);
					System.out.println(" ================== ("+said+") CRS Mood Update End ================== ");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			rtvs[0] = respBody;
			rtvs[1] = reqBody;		
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private String makeModityXmlCRS(String tempSvId, HashMap<String,String> crsInfo, String agentKeyCRS, String said) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map<String, String> bcParam = new HashMap<String, String>();
		bcParam.put("serviceId", tempSvId);
		bcParam.put("BCID", mapper.getBcIdFromServiceId(bcParam));
		Map<String, String> params = mapper.selectBroadcast(bcParam);
		bcParam.put("id", mapper.getScheduleIdFromBCID(bcParam));
		
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));	
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(agentKeyCRS));
		
		doc.getRootElement().addContent(transaction);

		Element request = new Element("request");
		Element service = new Element("service");
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element update = new Element("update");
		
		update.addContent(new Element("crsid").setText(String.valueOf(crsInfo.get("id"))));
		update.addContent(new Element("timestamp").setText(convertDateFormat3(new Date().toString())));
		update.addContent(new Element("serviceId").setText(serviceId));
		
		Element associatedDelivery = new Element("associatedDelivery");
		Element consumptionReport = null;
		consumptionReport = new Element("consumptionReport");
		consumptionReport.addContent(new Element("reportInterval").setText(params.get("moodReportInterval")));
		consumptionReport.addContent(new Element("moodUsageDataReportInterval").setText(String.valueOf(crsInfo.get("moodUsageDataReportInterval"))));
		associatedDelivery.addContent(consumptionReport);
		
		Element schedule = null;
		schedule = new Element("schedule");
		schedule.addContent(new Element("index").setText(String.valueOf(1)));
		//time format ex) 2015-04-10T17:24:09.000+09:00 
		schedule.addContent(new Element("start").setText(convertDateFormatNew(params.get("schedule_start"))));
		schedule.addContent(new Element("stop").setText(convertDateFormatNew(params.get("schedule_stop"))));
		Element contentSet = new Element("contentSet");
		Element serviceArea = new Element("serviceArea");
		
		serviceArea.addContent(new Element("said").setText(said));
		contentSet.addContent(serviceArea);
		update.addContent(schedule);
		update.addContent(contentSet);
		update.addContent(associatedDelivery);
		
		service.addContent(update);
		request.addContent(service);
		
		doc.getRootElement().addContent(request);
		System.out.println("============================ Mood Update Start ============================");
		System.out.println(outString(doc));
		System.out.println("============================ Mood Update End ============================");
		return outString(doc);
	}
	
	@SuppressWarnings("unchecked")
	public String makeModityXml(HashMap<String, List<HashMap<String,String>>> svId, String agentKey) {
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
			consumptionReport.setAttribute(new Attribute("reportClientId", "true"));
			consumptionReport.setAttribute(new Attribute("reportInterval", params.get("moodReportInterval")));
			consumptionReport.setAttribute(new Attribute("offsetTime", params.get("moodOffsetTime")));
			consumptionReport.setAttribute(new Attribute("randomTime", params.get("moodRandomTimePeriod")));			
			consumptionReport.setAttribute(new Attribute("samplePercentage", params.get("moodSamplePercentage")));
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
		
		Element mpd = new Element("mpd");
		mpd.setAttribute(new Attribute("changed", "false"));									
		mpd.addContent(new Element("mpdURI").setText(contentList.get(0).get("mpd")));

		Element mood = new Element("mood");
		Element r12MpdURI = new Element("r12MpdURI");
		System.out.println(contentList.get(0).get("r12mpdURI"));
		r12MpdURI.setText(contentList.get(0).get("r12mpdURI"));
		r12MpdURI.setAttribute(new Attribute("changed", "false"));				
		mood.addContent(r12MpdURI);
		
		Element bcBasePattern = new Element("bcBasePattern");
		if(contentList.size() != 0){
			if(contentList.get(0).get("bcBasePattern") != null){
				String[] pattern = contentList.get(0).get("bcBasePattern").split(",");
				for (int k = 0; k < pattern.length; k++) {
					bcBasePattern.setText(pattern[k]);
					if(bcBasePattern.getChildren().size() > 0){
						mood.addContent(bcBasePattern);
					}
				}
			}
		}
		
		//핵심부분
		Element bcServiceArea = new Element("bcServiceArea");
		for (int j = 0; j < svId.get(tempSvId).size(); j++) {
			System.out.println(svId.get(tempSvId).get(j).get("ntype"));
			System.out.println(svId.get(tempSvId).get(j).get("said"));
			if(svId.get(tempSvId).get(j).get("ntype").equals("UC")){
				bcServiceArea.addContent(new Element("said").setText(svId.get(tempSvId).get(j).get("said")));
			}
		}		
		System.out.println(bcServiceArea.getChildren().size());
		if(bcServiceArea.getChildren().size() > 0){
			mood.addContent(bcServiceArea);
		}
		
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
	
	//현재 시간 출력
	private String convertDateFormat3(String dateTime){
		Date dt = new Date();
		
		String retStr = "";
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		retStr = sdfTo.format(dt);
		return retStr;
	}
	
	//2017-02-27T16:00:00.000+09:00
	private String convertDateFormatNew(String dateTime){
		String retStr = "";
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		try {
			Date dateFrom = sdfFrom.parse(dateTime);
		    retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
		}
		return retStr;
	}

	private String outString(Document doc){
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}

	public void setInit(SqlSession ss){
		if (sqlSession != null)
			return;
		
		sqlSession = ss;
	}
	
}
