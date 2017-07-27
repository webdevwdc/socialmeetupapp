
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMeetupRequest {

    @SerializedName("result")
    @Expose
    private AddMeetupResult result;

    public AddMeetupResult getResult() {
        return result;
    }

    public void setResult(AddMeetupResult result) {
        this.result = result;
    }

}
