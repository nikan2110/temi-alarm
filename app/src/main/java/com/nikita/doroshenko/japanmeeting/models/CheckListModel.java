package com.nikita.doroshenko.japanmeeting.models;



public class CheckListModel {
    private String id;
    private String text;
    private boolean done;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean done() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "CheckListModel{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", isDone=" + done +
                '}';
    }
}
