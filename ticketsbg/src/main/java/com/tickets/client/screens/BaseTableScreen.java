package com.tickets.client.screens;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.table.NumberCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class BaseTableScreen<E extends ModelData> extends LayoutContainer {

    public BaseTableScreen() {
        setLayout(new BorderLayout());

        final NumberFormat currency = NumberFormat.getCurrencyFormat();
        final NumberFormat number = NumberFormat.getFormat("0.00");
        final NumberCellRenderer<Grid> numberRenderer = new NumberCellRenderer<Grid>(
                currency);


        GridCellRenderer gridNumber = new GridCellRenderer() {
            public String render(ModelData model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore store) {
                return numberRenderer.render(null, property, model.get(property));
            }
        };

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Company");
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("symbol");
        column.setHeader("Symbol");
        column.setWidth(100);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("last");
        column.setHeader("Last");
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setWidth(75);
        column.setRenderer(gridNumber);
        configs.add(column);

        column = new ColumnConfig("change", "Change", 100);
        column.setAlignment(HorizontalAlignment.RIGHT);
        configs.add(column);

        column = new ColumnConfig("date", "Last Updated", 100);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);

        ListStore<E> store = new ListStore<E>();
        store.add(new ArrayList<E>()); //TODO add items

        ColumnModel cm = new ColumnModel(configs);

        Grid<E> grid = new Grid<E>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("name");
        grid.setBorders(true);
        add(grid);

    }
}
