package com.sarvan.medicineplus.realm;

import io.realm.RealmObject;

/**
 * ChatMessage Class Model Definition
 */

public class ChatMessage extends RealmObject {
    private String message;
    private long messageDate;
    private String messagerName;
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
}
