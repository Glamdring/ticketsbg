package com.tickets.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tickets.controllers.security.DatabaseOperationType;

@Entity
public class LogEntry implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long logId;

    @ManyToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time = Calendar.getInstance();

    @Column
    private String entityClass;

    @Column
    private long entityId;

    @Column
    private DatabaseOperationType databaseOperationType;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public DatabaseOperationType getDatabaseOperationType() {
        return databaseOperationType;
    }

    public void setDatabaseOperationType(DatabaseOperationType databaseOperationType) {
        this.databaseOperationType = databaseOperationType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
