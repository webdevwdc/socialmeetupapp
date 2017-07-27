
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupDetailsRespond {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("meetup_id")
    @Expose
    private String meetupId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("parent_meetup_id")
    @Expose
    private String parentMeetupId;
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
    @SerializedName("respond_reply")
    @Expose
    private Integer respondReply;

    @SerializedName("is_like")
    @Expose
    private String is_like;

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

    public String getMeetupId() {
        return meetupId;
    }

    public void setMeetupId(String meetupId) {
        this.meetupId = meetupId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParentMeetupId() {
        return parentMeetupId;
    }

    public void setParentMeetupId(String parentMeetupId) {
        this.parentMeetupId = parentMeetupId;
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

    public Integer getRespondReply() {
        return respondReply;
    }

    public void setRespondReply(Integer respondReply) {
        this.respondReply = respondReply;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }
}
