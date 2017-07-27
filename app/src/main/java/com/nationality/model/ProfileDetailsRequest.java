
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailsRequest {

    @SerializedName("result")
    @Expose
    private ProfileDetailsResult1 result;

    public ProfileDetailsResult1 getResult() {
        return result;
    }

    public void setResult(ProfileDetailsResult1 result) {
        this.result = result;
    }

}
