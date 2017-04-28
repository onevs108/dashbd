package com.catenoid.dashbd.scheduling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.util.Utils;

public class DbBackupCron extends QuartzJobBean{
	
	private SqlSession sqlSession;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		ServiceAreaMapper saMapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Map<String, Object> backupStatus = mapper.selectAutoBackupStatus();
		String newHour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		String newMinute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		if(newHour.length() == 1){
			newHour = "0"+newHour;
		}
		if(newMinute.length() == 1){
			newMinute = "0"+newMinute;
		}
		String backupTime = newHour+":"+newMinute;
		if(backupStatus.get("auto_yn").equals("Y") && backupStatus.get("backup_time").toString().substring(0,5).equals(backupTime)){
			Utils util = new Utils();
			String databaseBackup = util.getProperyValue("b2.database.backup");
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			HashMap<String, Object> logMap = new HashMap<String, Object>();
			String newYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)); 
			String newMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1); 
			String newDay = String.valueOf(Calendar.getInstance().get(Calendar.DATE)); 
			newHour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)); 
			newMinute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
			String filePath = "";
			if(newMonth.length() == 1){
				newMonth = "0"+newMonth;
			}
			if(newDay.length() == 1){
				newDay = "0"+newDay;
			}
			if(newHour.length() == 1){
				newHour = "0"+newHour;
			}
			if(newMinute.length() == 1){
				newMinute = "0"+newMinute;
			}
			filePath = "/dashbd/backup/"+newYear+"/"+newMonth+"/"+newDay;
			String fileName = "backup-"+newYear+"-"+newMonth+"-"+newDay+"-"+newHour+""+newMinute+".dump";
			String exeDatabaseBackup = String.format("%s %s", databaseBackup, fileName);
			try{
				HashMap<String, Object> searchParam = new HashMap<String, Object>();
				searchParam.put("fileName", fileName);
				searchParam.put("filePath", filePath);
				searchParam.put("createdId", "auto");
				searchParam.put("backupType", "Auto");
					
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
			    
			    int rst = saMapper.insertSystemDbBackup(searchParam);
			    if(rst != 1){
			    	throw new Exception();
			    }
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
			System.out.println("Auto Backup Complete!");
		}
	}
	
	public void setInit(SqlSession ss){
		
		if (sqlSession != null)
			return;
		
		sqlSession = ss;
	}
	
}