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
    xmlns:tc="http://tickets.com/tc" template="publicTemplate.jsp">

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

.mapLink {
    font-weight: bold;
    color: darkblue;
    text-decoration: underline;
    cursor: pointer;
    font-size: 10pt;
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
                                styleClass="mapLink" id="fromStopHeader">
                                <rich:componentControl for="fromMapPanel"
                                    attachTo="fromStopHeader" operation="show" event="onclick"
                                    rendered="#{searchController.mapHandler.fromMapUrl != null}" />
                            </h:outputText>
                            <h:outputText value=" #{msg.searchResultsTo} " />
                            <h:outputText value="#{searchController.toStop}"
                                styleClass="mapLink" id="toStopHeader">
                                <rich:componentControl for="toMapPanel" attachTo="toStopHeader"
                                    operation="show" event="onclick"
                                    rendered="#{searchController.mapHandler.toMapUrl != null}" />
                            </h:outputText>
                                        (<h:outputText
                                value="#{searchController.date}">
                                <f:convertDateTime type="date" pattern="dd.MM.yyyy"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>)

                            <script type="text/javascript">
                                //<![CDATA[
                                function initFromMap() {
                                    var address = #{searchController.mapHandler.fromAddress};
                                    if (address == null) {
                                        fromMapVar.addOverlay(new GMarker(fromMapVar.getCenter()));
                                    } else {
                                        var clientgeocoder = new GClientGeocoder();
                                        clientgeocoder.getLatLng(address, setFromLocation);
                                    }
                                }

                                function initToMap() {
                                    var address = #{searchController.mapHandler.toAddress};
                                    if (address == null) {
                                        toMapVar.addOverlay(new GMarker(toMapVar.getCenter()));
                                    } else {
                                        var clientgeocoder = new GClientGeocoder();
                                        clientgeocoder.getLatLng(address, setToLocation);
                                    }
                                }

                                function setFromLocation(response) {
                                    fromMapVar.addOverlay(new GMarker(response));
                                    fromMapVar.panTo(response);
                                }

                                function setToLocation(response) {
                                    toMapVar.addOverlay(new GMarker(response));
                                    toMapVar.panTo(response);
                                }

                                //]]>
                            </script>
                        </h:panelGroup>
                    </f:facet>

                    <ui:include src="messages.jsp" />
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
                            <rich:extendedDataTable value="#{searchController.resultsModel}"
                                height="#{searchController.resultsModel.rowCount == 0 ? 50 : searchController.resultsModel.rowCount * (searchController.returnResultsModel == null ? 179 : 199) + 32}"
                                var="result" rowKeyVar="rowId" selectionMode="single"
                                enableContextMenu="false" id="resultsTable"
                                selection="#{searchController.selection}"
                                headerClass="tableHeader" noDataLabel="#{msg.noSearchResults}"
                                width="380px;" columnClasses="columnClass" border="0"
                                style="border-style: none;">

                                <a4j:support event="onselectionchange"
                                    reRender="selectedEntry,returnResultsTable,ticketCounts,seatChoices"
                                    eventsQueue="selectionSubmit"
                                    action="#{searchController.rowSelectionChanged}" />

                                <rich:column width="35px" sortable="false">
                                    <!-- For presentational purposes only -->
                                    <t:selectOneRow groupName="selectedEntry" id="selectedEntry"
                                        value="#{searchController.selectedRowId}">
                                        <!-- Dummy converter, doesn't work with no converter -->
                                        <f:converter converterId="javax.faces.Integer" />
                                    </t:selectOneRow>
                                </rich:column>

                                <rich:column sortable="false" width="345px">
                                    <f:facet name="header">
                                        <h:outputText value="${msg.oneWayHeaderLabel}"
                                            rendered="#{searchController.returnResultsModel != null}" />
                                    </f:facet>
                                    <rich:panel id="resultEntry" header="#{result.run.route.name}">
                                        <a4j:repeat value="#{result.run.route.stops}" var="stop"
                                            rowKeyVar="stopId">
                                            <h:outputText value=" → " rendered="#{stopId > 0}" />
                                            <h:outputText value="#{stop.name}" styleClass="bold"
                                                rendered="#{searchController.fromStop == stop.name || searchController.toStop == stop.name}" />
                                            <h:outputText value="#{stop.name}"
                                                rendered="#{searchController.fromStop != stop.name &amp;&amp; searchController.toStop != stop.name}" />
                                        </a4j:repeat>

                                        <h:panelGrid columns="2">
                                            <!-- Always render the oneWay price - in case no return options are available, or the user choses not to select one -->
                                            <h:outputText value="#{msg.oneWayPrice}: " />
                                            <h:outputText value="#{result.price.price}" styleClass="bold">
                                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                                <f:converter binding="#{currencyConverter}"
                                                    converterId="currencyConverter" />
                                            </h:outputText>
                                            <h:outputText value="#{msg.twoWayPrice}: "
                                                rendered="#{searchController.returnResultsModel != null}" />
                                            <h:outputText value="#{result.price.twoWayPrice}"
                                                rendered="#{searchController.returnResultsModel != null}"
                                                styleClass="bold">
                                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                                <f:converter binding="#{currencyConverter}"
                                                    converterId="currencyConverter" />
                                            </h:outputText>

                                            <h:outputText value="#{msg.departureTime}: " />
                                            <h:panelGroup>
                                                <h:outputText value="#{result.departureTime.time}"
                                                    styleClass="bold">
                                                    <f:convertDateTime type="time" pattern="HH:mm"
                                                        timeZone="#{timeZoneController.timeZone}" />
                                                </h:outputText> #{msg.hourAbbr}
                                            </h:panelGroup>

                                            <h:outputText value="#{msg.arrivalTime}: " />
                                            <h:panelGroup>
                                                <h:outputText value="#{result.arrivalTime.time}"
                                                    styleClass="bold">
                                                    <f:convertDateTime type="time" pattern="HH:mm"
                                                        timeZone="#{timeZoneController.timeZone}" />
                                                </h:outputText> #{msg.hourAbbr}
                                            </h:panelGroup>

                                            <h:outputText value="#{msg.duration}: " />
                                            <h:panelGroup>
                                                <h:outputText value="#{result.duration / 60}">
                                                    <f:convertNumber maxFractionDigits="0" />
                                                </h:outputText>:#{result.duration % 60}
                                            </h:panelGroup>

                                            <h:outputText value="#{msg.vacantSeats}: " />
                                            <h:outputText
                                                value="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop)}"
                                                style="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop) &lt; 5 ? 'color: red;' : 'color: black;'};font-weight: bold;" />

                                            <h:outputText value="#{msg.transportCompany}: " />
                                            <h:outputText value="#{result.run.route.firm.name}" />
                                        </h:panelGrid>
                                    </rich:panel>
                                </rich:column>
                            </rich:extendedDataTable>
                        </h:panelGroup>

                        <!-- Returns -->
                        <h:panelGroup id="return" style="border-style: none;">
                            <a4j:region>
                                <rich:extendedDataTable
                                    height="#{searchController.returnResultsModel.rowCount * 122 + 30}"
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

                                        <rich:panel id="resultEntry" header="#{result.run.route.name}">
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
                                                <h:panelGroup>
                                                    <h:outputText value="#{result.departureTime.time}"
                                                        styleClass="bold">
                                                        <f:convertDateTime type="time" pattern="HH:mm"
                                                            timeZone="#{timeZoneController.timeZone}" />
                                                    </h:outputText> #{msg.hourAbbr}
                                                </h:panelGroup>

                                                <h:outputText value="#{msg.arrivalTime}: " />
                                                <h:panelGroup>
                                                    <h:outputText value="#{result.arrivalTime.time}"
                                                        styleClass="bold">
                                                        <f:convertDateTime type="time" pattern="HH:mm"
                                                            timeZone="#{timeZoneController.timeZone}" />
                                                    </h:outputText> #{msg.hourAbbr}
                                                </h:panelGroup>

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
                            <!-- Not rendering if there is no run chose, or in case the page is just loaded with
                            the only possible run selected. See the end of document for how
                            the panel is rendered afterwards -->
                            <a4j:region
                                rendered="#{seatController.seatHandler != null and
                                (tc:getSize(searchController.resultsModel.wrappedData) != 1 || searchController.reRenderSeatChoices)}">
                                <a4j:include viewId="seats.jsp">
                                    <ui:param name="modifier" value="1" />
                                    <ui:param name="return" value="falase" />
                                </a4j:include>
                            </a4j:region>
                        </h:panelGroup>

                        <h:panelGroup id="returnSeatChoices">
                            <a4j:region
                                rendered="#{seatController.returnSeatHandler != null}">
                                <a4j:include viewId="seats.jsp">
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


                <rich:modalPanel id="fromMapPanel" autosized="true"
                    onshow="fromMapVar.checkResize(); initFromMap();"
                    onmaskclick="#{rich:component('fromMapPanel')}.hide()"
                    resizeable="false"
                    rendered="#{!empty searchController.mapHandler.fromMapUrl}">

                    <f:facet name="controls">
                        <h:panelGroup>
                            <h:graphicImage value="/images/close.png"
                                id="hidelinkFromMapPanel" styleClass="hidelink" />
                            <rich:componentControl for="fromMapPanel"
                                attachTo="hidelinkFromMapPanel" operation="hide" event="onclick" />
                        </h:panelGroup>
                    </f:facet>

                    <rich:panel id="fromMapHolder">
                        <rich:gmap lat="#{searchController.mapHandler.fromMapLat}"
                            lng="#{searchController.mapHandler.fromMapLng}" zoom="17"
                            mapType="G_HYBRID_MAP" showGMapTypeControl="false"
                            style="width: 500px; height: 500px;" gmapVar="fromMapVar" />
                    </rich:panel>
                </rich:modalPanel>

                <rich:modalPanel id="toMapPanel" autosized="true"
                    onmaskclick="#{rich:component('toMapPanel')}.hide()"
                    resizeable="false" onshow="toMapVar.checkResize(); initToMap();"
                    rendered="#{!empty searchController.mapHandler.toMapUrl}">
                    <f:facet name="controls">
                        <h:panelGroup>
                            <h:graphicImage value="/images/close.png" id="hidelinkToMapPanel"
                                styleClass="hidelink" />
                            <rich:componentControl for="toMapPanel"
                                attachTo="hidelinkToMapPanel" operation="hide" event="onclick" />
                        </h:panelGroup>
                    </f:facet>
                    <rich:panel id="toMapHolder">
                        <rich:gmap lat="#{searchController.mapHandler.toMapLat}"
                            lng="#{searchController.mapHandler.toMapLng}" zoom="17"
                            mapType="G_HYBRID_MAP" showGMapTypeControl="false"
                            style="width: 500px; height: 500px;" gmapVar="toMapVar" />
                    </rich:panel>
                </rich:modalPanel>
            </a4j:form>
        </f:view>
    </ui:define>
    <ui:define name="footer">
        <a4j:form ajaxSubmit="true">
            <a4j:jsFunction name="refreshSeatChoices" reRender="seatChoices"
                id="refreshSeatChoices" ajaxSingle="true" immediate="true">
                <a4j:actionparam assignTo="#{searchController.reRenderSeatChoices}"
                    value="true" id="reRenderParam" name="reRenderParam" />
            </a4j:jsFunction>

            <script type="text/javascript">
                //<![CDATA[
                    function load() {
                        if (#{tc:getSize(searchController.resultsModel.wrappedData)} == 1) {
                            //document.getElementById('searchResults:resultsTable:n:0').click();
                            refreshSeatChoices();
                        }
                    }
                    //document.body.onload=load();
                    setTimeout(load, 1000);
                //]]>
                </script>
        </a4j:form>
    </ui:define>
</ui:composition>