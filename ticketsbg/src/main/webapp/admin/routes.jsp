<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	template="admin_template.jsp">
	<ui:define name="body">
		<f:view>
			<h:form id="routeForm">
				<h:messages />
				<h:commandLink value="#{msg.add}" action="#{routeController.newRoute}" />
				<rich:dataTable
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					onRowMouseOut="this.style.backgroundColor='white'" cellpadding="0"
					cellspacing="0" width="700" border="0" var="route"
					value="#{routeController.routesModel}">

					<f:facet name="header">
						<rich:columnGroup>
							<rich:column>
								<h:outputText value="#{msg.name}" />
							</rich:column>
							<rich:column>
								<h:outputText value="#{msg.daysOfWeek}" />
							</rich:column>
							<rich:column />
						</rich:columnGroup>
					</f:facet>

					<rich:column>
						<h:outputText value="#{route.name}" />
					</rich:column>

					<rich:column>
						<a4j:repeat var="routeDay" value="#{route.routeDays}">
							<h:outputText
								value="#{routeController.dayNames[routeDay.day.name]}" />
							<h:outputText value="&#160;" />
						</a4j:repeat>
					</rich:column>

					<rich:column>
						<h:commandLink value="#{msg.edit}"
							action="#{routeController.edit}" />
						<h:outputText value="&#160;" />
						<h:commandLink value="#{msg.remove}"
							action="#{routeController.delete}" />
					</rich:column>

				</rich:dataTable>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>