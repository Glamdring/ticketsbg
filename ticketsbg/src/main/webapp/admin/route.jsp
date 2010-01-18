<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:tc="http://tickets.com/tc"
    xmlns:t="http://myfaces.apache.org/tomahawk"
    template="adminTemplate.jsp">
    <ui:define name="head">
        <style type="text/css">
.internalPanel {
    height: 230px;
    overflow: auto;
    text-align: left;
}

.downInternalPanel {
    height: 270px;
    overflow: auto;
    text-align: left;
}

.subInternalPanel {
    height: 230px;
    overflow: auto;
    text-align: left;
}
</style>
    </ui:define>
    <ui:define name="body">
        <f:view>
            <h:messages />
            <h:form id="routeForm">
                <div align="center"><h:panelGrid columns="1">
                    <rich:panel id="mainInfoPanel" style="text-align: left;">
                        <h:panelGrid columns="2" style="width: 100%;">
                            <h:panelGroup>
                                <a4j:outputPanel>
                                    <h:outputLabel value="#{msg.routeName}: " for="routeName" />
                                    <rich:toolTip value="#{msg.routeNameHint}" followMouse="true" />
                                </a4j:outputPanel>
                                <h:inputText value="#{routeController.route.name}"
                                    id="routeName" />
                            </h:panelGroup>

                            <h:panelGroup style="float: right;">

                                <h:panelGroup style="margin-right: 25px;">
                                    <h:outputLabel value="#{msg.stopSalesBeforeRunInHours}: "
                                        for="stopSalesBeforeRunInHours" />
                                    <h:inputText
                                        value="#{routeController.route.stopSalesBeforeRunInHours}"
                                        id="stopSalesBeforeRunInHours" size="3" />
                                </h:panelGroup>

                                <h:panelGroup style="margin-right: 25px;">
                                    <h:outputLabel for="requireReceiptAtCashDesk"
                                        value="#{msg.requireReceiptAtCashDesk}: "
                                        id="requireReceiptAtCashDeskLabel" />
                                    <rich:toolTip value="#{msg.requireReceiptAtCashDeskInfo}"
                                        for="requireReceiptAtCashDeskLabel" followMouse="true" />
                                    <h:selectBooleanCheckbox
                                        value="#{routeController.route.requireReceiptAtCashDesk}"
                                        id="requireReceiptAtCashDesk" />
                                </h:panelGroup>

                                <h:outputText value="#{msg.doOnlineSale} " />
                                <h:outputLabel value="#{msg.sellSeatsFrom}: "
                                    for="sellSeatsFrom" />
                                <h:inputText value="#{routeController.route.sellSeatsFrom}"
                                    id="sellSeatsFrom" size="3" />

                                <h:outputLabel value=" #{msg.sellSeatsTo}: " for="sellSeatsTo" />
                                <h:inputText value="#{routeController.route.sellSeatsTo}"
                                    id="sellSeatsTo" size="3" />
                            </h:panelGroup>
                        </h:panelGrid>
                    </rich:panel>

                    <rich:panel header="#{msg.routeDetailsHeader}" id="routeDetails"
                        style="text-align: left;">
                        <h:panelGrid columns="3"
                            columnClasses="gridContent,gridContent,gridContent">
                            <rich:panel header="#{msg.daysOfWeek}" styleClass="internalPanel"
                                id="daysPanel">
                                <rich:pickList showButtonsLabel="false" id="daysPickList"
                                    value="#{routeController.daysPickList}"
                                    converter="javax.faces.Integer"
                                    disabled="#{routeController.route.singleRun == true}">
                                    <c:forEach var="day" items="${routeController.days}">
                                        <f:selectItem itemLabel="#{day.label}" itemValue="${day.id}" />
                                    </c:forEach>
                                </rich:pickList>
                            </rich:panel>

                            <rich:panel header="#{msg.hours}" styleClass="internalPanel"
                                id="hoursPanel">
                                <t:inputDate type="short_time" value="#{routeController.hour}" />
                                <a4j:commandLink action="#{routeController.addHour}"
                                    reRender="hoursList"
                                    rendered="#{!routeController.route.singleRun}">
                                    <h:graphicImage value="/images/add.png" title="#{msg.add}"
                                        alt="#{msg.add}"
                                        style="width: 15px; height:15px; border-style: none;" />
                                </a4j:commandLink>
                                <br />
                                <h:selectOneListbox size="8" converter="javax.faces.Integer"
                                    value="#{routeController.selectedHour}" style="width: 98px;"
                                    id="hoursList">
                                    <c:forEach var="hour" varStatus="stat"
                                        items="#{routeController.route.routeHours}">
                                        <c:choose>
                                            <c:when test="#{hour.id > 0}">
                                                <f:selectItem itemLabel="#{hour.displayLabel}"
                                                    itemValue="#{hour.id}" />
                                            </c:when>
                                            <c:otherwise>
                                                <f:selectItem itemLabel="#{hour.displayLabel}"
                                                    itemValue="#{-stat.index}" />
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </h:selectOneListbox>
                                <br />
                                <a4j:commandButton action="#{routeController.removeHour}"
                                    value="#{msg.delete}" reRender="hoursList"
                                    disabled="#{routeController.route.singleRun == true}" />
                            </rich:panel>

                            <rich:panel header="${msg.stops}" styleClass="internalPanel"
                                id="stopsPanel">
                                <a4j:commandButton value="#{msg.addStop}"
                                    action="#{routeController.addStop}"
                                    oncomplete="#{rich:component('stopPanel')}.show()" />

                                <a4j:jsFunction name="stopOrderChanged" reRender="pricesPanel"
                                    action="#{routeController.listReordered}" immediate="true" />

                                <rich:orderingList binding="#{routeController.stopsTable}"
                                    var="stop" value="#{routeController.stopsInitialList}"
                                    converter="#{stopListConverter}" showButtonLabels="false"
                                    listWidth="500" listHeight="150" id="stopsTable"
                                    onorderchanged="stopOrderChanged();">

                                    <a4j:support event="onorderchanged" ajaxSingle="true"
                                        immediate="true" />

                                    <rich:column>
                                        <f:facet name="header">
                                            <h:outputText value="#{msg.stopName}" id="col1" />
                                        </f:facet>
                                        <h:outputText value="#{stop.name}" />
                                    </rich:column>
                                    <rich:column width="110">
                                        <f:facet name="header">
                                            <h:outputText value="#{msg.timeToArrival}" id="col2" />
                                        </f:facet>
                                        <h:outputText value="#{stop.timeToArrival}"
                                            converter="#{timeToMinutesConverter}" />
                                    </rich:column>
                                    <rich:column width="120">
                                        <f:facet name="header">
                                            <h:outputText value="#{msg.timeToDeparture}" id="col3" />
                                        </f:facet>
                                        <h:outputText value="#{stop.timeToDeparture}"
                                            converter="#{timeToMinutesConverter}" />
                                    </rich:column>
                                    <rich:column width="35">
                                        <f:facet name="header">
                                            <h:outputText value="" id="col4" />
                                        </f:facet>
                                        <a4j:commandLink
                                            oncomplete="#{rich:component('stopPanel')}.show()"
                                            title="#{msg.edit}">
                                            <h:graphicImage value="/images/edit.png"
                                                style="width:16px; height:16px; border-style: none;"
                                                alt="#{msg.edit}" title="#{msg.edit}" />

                                            <f:setPropertyActionListener value="#{stop}"
                                                target="#{routeController.stop}" />
                                        </a4j:commandLink>
                                        <h:outputText value="&#160;" />
                                        <a4j:commandLink action="#{routeController.deleteStop}"
                                            title="#{msg.remove}" reRender="stopsTable"
                                            oncomplete="stopOrderChanged();">
                                            <h:graphicImage value="/images/delete.png"
                                                style="width:16px; height:16px; border-style: none;"
                                                alt="#{msg.remove}" title="#{msg.remove}" />
                                        </a4j:commandLink>
                                    </rich:column>
                                </rich:orderingList>
                            </rich:panel>
                        </h:panelGrid>
                    </rich:panel>

                    <rich:panel id="pricesAndSettingsPanel">
                        <h:panelGrid columns="3"
                            columnClasses="gridContent,gridContent,gridContent">
                            <rich:panel header="#{msg.prices}" id="pricesPanel"
                                rendered="#{routeController.route.id > 0}"
                                styleClass="downInternalPanel">
                                <h:panelGrid columns="2" columnClasses="gridContent">
                                    <rich:panel styleClass="subInternalPanel" style="width: 250px;">
                                        <rich:tree switchType="client" ajaxSubmitSelection="true"
                                            style="width:210px;"
                                            value="#{routeController.pricesTreeData}" var="data"
                                            nodeFace="#{data.leaf ? 'end' : 'start'}" id="pricesTree"
                                            nodeSelectListener="#{routeController.nodeSelected}">

                                            <rich:treeNode type="start"
                                                reRender="priceField,twoWayPriceField,savePriceButton">
                                                <div style="font-size: 11px;"><h:outputText
                                                    value="#{msg.fromStop} " /> <h:outputText
                                                    value="#{data.stop.name}" /></div>
                                            </rich:treeNode>
                                            <rich:treeNode type="end"
                                                reRender="priceField,twoWayPriceField,savePriceButton">
                                                <div style="font-size: 11px;"><h:outputText
                                                    value="#{msg.toStop} " /> <h:outputText
                                                    value="#{data.stop.name}" /> <h:outputText value=" (" />
                                                <h:outputText value="#{data.price.price}"
                                                    converter="#{currencyConverter}">
                                                    <f:convertNumber maxFractionDigits="2"
                                                        minFractionDigits="2" />
                                                </h:outputText> <h:outputText value=")" /></div>
                                            </rich:treeNode>
                                        </rich:tree>
                                    </rich:panel>
                                    <rich:panel styleClass="subInternalPanel">
                                        <h:panelGrid columns="2" styleClass="dr-pnl-b"
                                            style="padding:0px; margin:0px;">
                                            <a4j:outputPanel>
                                                <h:outputLabel value="#{msg.oneWay}:" for="priceField" />
                                                <rich:toolTip value="#{msg.zeroPriceTip}" followMouse="true" />
                                            </a4j:outputPanel>
                                            <h:inputText value="#{routeController.priceValue}"
                                                id="priceField" size="10">
                                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                            </h:inputText>

                                            <a4j:outputPanel>
                                                <h:outputLabel value="#{msg.twoWay}:" for="twoWayPriceField" />
                                                <rich:toolTip value="#{msg.zeroPriceTip}" followMouse="true" />
                                            </a4j:outputPanel>
                                            <h:inputText value="#{routeController.twoWayPriceValue}"
                                                id="twoWayPriceField" size="10">
                                                <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                            </h:inputText>

                                            <h:outputText />
                                            <a4j:commandButton value="#{msg.save}" id="savePriceButton"
                                                action="#{routeController.savePrice}" reRender="pricesTree"
                                                onclick="if (#{routeController.price == null}) {alert('#{msg.subrouteMustBeSelected}');}" />
                                        </h:panelGrid>
                                    </rich:panel>
                                </h:panelGrid>
                            </rich:panel>

                            <rich:panel styleClass="downInternalPanel"
                                header="#{msg.discounts}" id="discountsPanel">
                                <a4j:commandButton value="#{msg.addDiscount}"
                                    action="#{routeController.addDiscount}"
                                    oncomplete="#{rich:component('discountPanel')}.show()" />
                                <rich:dataTable value="#{routeController.route.discounts}"
                                    var="discount"
                                    onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                    onRowMouseOut="this.style.backgroundColor='white'"
                                    cellpadding="0" cellspacing="0" border="0" id="discountsTable"
                                    width="310px">
                                    <rich:column>
                                        <f:facet name="header">
                                            #{msg.discountName}
                                           </f:facet>
                                        #{discount.name}
                                    </rich:column>

                                    <rich:column>
                                        <f:facet name="header">
                                            #{msg.discountType}
                                           </f:facet>
                                        <h:outputText value="-"
                                            rendered="#{discount.discountType == 'FIXED'}" />
                                        <h:outputText value="%"
                                            rendered="#{discount.discountType == 'PERCENTAGE'}" />
                                    </rich:column>

                                    <rich:column>
                                        <f:facet name="header">
                                            #{msg.discountValueShort}
                                        </f:facet>
                                        <h:outputText value="#{discount.value}">
                                            <f:convertNumber maxFractionDigits="2" minFractionDigits="2" />
                                        </h:outputText>
                                    </rich:column>

                                    <rich:column>
                                        <f:facet name="header">
                                            #{msg.discountTwoWayValueShort}
                                        </f:facet>
                                        <h:outputText value="#{discount.twoWayValue}">
                                            <f:convertNumber maxFractionDigits="2" minFractionDigits="2" />
                                        </h:outputText>
                                    </rich:column>

                                    <rich:column>
                                        <f:facet name="header">
                                        </f:facet>
                                        <a4j:commandLink
                                            oncomplete="#{rich:component('discountPanel')}.show()"
                                            title="#{msg.edit}">
                                            <h:graphicImage value="/images/edit.png"
                                                style="width:16px; height:16px; border-style: none;"
                                                alt="#{msg.edit}" title="#{msg.edit}" />

                                            <f:setPropertyActionListener value="#{discount}"
                                                target="#{routeController.discount}" />
                                        </a4j:commandLink>
                                        <h:outputText value="&#160;" />
                                        <a4j:commandLink action="#{routeController.deleteDiscount}"
                                            title="#{msg.remove}" reRender="discountsPanel">
                                            <h:graphicImage value="/images/delete.png"
                                                style="width:16px; height:16px; border-style: none;"
                                                alt="#{msg.remove}" title="#{msg.remove}" />
                                            <f:setPropertyActionListener value="#{discount}"
                                                target="#{routeController.discount}" />
                                        </a4j:commandLink>
                                    </rich:column>

                                </rich:dataTable>
                            </rich:panel>


                            <rich:panel styleClass="downInternalPanel"
                                header="#{msg.routeSettings}" id="routeSettingsPanel">
                                <h:panelGrid columns="2" styleClass="dr-pnl-b"
                                    style="padding:0px; margin:0px;">
                                    <h:outputLabel for="seats" value="#{msg.seats}: " />
                                    <h:inputText value="#{routeController.route.seats}" id="seats"
                                        size="18">
                                        <f:convertNumber maxFractionDigits="0" />
                                    </h:inputText>

                                    <h:outputLabel for="allowSeatChoice"
                                        value="#{msg.allowSeatChoice}: " />
                                    <h:selectBooleanCheckbox
                                        value="#{routeController.route.allowSeatChoice}"
                                        id="allowSeatChoice" />

                                    <h:outputText>
                                        <hr />
                                    </h:outputText>
                                    <h:outputText>
                                        <hr />
                                    </h:outputText>

                                    <a4j:outputPanel>
                                        <h:outputLabel for="publishedRunsPeriod"
                                            value="#{msg.publishedRunsPeriod}: " />
                                        <rich:toolTip value="#{msg.publishedRunsPeriodInfo}"
                                            followMouse="true" />
                                    </a4j:outputPanel>
                                    <h:inputText
                                        value="#{routeController.route.publishedRunsPeriod}"
                                        id="publishedRunsPeriod" size="18">
                                        <f:convertNumber maxFractionDigits="0" />
                                    </h:inputText>

                                    <h:outputText>
                                        <hr />
                                    </h:outputText>
                                    <h:outputText>
                                        <hr />
                                    </h:outputText>

                                    <h:outputLabel for="singleRun" value="#{msg.singleRun}: " />
                                    <h:selectBooleanCheckbox
                                        value="#{routeController.route.singleRun}" id="singleRun">
                                        <a4j:support event="onchange" ajaxSingle="true"
                                            reRender="singleRunDateTimeLabel,singleRunDateTimeCalendar,hoursPanel,daysPanel" />
                                    </h:selectBooleanCheckbox>

                                    <a4j:outputPanel id="singleRunDateTimeLabel">
                                        <h:outputLabel for="singleRunDateTime"
                                            value="#{msg.singleRunDateTime}: "
                                            rendered="#{routeController.route.singleRun == true}" />
                                    </a4j:outputPanel>
                                    <a4j:outputPanel id="singleRunDateTimeCalendar">
                                        <rich:calendar id="singleRunDateTime"
                                            datePattern="dd.MM.yyyy HH:mm" firstWeekDay="1"
                                            inputSize="14"
                                            value="#{routeController.route.singleRunDateTime.time}"
                                            rendered="#{routeController.route.singleRun == true}"
                                            showApplyButton="true" direction="top-left">
                                        </rich:calendar>
                                    </a4j:outputPanel>

                                    <h:outputText>
                                        <hr />
                                    </h:outputText>
                                    <h:outputText>
                                        <hr />
                                    </h:outputText>

                                    <!-- TODO: not allow both 'seasonal' and 'singleRun' at the same time -->
                                    <h:outputLabel for="seasonal" value="#{msg.seasonal}: " />
                                    <h:selectBooleanCheckbox
                                        value="#{routeController.route.seasonal}" id="seasonal">
                                        <a4j:support event="onchange" ajaxSingle="true"
                                            reRender="seasonStartLabel,seasonStartCalendar,seasonEndLabel,seasonEndCalendar" />
                                    </h:selectBooleanCheckbox>

                                    <a4j:outputPanel id="seasonStartLabel">
                                        <h:outputLabel for="seasonStart" value="#{msg.seasonStart}: "
                                            rendered="#{routeController.route.seasonal == true}" />
                                    </a4j:outputPanel>
                                    <a4j:outputPanel id="seasonStartCalendar">
                                        <rich:calendar id="seasonStart" datePattern="dd.MM.yyyy"
                                            firstWeekDay="1" inputSize="14"
                                            value="#{routeController.route.seasonStart.time}"
                                            rendered="#{routeController.route.seasonal == true}"
                                            direction="top-left">
                                        </rich:calendar>
                                    </a4j:outputPanel>

                                    <a4j:outputPanel id="seasonEndLabel">
                                        <h:outputLabel for="seasonEnd" value="#{msg.seasonEnd}: "
                                            rendered="#{routeController.route.seasonal == true}" />
                                    </a4j:outputPanel>
                                    <a4j:outputPanel id="seasonEndCalendar">
                                        <rich:calendar id="seasonEnd" datePattern="dd.MM.yyyy"
                                            firstWeekDay="1" inputSize="14"
                                            value="#{routeController.route.seasonEnd.time}"
                                            rendered="#{routeController.route.seasonal == true}"
                                            direction="top-left">
                                        </rich:calendar>
                                    </a4j:outputPanel>

                                </h:panelGrid>
                            </rich:panel>
                        </h:panelGrid>
                    </rich:panel>

                    <!-- Seats positioning -->
                    <rich:panel id="seatsPanel" header="#{msg.seatsSettings}"
                        rendered="#{routeController.route.id > 0}">

                        <h:outputLabel value="#{msg.vehicle}: " for="selectedVehicle" />
                        <h:selectOneMenu value="#{routeController.selectedVehicle}"
                            id="selectedVehicle" converter="#{entityConverter}">
                            <f:selectItems value="#{vehicleController.vehicleSelectItems}" />
                            <a4j:support event="onchange" limitToList="true"
                                reRender="seats,seatsPanel"
                                action="#{routeController.copyVehicleSettings}" />
                        </h:selectOneMenu>

                        <br />

                        <h:selectBooleanCheckbox
                            value="#{routeController.route.seatSettings.startRight}"
                            id="startRight">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="startRight" value="#{msg.startRight}" />

                        <h:outputText value=",&#160;" />

                        <h:selectBooleanCheckbox
                            value="#{routeController.route.seatSettings.lastRowHasFourSeats}"
                            id="lastRowHasFourSeats">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="lastRowHasFourSeats"
                            value="#{msg.lastRowHasFourSeats}" />

                        <h:outputText value=",&#160;" />

                        <h:selectBooleanCheckbox
                            value="#{routeController.route.seatSettings.doubleDecker}"
                            id="doubleDecker">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1,downstairsPanel"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="doubleDecker" value="#{msg.doubleDecker}" />

                        <h:outputText value=",&#160;" />

                        <h:panelGroup id="downstairsPanel">
                            <h:outputLabel value="#{msg.numberOfSeatsDownstairs}: "
                                for="numberOfSeatsDownstairs"
                                rendered="#{routeController.route.seatSettings.doubleDecker}" />
                            <h:inputText
                                value="#{routeController.route.seatSettings.numberOfSeatsDownstairs}"
                                rendered="#{routeController.route.seatSettings.doubleDecker}"
                                id="numberOfSeatsDownstairs">
                                <a4j:support event="onchange" ajaxSingle="true"
                                    action="#{seatController.seatHandler.refreshRows}"
                                    reRender="seatsViewInner1" eventsQueue="seatsDownstairsQueue"
                                    requestDelay="200" />
                            </h:inputText>
                        </h:panelGroup>

                        <br />
                        <a4j:include viewId="../seats.jsp">
                            <ui:param name="return" value="false" />
                            <ui:param name="modifier" value="1" />
                        </a4j:include>
                        <br />
                    </rich:panel>
                    <rich:panel id="buttonsPanel">
                        <h:commandButton action="#{routeController.save}"
                            value="#{msg.save}">
                        </h:commandButton>
                        <h:commandButton action="#{routeController.cancel}"
                            value="#{msg.cancel}" immediate="true" />
                    </rich:panel>
                </h:panelGrid></div>
            </h:form>
        </f:view>

        <rich:modalPanel id="stopPanel" autosized="true" width="400">
            <f:facet name="header">
                <h:outputText value="#{msg.addOrModifyStop}" />
            </f:facet>
            <ui:include src="/modalPanelCommons.jsp">
                <ui:param name="dialogId" value="stopPanel" />
            </ui:include>
            <a4j:include viewId="stop.jsp" />
        </rich:modalPanel>

        <rich:modalPanel id="discountPanel" autosized="true" width="300">
            <f:facet name="header">
                <h:outputText value="#{msg.addOrModifyDiscount}" />
            </f:facet>
            <ui:include src="/modalPanelCommons.jsp">
                <ui:param name="dialogId" value="discountPanel" />
            </ui:include>
            <a4j:include viewId="discount.jsp" />
        </rich:modalPanel>
    </ui:define>
</ui:composition>