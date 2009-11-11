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
    template="adminTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <rich:panel header="#{msg.promotions}">
                <h:form id="promotionForm">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{promotionController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show()" />
                    <a4j:region>
                        <rich:dataTable
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0"
                            var="promotion" value="#{promotionController.promotionsModel}"
                            id="promotionsTable">

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.id}" />
                                </f:facet>
                                <h:outputText value="#{promotion.promotionId}" />
                            </rich:column>

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.promotionShortText}" />
                                </f:facet>
                                <h:outputText value="#{promotion.shortText}" />
                            </rich:column>

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.startDate}" />
                                </f:facet>
                                <h:outputText value="#{promotion.startDate.time}">
                                    <f:convertDateTime pattern="dd.MM.yyyy"
                                        timeZone="#{timeZoneController.timeZone}" />
                                </h:outputText>
                            </rich:column>

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.endDate}" />
                                </f:facet>
                                <h:outputText value="#{promotion.endDate.time}">
                                    <f:convertDateTime pattern="dd.MM.yyyy"
                                        timeZone="#{timeZoneController.timeZone}" />
                                </h:outputText>
                            </rich:column>

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.sendToEmail}" />
                                </f:facet>
                                <h:outputText value="#{msg.yes}"
                                    rendered="#{promotion.sendToEmail}" />
                                <h:outputText value="#{msg.no}"
                                    rendered="#{!promotion.sendToEmail}" />
                            </rich:column>

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.showInSite}" />
                                </f:facet>
                                <h:outputText value="#{msg.yes}"
                                    rendered="#{promotion.showInSite}" />
                                <h:outputText value="#{msg.no}"
                                    rendered="#{!promotion.showInSite}" />
                            </rich:column>

                            <rich:column>
                                <a4j:commandLink title="#{msg.edit}" ajaxSingle="true"
                                    oncomplete="#{rich:component('entityPanel')}.show()">
                                    <h:graphicImage value="/images/edit.png"
                                        style="border-style: none;" alt="#{msg.edit}"
                                        title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="${promotion}"
                                        target="#{promotionController.promotion}" />
                                </a4j:commandLink>
                                <h:outputText value="&#160;" />
                                <h:commandLink action="#{promotionController.delete}"
                                    title="#{msg.remove}">
                                    <h:graphicImage value="/images/delete.png"
                                        style="border-style: none;" alt="#{msg.delete}"
                                        title="#{msg.delete}" />
                                </h:commandLink>
                            </rich:column>
                        </rich:dataTable>
                    </a4j:region>
                </h:form>
            </rich:panel>
        </f:view>

        <rich:modalPanel id="entityPanel" autosized="true" width="350"
            height="120" moveable="true" resizeable="false"
            domElementAttachment="parent" style="overflow: hidden;">
            <ui:include src="/modalPanelCommons.jsp">
                <ui:param name="dialogId" value="entityPanel" />
            </ui:include>
            <f:facet name="header">
                <h:outputText value="#{msg.promotion}" />
            </f:facet>
            <a4j:include viewId="promotion.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>