<?xml version="1.0" encoding="UTF-8" ?> 
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:j4j="http://javascript4jsf.dev.java.net/"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:cust="http://tickets.com/cust"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	template="admin_template.jsp">
	<ui:define name="header">
		<style>
.gridContent {
	vertical-align: top;
}

.internalPanel {
	height: 230px;
	text-align: center;
}
.subInternalPanel {
    height: 190px;
    text-align: center;
}
</style>
	</ui:define>
	<ui:define name="body">
		<f:view>
			<h:messages />
			<h:form id="routeForm">
				<div align="center"><h:panelGrid columns="1">
					<rich:panel id="mainInfoPanel">
						<a4j:outputPanel>
							<h:outputLabel value="#{msg.routeName}: " for="routeName" />
							<rich:toolTip value="#{msg.routeNameHint}" followMouse="true" />
						</a4j:outputPanel>
						<h:inputText value="#{routeController.route.name}" id="routeName" />
					</rich:panel>

					<rich:panel header="#{msg.routeDetailsHeader}" id="routeDetails">
						<h:panelGrid columns="3" columnClasses="gridContent">
							<rich:panel header="#{msg.daysOfWeek}" styleClass="internalPanel" id="daysPanel">
								<rich:pickList showButtonsLabel="false" id="daysPickList"
									value="#{routeController.daysPickList}"
									converter="javax.faces.Integer"
									disabled="#{routeController.route.singleRun == true}">
									<c:forEach var="day" items="${routeController.days}">
										<f:selectItem itemLabel="#{day.label}" itemValue="${day.id}" />
									</c:forEach>
								</rich:pickList>
							</rich:panel>

							<rich:panel header="#{msg.hours}" styleClass="internalPanel" id="hoursPanel">
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

							<rich:panel header="${msg.stops}" styleClass="internalPanel" id="stopsPanel">
								<a4j:commandButton value="#{msg.addStop}"
									action="#{routeController.addStop}" oncomplete="#{rich:component('stopPanel')}.show()" />
									
								<rich:orderingList binding="#{routeController.stopsTable}"
									var="stop" value="#{routeController.route.stops}"
									converter="#{stopListConverter}" showButtonLabels="false"
									valueChangeListener="#{routeController.listReordered}"
									listWidth="500" listHeight="150" id="stopsTable">

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
										<h:outputText value="#{stop.timeToArrival}" />
									</rich:column>
									<rich:column width="120">
										<f:facet name="header">
											<h:outputText value="#{msg.timeToDeparture}" id="col3" />
										</f:facet>
										<h:outputText value="#{stop.timeToDeparture}" />
									</rich:column>
									<rich:column width="35">
										<f:facet name="header">
											<h:outputText value="" id="col4" />
										</f:facet>
										<a4j:commandLink
											oncomplete="#{rich:component('stopPanel')}.show()"
											title="#{msg.edit}">
											<h:graphicImage value="/images/edit.png"
												style="width:16; height:16; border-style: none;"
												alt="#{msg.edit}" title="#{msg.edit}" />

											<f:setPropertyActionListener value="#{stop}"
												target="#{routeController.stop}" />
										</a4j:commandLink>
										<h:outputText value="&#160;" />
										<a4j:commandLink action="#{routeController.deleteStop}"
											title="#{msg.remove}" reRender="stopsPanel">
											<h:graphicImage value="/images/delete.png"
												style="width:16; height:16; border-style: none;"
												alt="#{msg.remove}" title="#{msg.remove}" />
										</a4j:commandLink>
									</rich:column>
								</rich:orderingList>
							</rich:panel>
						</h:panelGrid>
					</rich:panel>
                    
                    <h:panelGrid columns="3" columnClasses="gridContent" id="pricesAndSettingsPanel">
						<rich:panel header="#{msg.prices}" id="pricesPanel"
							rendered="#{routeController.route.id > 0}" styleClass="internalPanel">
							<h:panelGrid columns="2" columnClasses="gridContent">
								<rich:panel styleClass="subInternalPanel">
									<rich:tree switchType="client" ajaxSubmitSelection="true"
										style="width:230px;" value="#{routeController.pricesTreeData}"
										var="data" nodeFace="#{data.leaf ? 'end' : 'start'}"
										id="pricesTree"
										nodeSelectListener="#{routeController.nodeSelected}"
										adviseNodeOpened="#{routeController.getExpandedNodes}">
	
										<rich:treeNode type="start"
											reRender="priceField,twoWayPriceField">
											<div style="font-size: 11px;"><h:outputText
												value="#{msg.fromStop} " /> <h:outputText
												value="#{data.stop.name}" /></div>
										</rich:treeNode>
										<rich:treeNode type="end"
											reRender="priceField,twoWayPriceField">
											<div style="font-size: 11px;"><h:outputText
												value="#{msg.toStop} " /> <h:outputText
												value="#{data.stop.name}" /> <h:outputText value=" (" /> <h:outputText
												value="#{data.price.price}" converter="#{currencyConverter}" />
											<h:outputText value=")" /></div>
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
										<a4j:commandButton value="#{msg.save}"
											action="#{routeController.savePrice}" />
									</h:panelGrid>
								</rich:panel>
							</h:panelGrid>
						</rich:panel>
	
	                   <rich:panel styleClass="internalPanel" header="#{msg.discounts}"
                            id="discountsPanel">
                            <a4j:commandButton value="#{msg.addDiscount}"
                                    action="#{routeController.addDiscount}" oncomplete="#{rich:component('discountPanel')}.show()" />
                            <rich:dataTable value="#{routeController.route.discounts}"
                            var="discount" onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                            onRowMouseOut="this.style.backgroundColor='white'" cellpadding="0"
                            cellspacing="0" border="0" id="discountsTable" width="310px">
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
                                    <h:outputText value="-" rendered="#{discount.discountType == 'FIXED'}" />       
                                    <h:outputText value="%" rendered="#{discount.discountType == 'PERCENTAGE'}" />
                                </rich:column>
                                
                                <rich:column>
                                    <f:facet name="header">
                                        #{msg.discountValue}
                                    </f:facet>
                                    <h:outputText value="#{discount.value}">
                                        <f:convertNumber maxFractionDigits="2" 
                                            minFractionDigits="2" />
                                    </h:outputText>
                                </rich:column>

								<rich:column>
									<f:facet name="header">
									</f:facet>
									<a4j:commandLink
										oncomplete="#{rich:component('discountPanel')}.show()"
										title="#{msg.edit}">
										<h:graphicImage value="/images/edit.png"
											style="width:16; height:16; border-style: none;"
											alt="#{msg.edit}" title="#{msg.edit}" />

										<f:setPropertyActionListener value="#{discount}"
											target="#{routeController.discount}" />
									</a4j:commandLink>
									<h:outputText value="&#160;" />
									<a4j:commandLink action="#{routeController.deleteDiscount}"
										title="#{msg.remove}" reRender="discountsPanel">
										<h:graphicImage value="/images/delete.png"
											style="width:16; height:16; border-style: none;"
											alt="#{msg.remove}" title="#{msg.remove}" />
										<f:setPropertyActionListener value="#{discount}"
                                            target="#{routeController.discount}" />
									</a4j:commandLink>
								</rich:column>

							</rich:dataTable>
                       </rich:panel>
                        
	
						<rich:panel styleClass="internalPanel"
							header="#{msg.routeSettings}" id="routeSettingsPanel">
							<h:panelGrid columns="2" styleClass="dr-pnl-b"
								style="padding:0px; margin:0px;">
								<h:outputLabel for="seats" value="#{msg.seats}: " />
								<h:inputText value="#{routeController.route.seats}"
								    id="seats" size="18">
									<f:convertNumber maxFractionDigits="0" />
								</h:inputText>

								<h:outputLabel for="allowSeatChoice"
									value="#{msg.allowSeatChoice}: " />
								<h:selectBooleanCheckbox
									value="#{routeController.route.allowSeatChoice}"
									id="allowSeatChoice" />
	
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
									<rich:calendar id="singleRunDateTime" datePattern="dd.MM.yyyy HH:mm"
										firstWeekDay="1" inputSize="14" 
										value="#{routeController.route.singleRunDateTime.time}"
										rendered="#{routeController.route.singleRun == true}"
										showApplyButton="true" direction="top-right">
									</rich:calendar>
								</a4j:outputPanel>
	
							</h:panelGrid>
						</rich:panel>
					</h:panelGrid>
                    
					<rich:panel id="buttonsPanel">
						<h:commandButton action="#{routeController.save}"
							value="#{msg.save}">
							<cust:defaultAction />
						</h:commandButton>
						<h:commandButton action="#{routeController.cancel}"
							value="#{msg.cancel}" />
					</rich:panel>
				</h:panelGrid></div>
			</h:form>
		</f:view>

		<rich:modalPanel id="stopPanel" autosized="true" width="450">
			<f:facet name="header">
				<h:outputText value="#{msg.addOrModifyStop}" />
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/images/close.png" id="hidelink"
						styleClass="hidelink" />
					<rich:componentControl for="stopPanel" attachTo="hidelink"
						operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<a4j:include viewId="stop.jsp" />
		</rich:modalPanel>
		
        <rich:modalPanel id="discountPanel" autosized="true" width="250">
            <f:facet name="header">
                <h:outputText value="#{msg.addOrModifyStop}" />
            </f:facet>
            <f:facet name="controls">
                <h:panelGroup>
                    <h:graphicImage value="/images/close.png" id="hidelink1"
                        styleClass="hidelink" />
                    <rich:componentControl for="discountPanel" attachTo="hidelink1"
                        operation="hide" event="onclick" />
                </h:panelGroup>
            </f:facet>
            <a4j:include viewId="discount.jsp" />
        </rich:modalPanel>
	</ui:define>
</ui:composition>