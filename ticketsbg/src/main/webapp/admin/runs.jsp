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
	template="admin_template.jsp">
	<ui:define name="body">
		<f:view>
			<h:form id="runsForm">
				<h:messages />
				<rich:panel style="border-style: none;">
					<h:outputText value="#{msg.runsForRoute} " />
					<h:outputText value="#{runController.route.name}"
						style="font-weight: bold;" />
				</rich:panel>

				<rich:dataTable
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					id="runsTable" onRowMouseOut="this.style.backgroundColor='white'"
					cellpadding="0" cellspacing="0" width="300" border="0" var="run"
					value="#{runController.route.runs}" rows="20">

					<f:facet name="header">
						<rich:columnGroup>
							<rich:column>
								<h:outputText value="#" />
							</rich:column>
							<rich:column>
								<h:outputText value="#{msg.dateTime}" />
							</rich:column>
							<rich:column width="35" />
						</rich:columnGroup>
					</f:facet>

                    <rich:column>
                        <h:outputText value="#{run.runId}" />
                    </rich:column>
                    
					<rich:column>
						<h:outputText value="#{run.time.time}">
							<f:convertDateTime pattern="dd.MM.yyyy hh:mm" />
						</h:outputText>
					</rich:column>

					<rich:column>
						<h:commandLink action="#{runController.delete}"
							title="#{msg.remove}">
							<f:setPropertyActionListener value="#{run}"
								target="#{runController.run}" />
							<h:graphicImage value="/images/delete.png"
								style="width:16; height:16; border-style: none;"
								alt="#{msg.remove}" title="#{msg.remove}" />
						</h:commandLink>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller align="center" maxPages="20"
							page="#{runController.page}" id="scroller" />

					</f:facet>
				</rich:dataTable>
			</h:form>
		</f:view>
	</ui:define>
</ui:composition>