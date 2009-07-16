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
    
    <ui:insert name="head" />
  </head>
  <body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
    <a4j:poll action="#{keepAliveController.poll}" interval="240000"/>
    
    <f:loadBundle var="msg" basename="com.tickets.constants.messages" />
    
    <h:form>
        <rich:toolBar>
            <rich:dropDownMenu>
                <f:facet name="label"> 
                    <h:panelGroup>
                        <h:outputText value="#{msg.mainMenu}"/>
                    </h:panelGroup>
                </f:facet>
                <rich:menuItem value="#{msg.routesHeading}" action="routesList">
                </rich:menuItem>
                <rich:menuItem submitMode="ajax" value="Open"
                    action="#{ddmenu.doOpen}" icon="/images/icons/open.gif" />
                <rich:menuGroup value="Save As...">
                    <rich:menuItem submitMode="ajax" value="Save" 
                        action="#{ddmenu.doSave}" icon="/images/icons/save.gif" />
                    <rich:menuItem submitMode="ajax" value="Save All"
                        action="#{ddmenu.doSaveAll}">
                    </rich:menuItem>
                </rich:menuGroup>
                <rich:menuItem submitMode="ajax" value="Close"
                    action="#{ddmenu.doClose}" />
                <rich:menuSeparator id="menuSeparator11" />
                <rich:menuItem submitMode="ajax" value="Exit"
                    action="#{ddmenu.doExit}" />

            </rich:dropDownMenu>
        </rich:toolBar>
    </h:form>
    <ui:insert name="body" />
  </body>
</html>