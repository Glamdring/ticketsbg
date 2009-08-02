package com.tickets.model;

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

/**
 * Agents are 3rd part firms that sell tickets of more than one company.
 *
 * @author Bozhidar Bozhanov
 *
 */
@Entity
@Table(name = "agents")
public class Agent implements Serializable, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int agentId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String bulstat;

    @Column
    private String other;

    @Column
    private boolean isActive;

    @ManyToMany
    @JoinTable(name="agentsStaff")
    private Set<User> staff = new HashSet<User>();

    public Set<User> getStaff() {
        return staff;
    }

    public void setStaff(Set<User> staff) {
        this.staff = staff;
    }

    public Agent() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public String getLabel() {
        return getName();
    }
}
