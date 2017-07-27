
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupCommentLikeResult {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private MeetupCommentLikeData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public MeetupCommentLikeData getData() {
        return data;
    }

    public void setData(MeetupCommentLikeData data) {
        this.data = data;
    }

}
