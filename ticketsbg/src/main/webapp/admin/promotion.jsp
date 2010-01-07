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
        <h:form id="promotionForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2" styleClass="gridContent">
                    <h:outputLabel for="shortText" value="#{msg.promotionShortText}: " />
                    <h:inputText value="#{promotionController.promotion.shortText}"
                        id="shortText" size="30" />

                    <h:outputLabel for="startDate" value="#{msg.startDate}: " />
                    <rich:calendar value="#{promotionController.promotion.start.time}"
                        id="startDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        boundaryDatesMode="scroll" />

                    <h:outputLabel for="endDate" value="#{msg.endDate}: " />
                    <rich:calendar value="#{promotionController.promotion.end.time}"
                        id="endDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        boundaryDatesMode="scroll" />

                    <h:outputLabel for="sendToEmail" value="#{msg.sendToEmail}: " />
                    <h:selectBooleanCheckbox id="sendToEmail"
                        value="#{promotionController.promotion.sendToEmail}" />

                    <h:outputLabel for="showInSite" value="#{msg.showInSite}: " />
                    <h:selectBooleanCheckbox id="showInSite"
                        value="#{promotionController.promotion.showInSite}" />

                    <rich:componentControl attachTo="richTextOpener" event="onclick"
                        for="richTextPanel" operation="show" />
                    <h:outputText id="richTextOpener" styleClass="link"
                        style="color: blue; text-decoration: underline;"
                        value="#{msg.description}" />

                    <h:outputText value="" />
                    <a4j:commandButton value="${msg.save}"
                        action="#{promotionController.save}"
                        oncomplete="if (#{rich:component('entityPanel')} != null) {#{rich:component('entityPanel')}.hide();} #{rich:component('entityPanel')}.hide();"
                        reRender="promotionsTable" />
                </h:panelGrid>
            </a4j:outputPanel>

            <rich:modalPanel id="richTextPanel" autosized="true" width="200"
                height="320" moveable="true" resizeable="false"
                onmaskclick="#{rich:component('richTextPanel')}.hide()"
                domElementAttachment="parent">
                <ui:include src="/modalPanelCommons.jsp">
                    <ui:param name="dialogId" value="richTextPanel" />
                </ui:include>
                <f:facet name="header">
                    <h:outputText value="#{msg.description}" />
                </f:facet>
                <h:panelGroup>
                    <rich:editor readonly="false" viewMode="visual" height="280"
                        value="#{promotionController.promotion.richText}" theme="advanced">
                        <f:param name="theme_advanced_toolbar_location" value="top" />
                        <f:param name="theme_advanced_toolbar_align" value="left" />
                    </rich:editor>
                    <a4j:commandButton value="OK"
                        onclick="#{rich:component('richTextPanel')}.hide()" />
                </h:panelGroup>
            </rich:modalPanel>
        </h:form>

    </f:view>
</ui:composition>