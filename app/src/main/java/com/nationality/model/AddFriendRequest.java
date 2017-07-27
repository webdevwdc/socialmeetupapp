package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 27/4/17.
 */

public class AddFriendRequest {
    @SerializedName("result")
    @Expose
    private AddFriendResponse result;

    public AddFriendResponse getResult() {
        return result;
    }

    public void setResult(AddFriendResponse result) {
        this.result = result;
    }
}
