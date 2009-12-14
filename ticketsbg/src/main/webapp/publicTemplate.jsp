<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich" xmlns:tc="http://tickets.com/tc">
<head>
<meta http-equiv="Content-Type" content="text/xhtml; charset=UTF-8" />
<title>#{msg.siteTitle}</title>
<meta name="description" content="#{msg.siteDescription}" />
<meta name="keywords" content="#{msg.siteKeywords}" />
<link href="css/main.css" type="text/css" rel="stylesheet" />
<link rel="shortcut icon" href="favicon.png" />
<link rel="icon" href="favicon.png" />

<ui:insert name="head" />

<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-9048548-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(ga);
  })();

</script>
</head>
<body>
<f:view locale="#{localeController.locale}">
<f:loadBundle var="msg" basename="com.tickets.constants.messages" />
<div class="container"><a
    href="#{facesContext.externalContext.context.contextPath}/"> <img
    src="images/logo.png" alt="avtogara.com"
    style="border-style: none; width: 385px; height: 93px;" />
</a>
    <div style="float: right;"><h:form>
        <h:commandLink action="#{localew.ler.changeLocale}">
            <h:graphicImage url="images/flags/bg.jpg"
                style=" margin-right: 10px;" styleClass="languageIcon" alt="BG"
                title="BG" />
            <f:setPropertyActionListener value="bg"
                target="#{localeController.selectedLanguage}" />
        </h:commandLink>

        <h:commandLink action="#{localeController.changeLocale}">
            <h:graphicImage url="images/flags/en.jpg" styleClass="languageIcon"
                alt="EN" title="EN" />
            <f:setPropertyActionListener value="en"
                target="#{localeController.selectedLanguage}" />
        </h:commandLink>
    </h:form></div>
    <h:form style="float: left; height: 34px; margin-bottom: 5px;"
    id="menuForm" ajaxSingle="true">
    <rich:toolBar itemSeparator="line" width="1000" height="34"
        style="float: left; padding: 0px; margin: 0px;" id="menuToolbar">
        <rich:menuItem action="#{searchController.navigateToSearch}"
            id="searchMenuItem">
            <h:graphicImage value="/images/small/search.png"
                styleClass="menuIcon" alt="#{msg.searchMenuItem}" />
            <h:outputText value="#{msg.searchMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="alterTicketScreen" id="alterTicketMenuItem">
            <h:graphicImage value="/images/small/alterTicket.png"
                styleClass="menuIcon" alt="#{msg.alterTicketMenuItem}" />
            <h:outputText value="#{msg.alterTicketMenuItem}"
                styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="firmDetails" id="firmMenuItem"
            rendered="#{baseController.currentFirm != null}">
            <h:graphicImage value="/images/small/firm.png" styleClass="menuIcon"
                alt="#{msg.firm}" />
            <h:outputText value="#{msg.firm}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="registrationScreen"
            rendered="#{loggedUserHolder.loggedUser == null}">
            <h:graphicImage value="/images/small/users.png" styleClass="menuIcon"
                alt="#{msg.register}" />
            <h:outputText value="#{msg.register}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem submitMode="ajax"
            rendered="#{loggedUserHolder.loggedUser == null}"
            onclick="#{rich:component('loginPanel')}.show()" ajaxSingle="true"
            immediate="true">
            <h:graphicImage value="/images/small/login.png" styleClass="menuIcon"
                alt="#{msg.login}" />
            <h:outputText value="#{msg.login}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="history"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/history.png"
                styleClass="menuIcon" alt="#{msg.travelHistory}" />
            <h:outputText value="#{msg.travelHistory}" />
        </rich:menuItem>

        <rich:menuItem action="profile"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/profile.png"
                styleClass="menuIcon" alt="#{msg.profile}" />
            <h:outputText value="#{msg.profile}" />
            <!-- h:outputText value=" (#{loggedUserHolder.loggedUser.username})" /-->
        </rich:menuItem>

        <rich:menuItem action="#{loggedUserHolder.logout}"
            rendered="#{loggedUserHolder.loggedUser != null}">
            <h:graphicImage value="/images/small/logout.png"
                styleClass="menuIcon" alt="#{msg.logout}" />
            <h:outputText value="#{msg.logout}" styleClass="menuContent" />
        </rich:menuItem>
    </rich:toolBar>
</h:form> <a4j:status forceId="generalStatus">
    <f:facet name="start">
        <h:graphicImage value="/images/ajaxloadingBig.gif" id="statusImage"
            style="float: right; margin-left: -40px; padding-right: 3px; padding-top: 3px; width: 32px; height: 32px;"
            alt="loading..." />
    </f:facet>
</a4j:status> <ui:include src="loginPanel.jsp" /> <h:panelGrid columns="2"
    columnClasses="main,side" cellpadding="0" cellspacing="0"
    style="margin-top: 4px; margin-bottom: 0px; text-align: left; clear: both;">
    <ui:insert name="body" />
    <h:panelGroup>
        <ui:include src="purchaseDetailsSideScreen.jsp" />
        <ui:include src="statistics.jsp" />
        <ui:include src="paymentMethods.jsp" />
    </h:panelGroup>
</h:panelGrid>

<ui:insert name="bottomPanel" />

<h:form>
    <h:panelGroup styleClass="footer" layout="block">
        <a4j:commandLink onclick="#{rich:component('termsPanel')}.show();"
            value="#{msg.generalTerms}" /> |&#160;
    <a4j:commandLink
            onclick="#{rich:component('contactsPanel')}.show();"
            value="#{msg.footerContacts}" /> |
    <a href="http://www.facebook.com/pages/avtogaracom/216346729251">#{msg.footerFacebook}</a> |
    <a href="http://twitter.com/avtogara_com">Twitter</a>
        <ui:insert name="footer" />
    </h:panelGroup>
</h:form>

<rich:modalPanel id="termsPanel" width="400" height="300"
    onmaskclick="#{rich:component('termsPanel')}.hide();">
    <ui:include src="modalPanelCommons.jsp">
        <ui:param name="dialogId" value="termsPanel" />
    </ui:include>
    <f:facet name="header">
                #{msg.generalTerms}
            </f:facet>
    <h:panelGroup style="overflow: auto;" layout="block">
        <ui:include
            src="infopages/terms_#{facesContext.viewRoot.locale.language}.jsp" />
    </h:panelGroup>
</rich:modalPanel> <rich:modalPanel id="contactsPanel" autosized="true"
    onmaskclick="#{rich:component('contactsPanel')}.hide();">
    <ui:include src="modalPanelCommons.jsp">
        <ui:param name="dialogId" value="contactsPanel" />
    </ui:include>
    <f:facet name="header">
        #{msg.footerContacts}
    </f:facet>
    <h:panelGroup style="overflow: auto;" layout="block">
        <ui:include
            src="infopages/contacts_#{facesContext.viewRoot.locale.language}.jsp" />
    </h:panelGroup>
</rich:modalPanel></div>

<script src="http://static.getclicky.com/js" type="text/javascript"></script>
<script type="text/javascript">clicky.init(163258);</script>

<script type="text/javascript"
    src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php/#{localeController.locale}"></script>
<script type="text/javascript">
FB.init("329367adf1678d9b262d077844549559");
</script>
</f:view>
</body>
</html>