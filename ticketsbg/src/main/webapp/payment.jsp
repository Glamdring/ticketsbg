<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk"
    template="publicTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <a4j:form id="paymentForm">
                <rich:panel header="#{msg.payment}"
                    headerClass="rich-panel-header-main">
                    <h:messages />

                    <a4j:include viewId="personalInformation.jsp" />

                    <rich:panel header="#{msg.paymentDetails}">
                        <h:panelGrid columns="3" columnClasses="label">
                            <h:outputLabel for="paymentMethod" value="#{msg.paymentMethod}" />
                            <rich:comboBox id="paymentMethod" width="205"
                                value="#{paymentController.selectedPaymentMethod}">
                                <f:selectItems value="#{paymentController.paymentMethods}" />
                            </rich:comboBox>
                            <rich:message for="paymentMethod" errorClass="error" />
                        </h:panelGrid>

                        <h:panelGrid columns="3" columnClasses="label"
                            id="creditCardDetails">
                            <h:outputLabel value="#{msg.cardHolderName}" for="cardHolder" />
                            <h:inputText id="cardHolder" size="35" />
                            <rich:message for="cardHolder" errorClass="error" />

                            <h:outputLabel value="#{msg.cardNumber}" for="cardNumber" />
                            <h:inputText id="cardNumber" size="35">
                                <t:validateCreditCard mastercard="true" visa="true" />
                            </h:inputText>
                            <rich:message for="cardNumber" errorClass="error" />

                            <h:outputLabel value="#{msg.cardExpiration}" for="cardExpiration" />
                            <h:panelGroup id="cardExpiration">
                                <select name="CardExpirationMonth">
                                    <option value="0" selected="selected">Month</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                </select> / <select name="CardExpirationYear">
                                    <option value="0" selected="selected">Year</option>
                                    <option value="2009">2009</option>
                                    <option value="2010">2010</option>
                                    <option value="2011">2011</option>
                                    <option value="2012">2012</option>
                                    <option value="2013">2013</option>
                                    <option value="2014">2014</option>
                                    <option value="2015">2015</option>
                                    <option value="2016">2016</option>
                                    <option value="2017">2017</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                    <option value="2021">2021</option>
                                    <option value="2022">2022</option>
                                    <option value="2023">2023</option>
                                </select>
                            </h:panelGroup>
                            <rich:message for="cardExpiration" errorClass="error" />

                            <h:outputLabel value="#{msg.securityCode}" for="securityCode" />
                            <h:inputText id="securityCode" size="4" />
                            <rich:message for="securityCode" errorClass="error" />
                        </h:panelGrid>

                    </rich:panel>
                </rich:panel>
            </a4j:form>
        </f:view>
    </ui:define>
</ui:composition>