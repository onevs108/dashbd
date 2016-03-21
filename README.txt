** 프로젝트 설정

1. 환경설정
 1.1. tomcat conf에 context.xml에 <Context>안에 추가 
	: <Resources className="org.apache.naming.resources.VirtualDirContext" extraResourcePaths="/upload=\upload"/>
		
		참고> http://start.goodtime.co.kr/2014/04/%ED%86%B0%EC%BA%A3%EC%9D%98-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8-%EC%99%B8%EB%B6%80%EB%A1%9C-%EA%B2%BD%EB%A1%9C-%EB%A7%A4%ED%95%91%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95/

  
 1.2. tomcat conf에 server.xml에 URIEncoding 추가
	:<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="UTF-8"/>

 1.3. 개발디비 서버
   * 182.252.178.75
   * 콘솔 접속 - ssh2 port : 2222
   * id/pwd : nexdream/nexdream!@#
   * root pwd : nexdream!@#
   * tomcat BASE PATH : /usr/local/server/dashdb_tomcat-7.0.65/
 
 1.4.  lib
   * ssh lib - jsch-0.1.53.jar
     ref url> http://www.journaldev.com/246/java-program-to-run-shell-commands-on-ssh-enabled-system
     		  https://tomeuwork.wordpress.com/2014/09/24/java-program-to-run-ssh-commands/
     		  http://www.jcraft.com/jsch/
     

2. 소스설정
 2.1. interface URL 설정
    : src/main/resources/config.properties에 b2.interface.url의 값 변경
    
 2.2. xml 만드는페이지
    : src/catenoid/dashbd/service/XmlManager.java 안에서  xml 메이커하고 파싱 합니다.

3.  작업내용
  * [16.03.20] 
    1) bmsc  연동시 HOST 정보를 DB에 저장된 데이타고 함. 							=>  OK
    2) active content max  는 설정파일로 뺀다.							 	=>  OK
    3) 메인화면
        - [UI]active contents										=>  OK
        - [UI]waiting contents 										=>  OK
        - [UI]embms session Monitoring  (추가, 삭제 기능)					=>  OK
        - [기능] 쉘 명령어 구현												=>  OK
        - [기능] 영상 play  기능  											=> NOT yet