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
    xmlns:tc="http://tickets.com/tc">

    <style type="text/css">
.firstColumn {
    text-align: right;
    padding-right: 5px;
    width: 50px;
    font-weight: bold;
    vertical-align: middle;
}

.secondColumn {
    vertical-align: middle;
    padding: 0px;
}

.disabled-cell {
    text-decoration: line-through;
    background-color: lightgrey;
    background-image: none;
}

.searchFieldLabel {
    width: 50px;
    font-weight: bold;
}
</style>
    <script type="text/javascript">
//<![CDATA[
function showOrHideReturn() {

    if (#{!isAdmin}) {
        var oneWayChecked = document.getElementById("searchForm:travelType:0").checked;
        if (oneWayChecked || oneWayChecked == 'checked') {
            #{rich:element('returnPanel')}.style.display="none";
        } else {
            #{rich:element('returnPanel')}.style.display="block";
        }
    }
}
//]]>
</script>
    <f:view>
        <rich:panel style="#{isAdmin ? '' : 'height: 100%; width: 305px;'}"
            header="#{searchController.ticketToAlter == null ? msg.searchTitle : null}"
            headerClass="rich-panel-header-main">

            <h:outputText value="#{msg[param.errorKey]}"
                rendered="#{param.errorKey != null}" styleClass="error" />

            <ui:include src="messages.jsp">
                <ui:param name="ajaxRendered" value="false" />
            </ui:include>
            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn"
                rendered="#{!isAdmin}">
                <h:outputLabel value="" for="travelType" />
                <h:selectOneRadio value="#{searchController.travelType}"
                    id="travelType" style="font-weight: bold;"
                    disabled="#{searchController.ticketToAlter != null}"
                    onclick="showOrHideReturn();">
                    <a4j:support event="onchange" ajaxSingle="true" limitToList="true" />

                    <f:selectItem itemLabel="#{msg.oneWayTravelType}"
                        itemValue="oneWay" />
                    <f:selectItem itemLabel="#{msg.twoWayTravelType}"
                        itemValue="twoWay" />
                </h:selectOneRadio>
            </h:panelGrid>

            <h:panelGrid columns="#{isAdmin ? 4 : 2}"
                id="stopSelectionsHolder"
                columnClasses="firstColumn,secondColumn#{isAdmin ? ',firstColumn,secondCoumns' : ''}"
                cellpadding="5" style="#{isAdmin ? 'float: left;' : ''}">
                <h:outputLabel value="#{msg.fromStop}:" for="fromStop" />
                <h:panelGroup>
                    <rich:comboBox suggestionValues="#{searchController.stopNames}"
                        directInputSuggestions="false" required="true"
                        value="#{searchController.fromStop}" id="fromStop"
                        disabled="#{searchController.ticketToAlter != null}" width="165"
                        listStyle="text-align: left;">

                        <f:attribute name="label" value="#{msg.startStop}" />

                        <a4j:support event="onchange" reRender="toStop"
                            limitToList="true" eventsQueue="fromStopOnchange"
                            action="#{searchController.filterToStops}" ajaxSingle="true"
                            requestDelay="500" />
                        <a4j:support event="onselect" reRender="toStop" limitToList="true"
                            action="#{searchController.filterToStops}" ajaxSingle="true" />

                    </rich:comboBox>
                </h:panelGroup>

                <h:outputLabel value="#{msg.toStop}:" for="toStop" />
                <rich:comboBox suggestionValues="#{searchController.toStopNames}"
                    directInputSuggestions="false" required="${!isAdmin}"
                    value="#{searchController.toStop}" id="toStop"
                    disabled="#{searchController.ticketToAlter != null}" width="165"
                    listStyle="text-align: left;" defaultLabel="#{isAdmin ? msg.allDestinations : null}">
                    <f:attribute name="label" value="#{msg.endStop}" />
                </rich:comboBox>

                <h:outputText />
                <h:panelGroup layout="block" styleClass="dottedLine" rendered="#{!isAdmin}" />
            </h:panelGrid>

            <script type="text/javascript">
            //<![CDATA[
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var currentDate = new Date();

                function disallowPastDays(day) {

                    if (currentDate == undefined) {
                        currentDate = day.date.getDate;
                    }

                    if (currentDate.getTime() - day.date.getTime() >= ONE_DAY) {
                        return false;
                    } else {
                        return true;
                    }
                }

                function getDayClass(day) {
                    if (currentDate == undefined) {
                        currentDate = day.date.getDate;
                    }
                    if (currentDate.getTime() - day.date.getTime() >= ONE_DAY) {
                        return "disabled-cell";
                    } else {
                        return null;
                    }

                }
            //]]>
            </script>
            <!-- One way fields -->
            <h:panelGroup id="oneWayPanel" style="#{isAdmin ? 'float: left;' : ''}">
                <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                    <h:outputText />
                    <h:panelGrid columns="#{isAdmin ? 2 : 1}" id="oneWayPanelTable">
                        <h:outputLabel value="#{msg.departureDate}:" for="date"
                            styleClass="searchFieldLabel" style="width: 100%;"/>

                        <h:panelGroup>
                            <rich:calendar id="date" datePattern="dd.MM.yyyy"
                                firstWeekDay="1" value="#{searchController.date}"
                                required="true" enableManualInput="true"
                                isDayEnabled="disallowPastDays" boundaryDatesMode="scroll"
                                dayStyleClass="getDayClass">
                                <f:attribute name="label" value="#{msg.departureDate}" />
                            </rich:calendar>
                            <h:outputText value="&#160;" />
                            <h:graphicImage url="/images/small/timer.png" alt=""
                                style="cursor: pointer; vertical-align: middle;"
                                onclick="#{rich:component('outboundTimePanel')}.show()" />
                        </h:panelGroup>
                    </h:panelGrid>
                </h:panelGrid>


                <rich:modalPanel id="outboundTimePanel" autosized="true"
                    onmaskclick="#{rich:component('outboundTimePanel')}.hide()">
                    <ui:include src="/modalPanelCommons.jsp">
                        <ui:param name="dialogId" value="outboundTimePanel" />
                    </ui:include>
                    <f:facet name="header">
                        <h:outputText value="#{msg.hoursSpan} (#{msg.oneWayHeaderLabel})" />
                    </f:facet>


                    <h:panelGrid columns="7" cellpadding="0" cellspacing="2"
                        style="font-weight: bold;">

                        <h:selectOneMenu id="departureOrArival"
                            value="#{searchController.timeForDeparture}"
                            converter="#{booleanConverter}">
                            <f:selectItem itemLabel="#{msg.departureTime}" itemValue="true" />
                            <f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />
                        </h:selectOneMenu>

                        <h:outputLabel value="#{msg.fromHour}&#160;" for="fromHour" />
                        <rich:comboBox suggestionValues="#{searchController.hoursFrom}"
                            value="#{searchController.fromHour}" id="fromHour" width="50"
                            style="margin-right: 5px;">
                            <f:convertNumber minIntegerDigits="2" />
                        </rich:comboBox>

                        <h:outputLabel value="#{msg.toHour}&#160;" for="toHour" />
                        <rich:comboBox suggestionValues="#{searchController.hoursTo}"
                            value="#{searchController.toHour}" id="toHour" width="50"
                            style="margin-right: 5px;">
                            <f:convertNumber minIntegerDigits="2" />
                        </rich:comboBox>
                        <h:outputText value="#{msg.hourAbbr}" />

                        <input type="button" value="OK"
                            onclick="#{rich:component('outboundTimePanel')}.hide(); " />
                    </h:panelGrid>
                </rich:modalPanel>
            </h:panelGroup>


            <!--  Return fields -->
            <h:panelGroup id="returnPanel" rendered="#{!isAdmin}">
                <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                    <h:outputText />
                    <h:panelGroup layout="block" styleClass="dottedLine" />

                    <h:outputText />
                    <h:panelGrid columns="1">
                        <h:outputLabel value="#{msg.returnDate}:" for="returnDate"
                            styleClass="searchFieldLabel" style="width: 100%;" />
                        <h:panelGroup>
                            <rich:calendar id="returnDate" datePattern="dd.MM.yyyy"
                                firstWeekDay="1" value="#{searchController.returnDate}"
                                enableManualInput="true" isDayEnabled="disallowPastDays"
                                boundaryDatesMode="scroll" dayStyleClass="getDayClass" />

                            <h:outputText value="&#160;" />
                            <h:graphicImage url="/images/small/timer.png" alt=""
                                style="cursor: pointer; vertical-align: middle;"
                                onclick="#{rich:component('returnTimePanel')}.show()" />
                        </h:panelGroup>
                    </h:panelGrid>
                </h:panelGrid>

                <rich:modalPanel id="returnTimePanel" autosized="true"
                    onmaskclick="#{rich:component('returnTimePanel')}.hide()">
                    <ui:include src="/modalPanelCommons.jsp">
                        <ui:param name="dialogId" value="returnTimePanel" />
                    </ui:include>
                    <f:facet name="header">
                        <h:outputText value="#{msg.hoursSpan} (#{msg.returnHeaderLabel})" />
                    </f:facet>

                    <h:panelGrid columns="7" cellpadding="0" cellspacing="2"
                        style="font-weight: bold;">

                        <h:selectOneMenu id="returnDepartureOrArival"
                            value="#{searchController.returnTimeForDeparture}"
                            converter="#{booleanConverter}">

                            <f:selectItem itemLabel="#{msg.departureTime}" itemValue="true" />
                            <f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />
                        </h:selectOneMenu>

                        <h:outputLabel value="#{msg.fromHour}&#160;" for="returnFromHour" />
                        <rich:comboBox suggestionValues="#{searchController.hoursFrom}"
                            value="#{searchController.returnFromHour}" id="returnFromHour"
                            width="50" style="margin-right: 5px;">
                            <f:convertNumber minIntegerDigits="2" />
                        </rich:comboBox>

                        <h:outputLabel value="#{msg.toHour}&#160;" for="returnToHour" />
                        <rich:comboBox suggestionValues="#{searchController.hoursTo}"
                            value="#{searchController.returnToHour}" id="returnToHour"
                            width="50" style="margin-right: 5px;">
                            <f:convertNumber minIntegerDigits="2" />
                        </rich:comboBox>
                        <h:outputText value="#{msg.hourAbbr}" />

                        <input type="button" value="OK"
                            onclick="#{rich:component('returnTimePanel')}.hide(); " />
                    </h:panelGrid>
                </rich:modalPanel>
            </h:panelGroup>

            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn"
                style="margin-top: 5px;">
                <h:outputText></h:outputText>
                <h:commandButton value="#{msg.search}"
                    style="width: 170px; height: 25px;"
                    action="#{searchController.search}" />
            </h:panelGrid>
        </rich:panel>
    </f:view>

    <script type="text/javascript">
//<![CDATA[
    window.onload = function() {
        if (#{!isAdmin}) {
            var oneWay = #{searchController.travelType == 'oneWay'};
            if (oneWay) {
                #{rich:element('returnPanel')}.style.display="none";
            } else {
                #{rich:element('returnPanel')}.style.display="block";
            }
        }
    }
//]]>
</script>
</ui:composition>