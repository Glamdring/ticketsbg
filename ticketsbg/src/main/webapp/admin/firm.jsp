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
                    <h:inputText value="#{firmController.firm.name}" id="name"
                        size="30" />

                    <h:outputLabel for="description" value="#{msg.shortDescription}: " />
                    <h:inputTextarea value="#{firmController.firm.description}"
                        id="description" rows="2" cols="27" />

                    <h:outputLabel for="terms" value="#{msg.termsOfUse}: " />
                    <h:inputTextarea value="#{firmController.firm.terms}"
                        id="terms" rows="4" cols="27" />

                    <h:outputLabel for="subdomain" value="#{msg.subdomain}: " />
                    <h:inputText value="#{firmController.firm.subdomain}"
                        id="subdomain" size="30" />

                    <h:outputLabel for="bulstat" value="#{msg.bulstat}: " />
                    <h:inputText value="#{firmController.firm.bulstat}" id="bulstat"
                        size="30" />

                    <h:outputLabel for="epayKin" value="#{msg.epayKin}: " />
                    <h:inputText value="#{firmController.firm.epayKin}" id="epayKin"
                        size="30" />

                    <h:outputLabel for="iban" value="#{msg.iban}: " />
                    <h:inputText value="#{firmController.firm.iban}" id="iban"
                        size="30" />

                    <h:outputLabel for="bank" value="#{msg.bank}: " />
                    <h:inputText value="#{firmController.firm.bank}" id="bank"
                        size="30" />

                    <h:outputLabel for="bic" value="#{msg.bic}: " />
                    <h:inputText value="#{firmController.firm.bic}" id="bic" size="30" />

                    <h:outputLabel for="other" value="#{msg.other}: " />
                    <h:inputText value="#{firmController.firm.other}" id="other"
                        size="30" />

                    <h:outputLabel for="active" value="#{msg.active}: "
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />
                    <h:selectBooleanCheckbox value="#{firmController.firm.active}"
                        id="active"
                        rendered="#{loggedUserHolder.loggedUser.administrator}" />

                    <h:outputLabel for="hasAnotherTicketSellingSystem"
                        value="#{msg.hasAnotherTicketSellingSystem}: "/>
                    <h:selectBooleanCheckbox
                        value="#{firmController.firm.hasAnotherTicketSellingSystem}"
                        id="hasAnotherTicketSellingSystem" />

                    <h:outputLabel for="allowDiscounts"
                        value="#{msg.allowDiscounts}: "/>
                    <h:selectBooleanCheckbox
                        value="#{firmController.firm.allowDiscounts}"
                        id="allowDiscounts" />

                    <h:panelGroup>
                        <h:outputLabel for="requireReceiptAtCashDesk"
                            value="#{msg.requireReceiptAtCashDesk}: "
                            id="requireReceiptAtCashDeskLabel" />
                        <rich:toolTip value="#{msg.requireReceiptAtCashDeskInfo}"
                            for="requireReceiptAtCashDeskLabel" followMouse="true" />
                    </h:panelGroup>
                    <h:selectBooleanCheckbox
                        value="#{firmController.firm.requireReceiptAtCashDesk}"
                        id="requireReceiptAtCashDesk" />

                    <h:outputLabel for="hoursBeforeTravelAlterationAllowed"
                        value="#{msg.hoursBeforeTravelAlterationAllowed}: " />
                    <rich:inputNumberSpinner
                        value="#{firmController.firm.hoursBeforeTravelAlterationAllowed}"
                        id="hoursBeforeTravelAlterationAllowed" minValue="0" />


                    <rich:componentControl attachTo="richDescriptionOpener"
                        event="onclick" for="richDescriptionPanel" operation="show" />
                    <h:outputText id="richDescriptionOpener" styleClass="link"
                        style="color: blue; text-decoration: underline;"
                        value="#{msg.description}" />

                    <h:outputText value="" />
                    <a4j:commandButton value="${msg.save}"
                        action="#{firmController.save}"
                        oncomplete="if (#{rich:component('entityPanel') != null}) {#{rich:component('entityPanel') != null ? rich:component('entityPanel') : 'this'}.hide();} #{rich:component('firmPanel')}.hide();"
                        reRender="firmsTable" />
                </h:panelGrid>
            </a4j:outputPanel>

            <rich:modalPanel id="richDescriptionPanel" autosized="true"
                width="200" height="320" moveable="true" resizeable="false"
                onmaskclick="#{rich:component('richDescriptionPanel')}.hide()"
                domElementAttachment="parent" style="overflow: hidden;">
                <ui:include src="/modalPanelCommons.jsp">
                    <ui:param name="dialogId" value="richDescriptionPanel" />
                </ui:include>
                <f:facet name="header">
                    <h:outputText value="#{msg.description}" />
                </f:facet>
                <h:panelGroup>
                    <rich:editor readonly="false" viewMode="visual" height="280"
                        value="#{firmController.firm.richDescription}" theme="advanced">
                        <f:param name="theme_advanced_toolbar_location" value="top" />
                        <f:param name="theme_advanced_toolbar_align" value="left" />
                    </rich:editor>
                    <a4j:commandButton value="OK"
                        onclick="#{rich:component('richDescriptionPanel')}.hide()" />
                </h:panelGroup>
            </rich:modalPanel>
        </h:form>

    </f:view>
</ui:composition>