package com.dedov.distributed_computing.model;

public class editor {

    private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
