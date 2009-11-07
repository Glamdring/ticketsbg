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
            <h:form id="routeForm">
                <rich:panel header="#{msg.routes}"
                    headerClass="rich-panel-header-main">
                    <h:messages />
                    <h:commandButton value="#{msg.add}"
                        action="#{routeController.newRoute}" />
                    <rich:dataTable headerClass="dr-pnl-h"
                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                        onRowMouseOut="this.style.backgroundColor='white'" cellpadding="0"
                        cellspacing="0" width="900" border="0" var="route"
                        value="#{routeController.routesModel}" columnClasses="tableColumn">

                        <rich:column sortBy="#{route.id}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.id}" />
                            </f:facet>
                            <h:outputText value="#{route.id}"/>
                        </rich:column>

                        <rich:column sortBy="#{route.name}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.name}" />
                            </f:facet>
                            <h:outputText value="#{route.name}" />
                        </rich:column>

                        <rich:column rendered="#{!route.singleRun}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.daysOfWeek}" />
                            </f:facet>

                            <a4j:repeat var="routeDay" value="#{route.routeDays}">
                                <h:outputText
                                    value="#{routeController.dayNames[routeDay.day.name]}" />
                                <h:outputText value="&#160;" />
                            </a4j:repeat>
                        </rich:column>

                        <rich:column rendered="#{route.singleRun}">
                            <f:facet name="header">
                                <h:outputText value="#{msg.daysOfWeek}" />
                            </f:facet>
                            <h:outputText value="#{route.singleRunDateTime.time}">
                                <f:convertDateTime pattern="dd.MM.yyyy HH:mm" />
                            </h:outputText>
                        </rich:column>

                        <rich:column width="75px">
                            <h:commandLink action="#{routeController.edit}"
                                title="#{msg.edit}">
                                <h:graphicImage value="/images/edit.png"
                                    style="border-style: none;"
                                    alt="#{msg.edit}" title="#{msg.edit}" />
                            </h:commandLink>

                            <h:outputText value="&#160;" />

                            <h:commandLink action="#{runController.view}" title="#{msg.runs}">
                                <f:setPropertyActionListener value="#{route}"
                                    target="#{runController.route}" />
                                <h:graphicImage value="/images/runs.png"
                                    style="border-style: none;"
                                    alt="#{msg.remove}" title="#{msg.runs}" />
                            </h:commandLink>

                            <h:outputText value="&#160;" />

                            <h:commandLink action="#{routeController.delete}"
                                title="#{msg.remove}">
                                <h:graphicImage value="/images/delete.png"
                                    style="border-style: none;"
                                    alt="#{msg.remove}" title="#{msg.remove}" />
                            </h:commandLink>
                        </rich:column>

                    </rich:dataTable>
                </rich:panel>
            </h:form>
        </f:view>
    </ui:define>
</ui:composition>