
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProfileRequest {

    @SerializedName("result")
    @Expose
    private EditProfileResult result;

    public EditProfileResult getResult() {
        return result;
    }

    public void setResult(EditProfileResult result) {
        this.result = result;
    }

}
