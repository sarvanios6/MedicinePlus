package com.sarvan.medicineplus.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sarvan on 23/01/17.
 */

public class DoctorRealm extends RealmObject {

    @PrimaryKey
    private String channel;
    private String name;
    private String photoUrl;
    private String departmentName;
    private String admin;

    public DoctorRealm() {

    }

    public DoctorRealm(String channel, String name, String photoUrl, String departmentName, String admin) {
        this.channel = channel;
        this.name = name;
        this.photoUrl = photoUrl;
        this.departmentName = departmentName;
        this.admin = admin;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
