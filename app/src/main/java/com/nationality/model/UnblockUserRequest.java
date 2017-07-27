package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class UnblockUserRequest {

    @SerializedName("result")
    @Expose
    private UnblockUserResponse result;

    public UnblockUserResponse getResult() {
        return result;
    }

    public void setResult(UnblockUserResponse result) {
        this.result = result;
    }
}
