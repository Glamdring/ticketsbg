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
    xmlns:tc="http://tickets.com/tc" template="adminTemplate.jsp">

    <ui:define name="body">
        <f:view>
            <h:form id="tableReportsForm">
                <h:messages />
                <ui:include src="reportFields.jsp">
                    <ui:param name="target" value="tableHolder" />
                    <ui:param name="chart" value="false" />
                </ui:include>

                <h:panelGroup id="tableHolder">
                    <c:set var="tickets" value="#{statisticsController.tickets}" />

                    <h:outputText value="#{msg.ticketsTotal}: " />
                    <h:outputText styleClass="bold" value="#{tc:getSize(tickets)}" />

                    <h:outputText value=", #{msg.passengersCount}: " />
                    <h:outputText styleClass="bold" value="#{statisticsController.totalPassengersCount}" />

                    <rich:dataTable value="#{tickets}" var="ticket" rows="50">

                        <rich:column sortBy="#{ticket.run.route.name}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.routeName}" />
                            </f:facet>
                            <h:outputText value="#{ticket.run.route.name}" />
                        </rich:column>

                        <rich:column sortBy="#{ticket.creationTime.time}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.purchaseTime}" />
                            </f:facet>
                            <h:outputText value="#{ticket.creationTime.time}">
                                <f:convertDateTime type="time" pattern="dd.MM.yyyy HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                        </rich:column>

                        <rich:column sortBy="#{ticket.run.time.time}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.travelTime}" />
                            </f:facet>
                            <h:outputText value="#{ticket.run.time.time}">
                                <f:convertDateTime type="time" pattern="dd.MM.yyyy HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                        </rich:column>

                        <rich:column sortBy="#{ticket.totalPrice}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.price}" />
                            </f:facet>
                            <h:outputText value="#{ticket.totalPrice}"
                                rendered="#{!ticket.twoWay}">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>

                            <h:outputText value="#{ticket.totalPrice / 2}"
                                rendered="#{ticket.twoWay}">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{statisticsController.totalPrice}">
                                    <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                    <f:converter binding="#{currencyConverter}"
                                        converterId="currencyConverter" />
                                </h:outputText>
                            </f:facet>
                        </rich:column>

                        <rich:column sortBy="#{ticket.twoWay}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.twoWay}" />
                            </f:facet>
                            <h:outputText value="#{ticket.twoWay ? msg.yes : msg.no}" />
                        </rich:column>

                        <rich:column sortBy="#{ticket.passengersCount}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.passengersCount}" />
                            </f:facet>
                            <h:outputText value="#{ticket.passengersCount}" />
                        </rich:column>

                        <rich:column sortBy="#{ticket.customerInformation.customerName}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.customerName}" />
                            </f:facet>
                            <h:outputText value="#{ticket.customerInformation.name}" />
                            <h:outputText value=" (#{ticket.customerInformation.name})"
                                rendered="#{ticket.customerInformation.customerType == 'FIRM'}" />
                        </rich:column>

                        <rich:column>
                            <f:facet name="header">
                                <h:outputText value="#{msg.customerPhone}" />
                            </f:facet>
                            <h:outputText value="#{ticket.customerInformation.contactPhone}" />
                        </rich:column>

                        <rich:column>
                            <f:facet name="header">
                                <h:outputText value="#{msg.email}" />
                            </f:facet>
                            <h:outputText value="#{ticket.customerInformation.email}" />
                        </rich:column>

                        <f:facet name="footer">
                            <rich:datascroller align="center" maxPages="20"
                                page="#{statisticsController.page}" id="scroller" />
                        </f:facet>
                    </rich:dataTable>
                </h:panelGroup>
            </h:form>
        </f:view>
    </ui:define>
</ui:composition>