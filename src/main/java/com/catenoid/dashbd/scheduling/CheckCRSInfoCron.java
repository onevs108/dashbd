package com.catenoid.dashbd.scheduling;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.catenoid.dashbd.dao.ScheduleMapper;

public class CheckCRSInfoCron extends QuartzJobBean{
	
	private SqlSession sqlSession;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		List<Map<String, Object>> moodRequestInfo = mapper.selectMoodRequestInfo();
		Map<String, Object> modeLimit = mapper.selectCrsLimit();
		int uniMax = (Integer) modeLimit.get("unicast");
		for (int i = 0; i < moodRequestInfo.size(); i++) {
			if(uniMax < (Integer) moodRequestInfo.get(i).get("countUC")){
				System.out.println("초과했음");
			}else{
				System.out.println("안 초과했음");
			}
		}
	}
	
	public void setInit(SqlSession ss){
		
		if (sqlSession != null)
			return;
		
		sqlSession = ss;
	}
	
}