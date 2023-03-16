package com.nikita.doroshenko.japanmeeting.services;

import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CheckBoxListService {

    @GET("checkBoxes")
    Call<List<CheckBoxModel>> getAllCheckBoxesByLanguage(@Query("language") String language);

    @GET("checkBoxes/status")
    Call<List<CheckBoxModel>> getAllCheckBoxesByLanguageAndStatus(@Query("language") String language, @Query("status") boolean status);

    @PATCH("checkBoxes/{checkBoxId}")
    Call<CheckBoxModel> updateCheckBoxStatus(@Path("checkBoxId") String checkBoxId, @Body Map<String, Boolean> checkBoxStatusUpdate);

}
