<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk">

    <rich:panel header="#{msg.personalDetails}">
        <h:messages />
        <h:panelGroup layout="block"
            rendered="#{loggedUserHolder.loggedUser == null}">
            <a4j:commandLink value="#{msg.loginNow}"
                onclick="#{rich:component('loginPanel')}.show()" ajaxSingle="true" />
            <h:outputText value=" #{msg.orText} " />
            <h:commandLink value="#{msg.registerNow}" immediate="true"
                action="registrationScreen" />
            <h:outputText value=" #{msg.orText} " />
            <h:outputText value="#{msg.fillYourDetails}" />
        </h:panelGroup>

        <h:panelGrid columns="3" id="directInputFields" columnClasses="label">

            <h:outputLabel value="#{msg.customerType}" for="customerType" />
            <h:selectOneMenu
                value="#{personalInformationController.customer.customerType}"
                id="customerType" style="width: 205px;">
                <f:selectItems
                    value="#{personalInformationController.customerTypeItems}" />
                <a4j:support event="onchange" ajaxSingle="true"
                    reRender="companyLabel,companyNamePanel,companyMessage" />
            </h:selectOneMenu>
            <rich:message for="customerType" errorClass="error" />

            <a4j:outputPanel id="companyLabel">
                <h:outputLabel value="#{msg.companyName}" for="companyName"
                    rendered="#{personalInformationController.customer.customerType == 'BUSINESS'}" />
            </a4j:outputPanel>
            <a4j:outputPanel id="companyNamePanel">
                <h:inputText
                    value="#{personalInformationController.customer.companyName}"
                    id="companyName" size="35"
                    rendered="#{personalInformationController.customer.customerType == 'BUSINESS'}" />
            </a4j:outputPanel>
            <a4j:outputPanel id="companyMessage">
                <rich:message for="companyName" errorClass="error"
                    rendered="#{personalInformationController.customer.customerType == 'BUSINESS'} " />
            </a4j:outputPanel>

            <h:outputLabel for="name" value="#{msg.names}" />
            <h:inputText value="#{personalInformationController.customer.name}"
                id="name" size="35">
                <rich:beanValidator />
                <a4j:support event="onblur" ajaxSingle="true" />
            </h:inputText>
            <rich:message for="name" errorClass="error" />

            <h:outputLabel for="contactPhone" value="#{msg.contactPhone}" />
            <h:inputText
                value="#{personalInformationController.customer.contactPhone}"
                id="contactPhone" size="35">
                <rich:beanValidator />
                <a4j:support event="onblur" ajaxSingle="true" />
            </h:inputText>
            <rich:message for="contactPhone" errorClass="error" />

            <h:outputLabel for="email" value="#{msg.email}" />
            <h:inputText value="#{personalInformationController.customer.email}"
                id="email" size="35">
                <rich:beanValidator />
                <a4j:support event="onblur" ajaxSingle="true" />
            </h:inputText>
            <rich:message for="email" errorClass="error" />
        </h:panelGrid>
    </rich:panel>
</ui:composition>