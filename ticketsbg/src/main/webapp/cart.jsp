<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk">

    <rich:panel header="#{msg.tickets}">
        <rich:dataTable style="width: 740px;" id="cartTable"
            value="#{purchaseController.tickets}" var="ticket">

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.route}" />
                </f:facet>
                <h:outputText value="#{ticket.startStop}" />
                <h:outputText value=" → " />
                <h:outputText value="#{ticket.endStop}" />

            </rich:column>

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.twoWayShort}" />
                </f:facet>
                <h:outputText value="#{msg.yes}" rendered="#{ticket.twoWay}" />
                <h:outputText value="#{msg.no}" rendered="#{!ticket.twoWay}" />
            </rich:column>

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.dateTime}" />
                </f:facet>
                <h:outputText value="#{ticket.departureTime.time}">
                    <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                        timeZone="#{timeZoneController.timeZone}" />
                </h:outputText>
            </rich:column>

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.returnDateTime}" />
                </f:facet>
                <h:outputText value="#{ticket.returnDepartureTime.time}">
                    <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                        timeZone="#{timeZoneController.timeZone}" />
                </h:outputText>
            </rich:column>
            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.seat}" />
                </f:facet>
                <a4j:repeat value="#{ticket.passengerDetails}" var="detail"
                    rowKeyVar="rowKey">
                    <h:outputText value=", " rendered="#{rowKey > 0}" />
                    <h:outputText value="#{detail.seat}" />
                </a4j:repeat>

                <h:panelGroup rendered="#{ticket.twoWay}">
                    <h:outputText value=" / " />
                    <a4j:repeat value="#{ticket.passengerDetails}" var="detail"
                        rowKeyVar="rowKey">
                        <h:outputText value=", " rendered="#{rowKey > 0}" />
                        <h:outputText value="#{detail.returnSeat}" />
                    </a4j:repeat>
                </h:panelGroup>

            </rich:column>

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.firm}" />
                </f:facet>
                <h:outputText value="#{ticket.run.route.firm.name}" />

                <!-- Using this column for a footer label for the next -->
                <f:facet name="footer">
                    <h:panelGroup layout="block" style="text-align: right;">
                        <h:outputText value="#{msg.total}:"
                            style="font-weight: bold; color: red;" />
                    </h:panelGroup>
                </f:facet>
            </rich:column>

            <rich:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.price}" />
                </f:facet>
                <h:outputText
                    value="#{!ticket.altered ? ticket.totalPrice : ticket.alterationPriceDifference}">
                    <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                    <f:converter binding="#{currencyConverter}"
                        converterId="currencyConverter" />
                </h:outputText>

                <f:facet name="footer">
                    <h:outputText style="font-weight: bold; color: red;"
                        value="#{purchaseController.totalPrice}">
                        <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                        <f:converter binding="#{currencyConverter}"
                            converterId="currencyConverter" />
                    </h:outputText>
                </f:facet>
            </rich:column>

            <rich:column>
                <a4j:commandLink action="#{purchaseController.removeTicket}"
                    title="#{msg.remove}" reRender="cartTable,paymentGatewayFormWrapper"
                    onclick="if (!confirm('#{msg.confirmTicketRemovalFromCart}')) {return false;}"
                    immediate="true" actionListener="#{paymentController.refreshPaymentData}">
                    <f:setPropertyActionListener value="#{ticket}"
                        target="#{purchaseController.currentTicket}" />
                    <h:graphicImage value="/images/delete.png"
                        style="width:16px; height:16px; border-style: none;"
                        alt="#{msg.remove}" title="#{msg.remove}" />
                </a4j:commandLink>
            </rich:column>
        </rich:dataTable>
    </rich:panel>
</ui:composition>