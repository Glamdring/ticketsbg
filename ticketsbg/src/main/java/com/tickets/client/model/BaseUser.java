package com.tickets.client.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

public class BaseUser {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "username", nullable = false, length = 40)
    private String username;

    private String password;

    private String email;

    private String name;

    private boolean active;

    @OneToMany(mappedBy="user")
    @OrderBy("dtm DESC")
    private Set<UsersHistory> history = new HashSet<UsersHistory>();

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
