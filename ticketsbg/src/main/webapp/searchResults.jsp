<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
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

.evenRow {
    background-color: #F2F5F8;
    cursor: pointer;
}

.oddRow {
    background-color: white;
    cursor: pointer;
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

                                var colorToRestore;

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


                                function scrollDown() {
                                    document.location.href="#seatsChoice";
                                }
                                //]]>
                            </script>
                        </h:panelGroup>
                    </f:facet>

                    <ui:include src="messages.jsp" />

                    <a4j:jsFunction name="changeSelection"
                        eventsQueue="changeSelectionQueue" ignoreDupResponses="true"
                        ajaxSingle="true" action="#{searchController.rowSelectionChanged}"
                        reRender="selectedEntry,returnResultsTable,ticketCounts,seatChoices,returnSeatChoices,companyTerms">
                        <a4j:actionparam name="selectedRowIdx"
                            assignTo="#{searchController.selectedRowId}" />
                    </a4j:jsFunction>

                    <rich:dataTable value="#{searchController.resultsModel}"
                        var="result" id="resultsTable" headerClass="tableHeader"
                        rowKeyVar="rowId" width="770px;" rowClasses="oddRow,evenRow"
                        onRowMouseOver="colorToRestore = this.style.backgroundColor; this.style.backgroundColor='#F1F1F1'"
                        onRowMouseOut="this.style.backgroundColor=colorToRestore"
                        onRowClick="changeSelection(#{rowId})"
                        style="margin-bottom: 10px;">

                        <f:facet name="header">
                            <rich:columnGroup>
                                <rich:column width="20px">
                                    <h:outputText value="&#160;" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.route}" />
                                </rich:column>
                                <rich:column width="50px">
                                    <h:outputText value="#{msg.price}" />
                                </rich:column>
                                <rich:column
                                    rendered="#{searchController.returnResultsModel != null}">
                                    <h:outputText value="#{msg.twoWayPrice}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.departureTime}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.arrivalTime}" />
                                </rich:column>
                                <rich:column width="45px">
                                    <h:outputText value="#{msg.duration}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.seats}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.transportCompany}" />
                                </rich:column>
                            </rich:columnGroup>
                        </f:facet>

                        <rich:column width="20px" sortable="false">
                            <!-- For presentational purposes only -->
                            <t:selectOneRow groupName="selectedEntry" id="selectedEntry"
                                value="#{searchController.selectedRowId}">
                                <!-- Dummy converter, doesn't work with no converter -->
                                <f:converter converterId="javax.faces.Integer" />
                            </t:selectOneRow>
                        </rich:column>

                        <rich:column>
                            <a4j:repeat value="#{result.run.route.stops}" var="stop"
                                rowKeyVar="stopId">
                                <h:outputText value=" → " rendered="#{stopId > 0}" />
                                <h:outputText value="#{stop.name}" styleClass="bold"
                                    rendered="#{fn:startsWith(stop.name, searchController.fromStop) || fn:startsWith(stop.name, searchController.toStop)}" />
                                <h:outputText value="#{stop.name}"
                                    rendered="#{!fn:startsWith(stop.name, searchController.fromStop) and !fn:startsWith(stop.name, searchController.toStop)}" />
                            </a4j:repeat>
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{result.price.price}" styleClass="bold">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>
                        </rich:column>


                        <rich:column
                            rendered="#{searchController.returnResultsModel != null}">
                            <h:outputText value="#{result.price.twoWayPrice}"
                                styleClass="bold">
                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                <f:converter binding="#{currencyConverter}"
                                    converterId="currencyConverter" />
                            </h:outputText>
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{result.departureTime.time}"
                                styleClass="bold">
                                <f:convertDateTime type="time" pattern="HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                            <h:outputText value=" #{msg.hourAbbr}" styleClass="bold" />
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{result.arrivalTime.time}"
                                styleClass="bold">
                                <f:convertDateTime type="time" pattern="HH:mm"
                                    timeZone="#{timeZoneController.timeZone}" />
                            </h:outputText>
                            <h:outputText value=" #{msg.hourAbbr}" styleClass="bold" />
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{tc:divide(result.duration,60)}" styleClass="bold">
                                <f:convertNumber integerOnly="true" />
                            </h:outputText>:<h:outputText value="#{result.duration % 60}" styleClass="bold" />
                            <h:outputText value=" #{msg.hourAbbr}" styleClass="bold" />
                        </rich:column>


                        <rich:column>
                            <h:outputText
                                value="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop)}"
                                style="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop) &lt; 5 ? 'color: red;' : 'color: black;'};font-weight: bold;" />
                        </rich:column>

                        <rich:column>
                            <h:outputText value="#{result.run.route.firm.name}"
                                styleClass="bold" />
                        </rich:column>
                    </rich:dataTable>

                    <h:panelGroup id="seatChoices">
                        <a name="seatsChoice" />
                        <!-- Not rendering if there is no run chosen, or in case the page is just loaded with
                            the only possible run selected. See the end of document for how
                            the panel is rendered afterwards -->
                        <a4j:region
                            rendered="#{seatController.seatHandler != null and
                                (tc:getSize(searchController.resultsModel.wrappedData) != 1 || searchController.reRenderSeatChoices)}">
                            <a4j:include viewId="seats.jsp"
                                rendered="#{searchController.selectedEntry.run.route.allowSeatChoice}">
                                <ui:param name="modifier" value="1" />
                                <ui:param name="return" value="falase" />
                            </a4j:include>
                        </a4j:region>
                    </h:panelGroup>

                    <!-- Returns -->

                    <a4j:jsFunction name="changeReturnSelection"
                        eventsQueue="returnChangeSelectionQueue" ignoreDupResponses="true"
                        ajaxSingle="true"
                        action="#{searchController.returnRowSelectionChanged}"
                        reRender="selectedReturnEntry,ticketCounts,returnSeatChoices">
                        <a4j:actionparam name="selectedReturnRowIdx"
                            assignTo="#{searchController.selectedReturnRowId}" />
                    </a4j:jsFunction>

                    <a4j:region>
                        <!--  noDataLabel="#{searchController.selectedEntry == null ? msg.selectOutbound : msg.noSearchResults}" -->
                        <rich:dataTable value="#{searchController.returnResultsModel}"
                            var="result" id="returnResultsTable" headerClass="tableHeader"
                            rowKeyVar="returnRowId" width="770px;"
                            rowClasses="oddRow,evenRow"
                            onRowMouseOver="colorToRestore = this.style.backgroundColor; this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor=colorToRestore"
                            onRowClick="changeReturnSelection(#{returnRowId})"
                            rendered="#{searchController.returnResultsModel != null}"
                            style="margin-top: 30px; margin-bottom: 10px;">

                            <f:facet name="header">
                                <rich:columnGroup>
                                    <rich:column width="20px">
                                        <h:outputText value="&#160;" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.returnHeaderLabel}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.departureTime}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.arrivalTime}" />
                                    </rich:column>
                                    <rich:column width="45px">
                                        <h:outputText value="#{msg.duration}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.seats}" />
                                    </rich:column>
                                    <rich:column>
                                        <h:outputText value="#{msg.transportCompany}" />
                                    </rich:column>
                                </rich:columnGroup>
                            </f:facet>
                            <rich:column width="35px" sortable="false" style="width: 35px;">
                                <!-- For presentational purposes only -->
                                <t:selectOneRow groupName="selectedReturnEntry"
                                    id="selectedReturnEntry"
                                    value="#{searchController.selectedReturnRowId}">
                                    <!-- Dummy converter; doesn't work with no converter -->
                                    <f:converter converterId="javax.faces.Integer" />
                                </t:selectOneRow>

                            </rich:column>

                            <rich:column
                                filterExpression="#{searchController.selectedEntry != null and result.run.route.firm.firmId == searchController.selectedEntry.run.route.firm.firmId}">
                                <a4j:repeat value="#{result.run.route.stops}" var="stop"
                                    rowKeyVar="stopId">
                                    <h:outputText value=" → " rendered="#{stopId > 0}" />
                                    <h:outputText value="#{stop.name}" styleClass="bold"
                                        rendered="#{fn:startsWith(stop.name, searchController.fromStop) || fn:startsWith(stop.name, searchController.toStop)}" />
                                    <h:outputText value="#{stop.name}"
                                        rendered="#{!fn:startsWith(stop.name, searchController.fromStop) and !fn:startsWith(stop.name, searchController.toStop)}" />
                                </a4j:repeat>
                            </rich:column>

                            <rich:column>
                                <h:outputText value="#{result.departureTime.time}"
                                    styleClass="bold">
                                    <f:convertDateTime type="time" pattern="HH:mm"
                                        timeZone="#{timeZoneController.timeZone}" />
                                </h:outputText> #{msg.hourAbbr}
                                    </rich:column>

                            <rich:column>
                                <h:outputText value="#{result.arrivalTime.time}"
                                    styleClass="bold">
                                    <f:convertDateTime type="time" pattern="HH:mm"
                                        timeZone="#{timeZoneController.timeZone}" />
                                </h:outputText> #{msg.hourAbbr}
                                                    </rich:column>

                            <rich:column>
                                <h:outputText value="#{tc:divide(result.duration, 60)}" styleClass="bold">
                                    <f:convertNumber integerOnly="true" />
                                </h:outputText>:<h:outputText value="#{result.duration % 60}" styleClass="bold" />
                                <h:outputText value=" #{msg.hourAbbr}" styleClass="bold" />
                            </rich:column>

                            <rich:column>
                                <h:outputText
                                    value="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop)}"
                                    style="#{tc:getVacantSeats(result.run, searchController.fromStop, searchController.toStop) &lt; 5 ? 'color: red;' : 'color: black;'};font-weight: bold;" />
                            </rich:column>

                            <rich:column>
                                <h:outputText value="#{result.run.route.firm.name}"
                                    styleClass="bold" />
                            </rich:column>
                        </rich:dataTable>
                    </a4j:region>

                    <h:panelGroup id="returnSeatChoices">
                        <a4j:region rendered="#{seatController.returnSeatHandler != null}">
                            <a4j:include viewId="seats.jsp"
                                rendered="#{searchController.selectedReturnEntry.run.route.allowSeatChoice}">
                                <ui:param name="modifier" value="2" />
                                <ui:param name="return" value="true" />
                            </a4j:include>
                        </a4j:region>
                    </h:panelGroup>


                    <h:panelGroup id="companyTerms" style="clear: left; padding-top: 10px;" layout="block">

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
                    </h:panelGroup>
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

                    <h:panelGroup
                        rendered="#{tc:getSize(searchController.resultsModel.wrappedData) > 0}">
                        <h:commandButton value="#{msg.backToSearchScreen}"
                            action="#{searchController.toSearchScreen}"
                            style="font-size: 14px;" />
                        <h:outputText value=" " />
                        <h:commandButton action="#{searchController.proceed}"
                            value="#{msg.toPayment} *" style="font-size: 14px;" />
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
                    id="refreshSeatChoices" ajaxSingle="true" immediate="true"
                    limitToList="true">
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