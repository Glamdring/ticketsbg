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

<br />
<a4j:outputPanel id="showSeatsView" styleClass="hyperlink">
    <rich:effect for="showSeatsView" event="onclick" type="Fade" params="delay:0.0, duration:0.1" disableDefault="true" />
    <rich:effect for="showSeatsView" event="onclick" type="BlindDown" targetId="seatsView" params="delay:0.1,duration:1.0,from:0.0,to:1.0" />
    <rich:effect for="showSeatsView" event="onclick" type="Appear" targetId="seatsView" params="delay:0.1,duration:0.5,from:0.0,to:1.0" />
    <rich:effect for="showSeatsView" event="onclick" type="Appear" targetId="hideSeatsView" params="delay:0,duration:1.0,from:0.0,to:1.0" />
    <h:outputText value="#{msg.showSeatsView}" />
</a4j:outputPanel>

<a4j:outputPanel id="hideSeatsView" style="display:none" styleClass="hyperlink">
    <rich:effect for="hideSeatsView" event="onclick" type="BlindUp" targetId="seatsView" params="id:'source1', duration:1.0" />
    <rich:effect for="hideSeatsView" event="onclick" type="Appear" targetId="showSeatsView" params="delay:0, duration:0.5" />
    <rich:effect for="hideSeatsView" event="onclick" type="Fade" targetId="hideSeatsView" params="delay:0.0, duration:0.1" />
    <h:outputText value="#{msg.hideSeatsView}"/>
</a4j:outputPanel>


<rich:panel id="seatsView" style="display: none;">
	<a4j:outputPanel id="seatsViewInner" ajaxRendered="true">
		<rich:dataTable value="#{seatHandler.rows}" var="row">
			<t:selectManyCheckbox layout="spread" id="selectedSeats">
				<f:selectItems value="#{seatHandler.seatSelectItems}" />
			</t:selectManyCheckbox>
			
			<rich:column styleClass="seat">
					<t:checkbox index="#{row.first.number - 1}" for="selectedSeats" />
			</rich:column>
		    <rich:column styleClass="seat">
				<t:checkbox index="#{row.second.number - 1}" for="selectedSeats" />
			</rich:column>
		    <rich:column styleClass="isle">
				<t:checkbox index="#{row.middleSeat.number - 1}" for="selectedSeats"
					rendered="#{row.middleSeat != null}" />
			</rich:column>
		    <rich:column styleClass="seat">
		        <t:checkbox index="#{row.third.number - 1}" for="selectedSeats" />
		    </rich:column>
		    <rich:column styleClass="seat">
		        <t:checkbox index="#{row.fourth.number - 1}" for="selectedSeats" />
		    </rich:column>
		</rich:dataTable>
	</a4j:outputPanel>
</rich:panel>
</ui:composition>