package com.tickets.components.renderers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.checkbox.HtmlCheckbox;
import org.apache.myfaces.renderkit.html.ext.HtmlCheckboxRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

public class AdvancedHtmlCheckboxRenderer extends HtmlCheckboxRenderer {

    @Override
    protected void renderSingleCheckbox(FacesContext facesContext,
            HtmlCheckbox checkbox) throws IOException {
        String forAttr = checkbox.getFor();
        if (forAttr == null) {
            throw new IllegalStateException("mandatory attribute 'for'");
        }

        int index = checkbox.getIndex();
        if (index < 0) {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = checkbox.findComponent(forAttr);
        if (uiComponent == null) {
            throw new IllegalStateException("Could not find component '"
                    + forAttr + "' (calling findComponent on component '"
                    + checkbox.getClientId(facesContext) + "')");
        }

        if (!(uiComponent instanceof UISelectMany)) {
            throw new IllegalStateException("UISelectMany expected");
        }

        UISelectMany uiSelectMany = (UISelectMany) uiComponent;
        Converter converter = getConverter(facesContext, uiSelectMany);
        List selectItemList = RendererUtils.getSelectItemList(uiSelectMany);

        if (index >= selectItemList.size()) {
            throw new IndexOutOfBoundsException("index " + index + " >= "
                    + selectItemList.size());
        }

        SelectItem selectItem = (SelectItem) selectItemList.get(index);
        Object itemValue = selectItem.getValue();
        String itemStrValue = getItemStringValue(facesContext, uiSelectMany,
                converter, itemValue);

        Set lookupSet = RendererUtils.getSubmittedValuesAsSet(facesContext,
                uiComponent, converter, uiSelectMany);
        boolean useSubmittedValues = lookupSet != null;

        if (!useSubmittedValues) {
            lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext,
                    uiComponent, converter, uiSelectMany);
        }

        boolean checked = false;
        if (lookupSet != null) {
            checked = lookupSet.contains(itemStrValue);
        }

        boolean disabled = isDisabled(facesContext, uiSelectMany);
        if (!disabled) {
            disabled = selectItem.isDisabled();
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("label", uiSelectMany);
        renderCheckbox(facesContext, uiSelectMany, itemStrValue, selectItem
                .getLabel(), disabled , checked, false, index);
        writer.endElement("label");
    }
}
