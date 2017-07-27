
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendAcceptRejectRequest {

    @SerializedName("result")
    @Expose
    private FriendAcceptRejectResult result;

    public FriendAcceptRejectResult getResult() {
        return result;
    }

    public void setResult(FriendAcceptRejectResult result) {
        this.result = result;
    }

}
