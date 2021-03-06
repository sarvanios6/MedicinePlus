package com.sarvan.medicineplus.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sarvan on 29/12/16.
 */

public class UsersRealm extends RealmObject {
    @PrimaryKey
    private String channel;
    private String name;
    private String photoUrl;
    private String departmentName;

    public UsersRealm() {

    }

    public UsersRealm(String channel, String name,String departmentName,String photoUrl) {
        this.channel = channel;
        this.name = name;
        this.departmentName = departmentName;
        this.photoUrl = photoUrl;
    }

    public String getChannel() {

        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
