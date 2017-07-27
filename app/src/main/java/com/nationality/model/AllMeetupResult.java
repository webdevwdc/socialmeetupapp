
package com.nationality.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllMeetupResult {

    //meetup_request

    @SerializedName("meetup_request")
    @Expose
    private String meetup_request;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<AllMeetupData> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<AllMeetupData> getData() {
        return data;
    }

    public void setData(List<AllMeetupData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeetup_request() {
        return meetup_request;
    }

    public void setMeetup_request(String meetup_request) {
        this.meetup_request = meetup_request;
    }
}
