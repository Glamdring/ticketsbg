<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:j4j="http://javascript4jsf.dev.java.net/"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:cust="http://abax.bg/cust"
    xmlns:t="http://myfaces.apache.org/tomahawk">
    <style>
.gridContent {
    vertical-align: top;
}
</style>
    <f:view>
        <rich:messages />
        <a4j:outputPanel ajaxRendered="true">
            <t:inputHidden forceId="true" id="vehicleFormHasMessages"
                value="#{vehicleController.hasMessages}" />
        </a4j:outputPanel>
        <a4j:form id="vehicleForm">
            <a4j:outputPanel ajaxRendered="true">
                <rich:panel>
                    <h:panelGrid columns="2" styleClass="gridContent">

                        <h:outputLabel for="name" value="#{msg.name}: " />
                        <h:inputText value="#{vehicleController.vehicle.name}" id="name">
                            <f:attribute name="label" value="#{msg.name}" />
                        </h:inputText>

                        <h:outputLabel for="description" value="#{msg.description}: " />
                        <h:inputTextarea value="#{vehicleController.vehicle.description}"
                            id="description" rows="3" cols="25">
                            <f:attribute name="label" value="#{msg.description}" />
                        </h:inputTextarea>

                        <h:outputLabel for="seats" value="#{msg.seats}: " />
                        <h:inputText value="#{vehicleController.vehicle.seats}" id="seats">
                            <f:attribute name="label" value="#{msg.seats}" />
                        </h:inputText>

                        <a4j:commandButton value="#{msg.save}"
                            action="#{vehicleController.save}" type="submit"
                            oncomplete="if (document.getElementById('vehicleFormHasMessages').value == 'false') { #{rich:component('entityPanel')}.hide()}"
                            reRender="vehiclesTable,agentsUsersTable" />
                        <h:outputText value="" />
                    </h:panelGrid>
                </rich:panel>


                <!-- Seats positioning -->
                <rich:panel id="seatsPanel" header="#{msg.seatsSettings}"
                    rendered="#{vehicleController.vehicle.vehicleId > 0}">

                    <h:panelGrid columns="2">
                        <h:selectBooleanCheckbox
                            value="#{vehicleController.vehicle.seatSettings.startRight}"
                            id="startRight">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="startRight" value="#{msg.startRight}" />


                        <h:selectBooleanCheckbox
                            value="#{vehicleController.vehicle.seatSettings.lastRowHasFourSeats}"
                            id="lastRowHasFourSeats">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="lastRowHasFourSeats"
                            value="#{msg.lastRowHasFourSeats}" />

                        <h:selectBooleanCheckbox
                            value="#{vehicleController.vehicle.seatSettings.doubleDecker}"
                            id="doubleDecker">
                            <a4j:support event="onchange" ajaxSingle="true"
                                reRender="seatsViewInner1,downstairsPanel"
                                action="#{seatController.seatHandler.refreshRows}" />
                        </h:selectBooleanCheckbox>
                        <h:outputLabel for="doubleDecker" value="#{msg.doubleDecker}" />

                    </h:panelGrid>

                    <h:panelGroup id="downstairsPanel">
                        <h:outputLabel value="#{msg.numberOfSeatsDownstairs}: "
                            for="numberOfSeatsDownstairs"
                            rendered="#{vehicleController.vehicle.seatSettings.doubleDecker}" />
                        <h:inputText
                            value="#{vehicleController.vehicle.seatSettings.numberOfSeatsDownstairs}"
                            rendered="#{vehicleController.vehicle.seatSettings.doubleDecker}"
                            id="numberOfSeatsDownstairs">
                            <a4j:support event="onchange" ajaxSingle="true"
                                action="#{seatController.seatHandler.refreshRows}"
                                reRender="seatsViewInner1" eventsQueue="seatsDownstairsQueue"
                                requestDelay="200" />
                        </h:inputText>
                    </h:panelGroup>

                    <a4j:include viewId="../seats.jsp">
                        <ui:param name="return" value="false" />
                        <ui:param name="modifier" value="1" />
                    </a4j:include>
                    <br />
                </rich:panel>

            </a4j:outputPanel>
        </a4j:form>
    </f:view>
</ui:composition>