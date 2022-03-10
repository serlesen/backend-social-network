package com.sergio.socialnetwork.dto;

public class UserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;

    public UserDto() {
        super();
    }

    public UserDto(String id, String firstName, String lastName, String login) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
