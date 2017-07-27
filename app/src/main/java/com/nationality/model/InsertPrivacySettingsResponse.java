package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class InsertPrivacySettingsResponse {


    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private InsertPrivacySettingsData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public InsertPrivacySettingsData getData() {
        return data;
    }

    public void setData(InsertPrivacySettingsData data) {
        this.data = data;
    }


}
