package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Services;
import com.catenoid.dashbd.dao.model.ServicesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServicesMapper {
    int countByExample(ServicesExample example);

    int deleteByExample(ServicesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Services record);

    int insertSelective(Services record);
    
    int insertNoValues(Services record);

    List<Services> selectByExample(ServicesExample example);

    Services selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Services record, @Param("example") ServicesExample example);

    int updateByExample(@Param("record") Services record, @Param("example") ServicesExample example);

    int updateByPrimaryKeySelective(Services record);

    int updateByPrimaryKey(Services record);
}