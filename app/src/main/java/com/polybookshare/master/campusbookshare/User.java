package com.polybookshare.master.campusbookshare;

/**
 * Created by MASTER on 5/17/16.
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String school;
    private String votes;
    private String bio;
    private String virals [];
    private String picture_url;



    public User(String id, String name,
                String email, String phone,
                String school, String votes,
                String bio, String[] virals, String picture_url) {
        this.picture_url = picture_url;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.school = school;
        this.votes = votes;
        this.bio = bio;
        this.virals = virals;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String[] getVirals() {
        return virals;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public void setVirals(String[] virals) {
        this.virals = virals;
    }
}
