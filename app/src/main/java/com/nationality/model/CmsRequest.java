package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/5/17.
 */

public class CmsRequest {


    @SerializedName("result")
    @Expose
    private CmsResponse result;

    public CmsResponse getResult() {
        return result;
    }

    public void setResult(CmsResponse result) {
        this.result = result;
    }

}
