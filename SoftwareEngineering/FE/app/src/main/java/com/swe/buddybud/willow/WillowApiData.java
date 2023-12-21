package com.swe.buddybud.willow;

import com.google.gson.annotations.SerializedName;

public class WillowApiData {

    @SerializedName("sender_no")
    private Integer sender_no;

    @SerializedName("receiver_no")
    private Integer receiver_no;

    @SerializedName("chat_room_no")
    private Integer chat_room_no;

    @SerializedName("user_no")
    private Integer user_no;

    @SerializedName("content")
    private String content;

    @SerializedName("uid")
    private String uid;

    @SerializedName("receiver_id")
    private String receiver_id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("sender_gender")
    private String sender_gender;

    @SerializedName("sender_school")
    private String sender_school;

    @SerializedName("sender_id")
    private String sender_id;

    @SerializedName("latest_message")
    private String latest_message;

    @SerializedName("sendWillowRequestResult")
    private boolean sendWillowRequestResult;

    @SerializedName("deleteWillowResult")
    private boolean deleteWillowResult;

    @SerializedName("acceptWillowResult")
    private boolean acceptWillowResult;

    @SerializedName("sendChatResult")
    private boolean sendChatResult;

    public Integer getSender_no() {
        return sender_no;
    }

    public String getSender_id() {
        return sender_id;
    }

    public Integer getChat_room_no() {
        return chat_room_no;
    }

    public Integer getUser_no() {
        return user_no;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public Integer getReceiver_no() {
        return receiver_no;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSender_gender() {
        return sender_gender;
    }

    public String getSender_school() {
        return sender_school;
    }

    public String getLatest_message() {
        return latest_message;
    }

    public boolean getSendWillowRequestResult() {
        return sendWillowRequestResult;
    }

    public boolean getDeleteWillowResult() {
        return deleteWillowResult;
    }

    public boolean getAcceptWillowResult() {
        return acceptWillowResult;
    }

    public boolean getSendChatResult() {
        return sendChatResult;
    }
}
