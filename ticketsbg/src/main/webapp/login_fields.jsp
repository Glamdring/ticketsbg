<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">
	<f:view>
		<h:messages />
		<h:form>
			<h:panelGrid columns="2">
				<h:outputLabel value="#{msg.username}" for="username" />
				<h:inputText id="username" value="#{loginController.username}" />

				<h:outputLabel value="#{msg.password}" for="password" />
				<h:inputSecret id="password" value="#{loginController.password}" />

				<h:inputHidden id="admin" converter="#{booleanConverter}"
					value="#{isAdmin}" binding="#{loginController.admin}" />
				<h:commandButton action="#{loginController.login}" type="submit"
					value="#{msg.login}" />
			</h:panelGrid>
		</h:form>
	</f:view>
</ui:composition>