package com.my.vo;

import java.io.Serializable;

public class User implements Serializable {
    private String user_id;
    private String user_nickname;
    transient private String user_pwd;
    private String user_email;
    private String user_date;
    private int user_adm;

    public User() {
    }

    public User(String user_id, String user_nickname, String user_pwd, String user_email, String user_date, int user_adm) {
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.user_pwd = user_pwd;
        this.user_email = user_email;
        this.user_date = user_date;
        this.user_adm = user_adm;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }

    public int getUser_adm() {
        return user_adm;
    }

    public void setUser_adm(int user_adm) {
        this.user_adm = user_adm;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_pwd='" + user_pwd + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_date='" + user_date + '\'' +
                ", user_adm=" + user_adm +
                '}';
    }
}
