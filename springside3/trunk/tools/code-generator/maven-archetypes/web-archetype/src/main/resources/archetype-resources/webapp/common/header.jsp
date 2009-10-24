#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<div id="header">
	<div id="title">
		<h1>Mini-Web示例</h1>

		<h2>--CRUD管理界面演示</h2>
	</div>
	<div id="menu">
		<ul>
			<li><a href="${symbol_dollar}{ctx}/security/user.action">帐号列表</a></li>
			<li><a href="${symbol_dollar}{ctx}/security/role.action">角色列表</a></li>
			<li><a href="${symbol_dollar}{ctx}/j_spring_security_logout">退出登录</a></li>
		</ul>
	</div>
</div>