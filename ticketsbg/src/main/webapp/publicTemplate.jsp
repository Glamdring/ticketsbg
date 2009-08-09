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

    <ui:insert name="head" />
  </head>
  <body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
    <a4j:poll action="#{keepAliveController.poll}" interval="1000" immediate="true" ajaxSingle="true" />
    <f:loadBundle var="msg" basename="com.tickets.constants.messages" />


    <img src="images/logo.jpg" alt="bus.bg" width="1005" height="100" />
    <h:panelGrid columns="5">
        <!-- menu items -->
    </h:panelGrid>
    <h:panelGrid columns="2" columnClasses="side,main">
        <h:panelGroup>
            <ui:include src="purchaseDetailsSideScreen.jsp" />
        </h:panelGroup>
        <ui:insert name="body" />
    </h:panelGrid>
    <h:form id="commonForm">
        <h:commandLink value="#{msg.logout}"
            action="#{loggedUserHolder.logout}"
            rendered="#{loggedUserHolder.loggedUser != null}" />
    </h:form>
  </body>
</html>