package com.sarvan.medicineplus.realm;

/**
 * Created by Sarvan on 29/12/16.
 */

public class Message {
    private String userId;
    private String message;
    private String messageDate;
    private String messagerName;
    private String departmentName;
    private String photoUrl;

    public Message() {
    }

    public Message(String userId, String message, String messagerName, String date) {
        this.userId = userId;
        this.message = message;
        this.messagerName = messagerName;
        this.messageDate = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
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
