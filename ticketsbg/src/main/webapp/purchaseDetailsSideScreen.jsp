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
    xmlns:tc="http://tickets.com/tc"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions">
    <f:view>
        <a4j:form rendered="#{tc:getSize(purchaseController.tickets) > 0}"
            id="purchaseDetailsForm">
            <rich:panel header="#{msg.currentTickets}"
                headerClass="rich-panel-header-main" id="currentTickets">
                <a4j:repeat var="ticket" value="#{purchaseController.tickets}">
                #{ticket.startStop}<h:outputText value=" → " />#{ticket.endStop}
                (<h:outputText value="#{ticket.price}">
                        <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                        <f:converter binding="#{currencyConverter}"
                            converterId="currencyConverter" />
                    </h:outputText>)<h:outputText value=" (#{msg.twoWay})" rendered="#{ticket.twoWay}" />
                    <br />
                    <br />
                </a4j:repeat>

                <rich:toolTip value="#{msg.timerTooltip}" for="timeRemaining" />
                <h:panelGrid columns="2" id="timeHolder"
                    rendered="#{purchaseController.tickets != null and tc:getSize(purchaseController.tickets) > 0}">
                    <h:panelGroup>
                        <h:graphicImage url="/images/timer.png"
                            style="width: 16px; height: 16px;" />

                        <a4j:jsFunction name="timeoutPurchase" oncomplete="document.location='search.jsp';"
                            action="#{purchaseController.timeoutPurchase}" />

                        <script type="text/javascript">
                            //<![CDATA[
                                function updateTimer() {
                                    var c = document.getElementById("purchaseDetailsForm:timeRemaining");
                                    var parts = c.innerHTML.split(":");
                                    if (parts[0].substring(0,1) == "0") {
                                        parts[0] = parts[0].substring(1);
                                    }
                                    if (parts[1].substring(0,1) == "0") {
                                        parts[1] = parts[1].substring(1);
                                    }

                                    var sec = parseInt(parts[1]);
                                    var min = parseInt(parts[0]);
                                    if (sec == 0) {
                                        sec = "59";
                                        min --;
                                    } else {
                                        sec --;
                                    }

                                    if (min < 0) {
                                        clearInterval();
                                        timeoutPurchase();
                                    } else {
                                        minStr = "" + min;
                                        if (minStr.length == 1) {
                                            minStr = "0" + minStr;
                                        }
                                        sStr = "" + sec;
                                        if (sStr.length == 1) {
                                            sStr = "0" + sStr;
                                        }
                                        c.innerHTML = minStr + ":" + sStr;
                                    }
                                }
                                setInterval("updateTimer()", 1000);
                            //]]>
                        </script>
                    </h:panelGroup>
                    <h:outputText value="#{purchaseController.timeRemaining}"
                        id="timeRemaining" style="font-weight: bold;">
                        <f:convertDateTime pattern="mm:ss" />
                    </h:outputText>
                </h:panelGrid>
                <h:commandButton value="#{msg.payment}" action="paymentScreen"
                    rendered="#{!fn:contains(request.requestURI, 'payment.jsp')}" />
            </rich:panel>
        </a4j:form>
    </f:view>
</ui:composition>