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
                    <a4j:commandLink value="#{msg.busCompanies}:"
                        style="font-size: 14px;"
                        onclick="#{rich:component('companiesPanel')}.show()" />
                    <h:outputText value="#{publicStatsController.companiesCount}"
                        style="font-size: 14px;" />

                    <h:outputText value="#{msg.destinations}:" style="font-size: 14px;" />
                    <h:outputText value="#{publicStatsController.destinations}"
                        style="font-size: 14px;" />

                </h:panelGrid>

                <!-- No facebook fanbox needed -->
                <!--
                <div style="margin: 0px -10px 0px -10px; padding: 0px;">
                    <h:outputText
                        value="#{msg['lt']}fb:fan profile_id='216346729251' stream='0' connections='0' width='185' height='80'#{msg['gt']}#{msg['lt']}/fb:fan#{msg['gt']}"
                        escape="false" />
                 </div>
                  -->
            </rich:panel>

            <rich:modalPanel id="companiesPanel" autosized="true" width="250"
                height="120"
                onmaskclick="#{rich:component('companiesPanel')}.hide();">
                <ui:include src="modalPanelCommons.jsp">
                    <ui:param name="dialogId" value="companiesPanel" />
                </ui:include>
                <f:facet name="header">
                    #{msg.busCompanies}
                </f:facet>
                <h:panelGroup style="overflow: auto;" layout="block">
                    <a href="http://mbus-bg.com/" target="_blank">
                        <h:graphicImage url="/images/companies/mbus.jpg" alt="" style="border-style: none;" />
                    </a>
                </h:panelGroup>
            </rich:modalPanel>
        </a4j:form>
    </f:view>
</ui:composition>