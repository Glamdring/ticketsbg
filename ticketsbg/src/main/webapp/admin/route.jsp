<ui:composition xmlns="http://www.w3.org/1999/xhtml"
  xmlns:h="http://java.sun.com/jsf/html"
  xmlns:f="http://java.sun.com/jsf/core"
  xmlns:ui="http://java.sun.com/jsf/facelets"
  xmlns:a4j="http://richfaces.org/a4j"
  xmlns:rich="http://richfaces.org/rich"
  xmlns:j4j="http://javascript4jsf.dev.java.net/"
  xmlns:c="http://java.sun.com/jstl/core"
  xmlns:cust="http://tickets.com/cust"
  template="admin_template.jsp">
  <ui:define name="body">
    <f:view>
      <h:messages />
      <h:form id="routeForm">
        <table>
          <tr>
            <td><h:outputLabel value="#{msg.routeName}" for="routeName" /></td>
            <td><h:inputText value="#{routeController.route.name}" id="routeName" /></td>
          </tr>
          <tr>
            <td><h:outputLabel value="#{msg.daysOfWeek}" for="daysPickList" /></td>
            <td><rich:pickList showButtonsLabel="false" id="daysPickList"
                 value="#{routeController.daysPickList}" converter="javax.faces.Integer">
                    <c:forEach var="day" items="${routeController.days}">
                        <f:selectItem itemLabel="#{day.label}" itemValue="${day.id}"/>
                    </c:forEach>
                </rich:pickList>
            </td>
           </tr>
           <tr>
                <td colspan="2">
                <h:commandButton value="#{msg.addStop}"
                            action="#{stopController.addStop}" />
                    <rich:orderingList binding="#{stopController.stopsTable}"
                        var="stop" value="#{routeController.route.stops}"
                        converter="#{stopListConverter}" 
                        showButtonLabels="false" valueChangeListener="#{stopController.listReordered}"
                        listWidth="300" listHeight="150">
                        
                        <f:facet name="header">
                            <rich:columnGroup>
                                <rich:column>
                                    <h:outputText value="#{msg.stopName}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.arrivalTime}" />
                                </rich:column>
                                <rich:column>
                                    <h:outputText value="#{msg.departureTime}" />
                                </rich:column>
                                <rich:column />
                            </rich:columnGroup>
                        </f:facet>
                        <rich:column>
                            <h:outputText value="#{stop.name}"></h:outputText>
                        </rich:column>
                        <rich:column>
                            <h:outputText value="#{stop.timeToArrival}" />
                        </rich:column>
                        <rich:column>
                            <h:outputText value="#{stop.timeToDeparture}" />
                        </rich:column>
                        
                        <rich:column>
                            <h:commandLink action="#{stopController.edit}">
                                <h:outputText value="#{msg.edit}" />
                            </h:commandLink>
                            <h:outputText value="&#160;" />
                            <h:commandLink value="#{msg.remove}" action="#{stopController.delete}">
                            </h:commandLink>
                        </rich:column>
                    </rich:orderingList>
                </td>
           </tr>
          <tr>
            <td colspan="2">
              <h:commandButton action="#{routeController.save}" value="#{msg.save}">
              <cust:defaultAction />
              </h:commandButton>
              <h:commandButton action="routesList" value="#{msg.cancel}" />
            </td>
          </tr>
        </table>
      </h:form>
    </f:view>
  </ui:define>
</ui:composition>