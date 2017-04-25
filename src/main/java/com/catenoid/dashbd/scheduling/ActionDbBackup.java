package com.catenoid.dashbd.scheduling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.UsersMapper;

public class ActionDbBackup {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['b2.database.backup']}")
	private String databaseBackup;
	
	public ActionDbBackup() {
//		HttpSession session = request.getSession(false);
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		HashMap<String, Object> logMap = new HashMap<String, Object>();
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		String newYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)); 
		String newMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)); 
		String newDay = String.valueOf(Calendar.getInstance().get(Calendar.DATE)); 
		String newHour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR)); 
		String newMinute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)); 
		String filePath = "";
		if(newMonth.length() > 1){
			filePath = "/dashbd/backup/"+newYear+"/"+newMonth;
		}else{
			filePath = "/dashbd/backup/"+newYear+"/0"+newMonth;
		}
		if(newDay.length() > 1){
			filePath += "/"+newDay;
		}else{
			filePath += "/0"+newDay;
		}
			
		String RETURN_SHELL = "Session_Cnt:";
		
		String fileName = "backup-"+newYear+"-"+newMonth+"-"+newDay+"-"+newHour+""+newMinute+".dump";
		String exeDatabaseBackup = String.format("%s %s", databaseBackup, fileName);
		try{
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("fileName", fileName);
			searchParam.put("filePath", filePath);
			searchParam.put("createdId", "auto");

			int rst = mapper.insertSystemDbBackup(searchParam);
				
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(exeDatabaseBackup);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}

	        System.out.println("exeDatabaseBackup   >>  " + sb);
	        
			//String sessionHostNameCnt = sb.substring(RETURN_SHELL.length());
	        String sessionHostNameCnt = sb.toString();
				
			logMap.put("reqType", "Database");
			logMap.put("reqSubType", "DB Backup");
			logMap.put("reqUrl", "ActionDbBackup()");
			logMap.put("reqCode", "SUCCESS");
			logMap.put("targetId", "Admin");
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : Admin" + " - DB Backup(File Name : " + fileName + ", Type : Auto)");
			usersMapper.insertSystemAjaxLog(logMap);
	    }catch(Exception e){
	    	logMap.put("reqType", "Database Config");
			logMap.put("reqSubType", "systemDbBackup");
			logMap.put("reqUrl", "resources/systemDbBackup.do");
			logMap.put("reqCode", "Fail");
			logMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(logMap);
	    }
		
	}
	
}
