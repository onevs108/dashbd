<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="com.catenoid.dashbd.util.SessionCounterListener"%>

<div class="row border-bottom">
	<input type="hidden" id="globalGrade" value="${USER.grade}">
	<nav class="navbar navbar-static-top" role="navigation"
		style="margin-bottom: 0;">
		<div class="navbar-header">
			<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"> <i class="fa fa-bars"></i></a> <span class="navbar-form-custom">Dashboard (eMBMS Schedule Manager)</span>
		</div>

		<ul class="nav navbar-top-links navbar-right">
			<li>
				<div class="profile-element">
					<span class="clear">
						<span class="block m-t-xs">
							<strong class="font-bold">David Williams (Administrator)</strong>
						</span>
					</span>
				</div>
			</li>
			<li><a href="/dashbd/out"><i class="fa fa-sign-out"></i>
					Log out</a></li>
		</ul>
	</nav>
</div>

<div class="row border-bottom white-bg dashboard-header">
	<div class="session">
		<h2>Dashboard Main</h2>
		<div class="pull-right">
			<span>Num Sessions:<%=SessionCounterListener.getActiveSessions() %></span>
			<span>
				<span class="onDisp on1"></span><!-- 활성화일때 클래스 on1, on2 -->
				<span class="onDisp"></span>
			</span>
		</div>
	</div>
</div>