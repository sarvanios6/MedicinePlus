package com.sarvan.medicineplus.realm;


/**
 * Created by Sarvan on 29/11/16.
 */

public class Users {
    private String name;
    private String email;
    private String photoUrl;
    private String channel;

    public Users() {
    }

    public Users(String name, String email, String photoUrl, String channel) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getphotoUrl() {
        return photoUrl;
    }

    public void setphotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
