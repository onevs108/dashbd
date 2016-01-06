** 프로젝트 설정

1. 환경설정
 1.1. tomcat conf에 context.xml에 <Context>안에 추가 
	: <Resources className="org.apache.naming.resources.VirtualDirContext" extraResourcePaths="/upload=\upload"/>
		
		참고> http://start.goodtime.co.kr/2014/04/%ED%86%B0%EC%BA%A3%EC%9D%98-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8-%EC%99%B8%EB%B6%80%EB%A1%9C-%EA%B2%BD%EB%A1%9C-%EB%A7%A4%ED%95%91%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95/

  
 1.2. tomcat conf에 server.xml에 URIEncoding 추가
	:<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="UTF-8"/>


2. 소스설정
 2.1. interface URL 설정
    : src/main/resources/config.properties에 b2.interface.url의 값 변경
    
 2.2. xml 만드는페이지
    : src/catenoid/dashbd/service/XmlManager.java 안에서  xml 메이커하고 파싱 합니다.
   