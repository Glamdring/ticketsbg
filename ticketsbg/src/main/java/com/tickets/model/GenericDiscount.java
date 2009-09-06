package com.tickets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GenericDiscount extends DataObject implements Serializable, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    private Firm firm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public Firm getFirm() {
        return firm;
    }



    public void setFirm(Firm firm) {
        this.firm = firm;
    }



    @Override
    public String getLabel() {
        return getName();
    }
}
