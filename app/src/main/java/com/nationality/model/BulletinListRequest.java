
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulletinListRequest {

    @SerializedName("result")
    @Expose
    private BulletinListResponse result;

    public BulletinListResponse getResult() {
        return result;
    }

    public void setResult(BulletinListResponse result) {
        this.result = result;
    }

}
