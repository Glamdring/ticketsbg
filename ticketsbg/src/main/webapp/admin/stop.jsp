<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:j4j="http://javascript4jsf.dev.java.net/"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:cust="http://tickets.com/cust"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	template="admin_template.jsp">
	<ui:define name="body">
		<f:view>
			<h:messages />
			<h:form id="routeForm">
				<table>
					<tr>
						<td><h:outputLabel value="#{msg.stopName}" for="stopName" /></td>
						<td><h:inputText value="#{stopController.stop.name}"
							id="stopName" /></td>
					</tr>
					<tr>
						<td><h:outputLabel value="#{msg.timeToArrival}" /></td>
						<td><rich:inputNumberSpinner 
							value="${stopController.stop.timeToArrival}" /></td>
					</tr>
					<tr>
						<td><h:outputLabel value="#{msg.timeToDeparture}" /></td>
						<td><rich:inputNumberSpinner 
							value="${stopController.stop.timeToDeparture}" /></td>
					</tr>
					<tr>
						<td colspan="2"><h:commandButton
							action="#{stopController.save}" value="#{msg.save}">
							<cust:defaultAction />
						</h:commandButton> <h:commandButton action="routeScreen" value="#{msg.cancel}" /></td>
					</tr>
				</table>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>