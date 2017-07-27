
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupCommentDtlsRequest {

    @SerializedName("result")
    @Expose
    private MeetupCommentDtlsResult result;

    public MeetupCommentDtlsResult getResult() {
        return result;
    }

    public void setResult(MeetupCommentDtlsResult result) {
        this.result = result;
    }

}
