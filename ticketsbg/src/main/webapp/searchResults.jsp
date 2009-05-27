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
				<rich:dataTable value="#{searchController.resultsModel}"
					var="result">

					<rich:column>
						<f:facet name="header">
							<h:outputText value="#{msg.routeName}" />
						</f:facet>
						<h:outputText value="#{result.route.name}">
							<f:convertDateTime pattern="dd.MM.yyyy hh:mm" />
						</h:outputText>
					</rich:column>

					<rich:column>
						<f:facet name="header">
							<h:outputText value="#{msg.dateTime}" />
						</f:facet>
						<h:outputText value="#{result.time.time}">
							<f:convertDateTime pattern="dd.MM.yyyy hh:mm" />
						</h:outputText>
					</rich:column>
				</rich:dataTable>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>

