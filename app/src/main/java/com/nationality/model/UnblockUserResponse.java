package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class UnblockUserResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private UnblockUserData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UnblockUserData getData() {
        return data;
    }

    public void setData(UnblockUserData data) {
        this.data = data;
    }

}
