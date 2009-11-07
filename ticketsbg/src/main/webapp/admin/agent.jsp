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
        <h:form id="agentForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2" styleClass="gridContent">
                    <h:outputLabel for="name" value="#{msg.agentName}: " />
                    <h:inputText value="#{agentController.agent.name}" id="name" size="30" />

                    <h:outputLabel for="description" value="#{msg.description}: " />
                    <h:inputTextarea value="#{agentController.agent.description}"
                        id="description" rows="4" cols="27" />

                    <h:outputLabel for="eik" value="#{msg.eik}: " />
                    <h:inputText value="#{agentController.agent.eik}" id="bulstat" size="30" />

                    <h:outputLabel for="other" value="#{msg.other}: " />
                    <h:inputText value="#{agentController.agent.other}" id="other" size="30" />

                    <h:outputLabel for="active" value="#{msg.active}: "
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />
                    <h:selectBooleanCheckbox value="#{agentController.agent.active}"
                        id="active"
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />

                    <h:outputText value="" />
                    <a4j:commandButton value="${msg.save}"
                        action="#{agentController.save}"
                        oncomplete="#{rich:component('entityPanel')}.hide()"
                        reRender="agentsTable" />
                </h:panelGrid>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>