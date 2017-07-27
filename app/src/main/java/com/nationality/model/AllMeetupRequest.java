
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllMeetupRequest {

    @SerializedName("result")
    @Expose
    private AllMeetupResult result;

    public AllMeetupResult getResult() {
        return result;
    }

    public void setResult(AllMeetupResult result) {
        this.result = result;
    }

}
