package com.polybookshare.master.campusbookshare;

/**
 * Created by MASTER on 5/3/16.
 */
public class Klass {
    private int number;
    private String dep;
    private String professor;
    private String lastReview;
    private int comments;
    private int files;
    private int reviews;

    public Klass(int number, String dep, String professor, String lastReview, int comments, int files, int reviews) {
        this.number = number;
        this.dep = dep;
        this.professor = professor;
        this.lastReview = lastReview;
        this.comments = comments;
        this.files = files;
        this.reviews = reviews;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getLastReview() {
        return lastReview;
    }

    public void setLastReview(String lastReview) {
        this.lastReview = lastReview;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }



}
