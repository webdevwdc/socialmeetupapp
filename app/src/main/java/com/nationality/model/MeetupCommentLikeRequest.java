
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupCommentLikeRequest {

    @SerializedName("result")
    @Expose
    private MeetupCommentLikeResult result;

    public MeetupCommentLikeResult getResult() {
        return result;
    }

    public void setResult(MeetupCommentLikeResult result) {
        this.result = result;
    }

}
