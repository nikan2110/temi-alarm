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
import retrofit2.http.Query;

public interface PatientListService {

    @GET("temi/patients")
    Call<List<PatientModel>> getAllPatients();

    @GET("temi/patients/status")
    Call<List<PatientModel>> getAllPatientsByStatus(@Query("status") boolean status);

    @PATCH("temi/patients/{patientId}")
    Call<PatientModel> updatePatientStatus(@Path("patientId") String patientId, @Body Map<String, Boolean> patientStatusUpdate);

}
