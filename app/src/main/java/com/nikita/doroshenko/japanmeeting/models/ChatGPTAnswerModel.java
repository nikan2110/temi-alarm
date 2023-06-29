package com.nikita.doroshenko.japanmeeting.models;

public class ChatGPTAnswerModel {

    public ChatGPTAnswerModel() {
    }


    public ChatGPTAnswerModel(String answer) {
        this.answer = answer;
    }

    String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



    @Override
    public String toString() {
        return "ChatGPTAnswerModel{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
