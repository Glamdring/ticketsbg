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
        <h:form id="firmForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2" styleClass="gridContent">
                    <h:outputLabel for="name" value="#{msg.firmName}: " />
                    <h:inputText value="#{firmController.firm.name}" id="name" size="30" />

                    <h:outputLabel for="description" value="#{msg.description}: " />
                    <h:inputTextarea value="#{firmController.firm.description}"
                        id="description" rows="4" cols="27" />

                    <h:outputLabel for="subdomain" value="#{msg.subdomain}: " />
                    <h:inputText value="#{firmController.firm.subdomain}" id="subdomain" size="30" />

                    <h:outputLabel for="bulstat" value="#{msg.bulstat}: " />
                    <h:inputText value="#{firmController.firm.bulstat}" id="bulstat" size="30" />

                    <h:outputLabel for="epayKin" value="#{msg.epayKin}: " />
                    <h:inputText value="#{firmController.firm.epayKin}" id="epayKin" size="30" />

                    <h:outputLabel for="iban" value="#{msg.iban}: " />
                    <h:inputText value="#{firmController.firm.iban}" id="iban" size="30"/>

                    <h:outputLabel for="bank" value="#{msg.bank}: " />
                    <h:inputText value="#{firmController.firm.bank}" id="bank" size="30" />

                    <h:outputLabel for="bic" value="#{msg.bic}: " />
                    <h:inputText value="#{firmController.firm.bic}" id="bic" size="30" />

                    <h:outputLabel for="other" value="#{msg.other}: " />
                    <h:inputText value="#{firmController.firm.other}" id="other" size="30" />

                    <h:outputLabel for="active" value="#{msg.active}: "
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />
                    <h:selectBooleanCheckbox value="#{firmController.firm.active}"
                        id="active"
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />

                    <h:outputLabel for="hasAnotherTicketSellingSystem"
                        value="#{msg.hasAnotherTicketSellingSystem}: "
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />
                    <h:selectBooleanCheckbox
                        value="#{firmController.firm.hasAnotherTicketSellingSystem}"
                        id="hasAnotherTicketSellingSystem"
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />

                    <h:outputText value="" />
                    <a4j:commandButton value="${msg.save}"
                        action="#{firmController.save}"
                        oncomplete="#{rich:component('entityPanel')}.hide()"
                        reRender="firmsTable" />
                </h:panelGrid>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>