
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupAcceptRejectRequest {

    @SerializedName("result")
    @Expose
    private MeetupAcceptRejectResult result;

    public MeetupAcceptRejectResult getResult() {
        return result;
    }

    public void setResult(MeetupAcceptRejectResult result) {
        this.result = result;
    }

}
