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
            <h:form id="genericDiscountsForm">
                <rich:panel header="#{msg.users}"
                    headerClass="rich-panel-header-main">
                    <h:messages />
                    <a4j:commandButton value="#{msg.add}" ajaxSingle="true"
                        action="#{discountController.newRecord}"
                        oncomplete="#{rich:component('entityPanel')}.show();" />
                    <a4j:region>
                        <rich:dataTable
                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'"
                            cellpadding="0" cellspacing="0" width="700" border="0"
                            var="discount" value="#{discountController.discountsModel}"
                            id="discountsTable" columnClasses="tableColumn">

                            <f:facet name="header">
                                <rich:columnGroup>
                                    <rich:column sortBy="#{discount.id}" sortable="true">
                                        <h:outputText value="#{msg.id}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.name}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.description}" />
                                    </rich:column>
                                    <rich:column width="50" />
                                </rich:columnGroup>
                            </f:facet>

                            <rich:column sortBy="#{discount.id}" sortable="true">
                                <h:outputText value="#{discount.id}" />
                            </rich:column>
                            <rich:column>
                                <h:outputText value="#{discount.name}" />
                            </rich:column>
                            <rich:column>
                                <h:outputText value="#{discount.description}" />
                            </rich:column>

                            <rich:column>
                                <a4j:commandLink ajaxSingle="true" id="editlink"
                                    oncomplete="#{rich:component('entityPanel')}.show()"
                                    title="#{msg.edit}">
                                    <h:graphicImage value="/images/edit.png"
                                        style="border-style: none;"
                                        alt="#{msg.edit}" title="#{msg.edit}" />
                                    <f:setPropertyActionListener value="#{discount}"
                                        target="#{discountController.discount}" />
                                </a4j:commandLink>

                                <h:outputText value="&#160;" />

                                <h:commandLink action="#{discountController.delete}"
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
            height="120" moveable="true" resizeable="false">
            <ui:include src="/modalPanelCommons.jsp">
                <ui:param name="dialogId" value="entityPanel" />
            </ui:include>
            <f:facet name="header">
                <h:outputText value="#{msg.addOrModifyDiscount}" />
            </f:facet>
            <a4j:include viewId="genericDiscount.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>