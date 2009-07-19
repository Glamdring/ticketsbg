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

    <ui:define name="head">
        <style type="text/css">
.tableHeader {
    border-style: none;
    background-color: white;
}

.columnClass {
    border-style: none;
    padding: 0px;
    margin: 0px;
}

.gridContent {
    vertical-align: top;
}
</style>
    </ui:define>
    <ui:define name="body">
        <f:view>
            <a4j:form id="searchResults">
                <rich:panel headerClass="rich-panel-header-main">
                    <f:facet name="header">
                        <h:panelGroup style="font-weight: normal;">
                            <h:outputText value="#{msg.searchResultsFrom} " />
                            <h:outputText value="#{searchController.fromStop}"
                                styleClass="bold" />
                            <h:outputText value=" #{msg.searchResultsTo} " />
                            <h:outputText value="#{searchController.toStop}"
                                styleClass="bold" />
                                        (<h:outputText
                                value="#{searchController.date}">
                                <f:convertDateTime type="date" pattern="dd.MM.yyyy"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>)
                        </h:panelGroup>
                    </f:facet>

                    <rich:panel>
                        <ui:include src="../searchFields.jsp">
                            <ui:param name="isAdmin" value="true" />
                        </ui:include>
                        <h:inputHidden id="admin" binding="#{searchController.admin}"
                            value="true" converter="#{booleanConverter}" />
                    </rich:panel>

                    <rich:messages errorClass="error" ajaxRendered="true" />
                    <!-- show this button below the messages only if the message is
                    asking the question whether tickets are to be cancelled -->
                    <a4j:outputPanel rendered="#{searchController.proposeCancellation}">
                        <h:commandButton action="#{searchController.cancelTickets}"
                            value="#{msg.yes}" />
                        <h:commandButton action="#{searchController.cancelTickets}"
                            value="#{msg.noContinue}"
                            rendered="#{searchController.confirmPartialPurchase}" />
                    </a4j:outputPanel>

                    <h:panelGrid columns="2" columnClasses="gridContent,gridContent"
                        cellpadding="0" cellspacing="0">
                        <h:panelGroup id="oneWay" style="border-style: none;">
                            <rich:dataTable value="#{searchController.resultsModel}"
                                var="result" id="resultsTable" headerClass="tableHeader"
                                width="380px;" columnClasses="columnClass" border="0"
                                style="border-style: none;">

                                <a4j:support event="onselectionchange"
                                    reRender="selectedEntry,returnResultsTable,ticketCounts,seatChoices"
                                    eventsQueue="selectionSubmit"
                                    action="#{searchController.rowSelectionChanged}" />


                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.id}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.id}" />
                                </rich:column>


                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.date}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.time}">
                                        <f:convertDateTime pattern="dd.MM.yyyy" />
                                    </h:outputText>
                                </rich:column>

                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.routeName}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.route.name}" />
                                </rich:column>

                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.departureTime}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.time}">
                                        <f:convertDateTime pattern="hh:mm" />
                                    </h:outputText>
                                </rich:column>

                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.soldSeats}" />
                                    </f:facet>
                                    <h:outputText value="#{tc:getSize(result.run.tickets)}" />
                                </rich:column>

                                <rich:column sortable="true">
                                    <a4j:commandButton value="#{msg.oneWay}" />
                                    <h:outputText value="&#160;" />
                                    <a4j:commandButton value="#{msg.twoWay}" />
                                </rich:column>
                            </rich:dataTable>
                        </h:panelGroup>

                        <!-- Returns -->
                        <h:panelGroup id="return" style="border-style: none;">
                            <a4j:region>
                                <rich:extendedDataTable
                                    height="#{searchController.returnResultsModel.rowCount * 119 + 30}"
                                    value="#{searchController.returnResultsModel}" var="result"
                                    rowKeyVar="rowId" selectionMode="single"
                                    enableContextMenu="false" id="returnResultsTable"
                                    selection="#{searchController.returnSelection}"
                                    headerClass="tableHeader" noDataLabel="#{msg.noSearchResults}"
                                    width="380px;" columnClasses="columnClass"
                                    rendered="#{searchController.returnResultsModel != null}"
                                    style="border-style: none;" border="0">

                                    <a4j:support event="onselectionchange"
                                        reRender="selectedReturnEntry,ticketCounts,returnSeatChoices"
                                        eventsQueue="returnSelectionSubmit"
                                        action="#{searchController.returnRowSelectionChanged}" />

                                    <rich:column width="35px" sortable="false">
                                        <!-- For presentational purposes only -->
                                        <t:selectOneRow groupName="selectedReturnEntry"
                                            id="selectedReturnEntry"
                                            value="#{searchController.selectedReturnRowId}">
                                            <!-- Dummy converter; doesn't work with no converter -->
                                            <f:converter converterId="javax.faces.Integer" />
                                        </t:selectOneRow>

                                    </rich:column>

                                    <rich:column sortable="false" width="345px"
                                        filterExpression="#{searchController.selectedEntry == null || result.run.route.firm.firmId == searchController.selectedEntry.run.route.firm.firmId}">
                                        <f:facet name="header">
                                            #{msg.returnHeaderLabel}
                                        </f:facet>

                                        <rich:panel id="resultEntry" header="#{result.run.route.name}"
                                            style="width: 100%;">
                                            <a4j:repeat value="#{result.run.route.stops}" var="stop"
                                                rowKeyVar="stopId">
                                                <h:outputText value=" → " rendered="#{stopId > 0}" />
                                                <h:outputText value="#{stop.name}" styleClass="bold"
                                                    rendered="#{searchController.fromStop == stop.name || searchController.toStop == stop.name}" />
                                                <h:outputText value="#{stop.name}"
                                                    rendered="#{searchController.fromStop != stop.name &amp;&amp; searchController.toStop != stop.name}" />
                                            </a4j:repeat>

                                            <h:panelGrid columns="2">

                                                <h:outputText value="#{msg.departureTime}: " />
                                                <h:outputText value="#{result.departureTime.time}"
                                                    styleClass="bold">
                                                    <f:convertDateTime type="time" pattern="HH:mm"
                                                        timeZone="#{timeZoneController.timeZone}" />
                                                </h:outputText>

                                                <h:outputText value="#{msg.arrivalTime}: " />
                                                <h:outputText value="#{result.arrivalTime.time}"
                                                    styleClass="bold">
                                                    <f:convertDateTime type="time" pattern="HH:mm"
                                                        timeZone="#{timeZoneController.timeZone}" />
                                                </h:outputText>

                                                <h:outputText value="#{msg.vacantSeats}: " />
                                                <h:outputText
                                                    value="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop)}"
                                                    style="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop)} &lt; 5 ? 'color: red;' : 'color: black;'};font-weight: bold;" />
                                            </h:panelGrid>
                                        </rich:panel>
                                    </rich:column>
                                </rich:extendedDataTable>
                            </a4j:region>
                        </h:panelGroup>

                        <h:panelGroup id="seatChoices">
                            <a4j:region rendered="#{seatController.seatHandler != null}">
                                <a4j:include viewId="../seats.jsp">
                                    <ui:param name="modifier" value="1" />
                                    <ui:param name="return" value="falase" />
                                </a4j:include>
                            </a4j:region>
                        </h:panelGroup>

                        <h:panelGroup id="returnSeatChoices">
                            <a4j:region
                                rendered="#{seatController.returnSeatHandler != null}">
                                <a4j:include viewId="../seats.jsp">
                                    <ui:param name="modifier" value="2" />
                                    <ui:param name="return" value="true" />
                                </a4j:include>
                            </a4j:region>
                        </h:panelGroup>
                    </h:panelGrid>
                    <!-- Ticket counts & discounts -->
                    <style type="text/css">
