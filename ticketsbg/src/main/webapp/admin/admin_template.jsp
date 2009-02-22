<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Tickets</title>
  </head>
  <body style="margin-left: 0px; margin-top: 0px; margin-right: 0px">
    <f:loadBundle var="msg" basename="com.tickets.constants.messages" />
    
    <h:form>
        <rich:toolBar>
            <rich:dropDownMenu>
                <f:facet name="label"> 
                    <h:panelGroup>
                        <h:outputText value="File"/>
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

            <rich:dropDownMenu>

                <f:facet name="label">
                    <h:panelGrid cellpadding="0" cellspacing="0" columns="2"
                        style="vertical-align:middle">
                        <h:outputText value="Links" />
                    </h:panelGrid>
                </f:facet>

                <rich:menuItem submitMode="none"
                    onclick="document.location.href='http://labs.jboss.com/jbossrichfaces/'">
                    <h:outputLink value="http://labs.jboss.com/jbossrichfaces/">
                        <h:outputText value="RichFaces Home Page"></h:outputText>
                    </h:outputLink>
                </rich:menuItem>

                <rich:menuItem submitMode="none"
                    onclick="document.location.href='http://jboss.com/index.html?module=bb&amp;op=viewforum&amp;f=261'">
                    <h:outputLink
                        value="http://jboss.com/index.html?module=bb&amp;op=viewforum&amp;f=261">
                        <h:outputText value="RichFaces Forum"></h:outputText>
                    </h:outputLink>
                </rich:menuItem>

            </rich:dropDownMenu>
        </rich:toolBar>
    </h:form>
    <rich:spacer width="1" height="5"/>
    <br />
    <a4j:outputPanel ajaxRendered="true">
        <h:outputText value="Current Selection: "></h:outputText>
        <h:outputText style="font-weight:bold" value="#{ddmenu.current}"></h:outputText>
    </a4j:outputPanel>
    <br />
    <rich:spacer width="1" height="25" border="0" alt="" />
    
    <ui:insert name="body" />
  </body>
</html>