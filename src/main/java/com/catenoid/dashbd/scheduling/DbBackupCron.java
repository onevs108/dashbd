package com.catenoid.dashbd.scheduling;

import java.util.Calendar;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.dao.UsersMapper;

public class DbBackupCron extends QuartzJobBean{
	
	private SqlSession sqlSession;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> backupStatus = mapper.selectAutoBackupStatus();
		String newHour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR)); 
		String newMinute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		String newSecond = String.valueOf(Calendar.getInstance().get(Calendar.SECOND)); 
		String hour = "";
		String minute = "";
		String second = "";
		if(newHour.length() == 1){
			hour += "0"+newHour;
		}
		if(newMinute.length() == 1){
			minute += "0"+newMinute;
		}
		if(newSecond.length() == 1){
			second += "0"+newSecond;
		}
		String backupTime = hour+":"+minute+":"+second;
		if(backupStatus.get("auto_yn").equals("Y") && backupStatus.get("backup_time").equals(backupTime)){
			new ActionDbBackup();
			System.out.println("auto backup check");
		}
	}
	
	public void setInit(SqlSession ss){
		
		if (sqlSession != null)
			return;
		
		sqlSession = ss;
	}
	
}