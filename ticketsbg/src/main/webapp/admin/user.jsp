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
        <h:messages />
        <h:form id="userForm">
            <a4j:outputPanel ajaxRendered="true">
                <rich:panel>
                    <h:panelGrid columns="2" styleClass="gridContent">
                        <h:outputLabel for="username" value="#{msg.username}: " />
                        <h:inputText value="#{userController.user.username}" id="username"
                            disabled="#{userController.user.id > 0}" autocomplete="false" />

                        <h:outputLabel for="password" value="#{msg.password}: " />
                        <h:inputSecret value="#{userController.user.password}"
                            id="password" autocomplete="false" redisplay="true" />

                        <h:outputLabel for="names" value="#{msg.names}: " />
                        <h:inputText value="#{userController.user.name}" id="names" />

                        <h:outputLabel for="active" value="#{msg.active}: " />
                        <h:selectBooleanCheckbox value="#{userController.user.active}"
                            id="active" />

                        <h:outputLabel for="roles" value="#{msg.roles}: " />
                        <rich:pickList id="roles" showButtonsLabel="false"
                            value="#{userController.user.roles}"
                            converter="#{entityConverter}">
                            <f:selectItems value="#{userController.roleSelectItems}" />
                        </rich:pickList>

                        <a4j:commandButton value="#{msg.save}"
                            action="#{userController.save}"
                            oncomplete="#{rich:component('entityPanel')}.hide()"
                            reRender="usersTable" />
                        <h:outputText value="" />
                    </h:panelGrid>
                </rich:panel>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>