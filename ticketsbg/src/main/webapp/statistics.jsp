<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk">
    <f:view>
        <a4j:form>
            <rich:panel header=" "
                headerClass="rich-panel-header-main" id="statistics">
                <h:panelGrid columns="2">
                    <h:outputText value="#{msg.busCompanies}:" style="font-size: 14px;" />
                    <h:outputText value="#{publicStatsController.companiesCount}"
                        style="font-size: 14px;" />

                    <h:outputText value="#{msg.destinations}:" style="font-size: 14px;" />
                    <h:outputText value="#{publicStatsController.destinations}"
                        style="font-size: 14px;" />

                </h:panelGrid>

                <iframe scrolling="no" frameborder="0"
                    src="http://www.facebook.com/connect/connect.php?id=216346729251&amp;connections=0&amp;stream=0&amp;css=PATH_TO_STYLE_SHEET&amp;locale=bg_BG"
                    allowtransparency="true"
                    style="border: none; width: 185px; height: 80px; margin: 0px -10px 0px -10px; padding: 0px;"></iframe>

            </rich:panel>
        </a4j:form>
    </f:view>
</ui:composition>