
package com.nationality.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetupDetailsData {
    //is_join

    @SerializedName("is_join")
    @Expose
    private String is_join;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date_time")
    @Expose
    private String dateTime;


    @SerializedName("place")
    @Expose
    private String place;
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
    @SerializedName("invited_user_count")
    @Expose
    private Integer invitedUserCount;
    @SerializedName("total_view")
    @Expose
    private Integer totalView;
    @SerializedName("total_like")
    @Expose
    private Integer totalLike;
    @SerializedName("total_reply")
    @Expose
    private Integer totalReply;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("user_location")
    @Expose
    private String userLocation;
    @SerializedName("user_city")
    @Expose
    private String userCity;
    @SerializedName("is_creator")
    @Expose
    private String isCreator;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("attendees_list")
    @Expose
    private List<MeetupDetailsAttendeesList> attendeesList = null;
    @SerializedName("respond")
    @Expose
    private List<MeetupDetailsRespond> meetupRespond = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public Integer getInvitedUserCount() {
        return invitedUserCount;
    }

    public void setInvitedUserCount(Integer invitedUserCount) {
        this.invitedUserCount = invitedUserCount;
    }

    public Integer getTotalView() {
        return totalView;
    }

    public void setTotalView(Integer totalView) {
        this.totalView = totalView;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public Integer getTotalReply() {
        return totalReply;
    }

    public void setTotalReply(Integer totalReply) {
        this.totalReply = totalReply;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(String isCreator) {
        this.isCreator = isCreator;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public List<MeetupDetailsAttendeesList> getAttendeesList() {
        return attendeesList;
    }

    public void setAttendeesList(List<MeetupDetailsAttendeesList> attendeesList) {
        this.attendeesList = attendeesList;
    }

    public List<MeetupDetailsRespond> getMeetupRespond() {
        return meetupRespond;
    }

    public void setMeetupRespond(List<MeetupDetailsRespond> meetupRespond) {
        this.meetupRespond = meetupRespond;
    }

    public String getIs_join() {
        return is_join;
    }

    public void setIs_join(String is_join) {
        this.is_join = is_join;
    }
}
