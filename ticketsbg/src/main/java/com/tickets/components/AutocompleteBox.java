package com.tickets.components;

import org.richfaces.component.html.HtmlComboBox;

public class AutocompleteBox extends HtmlComboBox {

    public AutocompleteBox() {
        super();
        setRendererType("com.tickets.components.renderers.AutocompleteRenderer");
    }
}
