package com.tickets.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tickets.controllers.security.AccessLevel;

/**
 * The User object represents three types of registered users:
 * - private customer
 * - company (customerType=Business, companyName != null)
 * - bus company staff (isStaff=true)
 *
 * Inheritance is not used because there is no practical difference between
 * the user types in terms of login. And flow permissions are identified by
 * the privs variable.
 *
 * @author Bozhidar Bozhanov
 *
 */
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
        ),
        @NamedQuery(
            name = "User.getByUsername",
            query = "select u from User u where u.username=:username"
        ),
        @NamedQuery(
            name = "User.getByFirm",
            query = "select u from User u WHERE u.firm=:firm AND u.agent IS NULL"
        ),
        @NamedQuery(
            name = "User.getAgentsUsersByFirm",
            query = "select u from User u WHERE u.firm=:firm AND u.agent IS NOT NULL"
        )
})
public class User extends Customer implements Serializable {

    /**
     * nullable = true, to let the column be nullable,
     * but NotEmpty to disallow creating User objects with no username
     * This separation is needed because when Customer objects
     * are inserted the username is allowed to be null.
     */
    @Column(nullable = true, length = 40)
    @Length(max=20, min=4)
    @NotEmpty
    private String username;

    @Column(length = 40)
    @Length(max=40, min=4)
    private String password;

    @Transient
    @Length(max=40, min=4)
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

    @ManyToOne
    private Firm firm;

    @ManyToOne
    private Agent agent;

    @Column
    private AccessLevel accessLevel = AccessLevel.PUBLIC;

    @Column
    private boolean isStaff;

    @Column
    private String foundUsVia;

    @Column
    private boolean receiveNewsletter;

    @Transient
    private boolean agreedToTerms;

    @Column
    private String city;

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

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
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

    public String getFoundUsVia() {
        return foundUsVia;
    }

    public void setFoundUsVia(String foundUsVia) {
        this.foundUsVia = foundUsVia;
    }

    public boolean isReceiveNewsletter() {
        return receiveNewsletter;
    }

    public void setReceiveNewsletter(boolean receiveNewsletter) {
        this.receiveNewsletter = receiveNewsletter;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean isAdministrator() {
        return getAccessLevel() == AccessLevel.ADMINISTRATOR;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + username + " : " + getName() + " : " + password;
    }
}