.firstTicketColumn {
    width: 340px;
}

.secondTicketColumn {
    width: 40px;
}
</style>
                    <rich:panel header="#{msg.tickets}" id="ticketCounts"
                        style="width: 380px; margin-top: 15px;">
                        <h:panelGrid columns="2"
                            columnClasses="firstTicketColumn,secondTicketColumn">
                            <h:panelGroup>
                                <h:outputText value="#{msg.regularTicket}" styleClass="bold" />
                                <br />
                                <!-- TODO per-firm setting with this description -->
                                <h:outputText value="#{msg.regularTicketDescription}" />
                            </h:panelGroup>

                            <rich:inputNumberSpinner
                                value="#{searchController.regularTicketsCount}" minValue="0"
                                maxValue="#{searchController.selectedEntry == null ? 50 : tc:getVacantSeats(searchController.selectedEntry.run, searchController.fromStop, searchController.toStop)}"
                                inputSize="3">
                                <a4j:support event="onchange" ajaxSingle="true" />
                            </rich:inputNumberSpinner>
                        </h:panelGrid>

                        <a4j:outputPanel
                            rendered="#{searchController.selectedEntry != null}">
                            <!-- TODO rendered="#{searchController.selectedEntry.run.route.firm.allowDiscounts}" -->
                            <a4j:repeat value="#{searchController.ticketCounts}" var="tc">

                                <h:panelGrid columns="2"
                                    columnClasses="firstTicketColumn,secondTicketColumn">
                                    <h:panelGroup>
                                        <h:outputText value="#{tc.discount.name}" styleClass="bold" />
                                        <br />
                                        <h:outputText value="#{tc.discount.description}" />
                                    </h:panelGroup>

                                    <rich:inputNumberSpinner value="#{tc.numberOfTickets}"
                                        minValue="0" inputSize="3"
                                        maxValue="#{tc:getVacantSeats(searchController.selectedEntry.run, searchController.fromStop, searchController.toStop)}">
                                        <a4j:support event="onchange" ajaxSingle="true" />
                                    </rich:inputNumberSpinner>
                                </h:panelGrid>
                            </a4j:repeat>
                        </a4j:outputPanel>
                    </rich:panel>


                    <h:panelGroup>
                        <h:commandButton value="#{msg.backToSearchScreen}"
                            action="#{searchController.toSearchScreen}" />
                        <h:outputText value=" " />
                        <h:commandButton action="#{searchController.proceed}"
                            value="#{msg.toPayment}" />
                    </h:panelGroup>
                </rich:panel>
            </a4j:form>
        </f:view>
    </ui:define>
</ui:composition>