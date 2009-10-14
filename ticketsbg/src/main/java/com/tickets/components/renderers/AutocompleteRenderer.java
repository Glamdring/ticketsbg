package com.tickets.components.renderers;

import java.io.IOException;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.ScriptUtils;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.RendererUtils;
import org.richfaces.component.UIComboBox;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.html.ComboBoxRenderer;

public class AutocompleteRenderer extends ComboBoxRenderer {

    /**
     * Source copied from the (decompiled) richfaces class, with the rendering
     * of the button - removed.
     *
     * Search for "//Added / Removed" comment to see where code has been modified
     */

    @SuppressWarnings("unchecked")
    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context,
            UIComboBox component, ComponentVariables variables)
            throws IOException {
        String clientId = component.getClientId(context);

        Boolean disabled = (Boolean) component.getAttributes().get("disabled");
        variables.setVariable("disabled", disabled);

        String listHeight = (String) component.getAttributes()
                .get("listHeight");
        if ((listHeight == null) || (listHeight.length() == 0)
                || (listHeight.trim().startsWith("0")))
            listHeight = "200px";
        else {
            listHeight = HtmlUtil.qualifySize(listHeight);
        }
        variables.setVariable("listHeight", listHeight);

        String width = (String) component.getAttributes().get("width");
        String correction = null;
        if ((width == null) || (width.length() == 0)
                || (width.trim().startsWith("0"))) {
            width = "150px";
        } else {
            width = HtmlUtil.qualifySize(width);
        }

        correction = width.substring(0, width.indexOf("px"));
        correction = (Integer.parseInt(correction) - 10) + "px";

        variables.setVariable("width", width);
        variables.setVariable("correction", correction);

        String listWidth = (String) component.getAttributes().get("listWidth");

        if ((listWidth == null) || (listWidth.length() == 0)
                || (listWidth.trim().startsWith("0")))
            listWidth = width;
        else {
            listWidth = HtmlUtil.qualifySize(listWidth);
        }
        variables.setVariable("listWidth", listWidth);

        String inputSize = (String) component.getAttributes().get("inputSize");
        variables.setVariable("inputSize", inputSize);

        Object value = component.getSubmittedValue();
        if (value == null) {
            value = component.getAttributes().get("value");
        }

        String valueStyle = "rich-combobox-font-inactive";
        value = getConvertedStringValue(context, component, value);
        if ("".equals(value)) {
            valueStyle = "rich-combobox-font-disabled";
        }
        variables.setVariable("value", value);

        variables.setVariable("valueStyle", valueStyle);

        Object styleClass = component.getAttributes().get("styleClass");
        variables.setVariable("styleClass", styleClass);

        Object style = component.getAttributes().get("style");
        variables.setVariable("style", style);

        String buttonIcon = (String) component.getAttributes()
                .get("buttonIcon");
        if (!("".equals(buttonIcon))) {
            buttonIcon = "url('"
                    + getResource(buttonIcon).getUri(context, component) + "')";
        }
        variables.setVariable("buttonIcon", buttonIcon);

        String buttonIconDisabled = (String) component.getAttributes().get(
                "buttonIconDisabled");
        if (!("".equals(buttonIconDisabled))) {
            buttonIconDisabled = "url('"
                    + getResource(buttonIconDisabled)
                            .getUri(context, component) + "')";
        }
        variables.setVariable("buttonIconDisabled", buttonIconDisabled);

        String buttonIconInactive = (String) component.getAttributes().get(
                "buttonIconInactive");
        if (!("".equals(buttonIconInactive))) {
            buttonIconInactive = "url('"
                    + getResource(buttonIconInactive)
                            .getUri(context, component) + "')";
        }
        variables.setVariable("buttonIconInactive", buttonIconInactive);
        variables.setVariable("enableManualInput", Boolean.valueOf(!(component
                .isEnableManualInput())));

        variables.setVariable("spacer", getResource("images/spacer.gif")
                .getUri(context, component));

        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "id", clientId);

        writer.startElement("div", component);
        getUtils().writeAttribute(
                writer,
                "class",
                "rich-combobox-font rich-combobox "
                        + convertToString(variables.getVariable("styleClass")));
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "combobox");
        getUtils()
                .writeAttribute(
                        writer,
                        "style",
                        "width:"
                                + convertToString(variables
                                        .getVariable("width"))
                                + ";"
                                + convertToString(variables
                                        .getVariable("style")));

        getUtils().encodeAttributesFromArray(
                context,
                component,
                new String[] { "align", "dir", "lang", "onclick", "ondblclick",
                        "onkeydown", "onkeypress", "onkeyup", "onmousedown",
                        "onmousemove", "onmouseout", "onmouseover",
                        "onmouseup", "title", "xml:lang" });

        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class", "rich-combobox-list-cord");

        writer.endElement("div");
        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class",
                "rich-combobox-font rich-combobox-shell");
        getUtils().writeAttribute(
                writer,
                "style",
                "width:" + convertToString(variables.getVariable("width"))
                        + ";");

        writer.startElement("input", component);
        getUtils().writeAttribute(writer, "autocomplete", "off");
        getUtils().writeAttribute(
                writer,
                "class",
                convertToString(variables.getVariable("valueStyle"))
                        + " rich-combobox-input-inactive  "
                        + convertToString(component.getAttributes().get(
                                "inputInactiveClass")));
        getUtils().writeAttribute(writer, "disabled",
                variables.getVariable("disabled"));
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "comboboxField");
        getUtils().writeAttribute(writer, "name",
                convertToString(clientId) + "comboboxField");
        getUtils().writeAttribute(writer, "onfocus",
                component.getAttributes().get("onfocus"));
        getUtils().writeAttribute(writer, "readonly",
                variables.getVariable("enableManualInput"));
        getUtils().writeAttribute(
                writer,
                "style",
                "width:"
                        + convertToString(variables.getVariable("correction"))
                        + "; "
                        + convertToString(component.getAttributes().get(
                                "inputInactiveStyle")));
        getUtils().writeAttribute(writer, "tabindex",
                component.getAttributes().get("tabindex"));
        getUtils().writeAttribute(writer, "type", "text");
        getUtils().writeAttribute(writer, "value",
                variables.getVariable("value"));
        getUtils().writeAttribute(writer, "onblur",
                component.getAttributes().get("onblur"));

        writer.endElement("input");
        writer.startElement("input", component);

        //Removed
