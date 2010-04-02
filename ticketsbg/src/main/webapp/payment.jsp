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
    <ui:define name="header">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
    </ui:define>
    <ui:define name="body">
        <f:view>
            <h:panelGroup>
                <a4j:form id="paymentForm">
                    <rich:panel header="#{msg.payment}"
                        headerClass="rich-panel-header-main">
                        <ui:include src="messages.jsp">
                            <ui:param name="globalOnly" value="true" />
                        </ui:include>

                        <ui:include src="personalInformation.jsp" />

                        <ui:include src="cart.jsp" />

                        <style type="text/css">
.imageRow {
    height: 50px;
    vertical-align: top;
}

.optionRow {
    hieght: 20px;
}
</style>
                        <rich:panel header="#{msg.payment}" style="vertical-align: top;">
                            <div align="center">

                             <h:panelGrid columns="2" rowClasses="imageRow,optionRow"
                                style="text-align: center;" width="500px">

                                <h:outputLabel id="ePayLabel" for="selectedPaymentMethod">
                                    <h:graphicImage url="/images/epay.jpg" style="border: 0px;"
                                        height="43" alt="#{msg.E_PAY}" title="#{msg.E_PAY}" />
                                </h:outputLabel>

                                <h:panelGroup>
                                    <h:outputLabel id="ccLabel" for="selectedPaymentMethod">
                                        <h:graphicImage url="/images/creditCards.gif"
                                            alt="#{msg.CREDIT_CARD}" title="#{msg.CREDIT_CARD}"
                                            style="border: 0px; margin-top: -2px" height="45" />
                                    </h:outputLabel>
                                </h:panelGroup>

                                <a4j:commandButton action="#{paymentController.pay}"
                                    value="#{msg.pay}" style="font-size: 16px;"
                                    oncomplete="if(#{facesContext.maximumSeverity==null}) {document.getElementById('paymentGatewayForm').submit();}">
                                    <f:setPropertyActionListener value="E_PAY"
                                        target="#{paymentController.selectedPaymentMethod}" />
                                </a4j:commandButton>


                                <a4j:commandButton action="#{paymentController.pay}"
                                    value="#{msg.pay}" style="font-size: 16px;">
                                    <f:setPropertyActionListener value="CREDIT_CARD"
                                        target="#{paymentController.selectedPaymentMethod}" />
                                </a4j:commandButton>
                            </h:panelGrid>

                             <br />
                             <a4j:outputPanel ajaxRendered="true">
                                <h:outputText value="#{msg.validationErrorsAbove}"
                                    rendered="#{facesContext.maximumSeverity!=null}"
                                    styleClass="error" />
                            </a4j:outputPanel>
                            </div>
                        </rich:panel>
                    </rich:panel>

                    <script type="text/javascript">
                        function updatePaymentGatewayPage(method) {
                            var page = "";
                            if (method == "E_PAY") {
                                page = "paylogin";
                            }
                            if (method == "CREDIT_CARD") {
                                page = "credit_paydirect";
                            }
                            document.getElementById("PAGE").value = page;
                        }

                        window.onload=function() {
                            #{rich:element('ePayLabel')}.setAttribute("for", "paymentForm:selectedPaymentMethod:0");
                            #{rich:element('ccLabel')}.setAttribute("for", "paymentForm:selectedPaymentMethod:1");

                            #{rich:element('ePayLabel')}.setAttribute("htmlFor", "paymentForm:selectedPaymentMethod:0");
                            #{rich:element('ccLabel')}.setAttribute("htmlFor", "paymentForm:selectedPaymentMethod:1");
                        }
                    </script>
                </a4j:form>
                <c:set var="url"
                    value="http://#{facesContext.externalContext.request.serverName}:#{facesContext.externalContext.request.serverPort}#{facesContext.externalContext.request.contextPath}" />

                <h:panelGroup id="paymentGatewayFormWrapper">
                    <form action="#{paymentController.epayUrl}"
                        id="paymentGatewayForm"><input type="hidden" id="PAGE" name="PAGE"
                        value="paylogin" /> <input type="hidden" name="LANG" value="bg" />
                    <input type="hidden" name="ENCODED"
                        value="#{paymentController.encoded}" /> <input type="hidden"
                        name="CHECKSUM" value="#{paymentController.checksum}" /> <input
                        type="hidden" name="URL_OK"
                        value="#{url}/paymentSuccess.jsp" /> <input
                        type="hidden" name="URL_CANCEL"
                        value="#{url}/paymentCancelled.jsp" /></form>
                </h:panelGroup>
            </h:panelGroup>
        </f:view>
    </ui:define>
</ui:composition>