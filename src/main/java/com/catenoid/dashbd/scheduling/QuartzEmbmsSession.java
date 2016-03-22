package com.catenoid.dashbd.scheduling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.dao.BmscMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class QuartzEmbmsSession extends QuartzJobBean{
	private final String RETURN_SHELL = "Session_Cnt:";
	//WeatherService weatherService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SqlSession sqlSession;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("QuartzEmbmsSession START");
		
		BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map> list = mapper.selectEmbms(params);
		 
		for(Map map : list){
			Long id = (Long)map.get("id");
			String serverName = (String)map.get("serverName");
			//String protocol = (String)map.get("protocol");
			String IPAddress = (String)map.get("IPAddress");
			String loginId = (String)map.get("loginId");
			String password = (String)map.get("password");
			String command = (String)map.get("command");
			
			//ex> getSession.sh ServerName IPAddress UserID Password
			//return> Session_Cnt:3
			String exeCommand = String.format("%s %s %s %s", command, serverName, IPAddress,loginId, password);
			try{
				  Runtime runtime = Runtime.getRuntime();
	              Process process = runtime.exec(exeCommand);
	              InputStream is = process.getInputStream();
	              InputStreamReader isr = new InputStreamReader(is);
	              BufferedReader br = new BufferedReader(isr);
	              String line;
	              while((line = br.readLine()) != null) {
	                  System.out.println(line);
	              }
	            //String line = "session Cnt:3";  //for TEST
				String sessionCnt = line.substring(RETURN_SHELL.length());
				params.put("sessionCnt", sessionCnt);
				params.put("id", id);
				mapper.updateEmbms(params);
	              
		          //System.out.println("DONE");
		    }catch(Exception e){
		          logger.error("",e);
		          params.put("sessionCnt", "-1");
	              params.put("id", id);
	              mapper.updateEmbms(params);
		    }
		}
	}
	
	public void setInit(SqlSession ss){
		
		if (sqlSession != null)
			return;
		
		System.out.println("QuartzEmbmsSession init()");
		sqlSession = ss;
	}

}
