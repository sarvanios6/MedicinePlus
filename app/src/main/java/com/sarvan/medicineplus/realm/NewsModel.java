package com.sarvan.medicineplus.realm;

/**
 * NewsModel Class Definition
 */

public class NewsModel {
    private String id;
    private String drName;
    private String newsText;
    private String time;

    // Default Constructor
    public NewsModel() {
    }

    // Constructor
    public NewsModel(String id,String drName, String newsText, String time) {
        this.drName = drName;
        this.newsText = newsText;
        this.time = time;
        this.id = id;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
