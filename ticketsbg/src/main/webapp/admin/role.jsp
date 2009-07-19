<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:j4j="http://javascript4jsf.dev.java.net/"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:cust="http://abax.bg/cust"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<style>
.gridContent {
	vertical-align: top;
}
</style>
	<f:view>
		<h:messages />
		<h:form id="roleForm">
			<a4j:outputPanel ajaxRendered="true">
				<h:panelGrid columns="2" styleClass="gridContent">
					<h:outputLabel for="name" value="#{msg.roleName}: " />
					<h:inputText value="#{roleController.role.name}" id="name" />

					<h:outputLabel for="description" value="#{msg.description}: " />
					<h:inputTextarea value="#{roleController.role.description}"
						id="description" rows="4" cols="20" />

					<h:outputText value="" />
					<a4j:commandButton value="${msg.save}"
						action="#{roleController.save}"
						oncomplete="#{rich:component('entityPanel')}.hide()"
						reRender="rolesTable" />
				</h:panelGrid>
			</a4j:outputPanel>
		</h:form>
	</f:view>
</ui:composition>