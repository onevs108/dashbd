package com.catenoid.dashbd;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.OperatorService;
import com.catenoid.dashbd.service.PermissionService;
import com.catenoid.dashbd.service.UserService;
import com.google.gson.Gson;

/**
 * Operator 관리 Controller
 * 
 * @author iskwon
 */
@Controller
public class OperatorController {
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorController.class);
	
	@Resource(name = "transactionManager") 
	protected DataSourceTransactionManager txManager;
	
	@Autowired
	private OperatorService operatorServiceImpl;
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private PermissionService permissionServiceImpl;
	/**
	 * Operator 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/operator.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getOperatorMgmt(HttpServletRequest request, Model model) {
		Users user = (Users) request.getSession(false).getAttribute("USER");
		
//		List<Permission> permissionList = permissionServiceImpl.getPermissionList(null);
//		List<Operator> gradeList = operatorServiceImpl.getGradeListAll();
		List<Circle> circleList = operatorServiceImpl.getCircleListNameAll();
		
//		for(int i=0; i < gradeList.size(); i++) {
//			if(gradeList.get(i).getId() == 1 || gradeList.get(i).getId() == 2 || gradeList.get(i).getId() == 3) {
//				gradeList.remove(i);
//				i--;
//			}
//		}

//		model.addAttribute("permissionList", permissionList);
//		model.addAttribute("gradeList", gradeList);
		model.addAttribute("userGrade", user.getGrade());
		model.addAttribute("circleList", circleList);
		return "operator/operatorMgmt";
	}
	
	/**
	 * Operator 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);

			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			JSONArray rows = operatorServiceImpl.getOperatorListToJsonArray(sort, order, offset, limit);
			jsonResult.put("rows", rows);
			int total = operatorServiceImpl.getGradeListCount();
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * Operator 정보 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/info.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOeratorInfo(
			@RequestParam(value = "operatorId", required = true) Integer operatorId) {
		logger.info("-> [operatorId = {}]", operatorId);
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			Operator operator = operatorServiceImpl.getOperator(operatorId);
			jsonResult.put("operator", operator.toJSONObject());
		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Grade 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postGradeInsert(@ModelAttribute Operator operator) {
		logger.info("-> [operator = {}]", operator.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.insertGrade(operator));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Operator 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorInsert(@ModelAttribute Operator operator) {
		logger.info("-> [operator = {}]", operator.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.insertOperator(operator));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Grade 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String GradeDelete(@RequestParam(value = "gradeId", required = true) Integer gradeId) {
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.deleteGrade(gradeId));
		
		return jsonResult.toString();
	}
	
	/**
	 * Circle 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/circle/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String ciecleDelete(@RequestParam(value = "circleId", required = true) Integer circleId) {
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.deleteCircle(circleId));
		
		return jsonResult.toString();
	}
	
	/**
	 * Circle 선택 시 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/circle/selectCircle.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String selectCircle(@RequestBody HashMap<String,String> param) {
		
		JSONObject jsonResult = new JSONObject();

		int offset = Integer.parseInt(String.valueOf(param.get("offset")));
		int limit = Integer.parseInt(String.valueOf(param.get("limit")));
		param.put("start", Integer.toString(offset+1));
		param.put("end", Integer.toString(offset+limit));
		
		List<HashMap<String,String>> circleList = operatorServiceImpl.selectOperatorFromCircle(param);
		Gson gson = new Gson();
		String str = gson.toJson(circleList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		int total = operatorServiceImpl.selectOperatorFromCircleCount(param);
		jsonResult.put("total", total);
		return jsonResult.toString();
	}
	
	/**
	 * Grade Name 중복 체크
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/check.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postGradeCheck(@RequestParam(value = "operatorName", required = true) String operatorName) {
		logger.info("-> [operatorName = {}]", operatorName);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.checkGradeName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Operator Name 중복 체크
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/check.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorCheck(@RequestParam(value = "operatorName", required = true) String operatorName) {
		logger.info("-> [operatorName = {}]", operatorName);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.checkOperatorName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * 그룹 생성 모달 팝업 호출
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/callAddGruopModal.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ModelAndView callAddGruopModal(@RequestParam HashMap<String, Object> param, Model model) {
		ModelAndView mv = new ModelAndView("operator/addGroupModal");
		
		List<Users> initMemberList = null;
		List<Users> otherMemberList = null;
		List<Permission> permissionList = permissionServiceImpl.getPermissionList(null);
		
		if(param.get("accessDiv").equals("edit")) {
			Operator operator = null;
			
			if(param.get("groupDiv").equals("National")) {
				operator = operatorServiceImpl.selectByGradeName(param.get("groupName").toString());
				param.put("targetDiv", "grade");
				param.put("grade", operator.getId());
			} else if(param.get("groupDiv").equals("Regional")) {
				operator = operatorServiceImpl.selectByOperatorName(param.get("groupName").toString());
				param.put("targetDiv", "operator");
				param.put("operatorId", operator.getId());
			}
			initMemberList = operatorServiceImpl.selectMemberList(param);
			model.addAttribute("operator", operator);
			
			if(param.get("groupDiv").equals("National")) {
				param.put("grade", "");
				param.put("notGrade", operator.getId());
			} else if(param.get("groupDiv").equals("Regional")) {
				param.put("operatorId", "");
				param.put("notOperatorId", operator.getId());
			}
			otherMemberList = operatorServiceImpl.selectMemberList(param);
			
			String permissionStr = operator.getPermission();
			for(int i=0; i < permissionList.size(); i++) {
				StringTokenizer stk = new StringTokenizer(permissionStr, ",");
				
				Permission tempPermission = permissionList.get(i);
				String tempPerId = String.valueOf(tempPermission.getId());
				
				while(stk.hasMoreTokens()) {
					if(stk.nextToken().equals(tempPerId)) {
						tempPermission.setCheckYn("Y");
					}
				}
			}
		} else if(param.get("accessDiv").equals("add")) {
			otherMemberList = operatorServiceImpl.selectMemberList(param);
		}
		
		model.addAttribute("groupDiv", param.get("groupDiv").toString().toLowerCase());
		model.addAttribute("accessDiv", param.get("accessDiv"));
		model.addAttribute("permissionList", permissionList);
		model.addAttribute("initMemberList", initMemberList);
		model.addAttribute("otherMemberList", otherMemberList);
		return mv;
	}
	
	/**
	 * 멤버리스트 모달 팝업 호출
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/callMemberListModal.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ModelAndView callMemberListModal(@RequestParam HashMap<String, Object> param, Model model) {
		ModelAndView mv = new ModelAndView("operator/memberListModal");
		List<Operator> gradeList = operatorServiceImpl.getGradeListAll();
		
		//자신의 group_id는 제외하고 콤보 박스 값을 넘김
		for(int i=0; i < gradeList.size(); i++) {
			if(param.get("groupId").toString().equals(String.valueOf(gradeList.get(i).getId()))) {
				gradeList.remove(i);
				break;
			}
		}
		
		model.addAttribute("gradeList", gradeList);
		
		return mv;
	}
	
	/**
	 * 멤버리스트 테이블 조회
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/getMemberList.do", method = {RequestMethod.POST}, produces="application/json;charset=UTF-8;")
	@ResponseBody
	public String getMemberList(@RequestBody String body) {
		
		List<Users> memberList = null;
		
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			String tabDivId = (String) requestJson.get("tabDivId");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			int offset = Integer.parseInt(String.valueOf(requestJson.get("offset")));
			int limit = Integer.parseInt(String.valueOf(requestJson.get("limit")));
			int groupId = Integer.parseInt(requestJson.get("groupId").equals("")? "0" : (String) requestJson.get("groupId"));
			
			String searchGrade = (String) requestJson.get("searchGrade");
			String searchDiv = (String) requestJson.get("searchDiv");
			String searchInput = (String) requestJson.get("searchInput");
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("sort", sort);
			param.put("order", order);
			param.put("offset", offset);
			param.put("limit", limit);
			param.put("start", Integer.toString(offset+1));
			param.put("end", Integer.toString(offset+limit));
			
			param.put("searchGrade", searchGrade);
			param.put("searchDiv", searchDiv);
			param.put("searchInput", searchInput);
			
			if(tabDivId.equals("table3")) {
				param.put("targetDiv", "grade");
				param.put("grade", groupId);
				memberList = operatorServiceImpl.selectMemberList(param);
			} else if(tabDivId.equals("table4")) {
				param.put("targetDiv", "grade");
				param.put("grade", "");
				param.put("notGrade", groupId);
				memberList = operatorServiceImpl.selectMemberList(param);
			}
			
			JSONArray rows = new JSONArray();
			for (Users user : memberList)
				rows.add(user.toJSONObject());
			
			jsonResult.put("rows", rows);
			int total = operatorServiceImpl.getMemberListCount(param);
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * 그룹 추가,수정 메소드
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/proccessGroup.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String proccessGroup(@RequestParam HashMap<String, Object> param) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		JSONObject jsonResult = new JSONObject();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
		TransactionStatus txStatus= txManager.getTransaction(def);
		
		logger.info("-> [param = {}]", param);
		
		try {
			String accessDiv = param.get("accessDiv").toString();
			String proccessDiv = param.get("proccessDiv").toString();
			int groupId = Integer.parseInt(param.get("groupId").toString().equals("")? "0" : param.get("groupId").toString());
			String menuListStr = param.get("menuListStr").toString();
			String groupName = param.get("groupName").toString();
			String gruopDescription = param.get("gruopDescription").toString();
			String memberListStr = param.get("memberListStr").toString();
			
			//National Group 처리
			if(accessDiv.equals("national")) {
				if(proccessDiv.equals("add")) {
					//그룹명 존재 여부 조회
					if(operatorServiceImpl.checkGradeName(groupName)) {
						Operator operator = new Operator();
						operator.setName(groupName);
						operator.setDescription(gruopDescription);
						operator.setPermission(menuListStr);
						operatorServiceImpl.insertGrade(operator);
						
						StringTokenizer stk = new StringTokenizer(menuListStr, ",");
						List<String> permissions = new ArrayList<String>();
						while(stk.hasMoreTokens()) {
							permissions.add(stk.nextToken());
						}
						
						StringTokenizer stk1 = new StringTokenizer(memberListStr, ",");
						
						while(stk1.hasMoreTokens()) {
							String userId = stk1.nextToken();
							Users record = new Users();
							record.setUserId(userId);
							record.setGrade(operator.getId());
							record.setOperatorId(null);
							
							//새로운 그룹으로 유저 정보 변경
							usersMapper.updateByPrimaryKeySelective(record);
							//추가할 유저의 기존 권한 삭제
							usersMapper.deletePermissionOfUser(userId);
							//권한 추가
							permissionServiceImpl.insertUserPermission(userId, permissions);
						}
						
						txManager.commit(txStatus);
						jsonResult.put("resultCode", "S");
					} else {
						jsonResult.put("resultCode", "E");
					}
				} else if(proccessDiv.equals("edit")) {
					param.put("grade", groupId);
					List<Users> initMember = userServiceImpl.selectUserListByCondition(param);
					
					if(initMember.size() > 0) {
						//기존에 추가되어있던 사용자의 권한 모두 삭제
						for(int i=0; i < initMember.size(); i++) {
							Users record = new Users();
							record.setUserId(initMember.get(i).getUserId());
							record.setGrade(null);
							record.setOperatorId(null);
							usersMapper.updateByPrimaryKeySelective(record);
							usersMapper.deletePermissionOfUser(initMember.get(i).getUserId());
						}
					}
					
					Operator operator = new Operator();
					operator.setId(groupId);
					operator.setName(groupName);
					operator.setDescription(gruopDescription);
					operator.setPermission(menuListStr);
					operatorServiceImpl.insertGrade(operator);
					
					StringTokenizer stk = new StringTokenizer(menuListStr, ",");
					List<String> permissions = new ArrayList<String>();
					while(stk.hasMoreTokens()) {
						permissions.add(stk.nextToken());
					}
					
					StringTokenizer stk1 = new StringTokenizer(memberListStr, ",");
					
					while(stk1.hasMoreTokens()) {
						String userId = stk1.nextToken();
						Users record = new Users();
						record.setUserId(userId);
						record.setGrade(operator.getId());
						record.setOperatorId(null);
						
						//새로운 그룹으로 유저 정보 변경
						usersMapper.updateByPrimaryKeySelective(record);
						//추가할 유저의 기존 권한 삭제
						usersMapper.deletePermissionOfUser(userId);
						//권한 추가
						permissionServiceImpl.insertUserPermission(userId, permissions);
					}
					
					txManager.commit(txStatus);
					jsonResult.put("resultCode", "S");
				}
			} 
			//Regional Group 처리
			else {
				if(proccessDiv.equals("add")) {
					//Operator 이름 확인
					if(operatorServiceImpl.checkOperatorName(groupName)) {
						Operator operator = new Operator();
						operator.setCircleName(param.get("circleName").toString());
						operator.setName(groupName);
						operator.setDescription(gruopDescription);
						operator.setPermission(menuListStr);
						operatorServiceImpl.insertOperator(operator);
						
						StringTokenizer stk = new StringTokenizer(menuListStr, ",");
						List<String> permissions = new ArrayList<String>();
						while(stk.hasMoreTokens()) {
							permissions.add(stk.nextToken());
						}
						
						StringTokenizer stk1 = new StringTokenizer(memberListStr, ",");
						
						while(stk1.hasMoreTokens()) {
							String userId = stk1.nextToken();
							Users record = new Users();
							record.setUserId(userId);
							record.setGrade(9999);
							record.setOperatorId(operator.getId());
							
							//새로운 그룹으로 유저 정보 변경
							usersMapper.updateByPrimaryKeySelective(record);
							//추가할 유저의 기존 권한 삭제
							usersMapper.deletePermissionOfUser(userId);
							//권한 추가
							permissionServiceImpl.insertUserPermission(userId, permissions);
						}
						
						txManager.commit(txStatus);
						jsonResult.put("resultCode", "S");
					} else {
						jsonResult.put("resultCode", "E");
					}
				} else if(proccessDiv.equals("edit")) {
					param.put("operatorId", groupId);
					List<Users> initMember = userServiceImpl.selectUserListByCondition(param);
					
					if(initMember.size() > 0) {
						//기존에 추가되어있던 사용자의 권한 모두 삭제
						for(int i=0; i < initMember.size(); i++) {
							Users record = new Users();
							record.setUserId(initMember.get(i).getUserId());
							record.setGrade(null);
							record.setOperatorId(null);
							usersMapper.updateByPrimaryKeySelective(record);
							usersMapper.deletePermissionOfUser(initMember.get(i).getUserId());
						}
					}
					
					Operator operator = new Operator();
					operator.setId(groupId);
					operator.setCircleName(param.get("circleName").toString());
					operator.setName(groupName);
					operator.setDescription(gruopDescription);
					operator.setPermission(menuListStr);
					operatorServiceImpl.insertOperator(operator);
					
					StringTokenizer stk = new StringTokenizer(menuListStr, ",");
					List<String> permissions = new ArrayList<String>();
					while(stk.hasMoreTokens()) {
						permissions.add(stk.nextToken());
					}
					
					StringTokenizer stk1 = new StringTokenizer(memberListStr, ",");
					
					while(stk1.hasMoreTokens()) {
						String userId = stk1.nextToken();
						Users record = new Users();
						record.setUserId(userId);
						record.setGrade(9999);
						record.setOperatorId(operator.getId());
						
						//새로운 그룹으로 유저 정보 변경
						usersMapper.updateByPrimaryKeySelective(record);
						//추가할 유저의 기존 권한 삭제
						usersMapper.deletePermissionOfUser(userId);
						//권한 추가
						permissionServiceImpl.insertUserPermission(userId, permissions);
					}
					
					txManager.commit(txStatus);
					jsonResult.put("resultCode", "S");
				}
			}
		} catch(Exception e) {
			txManager.rollback(txStatus);
			jsonResult.put("resultCode", "F");
			e.printStackTrace();
		}
		
//		jsonResult.put("result", operatorServiceImpl.checkGradeName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	} 
		
	/**
	 * National 그룹 추가,삭제 메소드
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/proccessNationalGroup.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String proccessNationalGroup(@RequestParam HashMap<String, Object> param) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		JSONObject jsonResult = new JSONObject();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
		TransactionStatus txStatus= txManager.getTransaction(def);
		
		logger.info("-> [param = {}]", param);
		
		try {
			String proccessDiv = param.get("proccessDiv").toString();
			int groupId = Integer.parseInt(param.get("groupId").toString().equals("")? "0" : param.get("groupId").toString());
			String memberListStr = param.get("memberListStr").toString();
			
			if(proccessDiv.equals("add")) {
				StringTokenizer stk = new StringTokenizer(memberListStr, ",");
				
				while(stk.hasMoreTokens()) {
					String userId = stk.nextToken();
					
					List<String> realPermissions = new ArrayList<String>();
					List<Permission> permissions = usersMapper.selectPermissionsByUserId(userId);
					for(Permission per : permissions) {
						realPermissions.add(per.getId().toString());
					}
					
					Users record = new Users();
					record.setUserId(userId);
					record.setGrade(groupId);
					record.setOperatorId(null);
					
					//새로운 그룹으로 유저 정보 변경
					usersMapper.updateByPrimaryKeySelective(record);
					//추가할 유저의 기존 권한 삭제
					usersMapper.deletePermissionOfUser(userId);
					//권한 추가
					permissionServiceImpl.insertUserPermission(userId, realPermissions);
				}
			} else if(proccessDiv.equals("delete")) {
				StringTokenizer stk = new StringTokenizer(memberListStr, ",");
				
				while(stk.hasMoreTokens()) {
					String userId = stk.nextToken();
					
					Users record = new Users();
					record.setUserId(userId);
					record.setGrade(null);
					record.setOperatorId(null);
					
					//그룹 정보 초기화
					usersMapper.updateByPrimaryKeySelective(record);
					//삭제할 유저의 기존 권한 삭제(접근 권한 아예 삭제)
					usersMapper.deletePermissionOfUser(userId);
				}
			}
			
			txManager.commit(txStatus);
			jsonResult.put("resultCode", "S");
		} catch(Exception e) {
			txManager.rollback(txStatus);
			jsonResult.put("resultCode", "F");
			e.printStackTrace();
		}
		
//			jsonResult.put("result", operatorServiceImpl.checkGradeName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * addGruuopModal Search Method
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/getOtherUserList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getOtherUserList(@RequestParam HashMap<String, Object> param) {
		
		List<Users> memberList = new ArrayList<Users>();
		JSONObject jsonResult = new JSONObject();
		
		param.put("targetDiv", "grade");
		param.put("grade", "");
		param.put("notGrade", param.get("groupId"));
		memberList = operatorServiceImpl.selectMemberList(param);
	
		JSONArray rows = new JSONArray();
		for (Users user : memberList)
			rows.add(user.toJSONObject());
	
		jsonResult.put("rows", rows);
		
		return jsonResult.toString();
	}
}
