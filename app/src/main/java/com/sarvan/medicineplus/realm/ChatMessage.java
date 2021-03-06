package com.sarvan.medicineplus.realm;

import io.realm.RealmObject;

/**
 * ChatMessage Class Model Definition
 */

public class ChatMessage extends RealmObject {
    private String id;
    private String message;
    private long messageDate;
    private String messagerName;
    private String departmentName;
    private String photoUrl;

    public ChatMessage() {
    }

    public ChatMessage(String message, String messagerName,long date) {
        this.message = message;
        this.messagerName = messagerName;
        this.messageDate = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(long messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessagerName() {
        return messagerName;
    }

    public void setMessagerName(String messagerName) {
        this.messagerName = messagerName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
