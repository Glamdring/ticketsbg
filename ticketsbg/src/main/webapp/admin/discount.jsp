<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:j4j="http://javascript4jsf.dev.java.net/"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:cust="http://tickets.com/cust"
    xmlns:t="http://myfaces.apache.org/tomahawk">
    <f:view>
        <h:messages />
        <h:form id="discountForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2" columnClasses="rich-panel">
                    <h:outputLabel value="#{msg.discountName}" for="discountName" />
                    <h:inputText value="#{routeController.discount.name}" id="discountName" />
                    
                    <h:outputLabel value="#{msg.discountType}" for="discountType" />
                    <h:selectOneMenu value="#{routeController.discount.discountType}" id="discountType">
                        <f:selectItems value="#{routeController.discountTypeSelectItems}" />
                    </h:selectOneMenu>
                    
                    <h:outputLabel value="#{msg.discountValue}" for="discountValue" />
                    <h:inputText value="#{routeController.discount.value}" id="discountValue">
                        <f:convertNumber maxFractionDigits="2" minFractionDigits="2" />
                    </h:inputText>
                    
                    <h:outputLabel value="#{msg.description}" for="description" />
					<h:inputTextarea value="#{routeController.discount.description}"
						id="description" rows="5" cols="20" />


					<a4j:commandButton action="#{routeController.saveDiscount}"
                        value="#{msg.save}" reRender="discountsPanel"
                        oncomplete="#{rich:component('discountPanel')}.hide();">
                        <cust:defaultAction />
                    </a4j:commandButton>
                    <h:outputText></h:outputText>
                </h:panelGrid>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>