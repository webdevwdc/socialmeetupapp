package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class InsertPrivacySettingsRequest {

    @SerializedName("result")
    @Expose
    private InsertPrivacySettingsResponse result;

    public InsertPrivacySettingsResponse getResult() {
        return result;
    }

    public void setResult(InsertPrivacySettingsResponse result) {
        this.result = result;
    }

}
