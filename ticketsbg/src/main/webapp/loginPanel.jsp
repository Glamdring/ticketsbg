<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:tc="http://tickets.bg/tickets">

    <rich:modalPanel id="loginPanel" autosized="true" width="250"
        style="overflow: hidden;">
        <f:facet name="header">
            <h:outputText value="#{msg.login}" />
        </f:facet>
        <ui:include src="/modalPanelCommons.jsp">
            <ui:param name="dialogId" value="loginPanel" />
        </ui:include>

        <a4j:form ajaxSubmit="true" id="loginForm">
            <ui:include src="loginFields.jsp">
                <ui:param name="isAdmin" value="false" />
            </ui:include>
            <h:inputHidden id="admin" value="false"
                binding="#{loginController.admin}" />
            <a4j:support event="hide"
                action="#{personalInformationController.updateCustomer}" />
        </a4j:form>
    </rich:modalPanel>
</ui:composition>