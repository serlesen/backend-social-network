package com.sergio.socialnetwork.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "app_users")
public class MongoUser {

    private String id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private List<MongoUser> friends;
    private LocalDateTime createdDate;

    public MongoUser() {
        super();
    }

    private MongoUser(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public MongoUser(String firstName, String lastName, String login, String password, List<MongoUser> friends, LocalDateTime createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.friends = friends;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<MongoUser> getFriends() {
        return friends;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(List<MongoUser> friends) {
        this.friends = friends;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public MongoUser buildShareableUser() {
        return new MongoUser(this.id, this.firstName, this.lastName);
    }
}
