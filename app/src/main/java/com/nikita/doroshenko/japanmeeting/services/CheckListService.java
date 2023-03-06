package com.nikita.doroshenko.japanmeeting.services;

import com.nikita.doroshenko.japanmeeting.models.CheckListModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CheckListService {

    @GET("checkLists")
    Call<List<CheckListModel>> getAllCheckLists();

    @PATCH("checkLists/{checkListId}")
    Call<CheckListModel> updateStatus(@Path("checkListId") String checkListIdm, @Body Map<String, Boolean> checkListStatusUpdate);

}
