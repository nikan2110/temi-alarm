package com.nikita.doroshenko.japanmeeting.services;

import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel;
import com.nikita.doroshenko.japanmeeting.models.PatientModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface PatientListService {

    @GET("patients")
    Call<List<PatientModel>> getAllPatients();

    @PATCH("patients/{patientId}")
    Call<CheckBoxModel> updateStatus(@Path("patientId") String patientId, @Body Map<String, Boolean> patientStatusUpdate);

}
