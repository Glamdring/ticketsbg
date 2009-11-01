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
    xmlns:tc="http://tickets.com/tc"
    template="publicTemplate.jsp">

    <ui:define name="body">
        <f:view afterPhaseListener="#{purchaseController.afterPhase}">
            <!-- attribute renamed in facelets. -->
            <rich:panel header="#{msg.purchaseSuccessfulTitle}"
                headerClass="rich-panel-header-main">
                <a4j:form id="successForm">
                    <div align="center" style="width: 100%;"><h:outputText
                        value="#{msg.purchaseSuccessful}" style="font-size: 12px;" /> <a4j:repeat
                        value="#{purchaseController.tickets}" var="ticket">
                        <rich:separator />
                        <h:panelGrid style="text-align: center;" columns="1">
                            <h:panelGroup>
                                <h:outputText value="#{ticket.startStop} → #{ticket.endStop}"
                                    style="font-weight: bold;" />
                                <h:outputText value=" (#{msg.twoWay})"
                                    rendered="#{ticket.twoWay}" />
                            </h:panelGroup>

                            <h:outputText value="#{msg.ticketCode}: " />
                            <h:outputText value="#{ticket.ticketCode}"
                                style="font-size: 18px; font-weight: bold;" />

                            <h:panelGroup>
                                <h:outputText value="#{tc:getSize(ticket.passengerDetails) > 1 ? msg.seats : msg.seat}: " />
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
                            </h:panelGroup>
                            <h:outputText
                                value=" (#{ticket.run.route.requireReceiptAtCashDesk ? msg.requireReceiptAtCashDeskUserMessage : msg.showTicketAtVehicle})" />
                        </h:panelGrid>
                    </a4j:repeat></div>
                    <a4j:jsFunction name="clearPurchase"
                        action="#{purchaseController.clearPurchase}" />
                </a4j:form>
            </rich:panel>
        </f:view>
    </ui:define>
</ui:composition>