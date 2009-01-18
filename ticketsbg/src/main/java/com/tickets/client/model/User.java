package com.tickets.client.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="users")
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
            name = "User.getByUsernameAndEmail",
            query = "select u from User u where u.username=:username and u.email=:email"
        )
})
public class User extends DataObject implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int userId;

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

    @ManyToMany
    @JoinTable(name="firms_users", joinColumns=@JoinColumn(name="user_id", referencedColumnName="userId"),
            inverseJoinColumns=@JoinColumn(name="firm_id", referencedColumnName="firmId"))
    private Set<Firm> firms = new HashSet<Firm>();

    @Column
    private byte privs;

    @Column
    private boolean isStaff;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        set("userId", id);
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        set("username", username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        set("password", password);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        set("email", email);
        this.email = email;
    }

    public String getName() {
        set("name", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        set("active", active);
        this.active = active;
    }

    public Set<UsersHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<UsersHistory> history) {
        set("history", history);
        this.history = history;
    }

    public Set<Firm> getFirms() {
        return firms;
    }

    public void setFirms(Set<Firm> firms) {
        set("firms", firms);
        this.firms = firms;
    }

    public byte getPrivs() {
        return privs;
    }

    public void setPrivs(byte privs) {
        set("privs", privs);
        this.privs = privs;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        set("isStaff", isStaff);
        this.isStaff = isStaff;
    }

    public boolean isChangePasswordAfterLogin() {
        return changePasswordAfterLogin;
    }

    public void setChangePasswordAfterLogin(boolean changePasswordAfterLogin) {
        set("changePasswordAfterLogin", changePasswordAfterLogin);
        this.changePasswordAfterLogin = changePasswordAfterLogin;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        set("activationCode", activationCode);
        this.activationCode = activationCode;
    }

    public long getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(long registeredTimestamp) {
        set("registeredTimestamp", registeredTimestamp);
        this.registeredTimestamp = registeredTimestamp;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        set("temporaryPassword", temporaryPassword);
        this.temporaryPassword = temporaryPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        set("repeatPassword", repeatPassword);
        this.repeatPassword = repeatPassword;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + username + " : " + name + " : " + password;
    }
}
