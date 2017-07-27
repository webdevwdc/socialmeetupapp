
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterCountRequest {

    @SerializedName("result")
    @Expose
    private FooterCountResult result;

    public FooterCountResult getResult() {
        return result;
    }

    public void setResult(FooterCountResult result) {
        this.result = result;
    }

}