//        getUtils()
//                .writeAttribute(
//                        writer,
//                        "class",
//                        "rich-combobox-font-inactive rich-combobox-button-background rich-combobox-button-inactive");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "comboBoxButtonBG");
        getUtils().writeAttribute(writer, "readonly", "true");
        getUtils().writeAttribute(writer, "tabindex", "-1");
        getUtils().writeAttribute(writer, "type", "text");
        getUtils().writeAttribute(writer, "value", "");
        //Added
        getUtils().writeAttribute(writer, "style", "display: none;");

        writer.endElement("input");
        writer.startElement("input", component);
        //Added
        getUtils().writeAttribute(writer, "style", "display: none;");
        //Removed
//        getUtils()
//                .writeAttribute(
//                        writer,
//                        "class",
//                        "rich-combobox-font-inactive rich-combobox-button-icon-inactive rich-combobox-button-inactive "
//                                + convertToString(variables
//                                        .getVariable("buttonInactiveClass")));
        getUtils().writeAttribute(writer, "disabled",
                variables.getVariable("disabled"));
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "comboboxButton");
        getUtils().writeAttribute(writer, "readonly", "true");
        getUtils().writeAttribute(
                writer,
                "style",
                convertToString(component.getAttributes().get("buttonStyle"))
                        + "; background-image: "
                        + convertToString(variables
                                .getVariable("buttonIconInactive")) + ";");
        getUtils().writeAttribute(writer, "tabindex", "-1");
        getUtils().writeAttribute(writer, "type", "text");
        getUtils().writeAttribute(writer, "value", "");

        writer.endElement("input");
        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class",
                "rich-combobox-strut rich-combobox-font");
        getUtils()
                .writeAttribute(
                        writer,
                        "style",
                        "width:"
                                + convertToString(variables
                                        .getVariable("correction")));

        writer.writeText(convertToString("Strut"), null);

        writer.endElement("div");
        writer.endElement("div");
        writer.startElement("div", component);
        getUtils().writeAttribute(
                writer,
                "class",
                "rich-combobox-list-cord "
                        + convertToString(component.getAttributes().get(
                                "listClass")));
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "listParent");
        getUtils().writeAttribute(
                writer,
                "style",
                "display:none; "
                        + convertToString(component.getAttributes().get(
                                "listStyle"))
                        + "; position:absolute;z-index:1000;");

        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class", "rich-combobox-shadow");

        writer.startElement("table", component);
        getUtils().writeAttribute(writer, "border", "0");
        getUtils().writeAttribute(writer, "cellpadding", "0");
        getUtils().writeAttribute(writer, "cellspacing", "0");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "shadow");

        writer.startElement("tr", component);

        writer.startElement("td", component);
        getUtils().writeAttribute(writer, "class", "rich-combobox-shadow-tl");

        writer.startElement("img", component);
        getUtils().writeAttribute(writer, "alt", "");
        getUtils().writeAttribute(writer, "height", "1");
        getUtils().writeAttribute(writer, "src",
                variables.getVariable("spacer"));
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
        getUtils().writeAttribute(writer, "src",
                variables.getVariable("spacer"));
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
        getUtils().writeAttribute(writer, "src",
                variables.getVariable("spacer"));
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
        getUtils().writeAttribute(writer, "src",
                variables.getVariable("spacer"));
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
        getUtils().writeAttribute(writer, "class",
                "rich-combobox-list-position");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "listPosition");

        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class",
                "rich-combobox-list-decoration");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "listDecoration");

        writer.startElement("div", component);
        getUtils().writeAttribute(writer, "class", "rich-combobox-list-scroll");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "list");

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");

        variables.setVariable("hiddenValue", value);

        writer.startElement("input", component);
        getUtils().writeAttribute(writer, "autocomplete", "off");
        getUtils().writeAttribute(writer, "id",
                convertToString(clientId) + "comboboxValue");
        getUtils().writeAttribute(writer, "name", clientId);
        getUtils().writeAttribute(writer, "type", "hidden");

        writer.endElement("input");
        writer.endElement("div");
        HashMap classes = new HashMap();
        getUtils().addToScriptHash(classes, "normal",
                component.getAttributes().get("buttonInactiveClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(classes, "active",
                component.getAttributes().get("buttonClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(classes, "disabled",
                component.getAttributes().get("buttonDisabledClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap style1 = new HashMap();
        getUtils().addToScriptHash(style1, "normal",
                component.getAttributes().get("buttonInactiveStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style1, "active",
                component.getAttributes().get("buttonStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style1, "disabled",
                component.getAttributes().get("buttonDisabledStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap button = new HashMap();
        getUtils().addToScriptHash(button, "classes", classes, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(button, "style", style1, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap style2 = new HashMap();
        getUtils().addToScriptHash(style2, "normal",
                variables.getVariable("buttonIconInactive"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style2, "active",
                variables.getVariable("buttonIcon"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style2, "disabled",
                variables.getVariable("buttonIconDisabled"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap buttonicon = new HashMap();
        getUtils().addToScriptHash(buttonicon, "style", style2, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap classes2 = new HashMap();
        getUtils().addToScriptHash(classes2, "normal",
                component.getAttributes().get("inputInactiveClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(classes2, "active",
                component.getAttributes().get("inputClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(classes2, "disabled",
                component.getAttributes().get("inputDisabledClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap style3 = new HashMap();
        getUtils().addToScriptHash(style3, "normal",
                component.getAttributes().get("inputInactiveStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style3, "active",
                component.getAttributes().get("inputStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(style3, "disabled",
                component.getAttributes().get("inputDisabledStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap field = new HashMap();
        getUtils().addToScriptHash(field, "classes", classes2, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(field, "style", style3, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap classes3 = new HashMap();
        getUtils().addToScriptHash(classes3, "active",
                component.getAttributes().get("listClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap style4 = new HashMap();
        getUtils().addToScriptHash(style4, "active",
                component.getAttributes().get("listStyle"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap list = new HashMap();
        getUtils().addToScriptHash(list, "classes", classes3, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(list, "style", style4, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap item = new HashMap();
        getUtils().addToScriptHash(item, "normal",
                component.getAttributes().get("itemClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(item, "selected",
                component.getAttributes().get("itemSelectedClass"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap combolist = new HashMap();
        getUtils().addToScriptHash(combolist, "list", list, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(combolist, "item", item, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap userStyles = new HashMap();
        getUtils().addToScriptHash(userStyles, "button", button, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(userStyles, "buttonicon", buttonicon, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(userStyles, "field", field, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(userStyles, "combolist", combolist, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap listOptions = new HashMap();
        getUtils().addToScriptHash(listOptions, "listWidth",
                variables.getVariable("listWidth"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(listOptions, "listHeight",
                variables.getVariable("listHeight"), "200px",
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(listOptions, "itemsText",
                getItems(context, component), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(listOptions, "onlistcall",
                component.getAttributes().get("onlistcall"), null,
                RendererUtils.ScriptHashVariableWrapper.EVENT_HANDLER);
        getUtils().addToScriptHash(listOptions, "onlistclose",
                component.getAttributes().get("onlistclose"), null,
                RendererUtils.ScriptHashVariableWrapper.EVENT_HANDLER);

        getUtils().addToScriptHash(listOptions, "selectFirstOnUpdate",
                component.getAttributes().get("selectFirstOnUpdate"), "true",
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(listOptions, "showDelay",
                component.getAttributes().get("showDelay"), "0",
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(listOptions, "hideDelay",
                component.getAttributes().get("hideDelay"), "0",
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        HashMap fields = new HashMap();
        getUtils().addToScriptHash(fields, "directInputSuggestions",
                component.getAttributes().get("directInputSuggestions"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(fields, "defaultLabel",
                component.getAttributes().get("defaultLabel"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(fields, "disabled",
                variables.getVariable("disabled"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(fields, "filterNewValues",
                component.getAttributes().get("filterNewValues"), "true",
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        getUtils().addToScriptHash(fields, "onselect",
                component.getAttributes().get("onselect"), null,
                RendererUtils.ScriptHashVariableWrapper.EVENT_HANDLER);
        getUtils().addToScriptHash(fields, "onchange",
                component.getAttributes().get("onchange"), null,
                RendererUtils.ScriptHashVariableWrapper.EVENT_HANDLER);

        HashMap options = new HashMap();
        getUtils().addToScriptHash(options, "userStyles", userStyles, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(options, "listOptions", listOptions, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(options, "fields", fields, null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);
        getUtils().addToScriptHash(options, "value",
                variables.getVariable("value"), null,
                RendererUtils.ScriptHashVariableWrapper.DEFAULT);

        writer.startElement("script", component);
        getUtils().writeAttribute(writer, "type", "text/javascript");

        writer.writeText(convertToString("new Richfaces.ComboBox(\""
                + convertToString(clientId) + "\""), null);

        if (Boolean.valueOf(String.valueOf(!(getUtils().isEmpty(options))))
                .booleanValue()) {
            writer.writeText(convertToString(","), null);

            ScriptUtils.writeToStream(writer, options);
        }

        writer.writeText(convertToString(");"), null);

        writer.endElement("script");
        writer.endElement("div");
    }

    private String convertToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
}