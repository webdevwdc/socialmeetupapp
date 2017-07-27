
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupLikeRequest {

    @SerializedName("result")
    @Expose
    private MeetupLikeResult result;

    public MeetupLikeResult getResult() {
        return result;
    }

    public void setResult(MeetupLikeResult result) {
        this.result = result;
    }

}
