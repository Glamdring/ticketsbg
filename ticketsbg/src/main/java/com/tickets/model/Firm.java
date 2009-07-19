package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "firms")
public class Firm implements Serializable, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int firmId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String bulstat;

    @Column
    private String epayKin;

    @Column
    private String iban;

    @Column
    private String bank;

    @Column
    private String bic;

    @Column
    private String other;

    @Column
    private boolean isActive;

    @ManyToMany
    @JoinTable(name="firmsStaff")
    private Set<User> staff = new HashSet<User>();

    @Column
    private boolean allowDiscounts;

    public Set<User> getStaff() {
        return staff;
    }

    public void setStaff(Set<User> staff) {
        this.staff = staff;
    }

    public Firm() {
    }

    public int getFirmId() {
        return this.firmId;
    }

    public void setFirmId(int id) {
        this.firmId = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBulstat() {
        return this.bulstat;
    }

    public void setBulstat(String bulstat) {
        this.bulstat = bulstat;
    }

    public String getEpayKin() {
        return this.epayKin;
    }

    public void setEpayKin(String epayKin) {
        this.epayKin = epayKin;
    }

    public String getOther() {
        return this.other;
    }

    public void setOther(String other) {
        this.other = other;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public boolean isAllowDiscounts() {
        return allowDiscounts;
    }

    public void setAllowDiscounts(boolean allowDiscounts) {
        this.allowDiscounts = allowDiscounts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLabel() {
        return getName();
    }
}
