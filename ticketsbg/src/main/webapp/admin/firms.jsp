<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk" template="adminTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <h:form id="firmForm">
                <h:messages />
                <a4j:commandLink value="#{msg.add}" ajaxSingle="true"
                    action="#{firmController.newRecord}"
                    oncomplete="#{rich:component('entityPanel')}.show()" />
                <a4j:region>
                    <rich:dataTable
                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                        onRowMouseOut="this.style.backgroundColor='white'" cellpadding="0"
                        cellspacing="0" width="700" border="0" var="firm"
                        value="#{firmController.firmsModel}" id="firmsTable">

                        <f:facet name="header">
                            <rich:columnGroup>
                                <rich:column>
                                    <h:outputText value="#{msg.id}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.firmName}" />
                                </rich:column>
                                <rich:column width="35" />
                            </rich:columnGroup>
                        </f:facet>

                        <rich:column>
                            <h:outputText value="#{firm.firmId}" />
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{firm.name}" />
                        </rich:column>

                        <rich:column>
                            <a4j:commandLink title="#{msg.edit}" ajaxSingle="true"
                                oncomplete="#{rich:component('entityPanel')}.show()">
                                <h:graphicImage value="/images/edit.png"
                                    style="width:16; height:16; border-style: none;"
                                    alt="#{msg.edit}" title="#{msg.edit}" />
                                <f:setPropertyActionListener value="${firm}"
                                    target="#{firmController.firm}" />
                            </a4j:commandLink>
                            <h:outputText value="&#160;" />
                            <h:commandLink action="#{firmController.delete}"
                                title="#{msg.remove}">
                                <h:graphicImage value="/images/delete.png"
                                    style="width:16; height:16; border-style: none;"
                                    alt="#{msg.delete}" title="#{msg.delete}" />
                            </h:commandLink>
                        </rich:column>
                    </rich:dataTable>
                </a4j:region>
            </h:form>
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
                <h:outputText value="#{msg.firm}" />
            </f:facet>
            <a4j:include viewId="firm.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>