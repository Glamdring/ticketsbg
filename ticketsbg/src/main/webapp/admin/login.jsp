<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
  xmlns:h="http://java.sun.com/jsf/html"
  xmlns:f="http://java.sun.com/jsf/core"
  xmlns:ui="http://java.sun.com/jsf/facelets"
  xmlns:a4j="http://richfaces.org/a4j"
  xmlns:rich="http://richfaces.org/rich"
  xmlns:c="http://java.sun.com/jstl/core"
  template="basic_template.jsp">
  <ui:define name="body">
    <f:view>
      <h:messages />
      <h:form>
        <table align="center">
          <tr>
            <td align="center">
                <h:outputLabel value="#{msg.username}" for="username" />
            </td>
            <td>
                <h:inputText id="username" value="#{loginController.username}" />
            </td>
          </tr>
          <tr>
            <td align="center">
                <h:outputLabel value="#{msg.password}" for="password" />
            </td>
            <td>
                <h:inputSecret id="password" value="#{loginController.password}" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <h:inputHidden id="admin" converter="#{booleanConverter}" value="true" binding="#{loginController.admin}" />
                <h:commandButton action="#{loginController.login}" type="submit" value="#{msg.login}" />
            </td>
        </tr>
        </table>
      </h:form>
    </f:view>
  </ui:define>
</ui:composition>