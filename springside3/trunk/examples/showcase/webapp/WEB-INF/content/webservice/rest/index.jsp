<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>RESTful Web Service演示</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<link href="${ctx}/css/main.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div id="content">
<%@ include file="/common/left.jsp"%>
<div id="mainbar">
<h2>RESTful Web Service演示</h2>
<h4>技术说明：</h4>
<ul>
<li>服务端使用Jersey </li>
<li>客户端使用HttpClient 4.0</li>
<li>主要目的是简化Web Service的提供与使用，因此并不严格遵循GET/POST/PUT等REST概念，而注重演示REST中的ETag缓存与安全等特性。</li>
</ul>
</div>
</div>
</body>
</html>