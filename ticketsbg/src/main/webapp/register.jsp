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
            <a4j:form id="registerForm">
                <rich:panel headerClass="rich-panel-header-main"
                    header="#{msg.register}">
                    <rich:graphValidator>
                        <h:panelGrid columns="3">
                            <h:panelGroup>
                                <h:outputLabel value="#{msg.username}" for="username" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputText value="#{registerController.user.username}"
                                id="regUsername" size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputText>
                            <rich:message for="regUsername" errorClass="error" />

                            <h:panelGroup>
                                <h:outputLabel value="#{msg.password}" for="password" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputSecret value="#{registerController.user.password}"
                                id="regPassword" size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputSecret>
                            <rich:message for="regPassword" errorClass="error" />

                            <h:panelGroup>
                                <h:outputLabel value="#{msg.repeatPassword}" for="repeatPassword" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputSecret value="#{registerController.user.repeatPassword}"
                                id="repeatPassword" size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputSecret>
                            <rich:message for="repeatPassword" errorClass="error" />


                            <h:panelGroup>
                                <h:outputLabel value="#{msg.email}" for="email" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputText value="#{registerController.user.email}" id="email"
                                size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputText>
                            <rich:message for="email" errorClass="error" />

                            <h:outputLabel value="#{msg.customerType}" for="customerType" />
                            <h:selectOneMenu value="#{registerController.user.customerType}"
                                id="customerType" style="width: 205px;">
                                <f:selectItems value="#{registerController.customerTypeItems}" />
                                <a4j:support event="onchange" ajaxSingle="true"
                                    reRender="companyLabel,companyNamePanel,companyMessage" />
                            </h:selectOneMenu>
                            <rich:message for="customerType" errorClass="error" />

                            <a4j:outputPanel id="companyLabel">
                                <h:outputLabel value="#{msg.companyName}" for="companyName"
                                    rendered="#{registerController.user.customerType == 'BUSINESS'}" />
                                <h:outputText value=" *" styleClass="asterisk"
                                    rendered="#{registerController.user.customerType == 'BUSINESS'}" />
                            </a4j:outputPanel>
                            <a4j:outputPanel id="companyNamePanel">
                                <h:inputText value="#{registerController.user.companyName}"
                                    id="companyName" size="35"
                                    rendered="#{registerController.user.customerType == 'BUSINESS'}" />
                            </a4j:outputPanel>
                            <a4j:outputPanel id="companyMessage">
                                <rich:message for="companyName" errorClass="error"
                                    rendered="#{registerController.user.customerType == 'BUSINESS'} " />
                            </a4j:outputPanel>

                            <h:panelGroup>
                                <h:outputLabel value="#{msg.names}" for="names" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputText value="#{registerController.user.name}" id="names"
                                size="35">
                                <rich:ajaxValidator event="onblur" />
                            </h:inputText>
                            <rich:message for="names" errorClass="error" />

                            <h:panelGroup>
                                <h:outputLabel value="#{msg.contactPhone}" for="contactPhone" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <h:inputText value="#{registerController.user.contactPhone}"
                                id="contactPhone" size="35" />
                            <rich:message for="contactPhone" errorClass="error" />

                            <h:outputLabel value="#{msg.city}" for="city" />
                            <h:inputText value="#{registerController.user.city}" id="city"
                                size="35" />
                            <rich:message for="city" errorClass="error" />

                            <h:outputLabel value="#{msg.foundUsVia}" for="foundUsVia" />
                            <h:selectOneMenu value="#{registerController.user.foundUsVia}"
                                id="foundUsVia" style="width: 205px;">
                                <f:selectItem itemValue="" itemLabel="#{msg.selectOne}" />
                                <f:selectItem itemValue="WebAd"
                                    itemLabel="#{msg.foundUsViaWebAd}" />
                                <f:selectItem itemValue="RealWorldAd"
                                    itemLabel="#{msg.foundUsViaRealWorldAd}" />
                                <f:selectItem itemValue="Friend"
                                    itemLabel="#{msg.foundUsViaFriend}" />
                                <f:selectItem itemValue="SearchEngine"
                                    itemLabel="#{msg.foundUsViaSearchEngine}" />
                                <f:selectItem itemValue="Other"
                                    itemLabel="#{msg.foundUsViaOther}" />
                            </h:selectOneMenu>
                            <rich:message for="foundUsVia" errorClass="error" />

                            <h:outputText />
                            <h:panelGroup>
                                <h:selectBooleanCheckbox
                                    value="#{registerController.user.receiveNewsletter}"
                                    id="receiveNewsletter" label="#{msg.receiveNewsletter}" />
                                <h:outputLabel for="receiveNewsletter"
                                    value="#{msg.receiveNewsletter}" />
                            </h:panelGroup>
                            <rich:message for="receiveNewsletter" errorClass="error" />

                            <h:outputText />
                            <h:panelGroup>
                                <h:selectBooleanCheckbox
                                    value="#{registerController.user.agreedToTerms}"
                                    id="agreedToTerms" label="#{msg.agreedToterms}" />
                                <h:outputLabel for="agreedToTerms" value="#{msg.agreedToTerms}" />
                                <h:outputText value=" *" styleClass="asterisk" />
                            </h:panelGroup>
                            <rich:message for="agreedToTerms" errorClass="error" />

                            <h:outputText />
                            <a4j:commandButton value="#{msg.register}"
                                action="#{registerController.register}" />
                            <h:outputText />
                        </h:panelGrid>
                    </rich:graphValidator>
                </rich:panel>
            </a4j:form>
        </f:view>
    </ui:define>
</ui:composition>
