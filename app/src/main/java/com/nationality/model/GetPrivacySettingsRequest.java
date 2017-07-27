package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/5/17.
 */

public class GetPrivacySettingsRequest {

    @SerializedName("result")
    @Expose
    private GetPrivacySettingsResponse result;

    public GetPrivacySettingsResponse getResult() {
        return result;
    }

    public void setResult(GetPrivacySettingsResponse result) {
        this.result = result;
    }
}
