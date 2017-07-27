
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupDetailsRequest {

    @SerializedName("result")
    @Expose
    private MeetupDetailsResult result;

    public MeetupDetailsResult getResult() {
        return result;
    }

    public void setResult(MeetupDetailsResult result) {
        this.result = result;
    }

}
