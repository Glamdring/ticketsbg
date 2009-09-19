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
    <p:resources />
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
            <h:form id="chartsForm">
                <h:messages />
                <ui:include src="reportFields.jsp">
                    <ui:param name="target" value="chartHolder" />
                    <ui:param name="chart" value="true" />
                </ui:include>

                <h:panelGroup id="chartHolder">
                    <p:lineChart value="#{statisticsController.statistics}" var="sale"
                        xfield="#{sale.period}" id="chart" styleClass="line"
                        wmode="opaque">
                        <p:chartSeries label="#{msg[statisticsController.selectedDataType.key]}" value="#{sale.value}">
                            <f:convertNumber minFractionDigits="0" maxFractionDigits="2" />
                        </p:chartSeries>
                    </p:lineChart>
                </h:panelGroup>
            </h:form>
        </f:view>
    </ui:define>
</ui:composition>