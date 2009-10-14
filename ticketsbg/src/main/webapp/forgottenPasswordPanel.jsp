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

    <a4j:form ajaxSubmit="true" id="forgottenPasswordForm">
        <rich:modalPanel id="forgottenPasswordPanel" autosized="true"
            width="250" style="overflow: hidden;" domElementAttachment="form">
            <f:facet name="header">
                <h:outputText value="#{msg.forgottenPassword}" />
            </f:facet>
            <f:facet name="controls">
                <h:panelGroup>
                    <h:graphicImage value="/images/close.png" id="hidelinkFP"
                        styleClass="hidelink" />
                    <rich:componentControl for="forgottenPasswordPanel"
                        attachTo="hidelinkFP" operation="hide" event="onclick" />
                </h:panelGroup>
            </f:facet>


            <ui:include src="messages.jsp" />
            <h:outputLabel for="email" value="#{msg.email}: " />
            <h:inputText id="email" value="#{forgottenPasswordController.email}" />
            <a4j:commandButton value="#{msg.sendTempPassword}"
                action="#{forgottenPasswordController.sendTemporaryPassword}"
                oncomplete="if(#{facesContext.maximumSeverity == null || facesContext.maximumSeverity.ordinal == 1}) {#{rich:component('forgottenPasswordPanel')}.hide();}" />

        </rich:modalPanel>
    </a4j:form>

</ui:composition>
