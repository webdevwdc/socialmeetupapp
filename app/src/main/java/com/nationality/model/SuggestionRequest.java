
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestionRequest {

    @SerializedName("result")
    @Expose
    private SuggestionResult result;

    public SuggestionResult getResult() {
        return result;
    }

    public void setResult(SuggestionResult result) {
        this.result = result;
    }

}
