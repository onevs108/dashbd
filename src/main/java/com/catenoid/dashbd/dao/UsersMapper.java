package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.dao.model.UsersExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int countByExample(UsersExample example);

    int deleteUser(Users user);
    
    int deleteByExample(UsersExample example);

    int deleteByPrimaryKey(int userId);

    int insertUser(Users user);
    
    int insert(Users record);

    int insertSelective(Users record);

    List<Users> selectByExample(UsersExample example);

    List<Users> selectAll();
    
    List<Users> selectUserList(Map<String, Object> map);
    
    int selectUserListCount(Map<String, Object> map);
    
    Users selectByPrimaryKey(int userId);
    
    Users selectByUserId(String userId);
    
    int updateByExampleSelective(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByExample(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    
    
    List<Users> selectUserListInPermission(Map<String, Object> map);
    
    int selectUserListCountInPermission(Map<String, Object> map);
    
    List<Permission> selectPermissionAll();
    
    List<Permission> selectPermissionsByUserId(String userId);
    
    int insertPermissionOfUser(Map<String, Object> map);
    
    int deletePermissionOfUser(String userId);
    
    int insertSystemAjaxLog(Map<String, Object> map);
    
    int insertSystemInterFaceLog(Map<String, String> map);
}