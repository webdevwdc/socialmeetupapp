package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 17/5/17.
 */

public class UpdateLocationRequest {
    @SerializedName("result")
    @Expose
    private UpdateLocationResponse result;

    public UpdateLocationResponse getResult() {
        return result;
    }

    public void setResult(UpdateLocationResponse result) {
        this.result = result;
    }
}
