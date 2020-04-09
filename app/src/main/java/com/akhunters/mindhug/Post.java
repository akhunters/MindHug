package com.akhunters.mindhug;

public class Post {
    String date,title,location,profileUrl,category;

    public Post() {
    }

    public Post(String date, String title, String location, String profileUrl, String category) {
        this.date = date;
        this.title = title;
        this.location = location;
        this.profileUrl = profileUrl;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
