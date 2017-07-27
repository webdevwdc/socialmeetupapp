
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewMeetupRequestList {

    @SerializedName("result")
    @Expose
    private NewMeetupRequestResult result;

    public NewMeetupRequestResult getResult() {
        return result;
    }

    public void setResult(NewMeetupRequestResult result) {
        this.result = result;
    }

}
