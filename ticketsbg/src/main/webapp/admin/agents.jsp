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
            <rich:panel header="#{msg.agents}">
                <h:form id="agentForm">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{agentController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show()" />
                    <a4j:region>
                        <rich:dataTable headerClass="dr-pnl-h"
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0"
                            var="agent" value="#{agentController.agentsModel}"
                            id="agentsTable">

                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.id}" />
                                </f:facet>
                                <h:outputText value="#{agent.agentId}" />
                            </rich:column>

                            <rich:column sortBy="#{agent.name}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.agentName}" />
                                </f:facet>
                                <h:outputText value="#{agent.name}" />
                            </rich:column>

                            <rich:column width="35">
                                <a4j:commandLink title="#{msg.edit}" ajaxSingle="true"
                                    oncomplete="#{rich:component('entityPanel')}.show()">
                                    <h:graphicImage value="/images/edit.png"
                                        style="width:16; height:16; border-style: none;"
                                        alt="#{msg.edit}" title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="${agent}"
                                        target="#{agentController.agent}" />
                                </a4j:commandLink>
                                <h:outputText value="&#160;" />
                                <h:commandLink action="#{agentController.delete}"
                                    title="#{msg.remove}">
                                    <h:graphicImage value="/images/delete.png"
                                        style="width:16; height:16; border-style: none;"
                                        alt="#{msg.delete}" title="#{msg.delete}" />
                                </h:commandLink>
                            </rich:column>
                        </rich:dataTable>
                    </a4j:region>
                </h:form>
            </rich:panel>
        </f:view>

        <rich:modalPanel id="entityPanel" autosized="true" width="350"
            height="120" moveable="true" resizeable="false">
            <f:facet name="controls">
                <h:panelGroup>
                    <h:graphicImage value="/images/close.png" styleClass="hidelink"
                        id="hidelink" onclick="#{rich:component('entityPanel')}.hide()" />
                    <rich:componentControl for="entityPanel" attachTo="hidelink"
                        operation="hide" event="onclick" />
                </h:panelGroup>
            </f:facet>
            <f:facet name="header">
                <h:outputText value="#{msg.agent}" />
            </f:facet>
            <a4j:include viewId="agent.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>