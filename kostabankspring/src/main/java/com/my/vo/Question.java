package com.my.vo;

public class Question {
    private String question_id;
    private String content;
    private String correct_answer;
    private String explanation;
    private int total_answer_count;
    private int correct_answer_count;
    private int question_ox;
    private int mn_total;
    private String mn_id;

    public Question() {
    }

    public Question(String question_id, String content, String correct_answer, String explanation, int total_answer_count, int correct_answer_count, int question_ox, int mn_total) {
        this.question_id = question_id;
        this.content = content;
        this.correct_answer = correct_answer;
        this.explanation = explanation;
        this.total_answer_count = total_answer_count;
        this.correct_answer_count = correct_answer_count;
        this.question_ox = question_ox;
        this.mn_total = mn_total;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getTotal_answer_count() {
        return total_answer_count;
    }

    public void setTotal_answer_count(int total_answer_count) {
        this.total_answer_count = total_answer_count;
    }

    public int getCorrect_answer_count() {
        return correct_answer_count;
    }

    public void setCorrect_answer_count(int correct_answer_count) {
        this.correct_answer_count = correct_answer_count;
    }

    public int getQuestion_ox() {
        return question_ox;
    }

    public void setQuestion_ox(int question_ox) {
        this.question_ox = question_ox;
    }

    public int getMn_total() {
        return mn_total;
    }

    public void setMn_total(int mn_total) {
        this.mn_total = mn_total;
    }

    public String getMn_id() {
        return mn_id;
    }

    public void setMn_id(String mn_id) {
        this.mn_id = mn_id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question_id='" + question_id + '\'' +
                ", content='" + content + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                ", explanation='" + explanation + '\'' +
                ", total_answer_count=" + total_answer_count +
                ", correct_answer_count=" + correct_answer_count +
                ", question_ox=" + question_ox +
                ", mn_total=" + mn_total +
                ", mn_id='" + mn_id + '\'' +
                '}';
    }
}
