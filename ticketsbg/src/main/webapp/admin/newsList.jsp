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
            <rich:panel header="#{msg.newsList}">
                <h:form id="newsForm">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{newsController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show()" />
                    <a4j:region>
                        <rich:dataTable
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0"
                            var="news" value="#{newsController.newsListModel}"
                            id="newsListTable">

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.id}" />
                                </f:facet>
                                <h:outputText value="#{news.newsId}" />
                            </rich:column>


                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.headline}" />
                                </f:facet>
                                <h:outputText value="#{news.headline}" />
                            </rich:column>

                            <rich:column sortBy="#{news.date.time}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.date}" />
                                </f:facet>
                                <h:outputText value="#{news.date.time}">
                                    <f:convertDateTime pattern="dd.MM.yyyy"
                                        timeZone="#{timeZoneController.timeZone}" />
                                </h:outputText>
                            </rich:column>

                            <rich:column>
                                <a4j:commandLink title="#{msg.edit}" ajaxSingle="true"
                                    oncomplete="#{rich:component('entityPanel')}.show()">
                                    <h:graphicImage value="/images/edit.png"
                                        style="border-style: none;" alt="#{msg.edit}"
                                        title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="${news}"
                                        target="#{newsController.news}" />
                                </a4j:commandLink>
                                <h:outputText value="&#160;" />
                                <h:commandLink action="#{newsController.delete}"
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
                <h:outputText value="#{msg.news}" />
            </f:facet>
            <a4j:include viewId="news.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>