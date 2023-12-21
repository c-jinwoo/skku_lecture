package com.swe.buddybud.home;

public class CommentData {
    private int commentId;
    private int profileImageId;
    private String nickname;
    private String date;
    private String content;
    private int thumbsUpNumber;
    private int thumbsDownNumber;
    private int replyToCommentId;
    private String replyToNickname;
    private String isThumbsUpClicked;
    private String isThumbsDownClicked;
    private boolean isTranslated;
    private String initialIsThumbsUpClicked;
    private int initialThumbsUpNumber;
    private String initialIsThumbsDownClicked;
    private int initialThumbsDownNumber;
    private boolean likeStatusChanged = false;
    private boolean hateStatusChanged = false;

    // Constructor
    public CommentData(int commentId, int profileImageId, String nickname, String date, String content, int thumbsUpNumber,
                       int thumbsDownNumber, int replyToCommentId, String replyToNickname, String isThumbsUpClicked, String isThumbsDownClicked) {
        this.commentId = commentId;
        this.profileImageId = profileImageId;
        this.nickname = nickname;
        this.date = date;
        this.content = content;
        this.thumbsUpNumber = thumbsUpNumber;
        this.thumbsDownNumber = thumbsDownNumber;
        this.replyToCommentId = replyToCommentId;
        this.replyToNickname = replyToNickname;
        this.isThumbsUpClicked = isThumbsUpClicked;
        this.isThumbsDownClicked = isThumbsDownClicked;
        this.isTranslated = false;
        this.initialIsThumbsUpClicked = isThumbsUpClicked;
        this.initialThumbsUpNumber = thumbsUpNumber;
        this.initialIsThumbsDownClicked = isThumbsDownClicked;
        this.initialThumbsDownNumber = thumbsDownNumber;
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

    public int getThumbsDownNumber() {
        return thumbsDownNumber;
    }

    public void setThumbsDownNumber(int thumbsDownNumber) {
        this.thumbsDownNumber = thumbsDownNumber;
    }

    public String getReplyToNickname() {
        return replyToNickname;
    }

    public void setReplyToNickname(String replyToNickname) {
        this.replyToNickname = replyToNickname;
    }

    public String isThumbsUpClicked() {
        return isThumbsUpClicked;
    }

    public void setThumbsUpClicked(String thumbsUpClicked) {
        isThumbsUpClicked = thumbsUpClicked;
    }

    public String isThumbsDownClicked() {
        return isThumbsDownClicked;
    }

    public void setThumbsDownClicked(String thumbsDownClicked) {
        isThumbsDownClicked = thumbsDownClicked;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public void setTranslated(boolean translated) {
        isTranslated = translated;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getReplyToCommentId() {
        return replyToCommentId;
    }

    public void setReplyToCommentId(int replyToCommentId) {
        this.replyToCommentId = replyToCommentId;
    }

    // 아래 코드는 좋아요 / 싫어요 관련 코드
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

    public void decrementLikeCount() { this.thumbsUpNumber--; }

    public String getInitialIsThumbsUpClicked() {
        return initialIsThumbsUpClicked;
    }

    public int getInitialThumbsUpNumber() {
        return initialThumbsUpNumber;
    }

    public String getIsThumbsUpClicked() {
        return isThumbsUpClicked;
    }

    public boolean isHated() {
        return "Y".equals(this.isThumbsDownClicked);
    }

    // 싫어요 상태를 설정
    public void setHated(boolean isHated) {
        this.isThumbsDownClicked = isHated ? "Y" : "N";
    }

    // 싫어요 수 변경
    public void incrementHateCount() {
        this.thumbsDownNumber++;
    }

    public void decrementHateCount() { this.thumbsDownNumber--; }

    public String getInitialIsThumbsDownClicked() {
        return initialIsThumbsDownClicked;
    }

    public int getInitialThumbsDownNumber() {
        return initialThumbsDownNumber;
    }

    public String getIsThumbsDownClicked() {
        return isThumbsDownClicked;
    }

    public void setLikeStatusChanged(boolean likeStatusChanged) {
        this.likeStatusChanged = likeStatusChanged;
    }

    public boolean isLikeStatusChanged() {
        return likeStatusChanged;
    }

    public void setHateStatusChanged(boolean hateStatusChanged) {
        this.hateStatusChanged = hateStatusChanged;
    }

    public boolean isHateStatusChanged() {
        return hateStatusChanged;
    }
}
