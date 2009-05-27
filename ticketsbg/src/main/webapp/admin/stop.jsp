<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:j4j="http://javascript4jsf.dev.java.net/"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:cust="http://tickets.com/cust"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<f:view>
		<h:messages />
		<h:form id="stopForm">
			<a4j:outputPanel ajaxRendered="true">
				<h:panelGrid columns="2" columnClasses="rich-panel">
					<h:outputLabel value="#{msg.stopName}" for="stopName" />
					<!-- TODO : combo with all cities + autocomplete? -->
					<h:inputText value="#{routeController.stop.name}" id="stopName" />
					

					<h:outputLabel value="#{msg.timeToArrival}" for="timeToArrival" />
					<rich:inputNumberSpinner maxValue="1000"
						value="${routeController.stop.timeToArrival}" id="timeToArrival" />

					<h:outputLabel value="#{msg.timeToDeparture}" for="timeToDeparture" />
					<rich:inputNumberSpinner maxValue="1000"
						value="${routeController.stop.timeToDeparture}"
						id="timeToDeparture" />

					<a4j:commandButton action="#{routeController.saveStop}"
						value="#{msg.save}" reRender="stopsTable,pricesTree"
						oncomplete="#{rich:component('stopPanel')}.hide();">
						<cust:defaultAction />
					</a4j:commandButton>
					<h:outputText></h:outputText>
				</h:panelGrid>
			</a4j:outputPanel>
		</h:form>
	</f:view>
</ui:composition>