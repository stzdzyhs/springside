<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>安全高级演示</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
	<link href="${ctx}/css/style.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<%@ include file="/common/header.jsp" %>
<div id="content">
	<%@ include file="/common/left.jsp" %>
	<div id="main">
		<h1>安全高级演示</h1>
		<ul>
			<li><a href="${ctx}/security/login.action">SpringSecurity与认证码集成演示.</a></li>
			<li>SpringSecurity Sha1散列密码与同时登录人数限制演示.</li>
			<li>SHA-1消息摘要, HMAC-SHA1消息签名 及 DES对称加密演示.(见CryptoUtils及其测试用例)</li>
			<li>Random 与 UUID风格 Nonce演示.(见NonceUtils及其测试用例)</li>
			<li>数据级别的权限控制(计划中)</li>
		</ul>
	</div>
</div>
</body>
</html>