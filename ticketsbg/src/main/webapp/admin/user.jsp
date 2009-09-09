<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:j4j="http://javascript4jsf.dev.java.net/"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:cust="http://abax.bg/cust"
    xmlns:t="http://myfaces.apache.org/tomahawk">
    <style>
.gridContent {
    vertical-align: top;
}
</style>
    <f:view>
        <rich:messages/>
        <a4j:outputPanel ajaxRendered="true">
            <t:inputHidden forceId="true" id="userFormHasMessages"
                value="#{userController.hasMessages}" />
        </a4j:outputPanel>
        <a4j:form id="userForm">
            <a4j:outputPanel ajaxRendered="true">
                <rich:panel>
                    <h:panelGrid columns="2" styleClass="gridContent">
                        <h:outputLabel for="username" value="#{msg.username}: " />
                        <a4j:outputPanel>
                            <h:inputText value="#{userController.user.username}"
                                id="username" disabled="#{userController.user.id > 0}"
                                autocomplete="false">
                                <f:attribute name="label" value="#{msg.username}" />
                                <rich:ajaxValidator />
                            </h:inputText>
                            <rich:message errorClass="error" for="username" />
                        </a4j:outputPanel>


                        <h:outputLabel for="password" value="#{msg.password}: " />
                        <h:inputSecret value="#{userController.user.password}"
                            id="password" autocomplete="false" redisplay="true"
                            validatorMessage="#{msg.commonLengthMessage}">
                            <f:attribute name="label" value="#{msg.password}" />
                        </h:inputSecret>

                        <h:outputLabel for="names" value="#{msg.names}: " />
                        <h:inputText value="#{userController.user.name}" id="names">
                            <f:attribute name="label" value="#{msg.names}" />
                        </h:inputText>

                        <h:outputLabel for="email" value="#{msg.email}: " />
                        <h:inputText value="#{userController.user.email}" id="email">
                            <f:attribute name="label" value="#{msg.email}" />
                        </h:inputText>

                        <h:outputLabel for="contactPhone" value="#{msg.contactPhone}: " />
                        <h:inputText value="#{userController.user.contactPhone}"
                            id="contactPhone">
                            <f:attribute name="label" value="#{msg.contactPhone}" />
                        </h:inputText>

                        <h:outputLabel for="active" value="#{msg.active}: " />
                        <h:selectBooleanCheckbox value="#{userController.user.active}"
                            id="active" />

                        <h:outputLabel for="accessLevel" value="#{msg.accessLevel}: " />
                        <h:selectOneMenu id="accessLevel"
                            value="#{userController.user.accessLevel}"
                            converter="#{enumConverter}">
                            <f:selectItems value="#{userController.accessLevelSelectItems}" />
                        </h:selectOneMenu>

                        <h:outputLabel for="agent" value="#{msg.agent}: " />
                        <h:selectOneMenu id="agent" converter="#{entityConverter}"
                            value="#{userController.user.agent}">
                            <f:selectItems value="#{userController.agents}" />
                        </h:selectOneMenu>

                        <a4j:commandButton value="#{msg.save}"
                            action="#{userController.save}" type="submit"
                            oncomplete="if (document.getElementById('userFormHasMessages').value == 'false') { #{rich:component('entityPanel')}.hide()}"
                            reRender="usersTable,agentsUsersTable" />
                        <h:outputText value="" />
                    </h:panelGrid>
                </rich:panel>
            </a4j:outputPanel>
        </a4j:form>
    </f:view>
</ui:composition>