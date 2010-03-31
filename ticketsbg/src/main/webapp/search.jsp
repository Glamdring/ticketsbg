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

    <ui:define name="head">
        <style type="text/css">
.heightClass {
    height: 315px;
}

.innerColumn {
    vertical-align: top;
    height: 100%;
    margin-bottom: 0px;
    padding-bottom: 0px;
}
</style>
    </ui:define>
    <ui:define name="body">
        <f:view>
            <a4j:form id="searchForm" ajaxSubmit="true">
                <h:panelGrid columns="2" cellpadding="0" cellspacing="0"
                    columnClasses="innerColumn, innerColumn"
                    style="height: 320px; margin: 0px; padding: 0px;">
                    <a4j:outputPanel>
                        <ui:include src="searchFields.jsp">
                            <ui:param name="isAdmin" value="false" />
                        </ui:include>
                        <h:inputHidden id="admin" binding="#{searchController.admin}"
                            value="false" converter="#{booleanConverter}" />
                    </a4j:outputPanel>
                    <rich:panel headerClass="rich-panel-header-main"
                        style="text-align: center; width: 485px; margin-left: 5px; height: 100%;">
                        <f:facet name="header"><h4 style="margin-top: 2px;">#{msg.seoTitle}</h4></f:facet>
                        <h:graphicImage url="/images/banner.png" alt="" style="width: 445px; height: 235px;" />
                    </rich:panel>
                </h:panelGrid>
            </a4j:form>
        </f:view>
    </ui:define>

    <ui:define name="bottomPanel">
        <rich:panel header="#{msg.newsList}" styleClass="news"
            rendered="#{tc:getSize(publicNewsController.freshNews) > 0}">
            <a4j:repeat value="#{publicNewsController.freshNews}" var="news">
                <h:panelGroup layout="block"
                    style="margin-bottom: 5px; clear: both;">
                    <h:outputText style="font-weight: bold; font-size: 14px;"
                        value="#{news.headline}" />
            &#160;&#160;
            <h:outputText value="#{news.date.time}">
                        <f:convertDateTime type="date" pattern="dd.MM.yyyy"
                            timeZone="#{timeZoneController.timeZone}" />
                    </h:outputText>
                </h:panelGroup>

                <h:outputText value="#{tc:formatTextNewLines(news.content)}"
                    escape="false" />
            </a4j:repeat>
        </rich:panel>

    </ui:define>
</ui:composition>
