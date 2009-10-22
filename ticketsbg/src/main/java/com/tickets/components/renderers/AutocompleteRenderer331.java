package com.tickets.components.renderers;

import org.richfaces.renderkit.html.ComboBoxRenderer;

public class AutocompleteRenderer331 extends ComboBoxRenderer {

    /**
     * Source copied from the (decompiled) richfaces class,
     * with the rendering of the button - removed
     */
    /*
    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context,
            UIComboBox component, ComponentVariables variables)
            throws IOException {
         String clientId = component.getClientId(context);
         Boolean directInputSuggestions = (Boolean)component.getAttributes().get("directInputSuggestions");
         variables.setVariable("directInputSuggestions", directInputSuggestions);
         Boolean filterNewValues = (Boolean)component.getAttributes().get("filterNewValues");
         variables.setVariable("filterNewValues", filterNewValues);
         Boolean disabled = (Boolean)component.getAttributes().get("disabled");
         variables.setVariable("disabled", disabled);
         String listHeight = (String)component.getAttributes().get("listHeight");
         if(listHeight == null || listHeight.length() == 0 || listHeight.trim().startsWith("0"))
             listHeight = "200px";
         else
             listHeight = HtmlUtil.qualifySize(listHeight);
         variables.setVariable("listHeight", listHeight);
         String width = (String)component.getAttributes().get("width");
         String correction = null;
         if(width == null || width.length() == 0 || width.trim().startsWith("0"))
             width = "150px";
         else
             width = HtmlUtil.qualifySize(width);
         correction = width.substring(0, width.indexOf("px"));
         correction = (new StringBuilder()).append(Integer.parseInt(correction) - 10).append("px").toString();
         variables.setVariable("width", width);
         variables.setVariable("correction", correction);
         String listWidth = (String)component.getAttributes().get("listWidth");
         if(listWidth == null || listWidth.length() == 0 || listWidth.trim().startsWith("0"))
             listWidth = width;
         else
             listWidth = HtmlUtil.qualifySize(listWidth);
         variables.setVariable("listWidth", listWidth);
         String inputSize = (String)component.getAttributes().get("inputSize");
         variables.setVariable("inputSize", inputSize);
         String defaultLabel = (String)component.getAttributes().get("defaultLabel");
         variables.setVariable("defaultLabel", defaultLabel);
         Boolean selectFirstOnUpdate = (Boolean)component.getAttributes().get("selectFirstOnUpdate");
         variables.setVariable("selectFirstOnUpdate", selectFirstOnUpdate);
         Object value = component.getSubmittedValue();
         if(value == null)
             value = component.getAttributes().get("value");
         String valueStyle = "rich-combobox-font-inactive";
         value = getConvertedStringValue(context, component, value);
         if("".equals(value))
             valueStyle = "rich-combobox-font-disabled";
         variables.setVariable("value", value);
         String convertedValue = encodeValue((String)value);
         variables.setVariable("convertedValue", convertedValue);
         variables.setVariable("valueStyle", valueStyle);
         Object inputStyle = component.getAttributes().get("inputStyle");
         variables.setVariable("inputStyle", inputStyle);
         Object inputClass = component.getAttributes().get("inputClass");
         if("".equals(inputClass))
             inputClass = null;
         variables.setVariable("inputClass", inputClass);
         Object inputDisabledStyle = component.getAttributes().get("inputDisabledStyle");
         variables.setVariable("inputDisabledStyle", inputDisabledStyle);
         Object inputDisabledClass = component.getAttributes().get("inputDisabledClass");
         if("".equals(inputDisabledClass))
             inputDisabledClass = null;
         variables.setVariable("inputDisabledClass", inputDisabledClass);
         Object inputInactiveStyle = component.getAttributes().get("inputInactiveStyle");
         variables.setVariable("inputInactiveStyle", inputInactiveStyle);
         Object inputInactiveClass = component.getAttributes().get("inputInactiveClass");
         if("".equals(inputInactiveClass))
             inputInactiveClass = null;
         variables.setVariable("inputInactiveClass", inputInactiveClass);
         Object buttonInactiveClass = component.getAttributes().get("buttonInactiveClass");
         if("".equals(buttonInactiveClass))
             buttonInactiveClass = null;
         variables.setVariable("buttonInactiveClass", buttonInactiveClass);
         Object buttonInactiveStyle = component.getAttributes().get("buttonInactiveStyle");
         variables.setVariable("buttonInactiveStyle", buttonInactiveStyle);
         Object buttonDisabledClass = component.getAttributes().get("buttonDisabledClass");
         if("".equals(buttonDisabledClass))
             buttonDisabledClass = null;
         variables.setVariable("buttonDisabledClass", buttonDisabledClass);
         Object buttonDisabledStyle = component.getAttributes().get("buttonDisabledStyle");
         variables.setVariable("buttonDisabledStyle", buttonDisabledStyle);
         Object buttonClass = component.getAttributes().get("buttonClass");
         if("".equals(buttonClass))
             buttonClass = null;
         variables.setVariable("buttonClass", buttonClass);
         Object buttonStyle = component.getAttributes().get("buttonStyle");
         variables.setVariable("buttonStyle", buttonStyle);
         Object listStyle = component.getAttributes().get("listStyle");
         variables.setVariable("listStyle", listStyle);
         Object listClass = component.getAttributes().get("listClass");
         if("".equals(listClass))
             listClass = null;
         variables.setVariable("listClass", listClass);
         Object styleClass = component.getAttributes().get("styleClass");
         variables.setVariable("styleClass", styleClass);
         Object style = component.getAttributes().get("style");
         variables.setVariable("style", style);
         Object itemClass = component.getAttributes().get("itemClass");
         if("".equals(itemClass))
             itemClass = null;
         variables.setVariable("itemClass", itemClass);
         Object itemSelectedClass = component.getAttributes().get("itemSelectedClass");
         if("".equals(itemSelectedClass))
             itemSelectedClass = null;
         variables.setVariable("itemSelectedClass", itemSelectedClass);
         String buttonIcon = (String)component.getAttributes().get("buttonIcon");
         if(!"".equals(buttonIcon))
             buttonIcon = (new StringBuilder()).append("url('").append(getResource(buttonIcon).getUri(context, component)).append("')").toString();
         variables.setVariable("buttonIcon", buttonIcon);
         String buttonIconDisabled = (String)component.getAttributes().get("buttonIconDisabled");
         if(!"".equals(buttonIconDisabled))
             buttonIconDisabled = (new StringBuilder()).append("url('").append(getResource(buttonIconDisabled).getUri(context, component)).append("')").toString();
         variables.setVariable("buttonIconDisabled", buttonIconDisabled);
         String buttonIconInactive = (String)component.getAttributes().get("buttonIconInactive");
         if(!"".equals(buttonIconInactive))
             buttonIconInactive = (new StringBuilder()).append("url('").append(getResource(buttonIconInactive).getUri(context, component)).append("')").toString();
         variables.setVariable("buttonIconInactive", buttonIconInactive);
         variables.setVariable("enableManualInput", Boolean.valueOf(!component.isEnableManualInput()));
         variables.setVariable("spacer", getResource("images/spacer.gif").getUri(context, component));
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "id", clientId);
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", (new StringBuilder()).append("rich-combobox-font rich-combobox ").append(convertToString(variables.getVariable("styleClass"))).toString());
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("combobox").toString());
         getUtils().writeAttribute(writer, "style", (new StringBuilder()).append("width:").append(convertToString(variables.getVariable("width"))).append(";").append(convertToString(variables.getVariable("style"))).toString());
         getUtils().encodeAttributesFromArray(context, component, new String[] {
             "align", "dir", "lang", "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove",
             "onmouseout", "onmouseover", "onmouseup", "title", "xml:lang"
         });
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-list-cord");
         writer.endElement("div");
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-font rich-combobox-shell");
         getUtils().writeAttribute(writer, "style", (new StringBuilder()).append("width:").append(convertToString(variables.getVariable("width"))).append(";").toString());
         writer.startElement("input", component);
         getUtils().writeAttribute(writer, "autocomplete", "off");
         getUtils().writeAttribute(writer, "class", (new StringBuilder()).append(convertToString(variables.getVariable("valueStyle"))).append(" rich-combobox-input-inactive  ").append(convertToString(variables.getVariable("inputInactiveClass"))).toString());
         getUtils().writeAttribute(writer, "disabled", variables.getVariable("disabled"));
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("comboboxField").toString());
         getUtils().writeAttribute(writer, "name", (new StringBuilder()).append(convertToString(clientId)).append("comboboxField").toString());
         getUtils().writeAttribute(writer, "onchange", component.getAttributes().get("onchange"));
         getUtils().writeAttribute(writer, "onfocus", component.getAttributes().get("onfocus"));
         getUtils().writeAttribute(writer, "readonly", variables.getVariable("enableManualInput"));
         getUtils().writeAttribute(writer, "style", (new StringBuilder()).append("width:").append(convertToString(variables.getVariable("correction"))).append("; ").append(convertToString(variables.getVariable("inputInactiveStyle"))).toString());
         getUtils().writeAttribute(writer, "tabindex", component.getAttributes().get("tabindex"));
         getUtils().writeAttribute(writer, "type", "text");
         getUtils().writeAttribute(writer, "value", variables.getVariable("value"));
         getUtils().writeAttribute(writer, "onblur", component.getAttributes().get("onblur"));
         writer.endElement("input");

         writer.startElement("input", component);
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("comboBoxButtonBG").toString());
         getUtils().writeAttribute(writer, "readonly", "true");
         getUtils().writeAttribute(writer, "tabindex", "-1");
         getUtils().writeAttribute(writer, "style", "display: none;");
         getUtils().writeAttribute(writer, "type", "text");
         getUtils().writeAttribute(writer, "value", "");
         writer.endElement("input");
         writer.startElement("input", component);
         getUtils().writeAttribute(writer, "disabled", variables.getVariable("disabled"));
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("comboboxButton").toString());
         getUtils().writeAttribute(writer, "readonly", "true");
         getUtils().writeAttribute(writer, "style", "display: none;");
         getUtils().writeAttribute(writer, "tabindex", "-1");
         getUtils().writeAttribute(writer, "type", "text");
         getUtils().writeAttribute(writer, "value", "");
         writer.endElement("input");

         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-strut rich-combobox-font");
         getUtils().writeAttribute(writer, "style", (new StringBuilder()).append("width:").append(convertToString(variables.getVariable("correction"))).toString());
         writer.writeText(convertToString("Strut"), null);
         writer.endElement("div");
         writer.endElement("div");
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", (new StringBuilder()).append("rich-combobox-list-cord ").append(convertToString(variables.getVariable("listClass"))).toString());
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("listParent").toString());
         getUtils().writeAttribute(writer, "style", (new StringBuilder()).append("display:none; ").append(convertToString(variables.getVariable("listStyle"))).append("; position:absolute;z-index:1000;").toString());
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-shadow");
         writer.startElement("table", component);
         getUtils().writeAttribute(writer, "border", "0");
         getUtils().writeAttribute(writer, "cellpadding", "0");
         getUtils().writeAttribute(writer, "cellspacing", "0");
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("shadow").toString());
         writer.startElement("tr", component);
         writer.startElement("td", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-shadow-tl");
         writer.startElement("img", component);
         getUtils().writeAttribute(writer, "alt", "");
         getUtils().writeAttribute(writer, "height", "1");
         getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
         getUtils().writeAttribute(writer, "style", "border:0");
         getUtils().writeAttribute(writer, "width", "10");
         writer.endElement("img");
         writer.startElement("br", component);
         writer.endElement("br");
         writer.endElement("td");
         writer.startElement("td", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-shadow-tr");
         writer.startElement("img", component);
         getUtils().writeAttribute(writer, "alt", "");
         getUtils().writeAttribute(writer, "height", "10");
         getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
         getUtils().writeAttribute(writer, "style", "border:0");
         getUtils().writeAttribute(writer, "width", "1");
         writer.endElement("img");
         writer.startElement("br", component);
         writer.endElement("br");
         writer.endElement("td");
         writer.endElement("tr");
         writer.startElement("tr", component);
         writer.startElement("td", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-shadow-bl");
         writer.startElement("img", component);
         getUtils().writeAttribute(writer, "alt", "");
         getUtils().writeAttribute(writer, "height", "10");
         getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
         getUtils().writeAttribute(writer, "style", "border:0");
         getUtils().writeAttribute(writer, "width", "1");
         writer.endElement("img");
         writer.startElement("br", component);
         writer.endElement("br");
         writer.endElement("td");
         writer.startElement("td", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-shadow-br");
         writer.startElement("img", component);
         getUtils().writeAttribute(writer, "alt", "");
         getUtils().writeAttribute(writer, "height", "10");
         getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
         getUtils().writeAttribute(writer, "style", "border:0");
         getUtils().writeAttribute(writer, "width", "10");
         writer.endElement("img");
         writer.startElement("br", component);
         writer.endElement("br");
         writer.endElement("td");
         writer.endElement("tr");
         writer.endElement("table");
         writer.endElement("div");
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-list-position");
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("listPosition").toString());
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-list-decoration");
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("listDecoration").toString());
         writer.startElement("div", component);
         getUtils().writeAttribute(writer, "class", "rich-combobox-list-scroll");
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("list").toString());
         java.util.List items = encodeItems(context, component);
         writer.endElement("div");
         writer.endElement("div");
         writer.endElement("div");
         writer.endElement("div");
         variables.setVariable("hiddenValue", value);
         writer.startElement("input", component);
         getUtils().writeAttribute(writer, "id", (new StringBuilder()).append(convertToString(clientId)).append("comboboxValue").toString());
         getUtils().writeAttribute(writer, "name", clientId);
         getUtils().writeAttribute(writer, "type", "hidden");
         writer.endElement("input");
         writer.endElement("div");
         writer.startElement("script", component);
         getUtils().writeAttribute(writer, "type", "text/javascript");
         writer.writeText(convertToString((new StringBuilder()).append("var clientId = '").append(convertToString(clientId)).append("';\n \n\t\t\t\t\n\t\tvar comboboxUserStyles = {\n\t\t\tbutton : { \n\t\t\t\tclasses : \n\t\t\t\t\t{\n\t\t\t\t\t\tnormal:  \"").append(convertToString(variables.getVariable("buttonInactiveClass"))).append("\",\n\t\t\t\t\t\tactive: \"").append(convertToString(variables.getVariable("buttonClass"))).append("\",\n\t\t\t\t\t\tdisabled: \"").append(convertToString(variables.getVariable("buttonDisabledClass"))).append("\" \n\t\t\t\t\t},\n\t\t\t\tstyle :\n\t\t\t\t\t{\n\t\t\t\t\t\tnormal: \"").append(convertToString(variables.getVariable("buttonInactiveStyle"))).append("\",\n\t\t\t\t\t\tactive: \"").append(convertToString(variables.getVariable("buttonStyle"))).append("\",\n\t\t\t\t\t\tdisabled:\t\"").append(convertToString(variables.getVariable("buttonDisabledStyle"))).append("\"\n\t\t\t\t\t}\t \n\t\t\t},\n\t\t\tbuttonicon : {\n\t\t\t \tstyle :\n\t\t\t \t\t{\n\t\t\t \t\t\tnormal: \"").append(convertToString(variables.getVariable("buttonIconInactive"))).append("\",\n\t\t\t\t\t\tactive: \"").append(convertToString(variables.getVariable("buttonIcon"))).append("\",\n\t\t\t\t\t\tdisabled: \"").append(convertToString(variables.getVariable("buttonIconDisabled"))).append("\"\n\t\t\t \t\t}\t\t   \t\t\t   \n\t\t\t},\n\t\t\tfield : {\n\t\t\t\tclasses :\n\t\t\t\t\t{\n\t\t\t\t\t\tnormal:  \"").append(convertToString(variables.getVariable("inputInactiveClass"))).append("\",\n\t\t\t\t\t\tactive: \"").append(convertToString(variables.getVariable("inputClass"))).append("\",\n\t\t\t\t\t\tdisabled: \"").append(convertToString(variables.getVariable("inputDisabledClass"))).append("\" \n\t\t\t\t\t},\n\t\t\t\tstyle : \n\t\t\t\t\t{\n\t\t\t\t\t\tnormal : \"").append(convertToString(variables.getVariable("inputInactiveStyle"))).append("\",\n\t\t\t\t\t  \tactive : \"").append(convertToString(variables.getVariable("inputStyle"))).append("\",\n\t\t\t\t\t  \tdisabled : \"").append(convertToString(variables.getVariable("inputDisabledStyle"))).append("\"\n\t\t\t\t\t}\t\n\t\t\t},\n\t\t\tcombolist : {\n\t\t\t\t\t\tlist: {\n\t\t\t\t\t\t\tclasses: \n\t\t\t\t\t\t\t \t{\n\t\t\t\t\t\t\t \t\tactive: \"").append(convertToString(variables.getVariable("listClass"))).append("\"\n\t\t\t\t\t\t\t \t},\n\t\t\t\t\t\t\tstyle: \n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\tactive: \"'").append(convertToString(variables.getVariable("listStyle"))).append("\"\n\t\t\t\t\t\t\t\t} \t\t\t\t\t\t\n\t\t\t\t\t\t},\n\t\t\t\t\t\titem: {\n\t\t\t\t\t\t\tnormal : \"").append(convertToString(variables.getVariable("itemClass"))).append("\",\n\t\t\t\t\t\t\tselected: \"").append(convertToString(variables.getVariable("itemSelectedClass"))).append("\"\n\t\t\t\t\t\t}\n\t\t\t}\n\t\t};\n\t\t \n\t\t\n\t\tvar combobox = new Richfaces.ComboBox( \"").append(convertToString(clientId)).append("\", \n\t\t\t\t\t\t\t   \t\t\t\t   \"").append(convertToString(clientId)).append("list\", \n\t\t\t\t\t\t\t   \t\t\t\t   \"").append(convertToString(clientId)).append("listParent\",\n\t\t\t\t\t\t\t   \t\t\t\t   \"").append(convertToString(clientId)).append("comboboxValue\",\n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(clientId)).append("comboboxField\", \n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(clientId)).append("comboboxButton\", \n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(clientId)).append("comboBoxButtonBG\",\n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(clientId)).append("shadow\",\n\t\t\t\t\t\t\t\t\t\t\t   new Richfaces.ComboBoxStyles(), \n\t\t\t\t\t\t\t\t\t\t\t   comboboxUserStyles, \n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(variables.getVariable("listWidth"))).append("\", \"").append(convertToString(variables.getVariable("listHeight"))).append("\",\n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(getItemsTextAsJSArray(context, component, items))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(variables.getVariable("directInputSuggestions"))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(variables.getVariable("filterNewValues"))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(variables.getVariable("selectFirstOnUpdate"))).append(",\n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(getAsEventHandler(context, component, "onlistcall"))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(getAsEventHandler(context, component, "onlistclose"))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(getAsEventHandler(context, component, "onselect"))).append(",\n\t\t\t\t\t\t\t\t\t\t\t   \"").append(convertToString(variables.getVariable("defaultLabel"))).append("\",\n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(variables.getVariable("disabled"))).append(", ").append(convertToString(variables.getVariable("convertedValue"))).append(", \n\t\t\t\t\t\t\t\t\t\t\t   ").append(convertToString(component.getAttributes().get("showDelay"))).append(",  ").append(convertToString(component.getAttributes().get("hideDelay"))).append(");").toString()), null);
         writer.endElement("script");
         writer.endElement("div");
    }

    private String convertToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
    */
}
