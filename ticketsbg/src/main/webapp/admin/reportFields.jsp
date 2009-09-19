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
    xmlns:tc="http://tickets.com/tc">

    <h:panelGrid columns="#{chart == true ? 7 : 6}" columnClasses="gridContent">
        <h:outputText value="#{msg.route}" />
        <h:outputText value="#{msg.statsDataType}" rendered="#{chart}" />
        <h:outputText value="#{msg.purchaseMeansType}" />
        <h:outputText value="#{msg.periodType}" />
        <h:outputText value="#{msg.timeType}" />
        <h:outputText value="#{msg.fromDate}" />
        <h:outputText value="#{msg.toDate}" />

        <rich:comboBox value="#{statisticsController.selectedRouteName}"
            suggestionValues="#{statisticsController.routeNames}">
            <a4j:support event="onselect" reRender="#{target}" />
        </rich:comboBox>

        <h:selectOneMenu id="selectedDataType" converter="#{enumConverter}"
            value="#{statisticsController.selectedDataType}"
            rendered="#{chart}">
            <f:selectItems value="#{statisticsController.dataTypeItems}" />
            <a4j:support event="onchange" reRender="#{target}" />
        </h:selectOneMenu>

        <h:selectOneMenu id="selectedPurchaseMeansType" converter="#{enumConverter}"
            value="#{statisticsController.selectedPurchaseMeansType}"
            style="width: 100%;">
            <f:selectItems value="#{statisticsController.purchaseMeansTypeItems}" />
            <a4j:support event="onchange" reRender="#{target}" />
        </h:selectOneMenu>

        <h:selectOneMenu id="selectedPeriod"
            value="#{statisticsController.selectedPeriod}">
            <f:selectItems value="#{statisticsController.periodItems}" />
            <a4j:support event="onchange" reRender="#{target}" />
        </h:selectOneMenu>

        <h:selectOneMenu id="selectedTimeType"
            value="#{statisticsController.selectedTimeType}">
            <f:selectItems value="#{statisticsController.timeTypeItems}" />
            <a4j:support event="onchange" reRender="#{target}" />
        </h:selectOneMenu>

        <rich:calendar id="fromDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
            value="#{statisticsController.fromDate}" label="#{msg.fromDate}"
            reRender="#{target}">
            <a4j:support event="onchanged" reRender="#{target}" />
        </rich:calendar>

        <rich:calendar id="toDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
            value="#{statisticsController.toDate}" label="#{msg.toDate}">
            <a4j:support event="onchanged" reRender="#{target}" />
        </rich:calendar>
    </h:panelGrid>
</ui:composition>