
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulletinDetailsRequest {

    @SerializedName("result")
    @Expose
    private BulletinDetailsResponse result;

    public BulletinDetailsResponse getResult() {
        return result;
    }

    public void setResult(BulletinDetailsResponse result) {
        this.result = result;
    }

}
