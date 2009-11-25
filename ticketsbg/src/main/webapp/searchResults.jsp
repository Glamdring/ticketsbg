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
        <script type="text/javascript"
            src="http://maps.google.com/maps?file=api&amp;v=2&amp;hl=bg&amp;key=ABQIAAAAcS1gbQG8pb3-5DubTX566BQq5bQxlGIBYwxxbERPytfXE9xIuBSTrnBXT_ko6RzcLF5J7Z6fcnJ7oQ"></script>
        <script type="text/javascript"
            src="http://www.google.com/jsapi?key=ABQIAAAAcS1gbQG8pb3-5DubTX566BQq5bQxlGIBYwxxbERPytfXE9xIuBSTrnBXT_ko6RzcLF5J7Z6fcnJ7oQ"></script>
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

.firstTicketColumn {
    width: 340px;
}

.secondTicketColumn {
    width: 40px;
}
.extdt-empty-cell {
    width: 0px;
}
</style>
    </ui:define>
    <ui:define name="body">
        <f:view contentType="text/html">
            <!-- Needed by Gmap, according to documentation -->
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

                    <h:panelGrid columns="2" columnClasses="gridContent,gridContent"
                        cellpadding="0" cellspacing="0">
                        <h:panelGroup id="oneWay" style="border-style: none;">
                            <rich:extendedDataTable value="#{searchController.resultsModel}"
                                height="#{searchController.resultsModel.rowCount == 0 ? 50 : searchController.resultsModel.rowCount * (searchController.returnResultsModel == null ? 182 : 202) + 31}px"
                                var="result" rowKeyVar="rowId" selectionMode="single"
                                enableContextMenu="false" id="resultsTable"
                                selection="#{searchController.selection}"
                                headerClass="tableHeader" noDataLabel="#{msg.noSearchResults}"
                                width="380px" columnClasses="columnClass" border="0"
                                style="border-style: none; table-layout: fixed">

                                <a4j:support event="onselectionchange"
                                    reRender="selectedEntry,returnResultsTable,ticketCounts,seatChoices,returnSeatChoices"
                                    eventsQueue="selectionSubmit"
                                    action="#{searchController.rowSelectionChanged}" />

                                <rich:column width="35px" sortable="false" style="width: 35px;">
                                    <f:facet name="header">
                                        <h:outputText value="&#160;" />
                                    </f:facet>
                                    <!-- For presentational purposes only -->
                                    <t:selectOneRow groupName="selectedEntry" id="selectedEntry"
                                        value="#{searchController.selectedRowId}">
                                        <!-- Dummy converter, doesn't work with no converter -->
                                        <f:converter converterId="javax.faces.Integer" />
                                    </t:selectOneRow>
                                </rich:column>

                                <rich:column sortable="false" width="345px;">
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
                                    height="#{searchController.returnResultsModel.rowCount == 0 ? 50 : searchController.returnResultsModel.rowCount * 123 + 30}px"
                                    value="#{searchController.returnResultsModel}" var="result"
                                    rowKeyVar="rowId" selectionMode="single"
                                    enableContextMenu="false" id="returnResultsTable"
                                    selection="#{searchController.returnSelection}"
                                    headerClass="tableHeader"
                                    noDataLabel="#{searchController.selectedEntry == null ? msg.selectOutbound : msg.noSearchResults}"
                                    width="380px" columnClasses="columnClass"
                                    rendered="#{searchController.returnResultsModel != null}"
                                    style="border-style: none" border="0">

                                    <a4j:support event="onselectionchange"
                                        reRender="selectedReturnEntry,ticketCounts,returnSeatChoices"
                                        eventsQueue="returnSelectionSubmit"
                                        action="#{searchController.returnRowSelectionChanged}" />

                                    <rich:column width="35px" sortable="false" style="width: 35px;">
                                        <!-- For presentational purposes only -->
                                        <t:selectOneRow groupName="selectedReturnEntry"
                                            id="selectedReturnEntry"
                                            value="#{searchController.selectedReturnRowId}">
                                            <!-- Dummy converter; doesn't work with no converter -->
                                            <f:converter converterId="javax.faces.Integer" />
                                        </t:selectOneRow>

                                    </rich:column>

                                    <rich:column sortable="false" width="345px"
                                        filterExpression="#{searchController.selectedEntry != null and result.run.route.firm.firmId == searchController.selectedEntry.run.route.firm.firmId}">
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
                            <!-- Not rendering if there is no run chosen, or in case the page is just loaded with
                            the only possible run selected. See the end of document for how
                            the panel is rendered afterwards -->
                            <a4j:region
                                rendered="#{seatController.seatHandler != null and
                                (tc:getSize(searchController.resultsModel.wrappedData) != 1 || searchController.reRenderSeatChoices)}">
                                <a4j:include viewId="seats.jsp">
                                    <ui:param name="modifier" value="1" />
                                    <ui:param name="return" value="falase" />
                                </a4j:include>
                                <h:outputText value="&#160;" />
                                <a4j:commandButton type="button"
                                    value="#{msg.transportCompanyTerms}"
                                    onclick="#{rich:component('firmTermsPanel')}.show();" />
                                <rich:modalPanel id="firmTermsPanel" width="300" height="400"
                                    resizeable="false"
                                    onmaskclick="#{rich:component('firmTermsPanel')}.hide();">
                                    <ui:include src="/modalPanelCommons.jsp">
                                        <ui:param name="dialogId" value="firmTermsPanel" />
                                    </ui:include>
                                    <f:facet name="header">
                                        <h:outputText
                                            value="#{msg.transportCompanyTerms}: #{searchController.selectedEntry.run.route.firm.name}" />
                                    </f:facet>
                                    <h:inputTextarea id="termsTextarea"
                                        value="#{searchController.selectedEntry.run.route.firm.terms}"
                                        readonly="true" disabled="true"
                                        style="width: 100%; height: 100%;" />
                                </rich:modalPanel>
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
                    <rich:panel header="#{msg.tickets}" id="ticketCounts"
                        style="width: 380px; margin-top: 15px;"
                        rendered="#{tc:getSize(searchController.resultsModel.wrappedData) > 0}">
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
                                inputSize="3"
                                disabled="#{searchController.ticketToAlter != null}">
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
                                        maxValue="#{tc:getVacantSeats(searchController.selectedEntry.run, searchController.fromStop, searchController.toStop)}"
                                        disabled="#{searchController.ticketToAlter != null}">
                                        <a4j:support event="onchange" ajaxSingle="true" />
                                    </rich:inputNumberSpinner>
                                </h:panelGrid>
                            </a4j:repeat>
                        </a4j:outputPanel>
                    </rich:panel>

                    <rich:panel header="#{msg.totalPrice}"
                        rendered="#{tc:getSize(searchController.resultsModel.wrappedData) > 0}"
                        style="width: 380px; margin-top: 10px;">
                        <a4j:outputPanel ajaxRendered="true" id="totalPriceHolder">
                            <h:outputText style="font-weight: bold"
                                value="#{searchController.totalPrice}">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>
                        </a4j:outputPanel>
                    </rich:panel>

                    <h:panelGroup rendered="#{tc:getSize(searchController.resultsModel.wrappedData) > 0}">
                        <h:commandButton value="#{msg.backToSearchScreen}"
                            action="#{searchController.toSearchScreen}" />
                        <h:outputText value=" " />
                        <h:commandButton action="#{searchController.proceed}"
                            value="#{msg.toPayment} *" />
                        <br />* #{msg.agreeingToBothTerms}
                    </h:panelGroup>
                </rich:panel>


                <rich:modalPanel id="fromMapPanel" autosized="true"
                    onshow="preparefromMap(); fromMapVar.checkResize(); initFromMap();"
                    onmaskclick="#{rich:component('fromMapPanel')}.hide()"
                    resizeable="false"
                    rendered="#{!empty searchController.mapHandler.fromMapUrl}">

                    <ui:include src="/modalPanelCommons.jsp">
                        <ui:param name="dialogId" value="fromMapPanel" />
                    </ui:include>

                    <rich:panel id="fromMapHolder">
                        <a4j:include viewId="gmap.jsp">
                            <ui:param name="lat"
                                value="#{searchController.mapHandler.fromMapLat}" />
                            <ui:param name="lng"
                                value="#{searchController.mapHandler.fromMapLng}" />
                            <ui:param name="mapVar" value="fromMapVar" />
                            <ui:param name="componentId" value="fromMap" />
                        </a4j:include>
                    </rich:panel>
                </rich:modalPanel>

                <rich:modalPanel id="toMapPanel" autosized="true"
                    onmaskclick="#{rich:component('toMapPanel')}.hide()"
                    resizeable="false"
                    onshow="preparetoMap(); toMapVar.checkResize(); initToMap();"
                    rendered="#{!empty searchController.mapHandler.toMapUrl}">
                    <ui:include src="/modalPanelCommons.jsp">
                        <ui:param name="dialogId" value="toMapPanel" />
                    </ui:include>
                    <rich:panel id="toMapHolder">
                        <a4j:include viewId="gmap.jsp">
                            <ui:param name="lat"
                                value="#{searchController.mapHandler.toMapLat}" />
                            <ui:param name="lng"
                                value="#{searchController.mapHandler.toMapLng}" />
                            <ui:param name="mapVar" value="toMapVar" />
                            <ui:param name="componentId" value="toMap" />
                        </a4j:include>
                    </rich:panel>
                </rich:modalPanel>

                <a4j:jsFunction name="refreshSeatChoices" reRender="seatChoices"
                    id="refreshSeatChoices" ajaxSingle="true" immediate="true">
                    <a4j:actionparam assignTo="#{searchController.reRenderSeatChoices}"
                        value="true" id="reRenderParam" name="reRenderParam" />
                </a4j:jsFunction>
            </a4j:form>
        </f:view>
    </ui:define>
    <ui:define name="footer">
        <script type="text/javascript">
           //<![CDATA[
               function load() {
                   if (#{tc:getSize(searchController.resultsModel.wrappedData)} == 1) {
                       //document.getElementById('searchResults:resultsTable:n:0').click();
                       refreshSeatChoices();
                   }

                   google.load("maps", "2", {callback : mapsLoaded});
               }
               function mapsLoaded() {
                  //do nothing. The maps are loaded on click of the respective links
               }
               setTimeout(load, 1000);
           //]]>
           </script>
    </ui:define>
</ui:composition>