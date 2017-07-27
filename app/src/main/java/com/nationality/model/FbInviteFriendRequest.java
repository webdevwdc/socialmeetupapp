package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class FbInviteFriendRequest {
    @SerializedName("result")
    @Expose
    private FbInviteFriendResponse result;

    public FbInviteFriendResponse getResult() {
        return result;
    }

    public void setResult(FbInviteFriendResponse result) {
        this.result = result;
    }
}
