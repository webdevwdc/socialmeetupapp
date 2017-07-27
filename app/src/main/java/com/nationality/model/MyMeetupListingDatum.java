
package com.nationality.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyMeetupListingDatum {

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
    @SerializedName("meetup_creator")
    @Expose
    private String meetupCreator;
    @SerializedName("meetup_creator_pic")
    @Expose
    private String meetupCreatorPic;
    @SerializedName("total_people_attendee")
    @Expose
    private Integer totalPeopleAttendee;
    @SerializedName("total_like")
    @Expose
    private Integer totalLike;
    @SerializedName("is_creator")
    @Expose
    private String isCreator;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("attendee")
    @Expose
    private List<MyMetupListingAttendee> attendee = null;
    @SerializedName("other_attendee_number")
    @Expose
    private Integer otherAttendeeNumber;

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

    public Integer getTotalPeopleAttendee() {
        return totalPeopleAttendee;
    }

    public void setTotalPeopleAttendee(Integer totalPeopleAttendee) {
        this.totalPeopleAttendee = totalPeopleAttendee;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
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

    public List<MyMetupListingAttendee> getAttendee() {
        return attendee;
    }

    public void setAttendee(List<MyMetupListingAttendee> attendee) {
        this.attendee = attendee;
    }

    public Integer getOtherAttendeeNumber() {
        return otherAttendeeNumber;
    }

    public void setOtherAttendeeNumber(Integer otherAttendeeNumber) {
        this.otherAttendeeNumber = otherAttendeeNumber;
    }

}
