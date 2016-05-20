package com.polybookshare.master.campusbookshare;

/**
 * Created by MASTER on 5/6/16.
 */
public class Book {
    private String author;
    private String title;
    private String condition;
    private String description;
    private String cover_url;
    private String price;
    private String id;
    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book(String id, String author, String title, String condition, String description, String price,
                String cover_url) {
        this.author = author;
        this.title = title;
        this.condition = condition;
        this.description = description;
        this.price = price;
        this.id = id;
        this.cover_url = cover_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
