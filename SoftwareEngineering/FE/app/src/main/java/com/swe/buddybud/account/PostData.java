package com.swe.buddybud.account;

import com.google.gson.annotations.SerializedName;

public class PostData {
    @SerializedName("like_num")
    private String likeNum;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("title")
    private String title;

    @SerializedName("like_yn")
    private String likeYn;

    @SerializedName("post_no")
    private String postNo;

    @SerializedName("content")
    private String content;

    @SerializedName("last_modified_at")
    private String lastModifiedAt;

    // Getters
    public String getLikeNum() {
        return likeNum;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getLikeYn() {
        return likeYn;
    }

    public String getPostNo() {
        return postNo;
    }

    public String getContent() {
        return content;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    // Setters
    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikeYn(String likeYn) {
        this.likeYn = likeYn;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    // Constructor
    public PostData(String likeNum, String createdAt, String title, String likeYn, String postNo, String content, String lastModifiedAt) {
        this.likeNum = likeNum;
        this.createdAt = createdAt;
        this.title = title;
        this.likeYn = likeYn;
        this.postNo = postNo;
        this.content = content;
        this.lastModifiedAt = lastModifiedAt;
    }

    // toString() for logging purposes
    @Override
    public String toString() {
        return "PostData{" +
                "likeNum='" + likeNum + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", title='" + title + '\'' +
                ", likeYn='" + likeYn + '\'' +
                ", postNo='" + postNo + '\'' +
                ", content='" + content + '\'' +
                ", lastModifiedAt='" + lastModifiedAt + '\'' +
                '}';
    }
}
