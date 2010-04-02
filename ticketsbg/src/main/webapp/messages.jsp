<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:a4j="http://richfaces.org/a4j">

    <rich:messages id="messages#{additionalId}" errorClass="error" infoClass="message"
        globalOnly="#{globalOnly != null and globalOnly == true}" ajaxRendered="#{ajaxRendered != false}">
        <f:facet name="errorMarker">
            <h:graphicImage url="/images/validationError.png" width="14"
                height="14" style="margin-right: 4px;" alt="" />
        </f:facet>
    </rich:messages>

</ui:composition>