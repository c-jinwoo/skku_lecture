package com.swe.buddybud.account;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountApiData {
    @SerializedName("posts")
    private List<PostData> posts;

    @SerializedName("scraps")
    private List<ScrapData> scraps;

    public List<PostData> getPosts() {
        return posts;
    }

    public List<ScrapData> getScraps() {
        return scraps;
    }
}


