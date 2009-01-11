package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * UsersHistory generated by hbm2java
 */
@Entity
@Table(name = "users_history", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class UsersHistory implements java.io.Serializable {

    private UsersHistoryId id;

    private int userId;

    private long dtm;

    private boolean isStaff; //?

    private User user;

    private Staff staff;

    @ManyToOne
    @JoinColumn(name="FK_users_history_user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="FK_users_history_staff")
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public UsersHistory() {
    }

    public UsersHistory(UsersHistoryId id, int userId, long dtm) {
        this.id = id;
        this.userId = userId;
        this.dtm = dtm;
    }

    public UsersHistory(UsersHistoryId id, int userId, long dtm,
            boolean isStaff) {
        this.id = id;
        this.userId = userId;
        this.dtm = dtm;
        this.isStaff = isStaff;
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "runId", column = @Column(name = "run_id", nullable = false)),
            @AttributeOverride(name = "seat", column = @Column(name = "seat", nullable = false)) })
    public UsersHistoryId getId() {
        return this.id;
    }

    public void setId(UsersHistoryId id) {
        this.id = id;
    }

    @Column(name = "user_id", unique = true, nullable = false)
    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "dtm", nullable = false)
    public long getDtm() {
        return this.dtm;
    }

    public void setDtm(long dtm) {
        this.dtm = dtm;
    }

    @Column(name = "is_staff")
    public boolean getIsStaff() {
        return this.isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UsersHistory other = (UsersHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
