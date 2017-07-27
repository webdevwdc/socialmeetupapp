package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinCommentReplyData {

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("bulletin_id")
    @Expose
    private String bulletinId;
    @SerializedName("parent_bulletin_id")
    @Expose
    private String parentBulletinId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("respond_like")
    @Expose
    private Integer respondLike;
    @SerializedName("respond_reply")
    @Expose
    private Integer respondReply;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }

    public String getParentBulletinId() {
        return parentBulletinId;
    }

    public void setParentBulletinId(String parentBulletinId) {
        this.parentBulletinId = parentBulletinId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public Integer getRespondLike() {
        return respondLike;
    }

    public void setRespondLike(Integer respondLike) {
        this.respondLike = respondLike;
    }

    public Integer getRespondReply() {
        return respondReply;
    }

    public void setRespondReply(Integer respondReply) {
        this.respondReply = respondReply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
