
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NationalityRequest {

    @SerializedName("result")
    @Expose
    private NationalityResult result;

    public NationalityResult getResult() {
        return result;
    }

    public void setResult(NationalityResult result) {
        this.result = result;
    }

}
