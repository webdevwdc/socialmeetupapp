
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageRequest {

    @SerializedName("result")
    @Expose
    private LanguageResponse result;

    public LanguageResponse getResult() {
        return result;
    }

    public void setResult(LanguageResponse result) {
        this.result = result;
    }

}
