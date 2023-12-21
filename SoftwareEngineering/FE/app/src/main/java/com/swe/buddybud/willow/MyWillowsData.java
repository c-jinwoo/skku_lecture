package com.swe.buddybud.willow;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class MyWillowsData {
    public MyWillowsData(String userId, int userNo, LocalDateTime lastMsgTime, String lastMsg, int imgResId) {
        this.userId = userId;
        this.userNo = userNo;
        this.lastMsgTime = lastMsgTime;
        this.lastMsg = lastMsg;
        this.imgResId = imgResId;
    }

    private String userId;
    private int userNo;
    private LocalDateTime lastMsgTime;
    private String lastMsg;
    private int imgResId;

    public String getUserId() {
        return userId;
    }

    public int getUserNo() {
        return userNo;
    }

    public LocalDateTime getLastMsgTime() {
        return lastMsgTime;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getImgResId() {
        return imgResId;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("userid : %s lastmsg: %s", userId, lastMsg);
    }
}
