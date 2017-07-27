
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterRequestData {

    @SerializedName("connection_request")
    @Expose
    private Integer connectionRequest;
    @SerializedName("meetup_request")
    @Expose
    private Integer meetupRequest;
    @SerializedName("bulletin_request")
    @Expose
    private Integer bulletinRequest;
    @SerializedName("message_request")
    @Expose
    private Integer messageRequest;

    public Integer getConnectionRequest() {
        return connectionRequest;
    }

    public void setConnectionRequest(Integer connectionRequest) {
        this.connectionRequest = connectionRequest;
    }

    public Integer getMeetupRequest() {
        return meetupRequest;
    }

    public void setMeetupRequest(Integer meetupRequest) {
        this.meetupRequest = meetupRequest;
    }

    public Integer getBulletinRequest() {
        return bulletinRequest;
    }

    public void setBulletinRequest(Integer bulletinRequest) {
        this.bulletinRequest = bulletinRequest;
    }

    public Integer getMessageRequest() {
        return messageRequest;
    }

    public void setMessageRequest(Integer messageRequest) {
        this.messageRequest = messageRequest;
    }

}
