package com.nikita.doroshenko.japanmeeting.models;



public class CheckBoxModel {
    private String id;
    private String text;
    private boolean done;
    private String tag;
    private String shortDescription;
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
    public boolean isDone() {
        return done;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }


    @Override
    public String toString() {
        return "CheckBoxModel{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", done=" + done +
                ", tag='" + tag + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                '}';
    }
}
