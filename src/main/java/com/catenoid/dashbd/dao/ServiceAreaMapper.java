package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ScheduleSummary;
import com.catenoid.dashbd.dao.model.ScheduleSummarySearchParam;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaCount;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbSearchParam;
import com.catenoid.dashbd.dao.model.ServiceAreaExample;
import com.catenoid.dashbd.dao.model.ServiceAreaPermissionAp;
import com.catenoid.dashbd.dao.model.SystemBroadCastContents;
import com.catenoid.dashbd.dao.model.SystemDatabaseBackup;
import com.catenoid.dashbd.dao.model.SystemIncomingLog;

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
    
    long countByEnbsCount(HashMap<String, Object> searchParm);
    
    long countByServiceAreaCount(HashMap<String, Object> searchParm);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbAp(HashMap<String, Object> example);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApWithBounds(HashMap<String, Object> example);
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApOther(HashMap<String, Object> example);
    
    List<ServiceAreaCount> getServiceAreaCountByBmSc(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getSeviceAreaByBmScCity(BmscServiceAreaSearchParam searchParm);
    
    List<BmscServiceArea> getServiceAreaByLatLng(ServiceAreaEnbSearchParam searchParm);
    
    List<ScheduleSummary> getScheduleSummaryByServiceArea(ScheduleSummarySearchParam searchParm);
    
    List<ScheduleSummary> getScheduleSummaryByBmsc(HashMap<String, Object> searchParm);
    
    HashMap<String, Integer> getGBRSum(HashMap<String, Integer> searchParm);
    
    int addToServiceArea(HashMap< String, Integer > searchParm);
    
    int deleteFromServiceArea(HashMap< String, Object > searchParm);
    
    int createServiceArea(HashMap< String, Object > searchParm);
    
    int createENBs(HashMap< String, Object > searchParm);
    
    int updateENBs(HashMap< String, Object > searchParm);
    
    List<BmscServiceArea> getSeviceAreaByBmScId( BmscServiceAreaSearchParam searchParm );
    
    List<HashMap> downloadENBs( HashMap<String, Object> searchParm );
    
    List<HashMap> downloadENBsByServiceAreaId( HashMap<String, Object> searchParm );
    
    List<ServiceAreaEnbAp> getServiceAreaEnbApNotMappedSA(HashMap<String, Object> searchParm);
    
    int createBmScServiceArea(HashMap< String, Object > searchParm);
    
    List<HashMap<String, Object>> getSeviceAreaNotMapped(HashMap<String, Integer> searchParm);
    
    List<ServiceAreaEnbAp> getEnbsList(HashMap<String, Object> example);
    
    List<ServiceAreaPermissionAp> getPermissionList(HashMap<String, Object> example);
    
    int selectServiceAreaCnt(HashMap< String, Object > searchParm);
    
    int serviceAreaByDelete(HashMap< String, Object > serviceParam);
    
    int serviceAreaByENBDelete(HashMap< String, Object > serviceParam);

    int selectEnbListCount(HashMap<String, Integer> searchParam);
    
    List<SystemIncomingLog> getIncomingTrafficList(HashMap<String, Object> example);
    
    List<SystemIncomingLog> getInterTrafficList(HashMap<String, Object> example);
    
    List<SystemDatabaseBackup> getSystemDblist(HashMap<String, Object> example);
    
    int insertSystemDbBackup(HashMap< String, Object > searchParm);
    
    List<SystemBroadCastContents> getSystemBCContentsList(HashMap<String, Object> example);
    
    
    
    List<HashMap<String, Object>> getServiceAreaGroupList(HashMap<String, Object> searchParam);
    
    List<HashMap<String, Object>> getCitiesInCircle(HashMap<String, Object> searchParam);
    
    List<HashMap<String, Object>> getHotspotsInCities(HashMap<String, Object> searchParam);
    
    int checkServiceAreaGroupName(HashMap<String, Object> searchParam);
    
    int insertServiceAreaGroup(HashMap<String, Object> insertParam);
    
    int deleteServiceAreaGroup(HashMap<String, Object> insertParam);
    
    List<HashMap<String, Object>> getTreeNodeData(HashMap<String, Object> searchParam);
    
    int deleteServiceAreaGroupHotspot(HashMap<String, Object> insertParam);
    
    int insertServiceAreaGroupHotspot(HashMap<String, Object> insertParam);

	List<HashMap<String, String>> getServiceAreaOperator1(OperatorSearchParam searchParam);
    
}