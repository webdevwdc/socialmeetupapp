
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupLikeResult {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private MeetupLikeData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public MeetupLikeData getData() {
        return data;
    }

    public void setData(MeetupLikeData data) {
        this.data = data;
    }

}
