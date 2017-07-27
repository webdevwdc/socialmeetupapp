
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddUserRequest {

    @SerializedName("result")
    @Expose
    private AddUserResult result;

    public AddUserResult getResult() {
        return result;
    }

    public void setResult(AddUserResult result) {
        this.result = result;
    }

}
