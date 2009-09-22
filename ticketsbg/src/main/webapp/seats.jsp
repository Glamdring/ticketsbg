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

    <style type="text/css">
.seat {
    width: 38px;
    height: 35px;
    text-align: center;
    vertical-align: middle;
}

.isle {
    width: 20px;
    height: 35px;
    padding: 0px;
    margin: 0px;
    text-align: center;
    vertical-align: middle;
}

.hyperlink {
    color: darkblue;
    text-decoration: underline;
    cursor: pointer;
}

.separatorRow {
    width: 38px;
    height: 30px;
    padding: 0px;
    margin: 0px;
    background-color: #C8DCF9;
}
</style>
    <c:set var="handler" value="seatHandler" />
    <c:if test="#{return}">
        <c:set var="handler" value="returnSeatHandler" />
    </c:if>

    <h:outputText
        value="#{seatController[handler].run == null ? msg.showSeatsView : msg.chooseSeat}"
        styleClass="hyperlink">
        <rich:componentControl for="seatsModalPanel#{modifier}"
            event="onclick" operation="show" />
    </h:outputText>

    <rich:modalPanel id="seatsModalPanel#{modifier}" autosized="true">
        <f:facet name="header">
            <h:outputText value="#{msg.seats}" />
        </f:facet>
        <f:facet name="controls">
            <h:panelGroup>
                <h:graphicImage value="/images/close.png" id="hidelink#{modifier}"
                    styleClass="hidelink" />
                <rich:componentControl for="seatsModalPanel#{modifier}"
                    attachTo="hidelink#{modifier}" operation="hide" event="onclick" />
            </h:panelGroup>
        </f:facet>
        <a4j:form ajaxSubmit="true">
            <a4j:outputPanel id="seatsViewInner#{modifier}" ajaxRendered="true">
                <rich:dataTable value="#{seatController[handler].rows}" var="row"
                style="table-layout: fixed; width: 178px;">
                    <t:selectManyCheckbox layout="spread" id="selectedSeats#{modifier}"
                        value="#{seatController[handler].selectedSeats}"
                        converter="#{seatConverter}">
                        <f:selectItems value="#{seatController[handler].seatSelectItems}" />
                        <a4j:support event="onchange" />
                    </t:selectManyCheckbox>

                    <rich:column styleClass="seat">
                        <t:checkbox index="#{row.first.id - 1}"
                            for="selectedSeats#{modifier}" rendered="#{!row.separator}" />
                        <h:panelGroup rendered="#{row.separator}" layout="block"
                            styleClass="separatorRow">&#160;</h:panelGroup>
                    </rich:column>

                    <rich:column styleClass="seat">
                        <t:checkbox index="#{row.second.id - 1}"
                            for="selectedSeats#{modifier}" rendered="#{!row.separator}" />
                        <h:panelGroup rendered="#{row.separator}" layout="block"
                            styleClass="separatorRow">&#160;</h:panelGroup>
                    </rich:column>

                    <rich:column styleClass="isle" style="vertical-align: top;">
                        <t:checkbox index="#{row.middleSeat.id - 1}"
                            for="selectedSeats#{modifier}"
                            rendered="#{row.middleSeat != null and !row.separator}" />
                        <h:panelGroup rendered="#{row.separator}" layout="block"
                            styleClass="separatorRow">&#160;</h:panelGroup>
                    </rich:column>

                    <rich:column styleClass="seat">
                        <t:checkbox index="#{row.third.id - 1}"
                            for="selectedSeats#{modifier}" rendered="#{!row.separator}" />
                        <h:panelGroup rendered="#{row.separator}" layout="block"
                            styleClass="separatorRow">&#160;</h:panelGroup>
                    </rich:column>

                    <rich:column styleClass="seat">
                        <t:checkbox index="#{row.fourth.id - 1}"
                            for="selectedSeats#{modifier}" rendered="#{!row.separator}" />
                        <h:panelGroup rendered="#{row.separator}" layout="block"
                            styleClass="separatorRow">&#160;</h:panelGroup>
                    </rich:column>
                </rich:dataTable>
            </a4j:outputPanel>
            <div align="center"><a4j:commandButton value="OK" id="okButton">
                <rich:componentControl for="seatsModalPanel#{modifier}"
                    event="onclick" operation="hide" />
            </a4j:commandButton></div>
        </a4j:form>
    </rich:modalPanel>
</ui:composition>