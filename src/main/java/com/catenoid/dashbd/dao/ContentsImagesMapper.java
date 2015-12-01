package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ContentsImages;
import com.catenoid.dashbd.dao.model.ContentsImagesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentsImagesMapper {
    int countByExample(ContentsImagesExample example);

    int deleteByExample(ContentsImagesExample example);

    int deleteByPrimaryKey(Integer id);
    
    int deleteByForeignKey(Integer id);

    int insert(ContentsImages record);

    int insertSelective(ContentsImages record);

    List<ContentsImages> selectByExample(ContentsImagesExample example);

    ContentsImages selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContentsImages record, @Param("example") ContentsImagesExample example);

    int updateByExample(@Param("record") ContentsImages record, @Param("example") ContentsImagesExample example);

    int updateByPrimaryKeySelective(ContentsImages record);

    int updateByPrimaryKey(ContentsImages record);
}