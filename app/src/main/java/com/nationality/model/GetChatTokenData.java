package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/5/17.
 */

public class GetChatTokenData {

    @SerializedName("senderfbid")
    @Expose
    private String senderfbid;
    @SerializedName("recieverfbid")
    @Expose
    private String recieverfbid;
    @SerializedName("unique_chatid")
    @Expose
    private String uniqueChatid;
    @SerializedName("chat_type")
    @Expose
    private String chatType;
    @SerializedName("is_message_deleted")
    @Expose
    private String isMessageDeleted;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_message_done")
    @Expose
    private String isMessageDone;

    public String getSenderfbid() {
        return senderfbid;
    }

    public void setSenderfbid(String senderfbid) {
        this.senderfbid = senderfbid;
    }

    public String getRecieverfbid() {
        return recieverfbid;
    }

    public void setRecieverfbid(String recieverfbid) {
        this.recieverfbid = recieverfbid;
    }

    public String getUniqueChatid() {
        return uniqueChatid;
    }

    public void setUniqueChatid(String uniqueChatid) {
        this.uniqueChatid = uniqueChatid;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getIsMessageDeleted() {
        return isMessageDeleted;
    }

    public void setIsMessageDeleted(String isMessageDeleted) {
        this.isMessageDeleted = isMessageDeleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsMessageDone() {
        return isMessageDone;
    }

    public void setIsMessageDone(String isMessageDone) {
        this.isMessageDone = isMessageDone;
    }

}
