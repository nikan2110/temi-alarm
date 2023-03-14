package com.nikita.doroshenko.japanmeeting.models;

public class PatientModel {

    private String id;
    private String patientName;
    private String patientPhone;
    private String patientAge;
    private String patientType;
    private int patientStatus;
    private boolean checked;

    public PatientModel(String id, String patientName, String patientPhone, String patientAge, String patientType, int patientStatus, boolean checked) {
        this.id = id;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.patientAge = patientAge;
        this.patientType = patientType;
        this.patientStatus = patientStatus;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public int getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(int patientStatus) {
        this.patientStatus = patientStatus;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "id='" + id + '\'' +
                ", name='" + patientName + '\'' +
                ", phone='" + patientPhone + '\'' +
                ", age='" + patientAge + '\'' +
                ", type='" + patientType + '\'' +
                ", status=" + patientStatus +
                ", checked=" + checked +
                '}';
    }
}
