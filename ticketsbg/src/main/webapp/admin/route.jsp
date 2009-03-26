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
	<ui:define name="header">
		<style>
			.gridContent {
				vertical-align: top;
			}
			
			.internalPanel {
				height: 230px;
				text-align: center;
			}
		</style>
	</ui:define>
	<ui:define name="body">
		<f:view>
			<h:messages />
			<h:form id="routeForm">
				<div align="center"><h:panelGrid columns="1">
					<rich:panel>
						<a4j:outputPanel>
							<h:outputLabel value="#{msg.routeName}: " for="routeName" />
							<rich:toolTip value="#{msg.routeNameHint}" followMouse="true" />
						</a4j:outputPanel>
						<h:inputText value="#{routeController.route.name}" id="routeName" />

					</rich:panel>
					<rich:panel header="#{msg.routeDetailsHeader}">
						<h:panelGrid columns="3" columnClasses="gridContent">
							<rich:panel header="#{msg.daysOfWeek}" styleClass="internalPanel">
								<rich:pickList showButtonsLabel="false" id="daysPickList"
									value="#{routeController.daysPickList}"
									converter="javax.faces.Integer">
									<c:forEach var="day" items="${routeController.days}">
										<f:selectItem itemLabel="#{day.label}" itemValue="${day.id}" />
									</c:forEach>
								</rich:pickList>
							</rich:panel>

							<rich:panel header="#{msg.hours}" styleClass="internalPanel">
								<t:inputDate type="short_time" value="#{routeController.hour}" />
								<h:commandLink action="#{routeController.addHour}">
									<h:graphicImage value="/images/add.png" title="#{msg.add}"
										alt="#{msg.add}"
										style="width: 15px; height:15px; border-style: none;" />
								</h:commandLink>
								<br />
								<h:selectOneListbox size="8" converter="javax.faces.Integer"
									value="#{routeController.selectedHour}" style="width: 98px;">
									<c:forEach var="hour"
										items="#{routeController.route.routeHours}">
										<f:selectItem itemLabel="#{hour.displayLabel}"
											itemValue="#{hour.id}" />
									</c:forEach>
								</h:selectOneListbox>
								<br />
								<h:commandButton action="#{routeController.removeHour}"
									value="#{msg.delete}" />
							</rich:panel>

							<rich:panel header="${msg.stops}" styleClass="internalPanel">
								<h:commandButton value="#{msg.addStop}"
									action="#{routeController.addStop}" />
								<rich:orderingList binding="#{routeController.stopsTable}"
									var="stop" value="#{routeController.route.stops}"
									converter="#{stopListConverter}" showButtonLabels="false"
									valueChangeListener="#{routeController.listReordered}"
									listWidth="500" listHeight="150">

									<rich:column>
										<f:facet name="header">
											<h:outputText value="#{msg.stopName}" id="col1" />
										</f:facet>
										<h:outputText value="#{stop.name}" />
									</rich:column>
									<rich:column width="110">
										<f:facet name="header">
											<h:outputText value="#{msg.timeToArrival}" id="col2" />
										</f:facet>
										<h:outputText value="#{stop.timeToArrival}" />
									</rich:column>
									<rich:column width="110">
										<f:facet name="header">
											<h:outputText value="#{msg.timeToDeparture}" id="col3" />
										</f:facet>
										<h:outputText value="#{stop.timeToDeparture}" />
									</rich:column>
									<rich:column width="35">
										<f:facet name="header">
											<h:outputText value="" id="col4" />
										</f:facet>
										<h:commandLink action="#{routeController.editStop}"
											title="#{msg.edit}">
											<h:graphicImage value="/images/edit.png"
												style="width:16; height:16; border-style: none;"
												alt="#{msg.edit}" title="#{msg.edit}" />
										</h:commandLink>
										<h:outputText value="&#160;" />
										<h:commandLink action="#{routeController.deleteStop}"
											title="#{msg.remove}">
											<h:graphicImage value="/images/delete.png"
												style="width:16; height:16; border-style: none;"
												alt="#{msg.remove}" title="#{msg.remove}" />
										</h:commandLink>
									</rich:column>
								</rich:orderingList>
							</rich:panel>
						</h:panelGrid>
					</rich:panel>
					<rich:panel>
						<h:commandButton action="#{routeController.save}"
							value="#{msg.save}">
							<cust:defaultAction />
						</h:commandButton>
						<h:commandButton action="#{routeController.cancel}" value="#{msg.cancel}" />
					</rich:panel>
				</h:panelGrid></div>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>