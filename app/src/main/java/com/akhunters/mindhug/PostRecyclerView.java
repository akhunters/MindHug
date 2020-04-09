package com.akhunters.mindhug;

public class PostRecyclerView {

    String title, date, location,category,profileUrl;

    public PostRecyclerView(String title, String date, String location, String category, String profileUrl) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.profileUrl = profileUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public PostRecyclerView() {
    }
}
