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
	template="basic_template.jsp">

	<ui:define name="header">
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
</style>
	</ui:define>
	<ui:define name="body">
		<f:view>
			<a4j:form id="searchResults">
				<h:messages />
				<rich:extendedDataTable value="#{searchController.resultsModel}"
					var="result" rowKeyVar="rowId" selectionMode="single"
					enableContextMenu="false" id="resultsTable"
					selection="#{searchController.selection}" headerClass="tableHeader"
					noDataLabel="#{msg.noSearchResults}" width="800px;"
					columnClasses="columnClass">

					<a4j:support event="onselectionchange" reRender="selectedRoute"
						action="#{searchController.rowSelectionChanged}" />

					<rich:column width="35px" sortable="false">
						<!-- For presentational purposes only -->
						<t:selectOneRow groupName="selectedRoute" id="selectedRoute"
							value="#{searchController.selectedRowId}">
							<!-- Dummy converter, doesn't work with no converter -->
							<f:converter converterId="javax.faces.Integer" />
						</t:selectOneRow>

					</rich:column>

					<rich:column sortable="false" width="764px">
						<f:facet name="header">
							<h:panelGroup style="font-weight: normal;">
								<h:outputText value="#{msg.searchResultsFrom} " />
								<h:outputText value="#{searchController.fromStop}"
									styleClass="bold" />
								<h:outputText value=" #{msg.searchResultsTo} " />
								<h:outputText value="#{searchController.toStop}"
									styleClass="bold" />
							</h:panelGroup>
						</f:facet>

						<rich:panel id="resultEntry" header="#{result.run.route.name}">
                            <a4j:repeat value="#{result.run.route.stops}" var="stop"
                                rowKeyVar="stopId">
                                <h:outputText value="→" rendered="#{stopId > 0}" />
                                <h:outputText value="#{stop.name}" styleClass="bold"
                                    rendered="#{searchController.fromStop == stop.name || searchController.toStop == stop.name}" />
                                <h:outputText value="#{stop.name}"
                                    rendered="#{searchController.fromStop != stop.name &amp;&amp; searchController.toStop != stop.name}" />
                            </a4j:repeat>

                            <h:panelGrid columns="2">
	                            <h:outputText value="#{msg.oneWayPrice}: " rendered="#{returnResultsModel == null}" />
	                            <h:outputText value="#{result.price.price}"
	                                rendered="#{returnResultsModel == null}" styleClass="bold">
	                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
	                            </h:outputText>
	                            <h:outputText value="#{msg.twoWayPrice}: " rendered="#{returnResultsModel != null}" />
	                            <h:outputText value="#{result.price.twoWayPrice}"
	                                rendered="#{returnResultsModel != null}" styleClass="bold">
	                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
	                            </h:outputText>
	                            
	                            <h:outputText value="#{msg.departureTime}: " />
	                            <h:outputText value="#{result.departureTime.time}" styleClass="bold">
	                                <f:convertDateTime type="time" pattern="HH:mm" timeZone="#{timeZoneController.timeZone}" />
	                            </h:outputText>
	                            
	                            <h:outputText value="#{msg.arrivalTime}: " />
	                            <h:outputText value="#{result.arrivalTime.time}" styleClass="bold">
	                                <f:convertDateTime type="time" pattern="HH:mm" timeZone="#{timeZoneController.timeZone}" />
	                            </h:outputText>
	                            
                            </h:panelGrid>
                        </rich:panel>
					</rich:column>
				</rich:extendedDataTable>
				<h:commandLink value="#{msg.backToSearchScreen}"
					action="#{searchController.toSearchScreen}" />
			</a4j:form>
		</f:view>
	</ui:define>
</ui:composition>