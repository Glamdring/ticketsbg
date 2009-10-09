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
            <h:panelGroup>
                <a4j:form id="paymentForm">
                    <rich:panel header="#{msg.payment}"
                        headerClass="rich-panel-header-main">
                        <h:messages />

                        <ui:include src="personalInformation.jsp" />

                        <ui:include src="cart.jsp" />

                        <rich:panel header="#{msg.payment}">
                            <div align="center"><h:panelGrid columns="2" width="400px"
                                style="text-align: center;">
                                <a4j:commandLink action="#{paymentController.buy}"
                                    oncomplete="if(#{facesContext.maximumSeverity==null}) {document.getElementById('ePayForm').submit();}">
                                    <h:graphicImage url="/images/epay.jpg" style="border: 0px;"
                                        height="43" />
                                </a4j:commandLink>

                                <a4j:commandLink actionListener="#{paymentController.buy}"
                                    oncomplete="if(#{facesContext.maximumSeverity==null}) {document.getElementById('creditCardForm').submit();}">
                                    <h:graphicImage url="/images/creditCards.gif"
                                        style="border: 0px;" height="45" />
                                </a4j:commandLink>
                            </h:panelGrid></div>
                        </rich:panel>
                    </rich:panel>
                </a4j:form>
                <form action="https://devep2.datamax.bg/ep2/epay2_demo/"
                    id="ePayForm"><input type="hidden" name="PAGE"
                    value="paylogin" /> <input type="hidden" name="ENCODED"
                    value="[ENCODED]" /> <input type="hidden" name="CHECKSUM"
                    value="[CHECKSUM]" /> <input type="hidden" name="URL_OK"
                    value="http://..." /> <input type="hidden" name="URL_CANCEL"
                    value="http://..." /></form>
            </h:panelGroup>
        </f:view>
    </ui:define>
</ui:composition>