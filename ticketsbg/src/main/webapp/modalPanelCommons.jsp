<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich">

    <!-- Expecting a <ui:param name="dialogId" /> to be set -->

    <f:facet name="controls">
        <h:panelGroup>
            <h:graphicImage value="/images/close.png" styleClass="hidelink"
                onclick="#{rich:component(dialogId)}.hide()" />
        </h:panelGroup>
    </f:facet>
    <rich:hotKey key="esc" handler="#{rich:component(dialogId)}.hide();" />

</ui:composition>