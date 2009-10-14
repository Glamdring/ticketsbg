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
    width: 130px;
    font-weight: bold;
    vertical-align: middle;
}

.secondColumn {
    vertical-align: middle;
}

.disabled-cell {
    text-decoration: line-through;
    background-color: lightgrey;
    background-image: none;
}
</style>
    <f:view>
        <rich:panel
            header="#{searchController.ticketToAlter == null ? msg.searchTitle : null}"
            headerClass="rich-panel-header-main">
            <ui:include src="messages.jsp" />

            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn"
                rendered="#{!isAdmin}">
                <h:outputLabel value="" for="travelType" />
                <h:selectOneRadio value="#{searchController.travelType}"
                    id="travelType" style="font-weight: bold;"
                    disabled="#{searchController.ticketToAlter != null}">
                    <a4j:support event="onclick" reRender="returnPanel"
                        ajaxSingle="true" />
                    <f:selectItem itemLabel="#{msg.twoWayTravelType}"
                        itemValue="twoWay" />
                    <f:selectItem itemLabel="#{msg.oneWayTravelType}"
                        itemValue="oneWay" />
                </h:selectOneRadio>
            </h:panelGrid>

            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                <h:outputLabel value="#{msg.fromStop}:" for="fromStop" />
                <h:panelGroup>
                    <rich:comboBox suggestionValues="#{searchController.stopNames}"
                        directInputSuggestions="false" required="true"
                        value="#{searchController.fromStop}" id="fromStop"
                        disabled="#{searchController.ticketToAlter != null}">

                        <f:attribute name="label" value="#{msg.startStop}" />

                        <a4j:support event="onchange" reRender="toStop"
                            eventsQueue="fromStopOnchange"
                            action="#{searchController.filterToStops}" ajaxSingle="true"
                            requestDelay="500" />
                        <a4j:support event="onselect" reRender="toStop"
                            action="#{searchController.filterToStops}" ajaxSingle="true" />

                    </rich:comboBox>
                </h:panelGroup>

                <h:outputLabel value="#{msg.toStop}:" for="toStop" />
                <rich:comboBox suggestionValues="#{searchController.toStopNames}"
                    directInputSuggestions="false" required="${!isAdmin}"
                    value="#{searchController.toStop}" id="toStop"
                    disabled="#{searchController.ticketToAlter != null}">
                    <f:attribute name="label" value="#{msg.endStop}" />
                </rich:comboBox>
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
            <h:panelGroup id="oneWayPanel">
                <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                    <h:outputLabel value="#{msg.departureDate}:" for="date" />
                    <rich:calendar id="date" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        value="#{searchController.date}" required="true"
                        enableManualInput="true" isDayEnabled="disallowPastDays"
                        boundaryDatesMode="scroll" dayStyleClass="getDayClass">
                        <f:attribute name="label" value="#{msg.departureDate}" />
                    </rich:calendar>

                    <h:panelGroup>
                        <h:selectOneMenu id="departureOrArival"
                            value="#{searchController.timeForDeparture}"
                            converter="#{booleanConverter}">
                            <f:selectItem itemLabel="#{msg.departureTime}" itemValue="true" />
                            <f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />
                        </h:selectOneMenu>
                        <h:outputText value=":" />
                    </h:panelGroup>
                    <h:panelGrid columns="5" cellpadding="0" cellspacing="0"
                        style="font-weight: bold;">
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
                    </h:panelGrid>
                </h:panelGrid>

            </h:panelGroup>
            <!--  Return fields -->
            <a4j:outputPanel ajaxRendered="true" rendered="#{!isAdmin}">
                <h:panelGroup id="returnPanel"
                    rendered="#{searchController.travelType == 'twoWay'}">
                    <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                        <h:outputLabel value="#{msg.returnDate}:" for="returnDate" />
                        <rich:calendar id="returnDate" datePattern="dd.MM.yyyy"
                            firstWeekDay="1" value="#{searchController.returnDate}"
                            enableManualInput="true" isDayEnabled="disallowPastDays"
                            boundaryDatesMode="scroll" dayStyleClass="getDayClass" />

                        <h:panelGroup>
                            <h:selectOneMenu id="returnDepartureOrArival"
                                value="#{searchController.returnTimeForDeparture}"
                                converter="#{booleanConverter}">

                                <f:selectItem itemLabel="#{msg.departureTime}" itemValue="true" />
                                <f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />
                            </h:selectOneMenu>
                            <h:outputText value=":" />
                        </h:panelGroup>
                        <h:panelGrid columns="5" cellpadding="0" cellspacing="0"
                            style="font-weight: bold;">
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
                            </rich:comboBox> #{msg.hourAbbr}
                        </h:panelGrid>
                    </h:panelGrid>
                </h:panelGroup>
            </a4j:outputPanel>

            <h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
                <h:outputText></h:outputText>
                <h:commandButton value="#{msg.search}"
                    action="#{searchController.search}" />
            </h:panelGrid>
        </rich:panel>
    </f:view>
</ui:composition>