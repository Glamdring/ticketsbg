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
                <h2>Маршрути</h2>

                <p>Тази страница показва списък с всички маршрути и дава
                възможност за добавяне на нов маршрут, чрез бутона над таблицата.<br />
                За всеки маршрут е предоставена следната кратка информация:
                <ul>
                    <li><em>#</em> - номер. Фиктивен номер на маршрута в системата</li>
                    <li><em>Име</em> - кратко наименование на маршрута</li>
                    <li><em>Дни</em> - дните от седмицата, през които маршрутът е
                    активен</li>
                </ul>

                Възможно е сортиране по първите две колони чрез натискане на
                заглавието на колоната.</p>

                <p>Действията достъпни от таблицата, чрез бутоните (картинки) в
                дяснята част са:
                <ul>
                    <li><em>Редакция</em> - отваря страницата за редакция на
                    детайлите по маршрута</li>
                    <li><em>Курсове</em> - отваря списък с курсове за маршрута.
                    Курсовете се генерират автоматично от системата и зависят от
                    специална настройка на всеки отделен курс. По подразбиране тази
                    настройка има стойнсот 30 дни.</li>
                    <li><em>Изтриване</em> - изтрива избрания маршрут от списъка.
                    Той не може да бъде възстановен!</li>
                </ul>
                </p>
            </h:panelGroup>
        </f:view>
    </ui:define>
</ui:composition>