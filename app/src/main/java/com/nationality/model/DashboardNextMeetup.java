
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardNextMeetup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("meetup_lat")
    @Expose
    private String meetupLat;
    @SerializedName("meetup_long")
    @Expose
    private String meetupLong;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("meetup_creator")
    @Expose
    private String meetupCreator;
    @SerializedName("meetup_creator_pic")
    @Expose
    private String meetupCreatorPic;
    @SerializedName("people_add")
    @Expose
    private Integer peopleAdd;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeetupLat() {
        return meetupLat;
    }

    public void setMeetupLat(String meetupLat) {
        this.meetupLat = meetupLat;
    }

    public String getMeetupLong() {
        return meetupLong;
    }

    public void setMeetupLong(String meetupLong) {
        this.meetupLong = meetupLong;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMeetupCreator() {
        return meetupCreator;
    }

    public void setMeetupCreator(String meetupCreator) {
        this.meetupCreator = meetupCreator;
    }

    public String getMeetupCreatorPic() {
        return meetupCreatorPic;
    }

    public void setMeetupCreatorPic(String meetupCreatorPic) {
        this.meetupCreatorPic = meetupCreatorPic;
    }

    public Integer getPeopleAdd() {
        return peopleAdd;
    }

    public void setPeopleAdd(Integer peopleAdd) {
        this.peopleAdd = peopleAdd;
    }

}
