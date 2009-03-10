<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	template="admin_template.jsp">
	<ui:define name="body">
		<f:view>
			<h:form id="routeForm">
				<h:messages />
				<h:commandLink value="#{msg.add}"
					action="#{routeController.newRoute}" />
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
						<h:outputText value="&#160;" />
						<h:commandLink value="#{msg.addCourse}"
							action="#{routeController.addCourse}" />
					</rich:column>


					<!-- rich:subTable
            onRowMouseOver="this.style.backgroundColor='#F8F8F8'"
            onRowMouseOut="this.style.backgroundColor='grey'" var="route"
            value="#{record.items}">
            <rich:column>
              <h:outputText value="#{route.name}"></h:outputText>
            </rich:column>
            <rich:column>
              <h:commandLink action="#{routeController.edit}">
                <h:outputText value="#{msg.edit}" />
              </h:commandLink>
              <h:commandLink value="#{msg.remove}"
                action="#{routeController.delete}">
              </h:commandLink>
            </rich:column>
          </rich:subTable-->
				</rich:dataTable>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>