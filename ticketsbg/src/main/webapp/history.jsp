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
    template="publicTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <a4j:form id="historyForm">
                <rich:panel header="#{msg.travelHistory}"
                    headerClass="rich-panel-header-main">

                    <rich:dataTable headerClass="dr-pnl-h" rows="10"
                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                        onRowMouseOut="this.style.backgroundColor='white'" cellpadding="0"
                        cellspacing="0" width="900" border="0" var="ticket"
                        value="#{historyController.tickets}" id="historyTable">

                        <rich:column>
                            <f:facet name="header">
                                #{msg.ticketCode}
                             </f:facet>
                            #{ticket.ticketCode}
                        </rich:column>

                        <rich:column>
                            <f:facet name="header">
                                #{msg.startStop}
                            </f:facet>
                            #{ticket.startStop}
                        </rich:column>

                        <rich:column>
                            <f:facet name="header">
                                #{msg.endStop}
                            </f:facet>
                            #{ticket.endStop}
                        </rich:column>

                        <rich:column sortBy="#{ticket.run.time.time}">
                            <f:facet name="header">
                                #{msg.travelTime}
                            </f:facet>
                            <h:outputText value="#{ticket.run.time.time}">
                                <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                            </rich:column>

                        <rich:column sortBy="#{ticket.creationTime.time}">
                            <f:facet name="header">
                                #{msg.purchaseTime}
                            </f:facet>
                            <h:outputText value="#{ticket.creationTime.time}">
                                <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                            </rich:column>
                            <f:facet name="footer">
                                <rich:datascroller />
                            </f:facet>
                    </rich:dataTable>
                </rich:panel>
            </a4j:form>
        </f:view>
    </ui:define>
</ui:composition>