package com.catenoid.dashbd;

public class Const {
	
	/**
	 * 사용자 등급
	 */
	public static final int USER_GRADE_ADMIN = 0; // 슈퍼
	public static final int USER_GRADE_USER = 1; // 일반
	
	/**
	 * 로그인 관련
	 */
	public static final int LOGIN_SUCCESS					= 101; // 성공
	public static final int LOGIN_FAIL_MISMATCH				= 102; // 계정 or 비밀번호 불일치
	public static final int LOGIN_FAIL_DISABLED				= 103; // 계정 비활성화
	public static final int LOGIN_FAIL_ACCOUNT_EXPIRED		= 104; // 계정 만료
	public static final int LOGIN_FAIL_CREDENTIALS_EXPIRED	= 105; // 계정 권한 만료
	public static final int LOGIN_FAIL_LOCKED				= 106; // 계정 잠김
	
	public static final int COMMON_SERVER_ERROR				= 9999;
	
	/**
	 * ROLE
	 */
	public static final String ROLE_ADMIN 				= "ROLE_ADMIN";
	public static final String ROLE_USER_MGMT 			= "ROLE_USER_MGMT";
	public static final String ROLE_PERMISSION_MGMT 	= "ROLE_PERMISSION_MGMT";
	public static final String ROLE_CONTENTS_MGMT 		= "ROLE_CONTENTS_MGMT";
	public static final String ROLE_OPERATOR_MGMT 		= "ROLE_OPERATOR_MGMT";
	public static final String ROLE_BMSC_MGMT			= "ROLE_BMSC_MGMT";
	public static final String ROLE_SERVICE_AREA_MGMT 	= "ROLE_SERVICE_AREA_MGMT";
	public static final String ROLE_ENB_MGMT 			= "ROLE_ENB_MGMT";
	public static final String ROLE_SCHEDULE_MGMT 		= "ROLE_SCHEDULE_MGMT";
	public static final String ROLE_SYSTEM_MGMT 		= "ROLE_SYSTEM_MGMT";
	
	/**
	 * menu
	 */
	public static final String MENU_USER_MGMT 			= "USER_MGMT";
	public static final String MENU_PERMISSION_MGMT 	= "PERMISSION_MGMT";
	public static final String MENU_CONTENTS_MGMT 		= "CONTENTS_MGMT";
	public static final String MENU_OPERATOR_MGMT 		= "OPERATOR_MGMT";
	public static final String MENU_BMSC_MGMT			= "BMSC_MGMT";
	public static final String MENU_SERVICE_AREA_MGMT 	= "SERVICE_AREA_MGMT";
	public static final String MENU_ENB_MGMT 			= "ENB_MGMT";
	public static final String MENU_SCHEDULE_MGMT 		= "SCHEDULE_MGMT";
	public static final String MENU_SYSTEM_MGMT 		= "SYSTEM_MGMT";
	
}
