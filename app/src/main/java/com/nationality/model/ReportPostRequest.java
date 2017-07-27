package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 26/4/17.
 */

public class ReportPostRequest {

    @SerializedName("result")
    @Expose
    private ReportPostResponse result;

    public ReportPostResponse getResult() {
        return result;
    }

    public void setResult(ReportPostResponse result) {
        this.result = result;
    }


}
