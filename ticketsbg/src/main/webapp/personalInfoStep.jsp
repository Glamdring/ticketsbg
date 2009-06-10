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
	template="basicTemplate.jsp">

	<ui:define name="header">
		<style type="text/css">
.tableHeader {
	border-style: none;
	background-color: white;
}

.columnClass {
	border-style: none;
	padding: 0px;
	margin: 0px;
}
</style>
	</ui:define>
	<ui:define name="body">
		<f:view>
			<a4j:form id="personalInfoForm">
				<h:messages />
				
			</a4j:form>
		</f:view>
	</ui:define>
</ui:composition>