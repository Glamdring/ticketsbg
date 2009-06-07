package com.tickets.controllers.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.dao.Dao;
import com.tickets.model.Price;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;

@Component("searchResultEntryConverter")
@Scope("singleton")
public class SearchResultEntryConverter implements Converter {

    @Autowired
    private Dao dao;

    @Override
    public Object getAsObject(FacesContext facescontext,
            UIComponent uicomponent, String s) throws ConverterException {
        String[] parts = s.split(":");

        Run run = dao.getById(Run.class, Integer.parseInt(parts[0]));
        Price price = dao.getById(Price.class, Integer.parseInt(parts[1]));

        SearchResultEntry holder = new SearchResultEntry(run, price);

        return holder;
    }

    @Override
    public String getAsString(FacesContext facescontext,
            UIComponent uicomponent, Object obj) throws ConverterException {
        if (!(obj instanceof SearchResultEntry))
            throw new ConverterException("Object is of class " + obj.getClass() + ", instead of RunPriceHolder");

        SearchResultEntry holder = (SearchResultEntry) obj;

        return holder.getRun().getRunId() + ":" + holder.getPrice().getPriceId();
    }

}
