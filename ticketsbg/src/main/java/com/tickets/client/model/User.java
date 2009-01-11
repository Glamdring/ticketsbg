package com.tickets.client.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(length = 40)
    private String password;

    @Transient
    private String repeatPassword;

    @Column
    private String email;

    @Column
    private String name;

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

    @ManyToMany(mappedBy="user")
    private Set<Firm> firms = new HashSet<Firm>();

    @Column
    private byte privs;

    @Column
    private boolean isStaff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
