package daryadelan.sandogh.zikey.com.daryadelan.model;

public class LoginDto {
    public String username;
    public String password;
    public String grant_type;

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

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public LoginDto(String username, String password, String grant_type) {

        this.username = username;
        this.password = password;
        this.grant_type = grant_type;
    }
}
