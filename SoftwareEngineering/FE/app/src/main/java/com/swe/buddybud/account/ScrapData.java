package com.swe.buddybud.account;

import com.google.gson.annotations.SerializedName;

public class ScrapData {

    @SerializedName("scrap_yn")
    private String scrapYn;

    @SerializedName("like_num")
    private String likeNum;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("title")
    private String title;

    @SerializedName("like_yn")
    private String likeYn;

    @SerializedName("scrap_num")
    private String scrapNum;

    @SerializedName("post_no")
    private String postNo;

    @SerializedName("content")
    private String content;

    @SerializedName("last_modified_at")
    private String lastModifiedAt;

    @SerializedName("post_user_no")
    private String post_user_no;

    @SerializedName("post_user_id")
    private String post_user_id;

    // Constructor
    public ScrapData(String scrapYn, String likeNum, String createdAt, String title,
                     String likeYn, String scrapNum, String postNo, String content, String lastModifiedAt) {
        this.scrapYn = scrapYn;
        this.likeNum = likeNum;
        this.createdAt = createdAt;
        this.title = title;
        this.likeYn = likeYn;
        this.scrapNum = scrapNum;
        this.postNo = postNo;
        this.content = content;
        this.lastModifiedAt = lastModifiedAt;
    }

    // Getters
    public String getScrapYn() { return scrapYn; }
    public String getLikeNum() { return likeNum; }
    public String getCreatedAt() { return createdAt; }
    public String getTitle() { return title; }
    public String getLikeYn() { return likeYn; }
    public String getScrapNum() { return scrapNum; }
    public String getPostNo() { return postNo; }
    public String getContent() { return content; }
    public String getLastModifiedAt() { return lastModifiedAt; }
    public String getUserId() { return post_user_id; }

    // Setters
    public void setScrapYn(String scrapYn) { this.scrapYn = scrapYn; }
    public void setLikeNum(String likeNum) { this.likeNum = likeNum; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setTitle(String title) { this.title = title; }
    public void setLikeYn(String likeYn) { this.likeYn = likeYn; }
    public void setScrapNum(String scrapNum) { this.scrapNum = scrapNum; }
    public void setPostNo(String postNo) { this.postNo = postNo; }
    public void setContent(String content) { this.content = content; }
    public void setLastModifiedAt(String lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }

    // toString() method for logging purposes
    @Override
    public String toString() {
        return "ScrapData{" +
                "scrapYn='" + scrapYn + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", title='" + title + '\'' +
                ", likeYn='" + likeYn + '\'' +
                ", scrapNum='" + scrapNum + '\'' +
                ", postNo='" + postNo + '\'' +
                ", content='" + content + '\'' +
                ", lastModifiedAt='" + lastModifiedAt + '\'' +
                '}';
    }
}
