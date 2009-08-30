<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk"
    xmlns:p="http://primefaces.prime.com.tr/ui"
    template="adminTemplate.jsp">
    <ui:define name="head">
        <style type="text/css">
.line {
    width: 800px;
    height: 480px;
    float: left;
}
</style>
    </ui:define>
    <ui:define name="body">
        <f:view>
            <h:form id="statsForm">
                <h:messages />
                <h:panelGrid columns="5" columnClasses="gridContent">
                    <h:outputText value="#{msg.route}" />
                    <h:outputText value="#{msg.periodType}" />
                    <h:outputText value="#{msg.timeType}" />
                    <h:outputText value="#{msg.fromDate}" />
                    <h:outputText value="#{msg.toDate}" />

                    <rich:comboBox value="#{statisticsController.selectedRouteName}"
                        suggestionValues="#{statisticsController.routeNames}">
                        <a4j:support event="onselect" reRender="chartHolder" />
                    </rich:comboBox>

                    <h:selectOneMenu id="selectedPeriod"
                        value="#{statisticsController.selectedPeriod}">
                        <f:selectItems value="#{statisticsController.periodItems}" />
                        <a4j:support event="onchange" reRender="chartHolder" />
                    </h:selectOneMenu>

                    <h:selectOneMenu id="selectedTimeType"
                        value="#{statisticsController.selectedTimeType}">
                        <f:selectItems value="#{statisticsController.timeTypeItems}" />
                        <a4j:support event="onchange" reRender="chartHolder" />
                    </h:selectOneMenu>

                    <rich:calendar id="fromDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        value="#{statisticsController.fromDate}" label="#{msg.fromDate}"
                        reRender="chartHolder">
                        <a4j:support event="onchanged" reRender="chartHolder" />
                    </rich:calendar>

                    <rich:calendar id="toDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        value="#{statisticsController.toDate}" label="#{msg.toDate}">
                        <a4j:support event="onchanged" reRender="chartHolder" />
                    </rich:calendar>
                </h:panelGrid>

                <h:panelGroup id="chartHolder">
                    <p:lineChart value="#{statisticsController.soldTickets}" var="sale"
                        xfield="#{sale.period}" id="chart" styleClass="line"
                        wmode="opaque">
                        <p:chartSeries label="#{msg.soldTickets}" value="#{sale.tickets}" />
                    </p:lineChart>
                </h:panelGroup>
            </h:form>
        </f:view>
    </ui:define>
</ui:composition>