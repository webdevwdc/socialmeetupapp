
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinMeetupRequest {

    @SerializedName("result")
    @Expose
    private JoinMeetupResponse result;

    public JoinMeetupResponse getResult() {
        return result;
    }

    public void setResult(JoinMeetupResponse result) {
        this.result = result;
    }

}
