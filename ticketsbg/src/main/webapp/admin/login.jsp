<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core" template="adminTemplate.jsp">
	<ui:define name="body">
		<a4j:form ajaxSubmit="true">
			<ui:include src="../loginFields.jsp">
				<ui:param name="isAdmin" value="true" />
			</ui:include>
			<h:inputHidden id="admin" converter="#{booleanConverter}"
                value="true" binding="#{loginController.admin}" />
		</a4j:form>
	</ui:define>
</ui:composition>