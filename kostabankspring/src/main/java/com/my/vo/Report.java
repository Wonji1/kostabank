package com.my.vo;

public class Report {
    private String report_id;
    private User user;
    private Question question; // 종류 아이디 내용 정답률
    private String report_title;
    private String report_content;
    private String report_wdate;
    private int report_new;
    private int report_status;
    private String report_sol_wdate;
    private String report_sol_content;
    private int report_total;

    public Report() {
    }

    public Report(String report_id, User user, Question question, String report_title, String report_content, String report_wdate, int report_new, int report_status, String report_sol_wdate, String report_sol_content, int report_total) {
        this.report_id = report_id;
        this.user = user;
        this.question = question;
        this.report_title = report_title;
        this.report_content = report_content;
        this.report_wdate = report_wdate;
        this.report_new = report_new;
        this.report_status = report_status;
        this.report_sol_wdate = report_sol_wdate;
        this.report_sol_content = report_sol_content;
        this.report_total = report_total;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReport_content() {
        return report_content;
    }

    public void setReport_content(String report_content) {
        this.report_content = report_content;
    }

    public String getReport_wdate() {
        return report_wdate;
    }

    public void setReport_wdate(String report_wdate) {
        this.report_wdate = report_wdate;
    }

    public int getReport_new() {
        return report_new;
    }

    public void setReport_new(int report_new) {
        this.report_new = report_new;
    }

    public int getReport_status() {
        return report_status;
    }

    public void setReport_status(int report_status) {
        this.report_status = report_status;
    }

    public String getReport_sol_wdate() {
        return report_sol_wdate;
    }

    public void setReport_sol_wdate(String report_sol_date) {
        this.report_sol_wdate = report_sol_date;
    }

    public String getReport_sol_content() {
        return report_sol_content;
    }

    public void setReport_sol_content(String report_sol_content) {
        this.report_sol_content = report_sol_content;
    }

    public int getReport_total() {
        return report_total;
    }

    public void setReport_total(int report_total) {
        this.report_total = report_total;
    }

    @Override
    public String toString() {
        return "Report{" +
                "report_id='" + report_id + '\'' +
                ", user=" + user +
                ", question=" + question +
                ", report_title='" + report_title + '\'' +
                ", report_content='" + report_content + '\'' +
                ", report_wdate='" + report_wdate + '\'' +
                ", report_new=" + report_new +
                ", report_status=" + report_status +
                ", report_sol_wdate='" + report_sol_wdate + '\'' +
                ", report_sol_content='" + report_sol_content + '\'' +
                ", report_total=" + report_total +
                '}';
    }
}
