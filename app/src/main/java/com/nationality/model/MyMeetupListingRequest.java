
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyMeetupListingRequest {

    @SerializedName("result")
    @Expose
    private MyMetupListingResult result;

    public MyMetupListingResult getResult() {
        return result;
    }

    public void setResult(MyMetupListingResult result) {
        this.result = result;
    }

}
