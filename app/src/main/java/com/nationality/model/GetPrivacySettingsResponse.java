package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/5/17.
 */

public class GetPrivacySettingsResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private GetPrivacySettingsdata data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public GetPrivacySettingsdata getData() {
        return data;
    }

    public void setData(GetPrivacySettingsdata data) {
        this.data = data;
    }

}
