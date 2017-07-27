
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupCommentDtlsResult {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private MeetupCommentDtlsData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public MeetupCommentDtlsData getData() {
        return data;
    }

    public void setData(MeetupCommentDtlsData data) {
        this.data = data;
    }

}
