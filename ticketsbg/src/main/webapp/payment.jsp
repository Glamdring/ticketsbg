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
                        <rich:messages globalOnly="true" />

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

                                <t:selectOneRadio layout="spread" onchange="updatePaymentGatewayPage(this.value);"
                                    id="selectedPaymentMethod" required="true"
                                    value="#{paymentController.selectedPaymentMethod}">
                                    <f:selectItem itemValue="E_PAY" itemLabel="" />
                                    <f:selectItem itemValue="CREDIT_CARD" itemLabel="" />
                                </t:selectOneRadio>
                             <h:panelGrid columns="2" rowClasses="imageRow,optionRow"
                                style="text-align: center;" width="500px">

                                <h:outputLabel id="ePayLabel">
                                    <h:graphicImage url="/images/epay.jpg" style="border: 0px;"
                                        height="43" />
                                </h:outputLabel>

                                <h:panelGroup>
                                    <h:outputLabel id="ccLabel">
                                        <h:graphicImage url="/images/creditCards.gif"
                                            style="border: 0px; margin-top: -2px" height="45" />
                                        <br />
                                        <h:outputText value="#{msg.serviceFee}: "
                                            style="font-size: 10px;" />
                                        <h:outputText value="#{paymentController.serviceFee}"
                                            style="color: red; font-size: 10px;">
                                            <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                            <f:converter binding="#{currencyConverter}"
                                                converterId="currencyConverter" />
                                        </h:outputText>
                                    </h:outputLabel>
                                </h:panelGroup>

                                <t:radio for="selectedPaymentMethod" index="0" />
                                <t:radio for="selectedPaymentMethod" index="1" />

                            </h:panelGrid>

                            <a4j:commandButton action="#{paymentController.pay}"
                                value="#{msg.pay}" style="font-size: 16px;"
                                oncomplete="if(#{facesContext.maximumSeverity==null}) {document.getElementById('paymentGatewayForm').submit();}" />

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
                <h:panelGroup id="paymentGatewayFormWrapper">
                    <form action="https://devep2.datamax.bg/ep2/epay2_demo/"
                        id="paymentGatewayForm"><input type="hidden" id="PAGE" name="PAGE"
                        value="paylogin" /> <input type="hidden" name="LANG" value="bg" />
                    <input type="hidden" name="ENCODED"
                        value="#{paymentController.encoded}" /> <input type="hidden"
                        name="CHECKSUM" value="#{paymentController.checksum}" /> <input
                        type="hidden" name="URL_OK"
                        value="http://tickets.bg/paymentSuccess.jsp" /> <input
                        type="hidden" name="URL_CANCEL"
                        value="http://tickets.bg/paymentCancelled.jsp" /></form>
                </h:panelGroup>
            </h:panelGroup>
        </f:view>
    </ui:define>
</ui:composition>