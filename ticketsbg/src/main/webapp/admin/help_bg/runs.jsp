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
    template="../adminTemplate.jsp">
    <ui:define name="body">
        <f:view>
            <h:panelGroup styleClass="helpPanel" layout="block">
                <h2>Курсове</h2>
                Тази страница показва всички курсове за даден маршрут. На един екран се показват 20 курса, като чрез цифрите в дъното на таблицата могат да се отварят следващите екрани.

                Има възможност за:
                <ul>
                    <li>добавяне на нов курс</li>
                    <li>изтриване на избран курс</li>
                    <li>отваряне на пътния лист за избран курс</li>
                </ul>
            </h:panelGroup>
        </f:view>
    </ui:define>
</ui:composition>