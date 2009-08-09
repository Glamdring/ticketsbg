<?xml version="1.0" encoding="UTF-8" ?>
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

<a4j:form id="toolbarForm">
    <a4j:poll action="#{keepAliveController.poll}" interval="100000"
        immediate="true" ajaxSingle="true" />

    <rich:toolBar itemSeparator="line" height="34">
        <rich:menuItem action="adminSearchScreen" id="searchMenuItem">
            <h:graphicImage value="/images/search.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.searchMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="routesList">
            <h:graphicImage value="/images/routes.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.routes}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:dropDownMenu style="padding-right: 27px; padding-left: 27px;"
            hideDelay="100">
            <f:facet name="label">
                <h:panelGroup>
                    <h:graphicImage value="/images/settings.png"
                        styleClass="menuIcon" />
                    <h:outputText value="#{msg.settings}" styleClass="menuContent" />
                </h:panelGroup>
            </f:facet>

            <rich:menuItem value="#{msg.users}" action="usersList"
                icon="/images/users.png" iconStyle="width: 32px; height: 32px;" />

            <rich:menuItem value="#{msg.firmSettings}" ajaxSingle="true"
                oncomplete="#{rich:component('firmPanel')}.show()"
                icon="/images/firm.png" submitMode="ajax" reRender="firmPanel">
                <f:setPropertyActionListener
                    value="#{loggedUserHolder.loggedUser.firm}"
                    target="#{firmController.firm}" />
            </rich:menuItem>

            <rich:menuItem value="#{msg.agents}" action="agentsList"
                icon="/images/agents.png" />
        </rich:dropDownMenu>
    </rich:toolBar>

    <rich:modalPanel id="firmPanel" autosized="true" width="350"
        height="120" moveable="true" resizeable="false">
        <f:facet name="controls">
            <h:panelGroup>
                <h:graphicImage value="/images/close.png" styleClass="hidelink"
                    id="hidelink" onclick="#{rich:component('firmPanel')}.hide()" />
                <rich:componentControl for="firmPanel" attachTo="hidelink"
                    operation="hide" event="onclick" />
            </h:panelGroup>
        </f:facet>
        <f:facet name="header">
            <h:outputText value="#{msg.firm}" />
        </f:facet>
        <a4j:include viewId="firm.jsp" />
    </rich:modalPanel>

</a4j:form>
<ui:insert name="body" />
</body>
</html>