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
            <t:inputHidden forceId="true" id="officeFormHasMessages"
                value="#{officeController.hasMessages}" />
        </a4j:outputPanel>
        <a4j:form id="officeForm">
            <a4j:outputPanel ajaxRendered="true">
                <rich:panel>
                    <h:panelGrid columns="2" styleClass="gridContent">

                        <h:outputLabel for="name" value="#{msg.name}: " />
                        <h:inputText value="#{officeController.office.name}" id="name">
                            <f:attribute name="label" value="#{msg.name}" />
                        </h:inputText>

                        <h:outputLabel for="email" value="#{msg.email}: " />
                        <h:inputText value="#{officeController.office.email}" id="email">
                            <f:attribute name="label" value="#{msg.email}" />
                        </h:inputText>

                        <h:outputLabel for="contactPhone" value="#{msg.contactPhone}: " />
                        <h:inputText value="#{officeController.office.contactPhone}"
                            id="contactPhone">
                            <f:attribute name="label" value="#{msg.contactPhone}" />
                        </h:inputText>


                        <h:outputLabel for="description" value="#{msg.description}: " />
                        <h:inputTextarea value="#{officeController.office.description}"
                            id="description" rows="3" cols="25">
                            <f:attribute name="label" value="#{msg.description}" />
                        </h:inputTextarea>

                        <a4j:commandButton value="#{msg.save}"
                            action="#{officeController.save}" type="submit"
                            oncomplete="if (document.getElementById('officeFormHasMessages').value == 'false') { #{rich:component('entityPanel')}.hide()}"
                            reRender="officesTable,agentsUsersTable" />
                        <h:outputText value="" />
                    </h:panelGrid>
                </rich:panel>
            </a4j:outputPanel>
        </a4j:form>
    </f:view>
</ui:composition>