package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.ContentsExample;
import com.catenoid.dashbd.dao.model.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ContentsMapper {
	
	/* inbo coding START*/
	List<Map> selectContents(Map map);
	Map selectContent(Map map);
	List<Map> selectContentImages(Map map);
	int updateContent(Map map);
	int insertContent(Map map);
	int deleteContentImage(Map map);
	
	
	List<Contents> selectContentList(Map<String, Object> map);
	
	int selectContentListCount(Map<String, Object> map);
	/* inbo coding END*/
	
    int countByExample(ContentsExample example);

    int deleteByExample(ContentsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Contents record);

    int insertSelective(Contents record);
    
    

    List<Contents> selectByExampleWithBLOBs(ContentsExample example);

    List<Contents> selectByExample(ContentsExample example);
    
    Contents selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByExampleWithBLOBs(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByExample(@Param("record") Contents record, @Param("example") ContentsExample example);

    int updateByPrimaryKeySelective(Contents record);

    int updateByPrimaryKeyWithBLOBs(Contents record);

    int updateByPrimaryKey(Contents record);
    
	int selectContentsCount(HashMap<String, Object> params);
}