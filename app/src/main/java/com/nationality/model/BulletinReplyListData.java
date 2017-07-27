package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 26/4/17.
 */

public class BulletinReplyListData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("bulletin_id")
    @Expose
    private String bulletinId;
    @SerializedName("parent_bulletin_id")
    @Expose
    private String parentBulletinId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("respond_like")
    @Expose
    private Integer respondLike;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("respond_child")
    @Expose
    private List<BulletinReplyListReplyChild> respondChild = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getRespondLike() {
        return respondLike;
    }

    public void setRespondLike(Integer respondLike) {
        this.respondLike = respondLike;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public List<BulletinReplyListReplyChild> getRespondChild() {
        return respondChild;
    }

    public void setRespondChild(List<BulletinReplyListReplyChild> respondChild) {
        this.respondChild = respondChild;
    }
}
