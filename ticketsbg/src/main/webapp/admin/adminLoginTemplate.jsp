<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tickets</title>
<link href="../css/main.css" type="text/css" rel="stylesheet" />
<ui:insert name="head" />
</head>
<body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
<a4j:keepAlive beanName="keepAliveController" />

<f:loadBundle var="msg" basename="com.tickets.constants.messages" />

<div style="width: 100%; margin-top: 20px;" align="center">
<ui:insert name="body" />
</div>
</body>
</html>