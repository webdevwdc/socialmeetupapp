
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteMeetupUserRequest {

    @SerializedName("result")
    @Expose
    private InviteMeetupUserResult result;

    public InviteMeetupUserResult getResult() {
        return result;
    }

    public void setResult(InviteMeetupUserResult result) {
        this.result = result;
    }

}
