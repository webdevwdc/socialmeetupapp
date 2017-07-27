package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class RecentMessageListData{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("to_userid")
    @Expose
    private String toUserid;
    @SerializedName("from_userid")
    @Expose
    private String fromUserid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("to_user_badge")
    @Expose
    private String toUserBadge;
    @SerializedName("from_user_badge")
    @Expose
    private String fromUserBadge;
    @SerializedName("chat_type")
    @Expose
    private String chatType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("facebookId")
    @Expose
    private String facebookId;
    @SerializedName("chat_token")
    @Expose
    private String chatToken;

    @SerializedName("chat_date_time")
    @Expose
    private String chat_date_time;

    @SerializedName("badge_count")
    @Expose
    private int badgeCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToUserid() {
        return toUserid;
    }

    public void setToUserid(String toUserid) {
        this.toUserid = toUserid;
    }

    public String getFromUserid() {
        return fromUserid;
    }

    public void setFromUserid(String fromUserid) {
        this.fromUserid = fromUserid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToUserBadge() {
        return toUserBadge;
    }

    public void setToUserBadge(String toUserBadge) {
        this.toUserBadge = toUserBadge;
    }

    public String getFromUserBadge() {
        return fromUserBadge;
    }

    public void setFromUserBadge(String fromUserBadge) {
        this.fromUserBadge = fromUserBadge;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public String getChat_date_time() {
        return chat_date_time;
    }

    public void setChat_date_time(String chat_date_time) {
        this.chat_date_time = chat_date_time;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

}
