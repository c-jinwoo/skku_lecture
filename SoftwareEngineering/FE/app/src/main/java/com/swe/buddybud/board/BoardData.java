package com.swe.buddybud.board;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class BoardData implements Serializable {
    private int id;
    private int profileImageId;
    private String nickname;
    private String date;
    private String title;
    private String content;
    private int thumbsUpNumber;
    private int scrapNumber;
    private String isThumbsUpClicked;
    private String isScrapClicked;
    private boolean isTranslated;
    private String initialIsThumbsUpClicked;
    private int initialThumbsUpNumber;
    private String initialIsScrapClicked;
    private int initialScrapNumber;
    public BoardData(int id, int profileImageId, String nickname, String date, String title, String content,
                     String isThumbsUpClicked, String isScrapClicked, int thumbsUpNumber, int scrapNumber) {
        this.id = id;
        this.profileImageId = profileImageId;
        this.nickname = nickname;
        this.date = date;
        this.title = title;
        this.content = content;
        this.thumbsUpNumber = thumbsUpNumber;
        this.scrapNumber = scrapNumber;
        this.isThumbsUpClicked = isThumbsUpClicked;
        this.isScrapClicked = isScrapClicked;
        this.isTranslated = false;
        this.initialIsThumbsUpClicked = isThumbsUpClicked;
        this.initialThumbsUpNumber = thumbsUpNumber;
        this.initialIsScrapClicked = isScrapClicked;
        this.initialScrapNumber = scrapNumber;
    }
    public int getId() {
        return id;
    }

    public int getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getThumbsUpNumber() {
        return thumbsUpNumber;
    }

    public void setThumbsUpNumber(int thumbsUpNumber) {
        this.thumbsUpNumber = thumbsUpNumber;
    }

    public int getScrapNumber() {
        return scrapNumber;
    }

    public void setScrapNumber(int scrapNumber) {
        this.scrapNumber = scrapNumber;
    }

    public String isThumbsUpClicked() {
        return isThumbsUpClicked;
    }

    public void setThumbsUpClicked(String thumbsUpClicked) {
        isThumbsUpClicked = thumbsUpClicked;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public void setTranslated(boolean translated) {
        isTranslated = translated;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String isScrapClicked() {
        return isScrapClicked;
    }

    public void setScrapClicked(boolean isScrapClicked) {
        isScrapClicked = isScrapClicked;
    }

    // 아래 코드는 좋아요 / 스크랩 관련 코드
    // 좋아요 상태를 boolean으로 반환
    public boolean isLiked() {
        return "Y".equals(this.isThumbsUpClicked);
    }

    // 좋아요 상태를 설정
    public void setLiked(boolean isLiked) {
        this.isThumbsUpClicked = isLiked ? "Y" : "N";
    }

    // 좋아요 수 변경
    public void incrementLikeCount() {
        this.thumbsUpNumber++;
    }

    public void decrementLikeCount() {
        this.thumbsUpNumber--;
    }

    public String getInitialIsThumbsUpClicked() {
        return initialIsThumbsUpClicked;
    }

    public int getInitialThumbsUpNumber() {
        return initialThumbsUpNumber;
    }

    public String getIsThumbsUpClicked() {
        return isThumbsUpClicked;
    }

    public boolean isScrap() {
        return "Y".equals(this.isScrapClicked);
    }

    // 좋아요 상태를 설정
    public void setScrap(boolean isScrap) {
        this.isScrapClicked = isScrap ? "Y" : "N";
    }

    // 좋아요 수 변경
    public void incrementScrapCount() {
        this.scrapNumber++;
    }

    public void decrementScrapCount() {
        this.scrapNumber--;
    }

    public String getIsScrapClicked() {return isScrapClicked;}

    public String getInitialIsScrapClicked() {return initialIsScrapClicked;}

    public int getInitialScrapNumber() {return initialScrapNumber;}
}