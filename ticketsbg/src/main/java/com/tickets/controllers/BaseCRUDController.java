package com.tickets.controllers;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;

import com.tickets.services.Service;

public abstract class BaseCRUDController<E> extends BaseController {

    protected abstract String getSingleScreenName();
    protected abstract String getListScreenName();
    protected abstract ListDataModel getModel();
    protected abstract void setEntity(E entity);
    protected abstract E getEntity();
    protected abstract Service getService();
    protected abstract E createEntity();
    protected abstract void refreshList();

    @SuppressWarnings("unchecked")
    public String edit() {
        setEntity((E) getModel().getRowData());
        return getSingleScreenName();
    }

    @SuppressWarnings("unchecked")
    public void save() {
        getService().save(getEntity());
        refreshList();
    }

    public void newRecord() {
        setEntity(createEntity());
    }

    @SuppressWarnings("unchecked")
    public String delete() {
        getService().delete((getModel().getRowData()));
        refreshList();
        return getListScreenName();
    }

    @PostConstruct
    public void init() {
        refreshList();
    }
}
