package com.catenoid.dashbd;

import java.util.Calendar;
import java.util.Locale;

public class Const {
	
	/**
	 * 사용자 등급
	 */
	public static final int USER_GRADE_ADMIN = 13; // 슈퍼
	public static final int USER_GRADE_USER = 1; // 일반
	public static final int USER_GRADE_CIRCLE = 9999; // 일반
	
	/**
	 * 로그인 관련
	 */
	public static final int LOGIN_SUCCESS					= 101; // 성공
	public static final int LOGIN_FAIL_MISMATCH				= 102; // 계정 or 비밀번호 불일치
	public static final int LOGIN_FAIL_DISABLED				= 103; // 계정 비활성화
	public static final int LOGIN_FAIL_ACCOUNT_EXPIRED		= 104; // 계정 만료
	public static final int LOGIN_FAIL_CREDENTIALS_EXPIRED	= 105; // 계정 권한 만료
	public static final int LOGIN_FAIL_LOCKED				= 106; // 계정 잠김
//	public static final int LOGIN_FAIL_INIT					= 107; // 계정 비밀번호 초기화
	public static final int LOGIN_FAIL_MISMATCH_PASSWORD	= 108; // 계정 비밀번호 불일치
	
	
	public static final int COMMON_SERVER_ERROR				= 9999;
	
	/**
	 * ROLE
	 */
	public static final String ROLE_ADMIN 					= "ROLE_ADMIN";
//	public static final String ROLE_USER_MGMT 				= "ROLE_USER_MGMT";
	public static final String ROLE_PERMISSION_MGMT 		= "ROLE_PERMISSION_MGMT";
//	public static final String ROLE_CONTENTS_MGMT 			= "ROLE_CONTENTS_MGMT";
//	public static final String ROLE_OPERATOR_MGMT 			= "ROLE_OPERATOR_MGMT";
//	public static final String ROLE_BMSC_MGMT				= "ROLE_BMSC_MGMT";
//	public static final String ROLE_CIRCLE_MGMT				= "ROLE_CIRCLE_MGMT";
//	public static final String ROLE_HOTSPOT_MGMT			= "ROLE_HOTSPOT_MGMT";
//	public static final String ROLE_SERVICE_AREA_MGMT 		= "ROLE_SERVICE_AREA_MGMT";
//	public static final String ROLE_SERVICE_AREA_GROUP_MGMT = "ROLE_SERVICE_AREA_GROUP_MGMT";
//	public static final String ROLE_ENB_MGMT 				= "ROLE_ENB_MGMT";
//	public static final String ROLE_SCHEDULE_MGMT 			= "ROLE_SCHEDULE_MGMT";
//	public static final String ROLE_SYSTEM_MGMT 			= "ROLE_SYSTEM_MGMT";
//	public static final String ROLE_SYSTEM_STAT_MGMT		= "ROLE_SYSTEM_STAT_MGMT";
//	public static final String ROLE_SYSTEM_CONF_MGMT		= "ROLE_SYSTEM_CONF_MGMT";
//	public static final String ROLE_SYSTEM_DB_MGMT			= "ROLE_SYSTEM_DB_MGMT";
	public static final String ROLE_OPERATOR_MGMT 			= "ROLE_OPERATOR_MGMT";
	public static final String ROLE_OPERATOR_GROUP_MGMT 	= "ROLE_OPERATOR_GROUP_MGMT";
	public static final String ROLE_CONTENTS_MGMT 			= "ROLE_CONTENTS_MGMT";
	public static final String ROLE_SERVICE_AREA_MGMT 		= "ROLE_SERVICE_AREA_MGMT";
	public static final String ROLE_SERVICE_AREA_GROUP_MGMT = "ROLE_SERVICE_AREA_GROUP_MGMT";
	public static final String ROLE_SCHEDULE_MGMT 			= "ROLE_SCHEDULE_MGMT";
//	public static final String ROLE_SERVICE_CLASS_MGMT 		= "ROLE_SERVICE_CLASS_MGMT";
//	public static final String ROLE_SESSION_MONITORING 		= "ROLE_SESSION_MONITORING";
	public static final String ROLE_SYSTEM_MGMT 			= "ROLE_SYSTEM_MGMT";
	public static final String ROLE_MOOD_MONITORING 		= "MOOD_MONITORING";
	
