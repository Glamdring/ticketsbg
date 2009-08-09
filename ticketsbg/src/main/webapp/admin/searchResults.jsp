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
    text-align: left;
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
                            <h:outputText value="#{msg.allStops}"
                                rendered="#{searchController.toStop == null}" styleClass="bold" />

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


                    <h:panelGroup id="resultsPanel"
                        style="border-style: none; width: 100%;">
                        <rich:panel style="width: 100%">
                            <rich:dataTable value="#{searchController.resultsModel}"
                                var="result" id="resultsTable" headerClass="tableHeader"
                                width="100%" columnClasses="columnClass" border="0"
                                style="border-style: none;">

                                <a4j:support event="onselectionchange"
                                    reRender="selectedEntry,returnResultsTable,ticketCounts,seatChoices"
                                    eventsQueue="selectionSubmit"
                                    action="#{searchController.rowSelectionChanged}" />


                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.id}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.runId}" />
                                </rich:column>


                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.date}" />
                                    </f:facet>
                                    <h:outputText value="#{result.run.time.time}">
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
                                    <h:outputText value="#{result.run.time.time}">
                                        <f:convertDateTime pattern="hh:mm" />
                                    </h:outputText> #{msg.hourAbbr}
                                </rich:column>

                                <rich:column sortable="true">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.soldSeats}" />
                                    </f:facet>
                                    <h:outputText value="#{tc:getSize(result.run.tickets)}" />
                                </rich:column>

                                <rich:column sortable="true">
                                    <a4j:commandButton value="#{msg.oneWayTicket}"
                                        ajaxSingle="true" disabled="#{result.run.seatsExceeded}"
                                        oncomplete="#{rich:component('purchasePanel')}.show()"
                                        action="#{searchController.purchaseOneWayTicket}"
                                        reRender="purchasePanel">
                                        <f:setPropertyActionListener value="${result}"
                                            target="#{searchController.selectedEntry}" />
                                    </a4j:commandButton>

                                    <h:outputText value="&#160;" />

                                    <a4j:commandButton value="#{msg.twoWayTicket}"
                                        ajaxSingle="true" disabled="#{result.run.seatsExceeded}"
                                        oncomplete="#{rich:component('purchasePanel')}.show()"
                                        action="#{searchController.purchaseTwoWayTicket}"
                                        reRender="purchasePanel">
                                        <f:setPropertyActionListener value="${result}"
                                            target="#{searchController.selectedEntry}" />
                                    </a4j:commandButton>

                                    <h:outputText value="&#160;" />

                                    <h:commandLink action="#{travelListController.openTravelList}"
                                        title="#{msg.travelList}">
                                        <f:setPropertyActionListener value="#{result.run}"
                                            target="#{travelListController.run}" />
                                        <h:graphicImage value="/images/runs.png"
                                            style="width:16; height:16; border-style: none; vertical-align: middle;"
                                            alt="#{msg.travelList}" title="#{msg.travelList}" />
                                    </h:commandLink>

                                    <h:outputText value="&#160;" />

                                    <h:commandLink title="#{msg.setSeatsExceeded}"
                                        action="#{runController.setSeatsExceeded}"
                                        rendered="#{loggedUserHolder.loggedUser.firm.hasAnotherTicketSellingSystem and !result.run.seatsExceeded}">
                                        <f:setPropertyActionListener value="#{result.run}"
                                            target="#{runController.run}" />
                                        <h:graphicImage value="/images/close.png"
                                            style="width:16; height:16; border-style: none; vertical-align: middle;"
                                            alt="#{msg.remove}" title="#{msg.remove}" />
                                    </h:commandLink>

                                    <h:commandLink title="#{msg.undoSeatsExceeded}"
                                        action="#{runController.undoSeatsExceeded}"
                                        rendered="#{loggedUserHolder.loggedUser.firm.hasAnotherTicketSellingSystem and result.run.seatsExceeded}">

                                        <f:setPropertyActionListener value="#{result.run}"
                                            target="#{runController.run}" />
                                        <h:graphicImage value="/images/undo.png"
                                            style="width:16; height:16; border-style: none; vertical-align: middle;"
                                            alt="#{msg.remove}" title="#{msg.remove}" />
                                    </h:commandLink>


                                </rich:column>
                            </rich:dataTable>
                        </rich:panel>
                    </h:panelGroup>
                </rich:panel>
            </a4j:form>

            <!-- Ticket counts & discounts -->
            <style type="text/css">
.firstTicketColumn {
    width: 340px;
}

