
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailsResult1 {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private ProfileDetailsData1 data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ProfileDetailsData1 getData() {
        return data;
    }

    public void setData(ProfileDetailsData1 data) {
        this.data = data;
    }

}
