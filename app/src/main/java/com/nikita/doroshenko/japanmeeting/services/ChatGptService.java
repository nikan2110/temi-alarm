package com.nikita.doroshenko.japanmeeting.services;

import com.nikita.doroshenko.japanmeeting.models.ChatGPTAnswerModel;
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatGptService {

    @POST("chatgpt")
    Call<ChatGPTAnswerModel> askQuestion(@Body Map<String, String> question);

}
