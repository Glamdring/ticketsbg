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
    template="printTemplate.jsp">
    <ui:define name="body">
    <style type="text/css">
        body {
            background-color: white;
        }
    </style>
        <f:view>
            <h:form id="runsForm">
                <rich:panel style="border-style: none;">
                    <h:outputText value="#{msg.travelListFor} " />
                    <h:outputText value="#{travelListController.run.route.name} "
                        style="font-weight: bold;" />
                    (<h:outputText value="#{travelListController.run.time.time}"
                        style="font-weight: bold;">
                        <f:convertDateTime pattern="dd.MM.yyyy HH:mm"
                            timeZone="#{timeZoneController.timeZone}"/>
                    </h:outputText>)
                </rich:panel>

                <table width="800px;">
                <a4j:repeat value="#{travelListController.travelList}" var="entry">
                    <tr>
                        <td colspan="4" style="font-weight: bold; padding-top: 20px;">
                            <h:outputText value="#{entry.caption}"/> (<h:outputText value="#{tc:getSize(entry.tickets)}" />)
                        </td>
                    </tr>
                        <a4j:repeat value="#{entry.tickets}" var="ticket"
                            rowKeyVar="idx">
                            <h:outputText value="#{msg['lt']}tr#{msg['gt']}" rendered="#{idx % 4 == 0}" escape="false" />
                            <td style="font-weight: bold;">
                                #{ticket.ticketCode}
                                <h:outputText value=" (#{ticket.seatNumbers})" /></td>
                            <h:outputText value="#{msg['lt']}/tr#{msg['gt']}" rendered="#{idx % 4 == 3}" escape="false" />
                        </a4j:repeat>
                    </a4j:repeat>
                </table>
            </h:form>
        </f:view>
    </ui:define>
</ui:composition>