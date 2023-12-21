package com.swe.buddybud.user;

import com.google.gson.annotations.SerializedName;

public class UserApiData {

    @SerializedName("isUserLoginSucceed")
    private boolean isUserLoginSucceed;

    @SerializedName("loginUserNo")
    private Integer loginUserNo;

    @SerializedName("loginUserId")
    private String loginUserId;

    @SerializedName("loginUserLang")
    private String loginUserLang;

    @SerializedName("loginUserGender")
    private String loginUserGender;

    @SerializedName("isUserEmailDupl")
    private boolean isUserEmailDupl;

    @SerializedName("isUserIdDupl")
    private boolean isUserIdDupl;

    @SerializedName("inserUserInfoResult")
    private boolean inserUserInfoResult;

    @SerializedName("updateUserInfoResult")
    private boolean updateUserInfoResult;

    public boolean getIsUserLoginSucceed() { return isUserLoginSucceed; }

    public Integer getLoginUserNo() { return loginUserNo; }

    public String getLoginUserId() { return loginUserId; }

    public String getLoginUserLang() { return loginUserLang; }

    public String getLoginUserGender() { return loginUserGender; }

    public boolean getIsUserEmailDupl(){ return isUserEmailDupl; }

    public boolean getIsUserIdDupl(){
        return isUserIdDupl;
    }

    public boolean getInsertUserInfoResult(){
        return inserUserInfoResult;
    }

    public boolean getUpdateUserInfoResult(){
        return updateUserInfoResult;
    }
}
