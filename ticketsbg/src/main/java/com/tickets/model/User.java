package com.tickets.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("1")
@NamedQueries({
        @NamedQuery(
            name = "User.login",
            query = "select u from User u where u.username=:username and u.password=:password"
        ),
        @NamedQuery(
            name = "User.tempLogin",
            query = "select u from User u where u.username=:username and u.temporaryPassword=:password"
        ),
        @NamedQuery(
            name = "User.getByEmail",
            query = "select u from User u where u.email=:email"
        )
})
public class User extends Customer implements Serializable {

    @Column(nullable = false, length = 40)
    private String username;

    @Column(length = 40)
    private String password;

    @Transient
    private String repeatPassword;

    @Column
    private boolean active;

    @Column
    private boolean changePasswordAfterLogin;

    @Column
    private String activationCode;

    @Column
    private String temporaryPassword;

    @Column
    private long registeredTimestamp;

    @OneToMany(mappedBy="user")
    @OrderBy("dtm DESC")
    private Set<UsersHistory> history = new HashSet<UsersHistory>();

    @ManyToMany
    @JoinTable(name="firmsUsers", joinColumns=@JoinColumn(name="userId", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="firmId", referencedColumnName="firmId"))
    private Set<Firm> firms = new HashSet<Firm>();

    @Column
    private byte privs;

    @Column
    private boolean isStaff;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UsersHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<UsersHistory> history) {
        this.history = history;
    }

    public Set<Firm> getFirms() {
        return firms;
    }

    public void setFirms(Set<Firm> firms) {
        this.firms = firms;
    }

    public byte getPrivs() {
        return privs;
    }

    public void setPrivs(byte privs) {
        this.privs = privs;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public boolean isChangePasswordAfterLogin() {
        return changePasswordAfterLogin;
    }

    public void setChangePasswordAfterLogin(boolean changePasswordAfterLogin) {
        this.changePasswordAfterLogin = changePasswordAfterLogin;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public long getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(long registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + username + " : " + getName() + " : " + password;
    }
}
