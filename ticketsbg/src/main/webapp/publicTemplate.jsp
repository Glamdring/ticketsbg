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
<style type="text/css">
.menuContent {
    vertical-align: middle;
}

.menuIcon {
    margin-right: 5px;
    vertical-align: middle;
}
</style>
<ui:insert name="head" />
</head>
<body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
<f:loadBundle var="msg" basename="com.tickets.constants.messages" />


<img src="images/logo.jpg" alt="bus.bg" width="1005" height="100" />
<a4j:form style="padding: 0px; margin: 0px;">
    <a4j:poll action="#{keepAliveController.poll}" interval="500000"
        immediate="true" ajaxSingle="true" />

    <rich:toolBar itemSeparator="line" height="34" width="1005">
        <rich:menuItem action="#{searchController.navigateToSearch}" id="searchMenuItem">
            <h:graphicImage value="/images/search.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.searchMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="alterTicketScreen" id="alterTicketMenuItem">
            <h:graphicImage value="/images/alterTicket.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.alterTicketMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="firmDetails" id="firmMenuItem" rendered="#{baseController.currentFirm != null}">
            <h:graphicImage value="/images/firm.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.firm}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="registrationScreen" rendered="#{loggedUserHolder.loggedUser == null}">
            <h:graphicImage value="/images/users.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.register}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem submitMode="ajax" rendered="#{loggedUserHolder.loggedUser == null}"
            onclick="#{rich:component('loginPanel')}.show()" ajaxSingle="true" immediate="true">
            <h:graphicImage value="/images/login.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.login}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem style="text-align: right;" action="history"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/routes.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.travelHistory}"/>
        </rich:menuItem>

        <rich:menuItem style="text-align: right;" action="profile"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/profile.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.profile}" />
            <h:outputText value=" (#{loggedUserHolder.loggedUser.username})" />
        </rich:menuItem>

        <rich:menuItem action="#{loggedUserHolder.logout}" rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/logout.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.logout}" styleClass="menuContent" />
        </rich:menuItem>

    </rich:toolBar>
</a4j:form>

<a4j:include viewId="loginPanel.jsp" />

<h:panelGrid columns="2" columnClasses="main,side">
    <ui:insert name="body" />
    <h:panelGroup>
        <ui:include src="purchaseDetailsSideScreen.jsp" />
        <ui:include src="statistics.jsp" />
    </h:panelGroup>
</h:panelGrid>
<ui:insert name="footer" />
</body>
</html>