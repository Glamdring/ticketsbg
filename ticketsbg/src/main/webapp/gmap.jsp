<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk">

    <script type="text/javascript">
    //<![CDATA[
        // Call this function when the page has been loaded
        var #{mapVar};
        function prepare#{componentId}() {
          #{mapVar} = new google.maps.Map2(document.getElementById('#{componentId}'));
          #{mapVar}.setCenter(new google.maps.LatLng(#{lat}, #{lng}), 17);
          #{mapVar}.addControl(new GLargeMapControl());
          #{mapVar}.addControl(new GMapTypeControl());
        }

    //]]>
    </script>
    <div id="#{componentId}" style="width: 500px; height: 500px;"></div>

</ui:composition>