package com.my.vo;

public class Qa {
    private String qa_id;
    private User user;
    private String qa_title;
    private String qa_content;
    private String qa_wdate;
    private String qa_file;
    private int qa_new;
    private int qa_status;
    private String qa_sol_wdate;
    private String qa_sol_content;
    private int qa_total;

    public Qa() {
    }

    public Qa(String qa_id, String qa_title, String qa_content, String qa_wdate, String qa_file, int qa_new, int qa_status, String qa_sol_wdate, String qa_sol_content, int qa_total) {
        this.qa_id = qa_id;
        this.qa_title = qa_title;
        this.qa_content = qa_content;
        this.qa_wdate = qa_wdate;
        this.qa_file = qa_file;
        this.qa_new = qa_new;
        this.qa_status = qa_status;
        this.qa_sol_wdate = qa_sol_wdate;
        this.qa_sol_content = qa_sol_content;
        this.qa_total = qa_total;
    }

    public String getQa_id() {
        return qa_id;
    }

    public void setQa_id(String qa_id) {
        this.qa_id = qa_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQa_title() {
        return qa_title;
    }

    public void setQa_title(String qa_title) {
        this.qa_title = qa_title;
    }

    public String getQa_content() {
        return qa_content;
    }

    public void setQa_content(String qa_content) {
        this.qa_content = qa_content;
    }

    public String getQa_wdate() {
        return qa_wdate;
    }

    public void setQa_wdate(String qa_wdate) {
        this.qa_wdate = qa_wdate;
    }

    public String getQa_file() {
        return qa_file;
    }

    public void setQa_file(String qa_file) {
        this.qa_file = qa_file;
    }

    public int getQa_new() {
        return qa_new;
    }

    public void setQa_new(int qa_new) {
        this.qa_new = qa_new;
    }

    public int getQa_status() {
        return qa_status;
    }

    public void setQa_status(int qa_status) {
        this.qa_status = qa_status;
    }

    public String getQa_sol_wdate() {
        return qa_sol_wdate;
    }

    public void setQa_sol_wdate(String qa_sol_wdate) {
        this.qa_sol_wdate = qa_sol_wdate;
    }

    public String getQa_sol_content() {
        return qa_sol_content;
    }

    public void setQa_sol_content(String qa_sol_content) {
        this.qa_sol_content = qa_sol_content;
    }

    public int getQa_total() {
        return qa_total;
    }

    public void setQa_total(int qa_total) {
        this.qa_total = qa_total;
    }


    @Override
    public String toString() {
        return "Qa{" +
                "qa_id='" + qa_id + '\'' +
                ", user=" + user +
                ", qa_title='" + qa_title + '\'' +
                ", qa_content='" + qa_content + '\'' +
                ", qa_wdate='" + qa_wdate + '\'' +
                ", qa_file='" + qa_file + '\'' +
                ", qa_new=" + qa_new +
                ", qa_status=" + qa_status +
                ", qa_sol_wdate='" + qa_sol_wdate + '\'' +
                ", qa_sol_content='" + qa_sol_content + '\'' +
                ", qa_total=" + qa_total +
                '}';
    }
}
