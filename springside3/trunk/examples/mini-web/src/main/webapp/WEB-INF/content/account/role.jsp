<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.springside.modules.security.springsecurity.SpringSecurityUtils" %>
<%@ include file="/common/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Mini-Web 角色管理</title>
	<%@ include file="/common/meta.jsp" %>
	
	<link href="${ctx}/css/style.css" type="text/css" rel="stylesheet"/>
	
	<link href="${ctx}/css/blueprint/screen.css" type="text/css" rel="stylesheet" media="screen, projection"/>
	<link href="${ctx}/css/blueprint/print.css" type="text/css" rel="stylesheet" media="print"/>
	<!--[if lt IE 8]><link href="${ctx}/css/blueprint/blueprint/ie.css" type="text/css" rel="stylesheet" media="screen, projection"><![endif]-->
</head>

<body>
<div class="container">
<%@ include file="/common/header.jsp" %>
<div id="content">
	<div class="span-24 last">
	<div id="message"><s:actionmessage theme="custom" cssClass="success"/></div>

	<div id="filter">你好, <%=SpringSecurityUtils.getCurrentUserName()%>.</div>

	<div id="content">
		<table id="contentTable">
			<tr>
				<th>名称</th>
				<th>授权</th>
				<th>操作</th>
			</tr>

			<s:iterator value="allRoleList">
				<tr>
					<td>${name}</td>
					<td>${authNames}</td>
					<td>&nbsp;
						<security:authorize ifAnyGranted="ROLE_浏览角色">
							<security:authorize ifNotGranted="ROLE_修改角色">
								<a href="role!input.action?id=${id}">查看</a>&nbsp;
							</security:authorize>
						</security:authorize>

						<security:authorize ifAnyGranted="ROLE_修改角色">
							<a href="role!input.action?id=${id}" id="editLink-${name}">修改</a>&nbsp;
							<a href="role!delete.action?id=${id}" id="deleteLink-${name}">删除</a>
						</security:authorize>
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>

	<div>
		<security:authorize ifAnyGranted="ROLE_修改角色">
			<a href="role!input.action">增加新角色</a>
		</security:authorize>
	</div>
	</div>
</div>
<%@ include file="/common/footer.jsp" %>
</div>
</body>
</html>
