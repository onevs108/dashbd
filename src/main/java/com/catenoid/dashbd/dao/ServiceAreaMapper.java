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

import java.util.HashMap;
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
    
    List<ServiceAreaEnbAp> getServiceAreaEnbAp(HashMap<String, Object> example);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApWithBounds(HashMap<String, Object> example);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApOther(HashMap<String, Object> example);
    
    List<ServiceAreaCount> getServiceAreaCountByBmSc(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getSeviceAreaByBmScCity(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getServiceAreaByLatLng(ServiceAreaEnbSearchParam searchParm);
    
    List<ScheduleSummary> getScheduleSummaryByServiceArea(ScheduleSummarySearchParam searchParm);
    
    List<ScheduleSummary> getScheduleSummaryByBmsc(HashMap<String, Integer> searchParm);
    
    HashMap<String, Integer> getGBRSum(HashMap<String, Integer> searchParm);
    
    int addToServiceArea(HashMap< String, Integer > searchParm);
    
    int deleteFromServiceArea(HashMap< String, Object > searchParm);
    
    int createServiceArea(HashMap< String, Object > searchParm);
    
    int createENBs(HashMap< String, Object > searchParm);
    
    List<BmscServiceArea> getSeviceAreaByBmScId( BmscServiceAreaSearchParam searchParm );
    
    List<HashMap> downloadENBs( HashMap<String, Object> searchParm );
    
    List<HashMap> downloadENBsByServiceAreaId( HashMap<String, Object> searchParm );
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApWNotMappedSA(HashMap<String, Object> searchParm);
    
    int createBmScServiceArea(HashMap< String, Object > searchParm);

}