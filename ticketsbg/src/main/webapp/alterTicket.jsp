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
        <f:view>
            <rich:panel header="#{msg.alterTicket}"
                headerClass="rich-panel-header-main">
                <a4j:form id="alterTicketForm">
                    <ui:include src="messages.jsp">
                        <ui:param name="additionalId" value="alter" />
                    </ui:include>

                    <h:panelGrid columns="2">
                        <h:outputLabel value="#{msg.ticketCode}: " for="ticketCode" />
                        <h:inputText value="#{alterTicketController.ticketCode}" size="40" id="ticketCode"/>

                        <h:outputLabel value="#{msg.email}: " for="email" />
                        <h:inputText value="#{alterTicketController.email}" size="40" id="email"/>

                        <h:outputText />
                        <a4j:commandButton value="#{msg.alterTimes}"
                            action="#{alterTicketController.identifyTicket}"
                            reRender="searchFieldsHolder" />
                    </h:panelGrid>

                    <a4j:outputPanel ajaxRendered="true" id="searchFieldsHolder">
                        <h:panelGroup rendered="#{alterTicketController.ticket != null}">
                            <ui:include src="searchFields.jsp">
                                <ui:param name="isAdmin" value="false" />
                            </ui:include>
                        </h:panelGroup>
                    </a4j:outputPanel>

                </a4j:form>
            </rich:panel>
        </f:view>
    </ui:define>
</ui:composition>