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
	<ui:define name="body">
		<f:view>
			<h:form id="searchResults">
				<h:messages />
				<h:outputText value="#{msg.searchResultsFor}">
					<f:param value="#{searchController.fromStop}" />
					<f:param value="#{searchController.toStop}" />
				</h:outputText>
				<rich:dataGrid value="#{searchController.resultsModel}" var="result"
					columns="1" width="100%">
					<rich:panel id="resultEntry" header="#{result.route.name}">
						<a4j:repeat value="#{result.route.stops}" var="stop"
							rowKeyVar="row">
							<h:outputText value="→" rendered="#{row > 0}" />
							<h:outputText value="#{stop.name}" style="font-weight: bold;"
								rendered="#{searchController.fromStop == stop.name || searchController.toStop == stop.name}" />
							<h:outputText value="#{stop.name}"
								rendered="#{searchController.fromStop != stop.name &amp;&amp; searchController.toStop != stop.name}" />
						</a4j:repeat>
					</rich:panel>
				</rich:dataGrid>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>