<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:p="http://primefaces.prime.com.tr/ui">
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
<body style="margin-left: 0px; margin-top: 0px; margin-right: 0px; text-align: left;">
<f:loadBundle var="msg" basename="com.tickets.constants.messages" />

<a4j:form id="toolbarForm">
    <a4j:poll action="#{keepAliveController.poll}" interval="500000"
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
                    <h:graphicImage value="/images/settings.png" styleClass="menuIcon" />
                    <h:outputText value="#{msg.settings}" styleClass="menuContent" />
                </h:panelGroup>
            </f:facet>

            <rich:menuItem value="#{msg.users}" action="usersList"
                icon="/images/users.png" />

            <rich:menuItem value="#{msg.firmSettings}" ajaxSingle="true"
                oncomplete="#{rich:component('firmPanel')}.show()"
                icon="/images/firm.png" submitMode="ajax" reRender="firmPanel">
                <f:setPropertyActionListener
                    value="#{loggedUserHolder.loggedUser.firm}"
                    target="#{firmController.firm}" />
            </rich:menuItem>

            <rich:menuItem value="#{msg.discounts}" action="discountsList"
                icon="/images/discounts.png" />

            <rich:menuItem value="#{msg.promotions}" action="promotionsList"
                icon="/images/promotions.png" />

            <rich:menuItem value="#{msg.agents}" action="agentsList"
                icon="/images/agents.png" />

            <rich:menuItem value="#{msg.firms}" action="firmsList"
                icon="/images/agents.png"
                rendered="#{loggedUserHolder.loggedUser.accessLevel == 'ADMINISTRATOR'}" />
        </rich:dropDownMenu>


        <rich:dropDownMenu style="padding-right: 27px; padding-left: 27px;"
            hideDelay="100">
            <f:facet name="label">
                <h:panelGroup>
                    <h:graphicImage value="/images/statistics.png"
                        styleClass="menuIcon" />
                    <h:outputText value="#{msg.reports}" styleClass="menuContent" />
                </h:panelGroup>
            </f:facet>

            <rich:menuItem value="#{msg.charts}" action="charts" />

            <rich:menuItem value="#{msg.tableReports}" action="tableReports" />

        </rich:dropDownMenu>

        <rich:menuItem action="#{loggedUserHolder.logout}"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/logout.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.logout}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:dropDownMenu
            rendered="#{loggedUserHolder.loggedUser.accessLevel == 'ADMINISTRATOR'}">
            <f:facet name="label">
                <a4j:region selfRendered="true" renderRegionOnly="true">
                    <h:selectOneMenu id="currentFirm"
                        value="#{loggedUserHolder.loggedUser.firm}"
                        converter="#{entityConverter}">
                        <f:selectItems value="#{firmController.firmsSelectItems}" />
                        <a4j:support event="onchange" action="#{routeController.init}" />
                    </h:selectOneMenu>
                </a4j:region>
            </f:facet>
        </rich:dropDownMenu>
    </rich:toolBar>
</a4j:form>

<rich:modalPanel id="firmPanel" autosized="true" width="350"
    height="120" moveable="true" resizeable="false"
    domElementAttachment="parent" style="overflow: hidden;">
    <ui:include src="/modalPanelCommons.jsp">
        <ui:param name="dialogId" value="firmPanel" />
    </ui:include>
    <f:facet name="header">
        <h:outputText value="#{msg.firm}" />
    </f:facet>
    <a4j:include viewId="firm.jsp" />
</rich:modalPanel>
<h:panelGroup style="text-align: left;" layout="block">
<ui:insert name="body" />
</h:panelGroup>
</body>
</html>