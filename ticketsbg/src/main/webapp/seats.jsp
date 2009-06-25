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
	height: 38px;
	text-align: center;
	vertical-align: middle;
}

.isle {
	width: 20px;
	height: 38px;
	text-align: center;
	vertical-align: middle;
}

.hyperlink {
	color: darkblue;
	text-decoration: underline;
	cursor: pointer;
}
</style>

<a4j:outputPanel id="showSeatsView#{modifier}" styleClass="hyperlink">
    <rich:effect for="showSeatsView#{modifier}" event="onclick" type="Fade" params="delay:0.0, duration:0.1" disableDefault="true" />
    <rich:effect for="showSeatsView#{modifier}" event="onclick" type="BlindDown" targetId="seatsView#{modifier}" params="delay:0.1,duration:1.0,from:0.0,to:1.0" />
    <rich:effect for="showSeatsView#{modifier}" event="onclick" type="Appear" targetId="seatsView#{modifier}" params="delay:0.1,duration:0.5,from:0.0,to:1.0" />
    <rich:effect for="showSeatsView#{modifier}" event="onclick" type="Appear" targetId="hideSeatsView#{modifier}" params="delay:0,duration:1.0,from:0.0,to:1.0" />
    <h:outputText value="#{msg.showSeatsView}" />
</a4j:outputPanel>

<a4j:outputPanel id="hideSeatsView#{modifier}" style="display:none" styleClass="hyperlink">
    <rich:effect for="hideSeatsView#{modifier}" event="onclick" type="BlindUp" targetId="seatsView#{modifier}" params="id:'source1', duration:1.0" />
    <rich:effect for="hideSeatsView#{modifier}" event="onclick" type="Appear" targetId="showSeatsView#{modifier}" params="delay:0, duration:0.5" />
    <rich:effect for="hideSeatsView#{modifier}" event="onclick" type="Fade" targetId="hideSeatsView#{modifier}" params="delay:0.0, duration:0.1" />
    <h:outputText value="#{msg.hideSeatsView}"/>
</a4j:outputPanel>


<rich:panel id="seatsView#{modifier}" style="display: none;">
	<a4j:outputPanel id="seatsViewInner#{modifier}" ajaxRendered="true">
	<c:set var="handler" value="seatHandler"/>
	<c:if test="#{return}">
	   <c:set var="handler" value="returnSeatHandler"/>
	</c:if>
		<rich:dataTable value="#{seatController[handler].rows}" var="row">
			<t:selectManyCheckbox layout="spread" id="selectedSeats#{modifier}"
				value="#{seatController[handler].selectedSeats}" converter="#{seatConverter}">
				<f:selectItems value="#{seatController[handler].seatSelectItems}" />
				<a4j:support event="onchange" ajaxSingle="true" />
			</t:selectManyCheckbox>

			<rich:column styleClass="seat">
		        <rich:separator rendered="#{seatController[handler].route.seatSettings.doubleDecker and row.first.number == seatController[handler].route.seatSettings.numberOfSeatsDownstairs + 1}"/>	
				<t:checkbox index="#{row.first.number - 1}" for="selectedSeats#{modifier}" />
			</rich:column>
		    <rich:column styleClass="seat">
		        <rich:separator rendered="#{seatController[handler].route.seatSettings.doubleDecker and row.first.number == seatController[handler].route.seatSettings.numberOfSeatsDownstairs + 1}"/>
				<t:checkbox index="#{row.second.number - 1}" for="selectedSeats#{modifier}" />
			</rich:column>
		    <rich:column styleClass="isle">
		        <rich:separator rendered="#{seatController[handler].route.seatSettings.doubleDecker and row.first.number == seatController[handler].route.seatSettings.numberOfSeatsDownstairs + 1}"/>
				<t:checkbox index="#{row.middleSeat.number - 1}" for="selectedSeats#{modifier}"
					rendered="#{row.middleSeat != null}" />
			</rich:column>
		    <rich:column styleClass="seat">
		        <rich:separator rendered="#{seatController[handler].route.seatSettings.doubleDecker and row.first.number == seatController[handler].route.seatSettings.numberOfSeatsDownstairs + 1}"/>
		        <t:checkbox index="#{row.third.number - 1}" for="selectedSeats#{modifier}" />
		    </rich:column>
		    <rich:column styleClass="seat">
		        <rich:separator rendered="#{seatController[handler].route.seatSettings.doubleDecker and row.first.number == seatController[handler].route.seatSettings.numberOfSeatsDownstairs + 1}"/>
		        <t:checkbox index="#{row.fourth.number - 1}" for="selectedSeats#{modifier}" />
		    </rich:column>
		</rich:dataTable>
	</a4j:outputPanel>
</rich:panel>
</ui:composition>