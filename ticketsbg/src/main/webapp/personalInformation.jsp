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
	template="publicTemplate.jsp">
	<ui:define name="body">
		<f:view>
			<a4j:form id="personalInfoForm">
				<h:messages />
				<a4j:commandLink value="#{msg.loginNow}" onclick="#{rich:component('loginPanel')}.show()" />
				<br />
				<h:outputText value="#{msg.orText}" />
				<br />
				<h:commandLink value="#{msg.registerNow}" />
				<br />
				<h:outputText value="#{msg.orText}" />
				<br />

				<h:panelGroup>
					<!-- a4j:include viewId="register.jsp" /-->
				</h:panelGroup>

				<h:panelGrid columns="2" id="directInputFields">
					<h:outputLabel for="name" value="#{msg.names}" />
					<h:inputText value="#{personalInformationController.name}"
						id="name" />

					<h:outputLabel for="contactPhone" value="#{msg.contactPhone}" />
					<h:inputText value="#{personalInformationController.name}"
						id="contactPhone" />

					<h:outputLabel for="email" value="#{msg.email}" />
					<h:inputText value="#{personalInformationController.email}"
						id="email" />

				</h:panelGrid>
			</a4j:form>
		</f:view>

		<rich:modalPanel id="loginPanel" autosized="true" width="250">
			<f:facet name="header">
				<h:outputText value="#{msg.login}" />
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/images/close.png" id="hidelink"
						styleClass="hidelink" />
					<rich:componentControl for="loginPanel" attachTo="hidelink"
						operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<ui:include src="login_fields.jsp">
			     <ui:param name="admin" value="false" />
			</ui:include>
		</rich:modalPanel>
		
	</ui:define>
</ui:composition>