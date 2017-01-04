package com.catenoid.dashbd.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;

public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	// TODO @Autowired 적용이 안된다... 추후 원인파악할 것.
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		logger.info("-> [userId = {}]", userId);
		
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		Users user = mapper.selectByUserId(userId);
		
		Collection<SimpleGrantedAuthority> authorities = null;
		UserDetails userDetail = null;
		
		if (user == null) {
			// 따로 필요한 동작이 없다.
		}
		else {
			List<Permission> permissionsOfUser = mapper.selectPermissionsByUserId(userId);
			
			authorities = new ArrayList<SimpleGrantedAuthority>();
			if (user.getGrade() == Const.USER_GRADE_ADMIN)
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			else{
				for (Permission permission : permissionsOfUser)
					authorities.add(new SimpleGrantedAuthority(permission.getRole()));
			}
			
			// spring security의 객체이다.
			userDetail = new User(userId, user.getPassword(), true, true, true, true, authorities);
		}
		
		logger.info("<- [userDetail = {}]", userDetail.toString());
		return userDetail;
	}
	
}
