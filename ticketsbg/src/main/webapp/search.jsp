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

	<ui:define name="header">
		<style type="text/css">
.firstColumn {
	text-align: right;
	padding-right: 5px;
	width: 120px;
}

.secondColumn {
	text-align: right;
}
</style>
		<script type="text/javascript">
	//Couldn't do it with richfaces tags..
	function toggleReturnPanel(travelType) {
		var display = "none";
		if (travelType == "twoWay")
			display = "block";

		document.getElementById('searchForm:returnPanel').style.display = display;
	}
</script>
	</ui:define>
	<ui:define name="body">
		<f:view>
			<a4j:form id="searchForm" ajaxSubmit="true">
				<rich:panel header="#{msg.searchTitle}">
					<h:messages />
					<h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">

						<h:outputLabel value="#{msg.travelType}:" for="travelType" />
						<h:selectOneRadio value="#{searchController.travelType}"
							id="travelType">
							<a4j:support event="onclick" reRender="returnPanel"
								ajaxSingle="true" />
							<f:selectItem itemLabel="#{msg.twoWay}" itemValue="twoWay" />
							<f:selectItem itemLabel="#{msg.oneWay}" itemValue="oneWay" />
						</h:selectOneRadio>
					</h:panelGrid>
					<h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
						<h:outputLabel value="#{msg.fromStop}:" for="fromStop" />
						<rich:comboBox suggestionValues="#{searchController.stopNames}"
							directInputSuggestions="false" required="true"
							value="#{searchController.fromStop}" id="fromStop">

							<a4j:support event="onselect" reRender="toStop"
								action="#{searchController.filterToStops}" ajaxSingle="true" />
							<a4j:support event="onchange" reRender="toStop" eventsQueue="fromStopOnchange"
                                action="#{searchController.filterToStops}" ajaxSingle="true" requestDelay="1000"/>
						</rich:comboBox>

						<h:outputLabel value="#{msg.toStop}:" for="toStop" />
						<rich:comboBox suggestionValues="#{searchController.toStopNames}"
							directInputSuggestions="false" required="true"
							value="#{searchController.toStop}" id="toStop" />
					</h:panelGrid>

					<!-- One way firelds -->
					<rich:panel id="oneWayPanel">
						<h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
							<h:outputLabel value="#{msg.date}:" for="date" />
							<rich:calendar id="date" datePattern="dd.MM.yyyy"
								firstWeekDay="1" value="#{searchController.date}" />

							<h:panelGroup>
								<h:selectOneMenu id="departureOrArival"
									value="#{searchController.timeForDeparture}"
									converter="#{booleanConverter}">
									<f:selectItem itemLabel="#{msg.departureTime}" itemValue="true" />
									<f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />

								</h:selectOneMenu>
								<h:outputText value=":" />
							</h:panelGroup>
							<h:panelGroup>
								<h:outputLabel value="#{msg.fromHour}&#160;" for="fromHour"
									style="float: left;" />
								<rich:comboBox suggestionValues="#{searchController.hoursFrom}"
									value="#{searchController.fromHour}" id="fromHour" width="50"
									style="float:left; margin-right: 5px;">
									<f:convertNumber minIntegerDigits="2" />
								</rich:comboBox>

								<h:outputLabel value="#{msg.toHour}&#160;" for="toHour"
									style="float:left;" />
								<rich:comboBox suggestionValues="#{searchController.hoursTo}"
									value="#{searchController.toHour}" id="toHour" width="50"
									style="float:left;">
									<f:convertNumber minIntegerDigits="2" />
								</rich:comboBox>
							</h:panelGroup>
						</h:panelGrid>

					</rich:panel>
					<!--  Return fields -->
					<a4j:outputPanel ajaxRendered="true">

						<rich:panel id="returnPanel"
							rendered="#{searchController.travelType == 'twoWay'}">
							<h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
								<h:outputLabel value="#{msg.date}:" for="returnDate" />
								<rich:calendar id="returnDate" datePattern="dd.MM.yyyy"
									firstWeekDay="1" value="#{searchController.returnDate}" />

								<h:panelGroup>
									<h:selectOneMenu id="returnDepartureOrArival"
										value="#{searchController.returnTimeForDeparture}"
										converter="#{booleanConverter}">

										<f:selectItem itemLabel="#{msg.departureTime}"
											itemValue="true" />
										<f:selectItem itemLabel="#{msg.arrivalTime}" itemValue="false" />
									</h:selectOneMenu>
									<h:outputText value=":" />
								</h:panelGroup>
								<h:panelGroup>
									<h:outputLabel value="#{msg.fromHour}&#160;"
										for="returnFromHour" style="float:left;" />
									<rich:comboBox suggestionValues="#{searchController.hoursFrom}"
										value="#{searchController.returnFromHour}" id="returnFromHour"
										width="50" style="float:left; margin-right: 5px;">
										<f:convertNumber minIntegerDigits="2" />
									</rich:comboBox>

									<h:outputLabel value="#{msg.toHour}&#160;" for="returnToHour"
										style="float:left;" />
									<rich:comboBox suggestionValues="#{searchController.hoursTo}"
										value="#{searchController.returnToHour}" id="returnToHour"
										width="50" style="float:left;">
										<f:convertNumber minIntegerDigits="2" />
									</rich:comboBox>
								</h:panelGroup>
							</h:panelGrid>
						</rich:panel>
					</a4j:outputPanel>

					<h:panelGrid columns="2" columnClasses="firstColumn,secondColumn">
						<h:outputText></h:outputText>
						<h:commandButton value="#{msg.search}"
							action="#{searchController.search}" />
					</h:panelGrid>
				</rich:panel>
			</a4j:form>
		</f:view>
	</ui:define>
</ui:composition>

