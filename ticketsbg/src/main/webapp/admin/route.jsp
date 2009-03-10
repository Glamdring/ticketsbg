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