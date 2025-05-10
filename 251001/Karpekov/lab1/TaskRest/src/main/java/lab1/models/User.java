package lab1.models;

public class User {
    private long Id;
    private String Login = "";
    private String Firstname = "";
    private String Lastname = "";

    public String getFirstname() {
        return Firstname;
    }

    public User() {
    }

    public User(long id, String login, String firstname, String lastname) {
        Id = id;
        Login = login;
        Firstname = firstname;
        Lastname = lastname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
