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
    <f:view>
        <a4j:form rendered="#{tc:getSize(purchaseController.tickets) > 0}">
            <rich:panel header="#{msg.currentTickets}"
                headerClass="rich-panel-header-main" id="currentTickets">
                <a4j:repeat var="ticket" value="#{purchaseController.tickets}">
                #{ticket.startStop}<h:outputText value=" → " />#{ticket.endStop}
                (<h:outputText value="#{ticket.price}">
                        <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                        <f:converter binding="#{currencyConverter}"
                            converterId="currencyConverter" />
                    </h:outputText>)<h:outputText value=" (#{msg.twoWay})" rendered="#{ticket.twoWay}" />
                    <br />
                    <br />
                </a4j:repeat>

                <h:graphicImage url="/images/timer.png" style="width: 16px; height: 16px;" />
                <h:outputText value="#{purchaseController.timeRemaining}"
                    id="timeRemaining"
                    rendered="#{purchaseController.tickets != null and tc:getSize(purchaseController.tickets) > 0}">
                    <f:convertDateTime pattern="mm:ss" />
                </h:outputText>
                <a4j:poll interval="1000" reRender="timeRemaining" />
            </rich:panel>
        </a4j:form>
    </f:view>
</ui:composition>