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
            <h:form id="userForm">
                <rich:panel header="#{msg.users}"
                    headerClass="rich-panel-header-main">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{userController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show();" />
                    <a4j:region>
                        <rich:dataTable headerClass="dr-pnl-h"
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0" var="user"
                            value="#{userController.usersModel}" id="usersTable" columnClasses="tableColumn">

                            <rich:column sortBy="#{user.id}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.id}" />
                                </f:facet>
                                <h:outputText value="#{user.id}" />
                            </rich:column>
                            <rich:column sortBy="#{usr.username}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.username}" />
                                </f:facet>
                                <h:outputText value="#{user.username}" />
                            </rich:column>
                            <rich:column sortBy="#{user.name}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.names}" />
                                </f:facet>
                                <h:outputText value="#{user.name}" />
                            </rich:column>
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.contactPhone}" />
                                </f:facet>
                                <h:outputText value="#{user.contactPhone}" />
                            </rich:column>
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="#{msg.email}" />
                                </f:facet>
                                <h:outputText value="#{user.email}" />
                            </rich:column>
                            <rich:column sortBy="#{user.firm.name}"
                                rendered="#{loggedUserHolder.loggedUser.administrator}">
                                <f:facet name="header">
                                    <h:outputText value="#{msg.firm}" />
                                </f:facet>
                                <h:outputText value="#{user.firm.name}" />
                            </rich:column>

                            <rich:column>
                                <a4j:commandLink ajaxSingle="true" id="editlink"
                                    oncomplete="#{rich:component('entityPanel')}.show()"
                                    title="#{msg.edit}">
                                    <h:graphicImage value="/images/edit.png"
                                        style="border-style: none;"
                                        alt="#{msg.edit}" title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="#{user}"
                                        target="#{userController.user}" />
                                </a4j:commandLink>

                                <h:outputText value="&#160;" />

                                <h:commandLink action="#{userController.delete}"
                                    title="#{msg.remove}">
                                    <h:graphicImage value="/images/delete.png"
                                        style="border-style: none;"
                                        alt="#{msg.delete}" title="#{msg.delete}" />
                                </h:commandLink>
                            </rich:column>
                        </rich:dataTable>

                        <a4j:outputPanel
                            rendered="#{tc:getSize(userController.agentsUsersModel.wrappedData) > 0}">
                            <rich:separator style="margin-top: 20px; margin-bottom: 15px;" />
                            <h:outputText value="#{msg.agentsUsers}"
                                style="font-size: 13px; " />

                            <rich:dataTable headerClass="dr-pnl-h"
                                onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                onRowMouseOut="this.style.backgroundColor='white'"
                                cellpadding="0" cellspacing="0" width="700" border="0"
                                var="agentUser" value="#{userController.agentsUsersModel}"
                                id="agentsUsersTable">

                                <rich:column sortBy="#{agentUser.id}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.id}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.id}" />
                                </rich:column>
                                <rich:column sortBy="#{usr.username}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.username}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.username}" />
                                </rich:column>
                                <rich:column sortBy="#{agentUser.name}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.names}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.name}" />
                                </rich:column>
                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.contactPhone}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.contactPhone}" />
                                </rich:column>
                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.email}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.email}" />
                                </rich:column>
                                <rich:column sortBy="#{agentUser.agent.name}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.agentNameTitle}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.agent.name}" />
                                </rich:column>
                                <rich:column sortBy="#{agentUser.firm.name}"
                                    rendered="#{loggedUserHolder.loggedUser.administrator}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.firm}" />
                                    </f:facet>
                                    <h:outputText value="#{agentUser.firm.name}" />
                                </rich:column>

                                <rich:column>
                                    <a4j:commandLink ajaxSingle="true" id="editlink"
                                        oncomplete="#{rich:component('entityPanel')}.show()"
                                        title="#{msg.edit}">
                                        <h:graphicImage value="/images/edit.png"
                                            style="width:16; height:16; border-style: none;"
                                            alt="#{msg.edit}" title="#{msg.edit}" />
                                        <f:setPropertyActionListener value="#{agentUser}"
                                            target="#{userController.user}" />
                                    </a4j:commandLink>

                                    <h:outputText value="&#160;" />

                                    <h:commandLink action="#{userController.deleteAgentUser}"
                                        title="#{msg.remove}">
                                        <h:graphicImage value="/images/delete.png"
                                            style="width:16; height:16; border-style: none;"
                                            alt="#{msg.delete}" title="#{msg.delete}" />
                                    </h:commandLink>
                                </rich:column>
                            </rich:dataTable>
                        </a4j:outputPanel>
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
                <h:outputText value="#{msg.user}" />
            </f:facet>
            <a4j:include viewId="user.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>