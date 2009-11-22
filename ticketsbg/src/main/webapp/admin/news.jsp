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
        <h:form id="newsForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2" styleClass="gridContent">
                    <h:outputLabel for="headline" value="#{msg.headline}: " />
                    <h:inputText value="#{newsController.news.headline}"
                        id="shortText" size="30" />

                    <h:outputLabel for="content" value="#{msg.content}: " />
                    <h:inputTextarea value="#{newsController.news.content}"
                        id="content" rows="6" cols="30" />

                    <h:outputLabel for="startDate" value="#{msg.date}: " />
                    <rich:calendar value="#{newsController.news.date.time}"
                        id="startDate" datePattern="dd.MM.yyyy" firstWeekDay="1"
                        boundaryDatesMode="scroll" />


                    <h:outputText value="" />
                    <a4j:commandButton value="${msg.save}"
                        action="#{newsController.save}"
                        oncomplete="if (#{rich:component('entityPanel')} != null) {#{rich:component('entityPanel')}.hide();} #{rich:component('entityPanel')}.hide();"
                        reRender="newsListTable" />
                </h:panelGrid>
            </a4j:outputPanel>
        </h:form>

    </f:view>
</ui:composition>