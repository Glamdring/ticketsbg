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
    xmlns:tc="http://tickets.com/tc" template="adminTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <h:form id="officeForm">
                <rich:panel header="#{msg.offices}"
                    headerClass="rich-panel-header-main">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{officeController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show();" />
                    <a4j:region>
                        <rich:dataTable headerClass="dr-pnl-h"
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0" var="office"
                            value="#{officeController.officesModel}" id="officesTable" columnClasses="tableColumn">

                            <rich:column sortBy="#{office.officeId}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.id}" />
                                </f:facet>
                                <h:outputText value="#{office.officeId}" />
                            </rich:column>
                            <rich:column sortBy="#{office.name}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.name}" />
                                </f:facet>
                                <h:outputText value="#{office.name}" />
                            </rich:column>
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.contactPhone}" />
                                </f:facet>
                                <h:outputText value="#{office.contactPhone}" />
                            </rich:column>
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.email}" />
                                </f:facet>
                                <h:outputText value="#{office.email}" />
                            </rich:column>
                            <rich:column sortBy="#{office.firm.name}"
                                rendered="#{loggedUserHolder.loggedUser.administrator}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.firm}" />
                                </f:facet>
                                <h:outputText value="#{office.firm.name}" />
                            </rich:column>

                            <rich:column>
                                <a4j:commandLink ajaxSingle="true" id="editlink"
                                    oncomplete="#{rich:component('entityPanel')}.show()"
                                    title="#{msg.edit}">
                                    <h:graphicImage value="/images/edit.png"
                                        style="border-style: none;"
                                        alt="#{msg.edit}" title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="#{office}"
                                        target="#{officeController.office}" />
                                </a4j:commandLink>

                                <h:outputText value="&#160;" />

                                <h:commandLink action="#{officeController.delete}"
                                    title="#{msg.remove}">
                                    <h:graphicImage value="/images/delete.png"
                                        style="border-style: none;"
                                        alt="#{msg.delete}" title="#{msg.delete}" />
                                </h:commandLink>
                            </rich:column>
                        </rich:dataTable>

                    </a4j:region>
                </rich:panel>
            </h:form>
        </f:view>

        <rich:modalPanel id="entityPanel" autosized="true" width="300"
            height="120" moveable="true" resizeable="false" style="overflow: hidden;">
            <ui:include src="/modalPanelCommons.jsp">
                <ui:param name="dialogId" value="entityPanel" />
            </ui:include>
            <f:facet name="header">
                <h:outputText value="#{msg.office}" />
            </f:facet>
            <a4j:include viewId="office.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>