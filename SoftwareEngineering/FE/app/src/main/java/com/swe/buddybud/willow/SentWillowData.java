package com.swe.buddybud.willow;

import androidx.annotation.NonNull;

public class SentWillowData {
    SentWillowData(String userId, int userNo, int imgResId){
        this.userId = userId;
        this.userNo = userNo;
        this.imgResId = imgResId;
    }
    private String userId;
    private int userNo;
    private int imgResId;

    public String getUserId() {
        return userId;
    }

    public int getImgResId() {
        return imgResId;
    }

    public int getUserNo() {
        return userNo;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("sent userid : %s", userId);
    }
}
