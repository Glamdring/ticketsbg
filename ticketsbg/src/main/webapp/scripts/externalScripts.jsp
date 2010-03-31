<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:tc="http://tickets.com/tc"
    xmlns:ui="http://java.sun.com/jsf/facelets">

    <f:view contentType="text/javascript">
        <!-- h:outputText
            value="#{tc:getExternalResource('http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php/')}"
            escape="false" /-->
        <h:outputText
            value="#{tc:getExternalResource('http://static.getclicky.com/js')}"
            escape="false" />
    </f:view>

</ui:composition>