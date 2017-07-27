
package com.nationality.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionListResult {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("friend_request_number")
    @Expose
    private String friend_request_number;

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<ConnectionListData> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ConnectionListData> getData() {
        return data;
    }

    public void setData(List<ConnectionListData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFriend_request_number() {
        return friend_request_number;
    }

    public void setFriend_request_number(String friend_request_number) {
        this.friend_request_number = friend_request_number;
    }
}
