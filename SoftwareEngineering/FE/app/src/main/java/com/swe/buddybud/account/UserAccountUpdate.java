package com.swe.buddybud.account;

import com.google.gson.annotations.SerializedName;

public class UserAccountUpdate {
    @SerializedName("id")
    private String userid;
    @SerializedName("lang")
    private String lang;
    @SerializedName("gender")
    private String gender;

    public UserAccountUpdate(String userid, String lang, String gender) {
        this.userid = userid;
        this.lang = lang;
        this.gender = gender;
    }

    // Getters

    public String getUserid() {
        return userid;
    }

    public String getLang() {
        return lang;
    }

    public String getGender() {
        return gender;
    }

    // Setters
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