.secondTicketColumn {
    width: 40px;
}
</style>

            <rich:modalPanel id="purchasePanel" autosized="true" width="200"
                height="120" moveable="true" resizeable="false">
                <f:facet name="controls">
                    <h:panelGroup>
                        <h:graphicImage value="/images/close.png" styleClass="hidelink"
                            id="hidelink" onclick="#{rich:component('purchasePanel')}.hide()" />
                        <rich:componentControl for="purchasePanel" attachTo="hidelink"
                            operation="hide" event="onclick" />
                    </h:panelGroup>
                </f:facet>
                <f:facet name="header">
                    <h:outputText
                        value="#{searchController.travelType == 'TWO_WAY' ? msg.twoWayTicket : msg.oneWayTicket}" />
                </f:facet>
                <a4j:form>
                    <a4j:outputPanel ajaxRendered="true">
                        <h:panelGrid columns="2">
                            <h:outputLabel for="purchaseStartStop" value="#{msg.fromStop}" />
                            <h:inputText value="#{searchController.fromStop}"
                                id="purchaseStartStop" readonly="true" />

                            <h:outputLabel for="purchaseStartStop" value="#{msg.toStop}" />
                            <rich:comboBox
                                suggestionValues="#{searchController.currentAvailableTargetStopNames}"
                                value="#{searchController.toStopPerPurchase}">
                                <a4j:support event="onselect"
                                    action="#{searchController.toStopSelected}"
                                    reRender="returnDate,oneWayPrice" />
                            </rich:comboBox>
                        </h:panelGrid>

                        <h:panelGroup id="oneWayPrice">
                            <h:outputText value="#{msg.oneWayPrice}: " />

                            <h:outputText
                                value="#{searchController.selectedEntry.price.price}"
                                styleClass="bold"
                                rendered="#{searchController.toStopPerPurchase != null}">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>
                        </h:panelGroup>

                        <br />

                        <rich:panel id="returnsPanel" header="#{msg.returnHeaderLabel}"
                            rendered="#{searchController.travelType == 'TWO_WAY'}">

                            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                                <h:outputLabel value="#{msg.returnDate}:" for="returnDate" />
                                <rich:calendar id="returnDate" datePattern="dd.MM.yyyy"
                                    firstWeekDay="1" value="#{searchController.returnDate}"
                                    disabled="#{searchController.toStopPerPurchase == null}">
                                    <a4j:support event="onchanged"
                                        action="#{searchController.returnDateSelected}"
                                        reRender="returnsTable" />
                                </rich:calendar>
                            </h:panelGrid>

                            <rich:dataTable id="returnsTable"
                                value="#{searchController.returnResultsModel}"
                                var="returnResult">

                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.departureTime}" />
                                    </f:facet>
                                    <h:outputText value="#{returnResult.departureTime.time}">
                                        <f:convertDateTime pattern="HH:mm"
                                            timeZone="#{timeZoneController.timeZone}" />
                                    </h:outputText> #{msg.hourAbbr}
                                </rich:column>

                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.arrivalTime}" />
                                    </f:facet>
                                    <h:outputText value="#{returnResult.arrivalTime.time}">
                                        <f:convertDateTime pattern="HH:mm"
                                            timeZone="#{timeZoneController.timeZone}" />
                                    </h:outputText> #{msg.hourAbbr}
                                </rich:column>

                                <rich:column
                                    rendered="#{loggedUserHolder.loggedUser.agent != null}">
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.firm}" />
                                    </f:facet>
                                    <h:outputText value="#{returnResult.run.route.firm.name}" />
                                </rich:column>

                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="#{msg.twoWayPrice}" />
                                    </f:facet>
                                    <h:outputText value="#{returnResult.price.twoWayPrice}">
                                        <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                        <f:converter binding="#{currencyConverter}"
                                            converterId="currencyConverter" />
                                    </h:outputText>
                                </rich:column>

                                <rich:column>
                                    <a4j:commandButton value="#{msg.select}"
                                        reRender="selectedReturnChoice">
                                        <f:setPropertyActionListener value="#{returnResult}"
                                            target="#{searchController.selectedReturnEntry}" />
                                    </a4j:commandButton>
                                </rich:column>
                            </rich:dataTable>
                            <h:panelGroup id="returnChoice" style="font-weight: bold;">
                                <h:panelGroup
                                    rendered="#{searchController.selectedReturnEntry != null}">
                                    <h:outputText
                                        value="#{searchController.selectedReturnEntry.departureTime.time}">
                                        <f:convertDateTime pattern="HH:mm"
                                            timeZone="#{timeZoneController.timeZone}" />
                                    </h:outputText> #{msg.hourAbbr} - <h:outputText
                                        value="#{searchController.selectedReturnEntry.price.twoWayPrice}">

                                        <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                        <f:converter binding="#{currencyConverter}"
                                            converterId="currencyConverter" />
                                    </h:outputText>
                                </h:panelGroup>
                            </h:panelGroup>
                        </rich:panel>

                        <h:panelGroup id="seatChoices">
                            <a4j:region rendered="#{seatController.seatHandler != null}">
                                <a4j:include viewId="../seats.jsp">
                                    <ui:param name="modifier" value="1" />
                                    <ui:param name="return" value="false" />
                                </a4j:include>
                            </a4j:region>
                        </h:panelGroup>

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
                        </rich:panel>

                        <a4j:commandButton
                            oncomplete="#{rich:component('purchasePanel')}.hide()"
                            action="#{searchController.proceed}" value="#{msg.markPurchased}"
                            reRender="resultsPanel" />

                    </a4j:outputPanel>
                </a4j:form>
            </rich:modalPanel>

        </f:view>
    </ui:define>
</ui:composition>