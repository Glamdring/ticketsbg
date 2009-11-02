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
<script type="text/javascript" src="js/jquery.pngFix.js"></script>
<!-- script type="text/javascript">
    $(document).ready(function(){
        $(document).pngFix();
    });
</script-->

<ui:insert name="head" />
</head>
<body>

<f:loadBundle var="msg" basename="com.tickets.constants.messages" />

<div class="container"><a
    href="#{facesContext.externalContext.context.contextPath}/"> <img
    src="images/logo.png" alt="avtogara.com" style="border-style: none;" />
</a> <h:form style="padding: 0px; margin: 0px; float: left" id="menuForm">

    <rich:toolBar itemSeparator="line" height="34" width="1005"
        style="float: left;" id="toolbar">
        <rich:menuItem action="#{searchController.navigateToSearch}"
            id="searchMenuItem">
            <h:graphicImage value="/images/small/search.png"
                styleClass="menuIcon" />
            <h:outputText value="#{msg.searchMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="alterTicketScreen" id="alterTicketMenuItem">
            <h:graphicImage value="/images/small/alterTicket.png"
                styleClass="menuIcon" />
            <h:outputText value="#{msg.alterTicketMenuItem}"
                styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="firmDetails" id="firmMenuItem"
            rendered="#{baseController.currentFirm != null}">
            <h:graphicImage value="/images/small/firm.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.firm}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="registrationScreen"
            rendered="#{loggedUserHolder.loggedUser == null}">
            <h:graphicImage value="/images/small/users.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.register}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem submitMode="ajax"
            rendered="#{loggedUserHolder.loggedUser == null}"
            onclick="#{rich:component('loginPanel')}.show()" ajaxSingle="true"
            immediate="true">
            <h:graphicImage value="/images/small/login.png" styleClass="menuIcon" />
            <h:outputText value="#{msg.login}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="history"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/history.png"
                styleClass="menuIcon" />
            <h:outputText value="#{msg.travelHistory}" />
        </rich:menuItem>

        <rich:menuItem action="profile"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/profile.png"
                styleClass="menuIcon" />
            <h:outputText value="#{msg.profile}" />
            <!-- h:outputText value=" (#{loggedUserHolder.loggedUser.username})" /-->
        </rich:menuItem>

        <rich:menuItem action="#{loggedUserHolder.logout}"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/logout.png"
                styleClass="menuIcon" />
            <h:outputText value="#{msg.logout}" styleClass="menuContent" />
        </rich:menuItem>
    </rich:toolBar>
</h:form>

<a4j:status forceId="generalStatus">
    <f:facet name="start">
        <h:graphicImage value="/images/ajaxloadingBig.gif" id="statusImage"
            style="float: right; margin-left: -40px; padding-right: 3px; padding-top: 3px; clear: right;" />
    </f:facet>
</a4j:status>

<a4j:include viewId="loginPanel.jsp" /> <h:panelGrid columns="2"
    columnClasses="main,side" cellpadding="0" cellspacing="0"
    style="margin-top: 4px; text-align: left;">
    <ui:insert name="body" />
    <h:panelGroup>
        <ui:include src="purchaseDetailsSideScreen.jsp" />
        <ui:include src="statistics.jsp" />
        <ui:include src="paymentMethods.jsp" />
    </h:panelGroup>
</h:panelGrid> <h:form style="margin: 0px; padding: 0px;">
    <h:panelGroup styleClass="footer" layout="block">
        <a4j:commandLink onclick="#{rich:component('termsPanel')}.show();"
            value="#{msg.generalTerms}" />  |
    <a href="#">#{msg.footerAbout}</a> |
    <a href="#">#{msg.footerHelp}</a> |
    <a href="#">#{msg.footerContacts}</a> |
    <a href="#">#{msg.footerFacebook}</a> |
    <a href="#">#{msg.footerForFirms}</a>
        <ui:insert name="footer" />
    </h:panelGroup>
</h:form> <rich:modalPanel id="termsPanel" autosized="true"
    style="overflow: hidden;" width="400" height="300"
    onmaskclick="#{rich:component('termsPanel')}.hide();">
    <ui:include src="modalPanelCommons.jsp">
        <ui:param name="dialogId" value="termsPanel" />
    </ui:include>
    <f:facet name="header">
        #{msg.generalTerms}
    </f:facet>
    <h:panelGroup style="overflow: auto;">
        <ui:include src="terms_#{facesContext.viewRoot.locale.language}.jsp" />
    </h:panelGroup>
</rich:modalPanel></div>
</body>
</html>