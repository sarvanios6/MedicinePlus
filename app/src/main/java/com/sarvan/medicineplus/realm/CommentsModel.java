package com.sarvan.medicineplus.realm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sarvan on 30/01/17.
 */

public class CommentsModel {
    private String comments;
    private String timeStamp;
    private String userName;
    private String userId;

    // Constructor Default
    public CommentsModel() {

    }

    // Constructor
    public CommentsModel(String comments,String timeStamp, String userName, String userId) {
        this.comments = comments;
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.userId = userId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
