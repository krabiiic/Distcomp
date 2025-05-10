package com.dedov.distributed_computing.dto.request;

import com.dedov.distributed_computing.validation.groups.OnCreateOrUpdate;
import com.dedov.distributed_computing.validation.groups.OnPatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class editorRequestTo {

    private long id;

    @Size(min = 2, max = 64, message = "Login size must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    @NotBlank(message = "Email can't be empty", groups = OnCreateOrUpdate.class)
    private String login;

    @NotEmpty(message="Password can't be empty", groups = OnCreateOrUpdate.class)
    @Size(min = 8, max = 128, message = "Password must be between 8..128 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    private String password;

    @Size(min = 2, max = 64, message = "Your firstname must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    private String firstname;

    @Size(min = 2, max = 64, message = "Your lastname must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    private String lastname;

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


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
