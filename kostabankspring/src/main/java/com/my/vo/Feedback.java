package com.my.vo;

public class Feedback {
    private int feedback_sort;  //0 은 qa  1은 report
    private String feedback_id; // 종류와 아이디를 같이 받아야함
    private String feedback_title;
    private String feedback_date;

    public Feedback() {
    }

    public Feedback(int feedback_sort, String feedback_id, String feedback_title, String feedback_date) {
        this.feedback_sort = feedback_sort;
        this.feedback_id = feedback_id;
        this.feedback_title = feedback_title;
        this.feedback_date = feedback_date;
    }

    public int getFeedback_sort() {
        return feedback_sort;
    }

    public void setFeedback_sort(int feedback_sort) {
        this.feedback_sort = feedback_sort;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFeedback_title() {
        return feedback_title;
    }

    public void setFeedback_title(String feedback_title) {
        this.feedback_title = feedback_title;
    }

    public String getFeedback_date() {
        return feedback_date;
    }

    public void setFeedback_date(String feedback_date) {
        this.feedback_date = feedback_date;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedback_sort=" + feedback_sort +
                ", feedback_id='" + feedback_id + '\'' +
                ", feedback_title='" + feedback_title + '\'' +
                ", feedback_date='" + feedback_date + '\'' +
                '}';
    }
}
