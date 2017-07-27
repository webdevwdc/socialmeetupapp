
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FbSignUpRequest {

    @SerializedName("result")
    @Expose
    private FbSignUpResult result;

    public FbSignUpResult getResult() {
        return result;
    }

    public void setResult(FbSignUpResult result) {
        this.result = result;
    }

}
