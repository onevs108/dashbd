package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.ContentsExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ContentsMapper {
	
	/* inbo coding START*/
	List<Map> selectContents(Map map);
	/* inbo coding END*/
	
    int countByExample(ContentsExample example);

    int deleteByExample(ContentsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Contents record);

    int insertSelective(Contents record);
    
    int insertContent(Map map);

    List<Contents> selectByExampleWithBLOBs(ContentsExample example);

    List<Contents> selectByExample(ContentsExample example);
    
    Contents selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByExampleWithBLOBs(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByExample(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByPrimaryKeySelective(Contents record);

    int updateByPrimaryKeyWithBLOBs(Contents record);

    int updateByPrimaryKey(Contents record);
}