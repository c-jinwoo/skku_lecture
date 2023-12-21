package com.swe.buddybud.account;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {
    @SerializedName("updateUserInfoResult")
    private boolean updateUserInfoResult;

    // Getter and setter
    public boolean isUpdateResult() {
        return updateUserInfoResult;
    }

    public void setUpdateResult(boolean updateResult) {
        this.updateUserInfoResult = updateResult;
    }
}
