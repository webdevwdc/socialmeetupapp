
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetupInviteUserRequest {

    @SerializedName("result")
    @Expose
    private MeetupInviteUserResult result;

    public MeetupInviteUserResult getResult() {
        return result;
    }

    public void setResult(MeetupInviteUserResult result) {
        this.result = result;
    }

}
