<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:tc="http://tickets.bg/tickets">

    <!-- Expecting param 'controller' to be set -->
    <rich:modalPanel id="travelListOptionsPanel" autosized="true">
        <f:facet name="header">
            <h:outputText value="#{msg.travelList}" />
        </f:facet>
        <ui:include src="/modalPanelCommons.jsp">
            <ui:param name="dialogId" value="travelListOptionsPanel" />
        </ui:include>

        <h:form id="travelListForm">
            <h:panelGrid columns="1" style="width: 400px;">
                <h:selectOneRadio value="#{controller.onlineOnly}"
                    converter="#{booleanConverter}">
                    <f:selectItem itemValue="true"
                        itemLabel="#{msg.travelListOnlineOnly}" />
                    <f:selectItem itemValue="false" itemLabel="#{msg.travelListAll}" />
                </h:selectOneRadio>

                <h:selectOneRadio value="#{controller.showNames}"
                    converter="#{booleanConverter}">
                    <f:selectItem itemValue="true"
                        itemLabel="#{msg.travelListShowNames}" />
                    <f:selectItem itemValue="false" itemLabel="#{msg.travelListNoNames}" />
                </h:selectOneRadio>


                <h:commandLink action="#{controller.openTravelList}" target="_blank"
                    title="#{msg.travelList}" value="#{msg.openTravelList}" />

            </h:panelGrid>
        </h:form>
    </rich:modalPanel>
</ui:composition>