	/**
	 * menu
	 */
//	public static final String MENU_USER_MGMT 				= "USER_MGMT";
	public static final String MENU_PERMISSION_MGMT 		= "PERMISSION_MGMT";
	public static final String MENU_CONTENTS_MGMT 			= "CONTENTS_MGMT";
//	public static final String MENU_OPERATOR_MGMT 			= "OPERATOR_MGMT";
//	public static final String MENU_BMSC_MGMT				= "BMSC_MGMT";
//	public static final String MENU_CIRCLE_MGMT				= "MENU_CIRCLE_MGMT";
//	public static final String MENU_HOTSPOT_MGMT			= "MENU_HOTSPOT_MGMT";
//	public static final String MENU_SERVICE_AREA_MGMT 		= "SERVICE_AREA_MGMT";
//	public static final String MENU_SERVICE_AREA_GROUP_MGMT = "SERVICE_AREA_GROUP_MGMT";
//	public static final String MENU_ENB_MGMT 				= "ENB_MGMT";
//	public static final String MENU_SCHEDULE_MGMT 			= "SCHEDULE_MGMT";
//	public static final String MENU_SYSTEM_MGMT 			= "SYSTEM_MGMT";
//	public static final String MENU_SYSTEM_STAT_MGMT		= "SYSTEM_STAT_MGMT";
	
	public static final String MENU_OPERATOR_MGMT 			= "USER_MGMT";
	public static final String MENU_OPERATOR_GROUP_MGMT		= "OPERATOR_MGMT";
	public static final String MENU_CIRCLE_MGMT				= "CIRCLE_MGMT";
	public static final String MENU_HOTSPOT_MGMT			= "HOTSPOT_MGMT";
	public static final String MENU_SERVICE_AREA_MGMT		= "SERVICE_AREA_MGMT";
	public static final String MENU_SERVICE_AREA_GROUP_MGMT	= "SERVICE_AREA_GROUP_MGMT";
	public static final String MENU_SCHEDULE_MGMT			= "SCHEDULE_MGMT";
//	public static final String MENU_SERVICE_CLASS_MGMT		= "SERVICE_CLASS_MGMT";
//	public static final String MENU_SESSION_MONITORING		= "SESSION_MONITORING";
	public static final String MENU_SYSTEM_MGMT				= "SYSTEM_MGMT";
	public static final String MENU_LOG_MGMT				= "LOG_MGMT";
	public static final String MENU_SYSTEM_CONF_MGMT		= "SYSTEM_CONF_MGMT";
	public static final String MENU_SYSTEM_DB_MGMT			= "SYSTEM_DB_MGMT";
	public static final String MENU_MOOD_MONITORING			= "MOOD_MONITORING";
	
	public static String getLogTime() {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		String year = cal.get ( Calendar.YEAR ) + "";
		String month = ( cal.get ( Calendar.MONTH ) + 1 ) + "";
		String date = cal.get ( Calendar.DATE ) + "";
		String hour = cal.get ( Calendar.HOUR_OF_DAY ) + "";
		String minute = cal.get ( Calendar.MINUTE ) + "";
		String second = cal.get ( Calendar.SECOND ) + "";
		
		month = (month.length() == 1)? "0" + month : month;
		date = (date.length() == 1)? "0" + date : date;
		hour = (hour.length() == 1)? "0" + hour : hour;
		minute = (minute.length() == 1)? "0" + minute : minute;
		second = (second.length() == 1)? "0" + second : second;
		
        String sysdate = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
		return sysdate;
	}
	
	public static final int hddLogLine = 4;
}
