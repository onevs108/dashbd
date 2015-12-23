package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbSearchParam;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.OperatorBmsc;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ScheduleSummary;
import com.catenoid.dashbd.dao.model.ScheduleSummarySearchParam;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaCount;
import com.catenoid.dashbd.dao.model.ServiceAreaExample;
import com.catenoid.dashbd.dao.model.ServiceAreaSearchParam;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceAreaMapper {
    int countByExample(ServiceAreaExample example);

    int deleteByExample(ServiceAreaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceArea record);

    int insertSelective(ServiceArea record);

    List<ServiceArea> selectByExample(ServiceAreaExample example);
    
    List<ServiceAreaEnbAp> selectServiceAreaEnbAp(ServiceAreaEnbApExample example);
    
    List<ServiceAreaEnbAp> selectServiceAreaEnbApRangeOuterJoin(ServiceAreaEnbApExample example);

    ServiceArea selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceArea record, @Param("example") ServiceAreaExample example);

    int updateByExample(@Param("record") ServiceArea record, @Param("example") ServiceAreaExample example);

    int updateByPrimaryKeySelective(ServiceArea record);

    int updateByPrimaryKey(ServiceArea record);
    
    List<Operator> getServiceAreaOperator(OperatorSearchParam searchParam);
    
    List<BmscServiceArea> getSeviceAreaByBmSc(BmscServiceAreaSearchParam searchParm);
    
    List<Bmsc> getSeviceAreaBmSc(OperatorSearchParam searchParm);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbAp(ServiceAreaEnbSearchParam example);
    
    List<ServiceAreaCount> getServiceAreaCountByBmSc(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getSeviceAreaByBmScCity(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getServiceAreaByLatLng(ServiceAreaEnbSearchParam searchParm);
    
    List<ScheduleSummary> getScheduleSummaryByServiceArea(ScheduleSummarySearchParam searchParm);
    
    
}