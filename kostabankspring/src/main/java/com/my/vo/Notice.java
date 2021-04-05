package com.my.vo;

public class Notice {
    private String notice_id;
    private String notice_title;
    private String notice_content;
    private String notice_wdate;
    private int notice_view;
    private String notice_file;

    public Notice() {
    }

    public Notice(String notice_id, String notice_title, String notice_content, String notice_wdate, int notice_view, String notice_file) {
        this.notice_id = notice_id;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_wdate = notice_wdate;
        this.notice_view = notice_view;
        this.notice_file = notice_file;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getNotice_wdate() {
        return notice_wdate;
    }

    public void setNotice_wdate(String notice_wdate) {
        this.notice_wdate = notice_wdate;
    }

    public int getNotice_view() {
        return notice_view;
    }

    public void setNotice_view(int notice_view) {
        this.notice_view = notice_view;
    }

    public String getNotice_file() {
        return notice_file;
    }

    public void setNotice_file(String notice_file) {
        this.notice_file = notice_file;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "notice_id='" + notice_id + '\'' +
                ", notice_title='" + notice_title + '\'' +
                ", notice_content='" + notice_content + '\'' +
                ", notice_wdate='" + notice_wdate + '\'' +
                ", notice_view=" + notice_view +
                ", notice_file='" + notice_file + '\'' +
                '}';
    }
}
