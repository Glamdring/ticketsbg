<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Tickets</title>
    <link href="css/main.css" type="text/css" rel="stylesheet" />
    
    <ui:insert name="header" />
  </head>
  <body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
    <f:loadBundle var="msg" basename="com.tickets.constants.messages" />
    <ui:insert name="body" />
    <h:form id="commonForm">
		<h:commandLink value="#{msg.logout}"
			action="#{loggedUserHolder.logout}"
			rendered="#{loggedUserHolder.loggedUser != null}" />
    </h:form>
  </body>
</html>