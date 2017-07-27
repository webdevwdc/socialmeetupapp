package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinCommentLikeData {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("bulletin_respond_id")
    @Expose
    private String bulletinRespondId;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBulletinRespondId() {
        return bulletinRespondId;
    }

    public void setBulletinRespondId(String bulletinRespondId) {
        this.bulletinRespondId = bulletinRespondId;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
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


}
