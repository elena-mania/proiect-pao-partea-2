package models;

public class User {
    private int userId; //id ul individual pentru fiecare user
    private String name, email, password;

    public User(String name, String email, String password, int id) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {}
    public void setUserId(int id){this.userId=id;}
    public int getUserId() {return userId;}

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return password;
    }

    public void setUserPassword(String password) {
        this.password = password;
    }
}
