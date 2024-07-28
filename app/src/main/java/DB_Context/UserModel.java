package DB_Context;

public class UserModel {
    // ... (class members and methods)
    private int id;
    private String username,password;

    public UserModel(int id, String username,  String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    // ... (getter and setter methods for each member variable)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
