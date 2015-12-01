package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ScheduleContents;
import com.catenoid.dashbd.dao.model.ScheduleContentsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScheduleContentsMapper {
    int countByExample(ScheduleContentsExample example);

    int deleteByExample(ScheduleContentsExample example);

    int insert(ScheduleContents record);

    int insertSelective(ScheduleContents record);

    List<ScheduleContents> selectByExample(ScheduleContentsExample example);
    
    List<ScheduleContents> selectScheduleContents(ScheduleContentsExample example);
    
    List<ScheduleContents> selectBandwidthCheckScheduleContents(ScheduleContents record);
    
    List<ScheduleContents> selectScheduleContentsByScheduleId(Integer id);
    
    List<ScheduleContents> selectScheduleContentsByContentId(Integer id);
    
    List<ScheduleContents> selectScheduleContentsByServiceAreaId(ScheduleContents record);

    int updateByExampleSelective(@Param("record") ScheduleContents record, @Param("example") ScheduleContentsExample example);

    int updateByExample(@Param("record") ScheduleContents record, @Param("example") ScheduleContentsExample example);
}