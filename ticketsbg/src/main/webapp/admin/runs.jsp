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
            <a4j:form id="runsForm">
                <h:messages />
                <h:panelGroup>
                    <h:outputText value="#{msg.runsForRoute} " />
                    <h:outputText value="#{runController.route.name}"
                        style="font-weight: bold;" />
                </h:panelGroup>
                <br />
                <a4j:commandButton value="#{msg.addRun}"
                    action="#{runController.newRun}" reRender="newRunHolder"
                    oncomplete="#{rich:component('newRunPanel')}.show()" />

                <rich:dataTable
                    onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                    id="runsTable" onRowMouseOut="this.style.backgroundColor='white'"
                    cellpadding="0" cellspacing="0" width="300" border="0" var="run"
                    value="#{runController.route.runs}" rows="20">

                    <f:facet name="header">
                        <rich:columnGroup>
                            <rich:column>
                                <h:outputText value="#" />
                            </rich:column>
                            <rich:column>
                                <h:outputText value="#{msg.dateTime}" />
                            </rich:column>
                            <rich:column width="35" />
                        </rich:columnGroup>
                    </f:facet>

                    <rich:column>
                        <h:outputText value="#{run.runId}" />
                    </rich:column>

                    <rich:column>
                        <h:outputText value="#{run.time.time}">
                            <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                                timeZone="#{timeZoneController.timeZone}" />
                        </h:outputText>
                    </rich:column>

                    <rich:column>
                        <h:commandLink action="#{runController.delete}"
                            title="#{msg.remove}">
                            <f:setPropertyActionListener value="#{run}"
                                target="#{runController.run}" />
                            <h:graphicImage value="/images/delete.png"
                                style="width:16; height:16; border-style: none;"
                                alt="#{msg.remove}" title="#{msg.remove}" />
                        </h:commandLink>
                        <h:commandLink action="#{travelListController.openTravelList}"
                            title="#{msg.travelList}">
                            <f:setPropertyActionListener value="#{run}"
                                target="#{travelListController.run}" />
                            <h:graphicImage value="/images/runs.png"
                                style="width:16; height:16; border-style: none;"
                                alt="#{msg.seatsTravelList}" title="#{msg.travelList}" />
                        </h:commandLink>
                        <h:commandLink title="#{msg.setSeatsExceeded}"
                            action="#{runController.setSeatsExceeded}"
                            rendered="#{loggedUserHolder.loggedUser.firm.hasAnotherTicketSellingSystem and !run.seatsExceeded}">
                            <f:setPropertyActionListener value="#{run}"
                                target="#{runController.run}" />
                            <h:graphicImage value="/images/close.png"
                                style="width:16; height:16; border-style: none;"
                                alt="#{msg.seatsExceeded}" title="#{msg.seatsExceeded}" />
                        </h:commandLink>

                        <h:commandLink title="#{msg.undoSeatsExceeded}"
                            action="#{runController.undoSeatsExceeded}"
                            rendered="#{loggedUserHolder.loggedUser.firm.hasAnotherTicketSellingSystem and run.seatsExceeded}">

                            <f:setPropertyActionListener value="#{run}"
                                target="#{runController.run}" />
                            <h:graphicImage value="/images/undo.png"
                                style="width:16; height:16; border-style: none;"
                                alt="#{msg.undoSeatsExceeded}" title="#{msg.undoSeatsExceeded}" />
                        </h:commandLink>
                    </rich:column>
                    <f:facet name="footer">
                        <rich:datascroller align="center" maxPages="20"
                            page="#{runController.page}" id="scroller" />
                    </f:facet>
                </rich:dataTable>
            </a4j:form>

            <a4j:form id="newRunHolder" ajaxSubmit="true">
                    <rich:modalPanel id="newRunPanel" autosized="true" moveable="true"
                        resizeable="false" style="overflow: hidden;" width="220"
                        rendered="#{runController.run != null}" domElementAttachment="form">
                        <ui:include src="/modalPanelCommons.jsp">
                            <ui:param name="dialogId" value="newRunPanel" />
                        </ui:include>
                        <f:facet name="header">
                            <h:outputText value="#{msg.addRun}" />
                        </f:facet>
                        <rich:messages ajaxRendered="true" />
                        <rich:calendar id="singleRunDateTime"
                            datePattern="dd.MM.yyyy HH:mm" firstWeekDay="1" inputSize="16"
                            value="#{runController.run.time.time}" showApplyButton="true"
                            direction="top-left">
                        </rich:calendar>

                        <a4j:commandButton value="#{msg.add}"
                            oncomplete="#{rich:component('newRunPanel')}.hide();"
                            action="#{runController.addRun}" reRender="runsTable" />


                    </rich:modalPanel>
            </a4j:form>

        </f:view>
    </ui:define>
</ui:composition>