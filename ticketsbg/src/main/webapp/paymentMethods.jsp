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
            <rich:panel header="#{msg.paymentMethods}"
                headerClass="rich-panel-header-main" id="statistics">
                <h:panelGrid columns="1" style="text-align: center;">
                    <h:graphicImage url="/images/epay.jpg" />
                    <h:graphicImage url="/images/creditCards.gif" />
                </h:panelGrid>
            </rich:panel>
        </a4j:form>
    </f:view>
</ui:composition>