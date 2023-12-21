package com.swe.buddybud.home;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class FeedData implements Serializable {
    private int id;
    private int profileImageId;
    private String nickname;
    private int userId;
    private String date;
    private String title;
    private String content;
    private int thumbsUpNumber;
    private int commentNumber;
    private String isThumbsUpClicked;
    private String isCommentWritten;
    private boolean isTranslated;
    private List<Uri> imageUris;
    private String initialIsThumbsUpClicked;
    private int initialThumbsUpNumber;
    public FeedData(int id, int profileImageId, String nickname, int userId, String date, String title, String content,
                    int thumbsUpNumber, int commentNumber, String isThumbsUpClicked, String isCommentWritten, List<Uri> imageUris) {
        this.id = id;
        this.profileImageId = profileImageId;
        this.nickname = nickname;
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.content = content;
        this.thumbsUpNumber = thumbsUpNumber;
        this.commentNumber = commentNumber;
        this.isThumbsUpClicked = isThumbsUpClicked;
        this.isCommentWritten = isCommentWritten;
        this.imageUris = imageUris;
        this.initialIsThumbsUpClicked = isThumbsUpClicked;
        this.initialThumbsUpNumber = thumbsUpNumber;
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

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
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

    public String isCommentWritten() {
        return isCommentWritten;
    }

    public void setCommentWritten(String commentWritten) {
        isCommentWritten = commentWritten;
    }

    public List<Uri> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // 아래 코드는 좋아요 / 댓글 관련 코드
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
}