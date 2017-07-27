
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendListRequest {

    @SerializedName("result")
    @Expose
    private FriendListResult result;

    public FriendListResult getResult() {
        return result;
    }

    public void setResult(FriendListResult result) {
        this.result = result;
    }

}
