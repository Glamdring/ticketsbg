<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:rich="http://richfaces.org/rich">

    <rich:messages id="messages#{additionalId}" errorClass="error"
        globalOnly="#{globalOnly != null and globalOnly == true}">
        <f:facet name="errorMarker">
            <h:graphicImage url="/images/validationError.png" width="14"
                height="14" style="margin-right: 4px;" />
        </f:facet>
    </rich:messages>

</ui:composition>