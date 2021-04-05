package com.my.vo;

import java.util.List;

public class Board {
    private String board_id;
    private User user;
    private String board_subtitle;
    private String board_title;
    private String board_content;
    private String board_wdate;
    private int board_view;
    private String board_file;
    private List<BoardComment> board_comment;
    private int board_up;
    private int board_total;

    public Board() {
    }

    public Board(String board_id, User user, String board_subtitle, String board_title, String board_content, String board_wdate, int board_view, String board_file, List<BoardComment> board_comment, int board_up) {
        this.board_id = board_id;
        this.user = user;
        this.board_subtitle = board_subtitle;
        this.board_title = board_title;
        this.board_content = board_content;
        this.board_wdate = board_wdate;
        this.board_view = board_view;
        this.board_file = board_file;
        this.board_comment = board_comment;
        this.board_up = board_up;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBoard_subtitle() {
        return board_subtitle;
    }

    public void setBoard_subtitle(String board_subtitle) {
        this.board_subtitle = board_subtitle;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public String getBoard_wdate() {
        return board_wdate;
    }

    public void setBoard_wdate(String board_wdate) {
        this.board_wdate = board_wdate;
    }

    public int getBoard_view() {
        return board_view;
    }

    public void setBoard_view(int board_view) {
        this.board_view = board_view;
    }

    public String getBoard_file() {
        return board_file;
    }

    public void setBoard_file(String board_file) {
        this.board_file = board_file;
    }

    public List<BoardComment> getBoard_comment() {
        return board_comment;
    }

    public void setBoard_comment(List<BoardComment> board_comment) {
        this.board_comment = board_comment;
    }

    public int getBoard_up() {
        return board_up;
    }

    public void setBoard_up(int board_up) {
        this.board_up = board_up;
    }

    public int getBoard_total() {
        return board_total;
    }

    public void setBoard_total(int board_total) {
        this.board_total = board_total;
    }

    @Override
    public String toString() {
        return "Board{" +
                "board_id='" + board_id + '\'' +
                ", user=" + user +
                ", board_subtitle='" + board_subtitle + '\'' +
                ", board_title='" + board_title + '\'' +
                ", board_content='" + board_content + '\'' +
                ", board_wdate='" + board_wdate + '\'' +
                ", board_view=" + board_view +
                ", board_file='" + board_file + '\'' +
                ", board_comment=" + board_comment +
                ", board_up=" + board_up +
                '}';
    }
}
