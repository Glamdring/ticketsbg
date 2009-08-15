<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core" template="publicTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <a4j:form id="profileForm">
                <rich:panel headerClass="rich-panel-header-main"
                    header="#{msg.profile}">
                    <a4j:outputPanel ajaxRendered="true" id="messages">
                        <h:messages styleClass="message" globalOnly="true"
                            errorClass="error" />
                    </a4j:outputPanel>

                    <rich:graphValidator>
                        <h:panelGrid columns="3">
                            <h:outputLabel value="#{msg.username}" for="username" />
                            <h:inputText value="#{loggedUserHolder.loggedUser.username}"
                                id="username" size="35" disabled="true">
                            </h:inputText>
                            <a4j:commandButton value="#{msg.changePassword}"
                                onclick="#{rich:component('changePasswordPanel')}.show()" />

                            <h:outputLabel value="#{msg.email}" for="email" />
                            <h:inputText value="#{loggedUserHolder.loggedUser.email}" id="email"
                                size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputText>
                            <rich:message for="email" errorClass="error" />

                            <h:outputLabel value="#{msg.customerType}" for="customerType" />
                            <h:selectOneMenu value="#{loggedUserHolder.loggedUser.customerType}"
                                id="customerType" style="width: 205px;">
                                <f:selectItems value="#{profileController.customerTypeItems}" />
                                <a4j:support event="onchange" ajaxSingle="true"
                                    reRender="companyLabel,companyNamePanel,companyMessage" />
                            </h:selectOneMenu>
                            <rich:message for="customerType" errorClass="error" />

                            <a4j:outputPanel id="companyLabel">
                                <h:outputLabel value="#{msg.companyName}" for="companyName"
                                    rendered="#{loggedUserHolder.loggedUser.customerType == 'BUSINESS'}" />
                            </a4j:outputPanel>
                            <a4j:outputPanel id="companyNamePanel">
                                <h:inputText value="#{loggedUserHolder.loggedUser.companyName}"
                                    id="companyName" size="35"
                                    rendered="#{loggedUserHolder.loggedUser.customerType == 'BUSINESS'}" />
                            </a4j:outputPanel>
                            <a4j:outputPanel id="companyMessage">
                                <rich:message for="companyName" errorClass="error"
                                    rendered="#{loggedUserHolder.loggedUser.customerType == 'BUSINESS'} " />
                            </a4j:outputPanel>

                            <h:outputLabel value="#{msg.names}" for="names" />
                            <h:inputText value="#{loggedUserHolder.loggedUser.name}" id="names"
                                size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputText>
                            <rich:message for="names" errorClass="error" />

                            <h:outputLabel value="#{msg.contactPhone}" for="contactPhone" />
                            <h:inputText value="#{loggedUserHolder.loggedUser.contactPhone}"
                                id="contactPhone" size="35" />
                            <rich:message for="contactPhone" errorClass="error" />

                            <h:outputLabel value="#{msg.city}" for="city" />
                            <h:inputText value="#{loggedUserHolder.loggedUser.city}" id="city"
                                size="35" />
                            <rich:message for="city" errorClass="error" />

                            <h:outputText />
                            <h:panelGroup>
                                <h:selectBooleanCheckbox
                                    value="#{loggedUserHolder.loggedUser.receiveNewsletter}"
                                    id="receiveNewsletter" label="#{msg.receiveNewsletter}" />
                                <h:outputLabel for="receiveNewsletter"
                                    value="#{msg.receiveNewsletter}" />
                            </h:panelGroup>
                            <rich:message for="receiveNewsletter" errorClass="error" />


                            <h:outputText />
                            <a4j:commandButton value="#{msg.save}"
                                action="#{profileController.save}" />
                            <h:outputText />
                        </h:panelGrid>
                    </rich:graphValidator>
                </rich:panel>
            </a4j:form>
        </f:view>
        <ui:include src="changePasswordPanel.jsp" />
    </ui:define>
</ui:composition>
