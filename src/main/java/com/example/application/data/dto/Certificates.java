package com.example.application.data.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Certificates  {
    private Long id;
    private String citizenshipNo;
    private String gender;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isNew() {
        return New;
    }
    private boolean enabled;

    public void setNew(boolean aNew) {
        New = aNew;
    }

    private String nationality;
    @JsonProperty("new")
    private boolean New;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCitizenshipNo() {
        return citizenshipNo;
    }

    public void setCitizenshipNo(String citizenshipNo) {
        this.citizenshipNo = citizenshipNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


}
