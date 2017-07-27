package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class InsertPrivacySettingsData {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("is_anyone_find_me")
    @Expose
    private String isAnyoneFindMe;
    @SerializedName("is_anyone_see_my_profile")
    @Expose
    private String isAnyoneSeeMyProfile;
    @SerializedName("is_everyone_see_my_bulletin")
    @Expose
    private String isEveryoneSeeMyBulletin;
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

    public String getIsAnyoneFindMe() {
        return isAnyoneFindMe;
    }

    public void setIsAnyoneFindMe(String isAnyoneFindMe) {
        this.isAnyoneFindMe = isAnyoneFindMe;
    }

    public String getIsAnyoneSeeMyProfile() {
        return isAnyoneSeeMyProfile;
    }

    public void setIsAnyoneSeeMyProfile(String isAnyoneSeeMyProfile) {
        this.isAnyoneSeeMyProfile = isAnyoneSeeMyProfile;
    }

    public String getIsEveryoneSeeMyBulletin() {
        return isEveryoneSeeMyBulletin;
    }

    public void setIsEveryoneSeeMyBulletin(String isEveryoneSeeMyBulletin) {
        this.isEveryoneSeeMyBulletin = isEveryoneSeeMyBulletin;
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
