package com.swe.buddybud.willow;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class ChatData {
    private LocalDateTime timestamp;
    private String message;
    private String name;
    private int imgResId;
    private Uri imageURI;

    public ChatData(LocalDateTime timestamp, String message, String name, int imgResId, Uri imageURI) {
        this.timestamp = timestamp;
        if(imageURI!=null) {
            this.message = "";
            this.imageURI = imageURI;
        }
        else {
            this.message = message.trim();
            this.imageURI = null;
        }
        this.name = name;
        this.imgResId = imgResId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public int getImgResId() {
        return imgResId;
    }
    public Uri getImageURI() {
        return imageURI;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ChatData){
            return this.getMessage() == ((ChatData) obj).getMessage() && this.getTimestamp() == ((ChatData) obj).getTimestamp();
        }
        else return false;

    }
}
