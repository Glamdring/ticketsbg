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
        <h:form id="genericDiscountForm">
            <a4j:outputPanel ajaxRendered="true">
                <rich:panel>
                    <h:panelGrid columns="2" styleClass="gridContent">
                        <h:outputLabel value="#{msg.discountName}" for="discountName" />
                        <h:inputText value="#{discountController.discount.name}"
                            id="discountName" />


                        <h:outputLabel value="#{msg.description}" for="description" />
                        <h:inputTextarea value="#{discountController.discount.description}"
                            id="description" rows="5" cols="20" />

                        <a4j:commandButton value="#{msg.save}"
                            action="#{discountController.save}"
                            oncomplete="#{rich:component('entityPanel')}.hide()"
                            reRender="discountsTable" />
                        <h:outputText value="" />
                    </h:panelGrid>
                </rich:panel>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>