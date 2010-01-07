<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:tc="http://tickets.bg/tickets">

    <rich:modalPanel id="changePasswordPanel" autosized="true" width="500">
        <f:facet name="header">
            <h:outputText value="#{msg.changePassword}" />
        </f:facet>
        <ui:include src="/modalPanelCommons.jsp">
            <ui:param name="dialogId" value="changePasswordPanel" />
        </ui:include>

        <a4j:form ajaxSubmit="true" id="changePasswordForm">
            <h:panelGrid columns="3">

                <h:outputLabel value="#{msg.oldPassword}" for="oldPassword" />
                <h:inputSecret value="#{changePasswordController.oldPassword}"
                    id="oldPassword" size="35">
                    <a4j:support event="onblur" ajaxSingle="true" />
                </h:inputSecret>
                <rich:message for="oldPassword" errorClass="error" />

                <h:outputLabel value="#{msg.newPassword}" for="newPassword" />
                <h:inputSecret value="#{changePasswordController.newPassword}"
                    id="newPassword" size="35" label="#{msg.newPassword}">
                    <f:validateLength minimum="4" maximum="40" />
                    <rich:ajaxValidator event="onblur" />
                </h:inputSecret>
                <rich:message for="newPassword" errorClass="error" />

                <h:outputLabel value="#{msg.newPassword2}" for="newPassword2" />
                <h:inputSecret value="#{changePasswordController.newPassword2}"
                    id="newPassword2" size="35" label="#{msg.newPassword2}">
                    <f:validateLength minimum="4" maximum="40" />
                    <rich:ajaxValidator event="onblur" />
                </h:inputSecret>
                <rich:message for="newPassword2" errorClass="error" />

                <h:outputText />
                <a4j:commandButton
                    action="#{changePasswordController.changePassword}"
                    oncomplete="#{rich:component('changePasswordPanel')}.hide()"
                    value="#{msg.doChangePassword}" reRender="messages" />
                <h:outputText />

            </h:panelGrid>
        </a4j:form>
    </rich:modalPanel>
</ui:composition>