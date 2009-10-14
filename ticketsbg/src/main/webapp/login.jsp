<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core" template="publicTemplate.jsp">
    <ui:define name="body">
        <a4j:form ajaxSubmit="true">
            <rich:panel header="#{msg.login}"
                headerClass="rich-panel-header-main">
                <ui:include src="loginFields.jsp">
                    <ui:param name="isAdmin" value="false" />
                </ui:include>
                <h:inputHidden id="admin"
                    value="false" binding="#{loginController.admin}" />
            </rich:panel>
        </a4j:form>
    </ui:define>
</ui:composition>