package at.bif3.swe1.kisin.httpServer.serializer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCredentials {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    public UserCredentials(){}
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
    @Override
    public String toString(){
          return "Username: " + this.username + "\nPassword: " + this.password;
    }

}
