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
    template="publicTemplate.jsp">

    <ui:define name="body">
        <f:view afterPhase="#{paymentController.afterPhase}"><!-- TODO phases -->
            <rich:panel header="#{msg.purchaseSuccessfulTitle}"
                headerClass="rich-panel-header-main">
                <a4j:form id="successForm">
                <div align="center" style="width: 100%;">
                    <h:outputText value="#{msg.purchaseSuccessful}" style="font-size: 12px;" />

                    <a4j:repeat value="#{purchaseController.tickets}" var="ticket">
                        <rich:separator/>
                        <h:panelGrid style="text-align: center;" columns="1">
                            <h:outputText value="#{msg.ticketCode}: "/>
                            <h:outputText value="#{ticket.ticketCode}" style="font-size: 18px; font-weight: bold;" />
                            <h:outputText value=" (#{ticket.run.route.requireReceiptAtCashDesk ? msg.requireReceiptAtCashDeskUserMessage : msg.showTicketAtVehicle})" />
                        </h:panelGrid>
                    </a4j:repeat>
                </div>
                <a4j:jsFunction name="clearPurchase" action="#{purchaseController.clearPurchase}" />
                </a4j:form>
            </rich:panel>
        </f:view>
    </ui:define>
</ui:composition>