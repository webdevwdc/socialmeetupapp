
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBulletinRequest {

    @SerializedName("result")
    @Expose
    private AddBulletinResponse result;

    public AddBulletinResponse getResult() {
        return result;
    }

    public void setResult(AddBulletinResponse result) {
        this.result = result;
    }

}
