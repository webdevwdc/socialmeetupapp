
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulletinMyListRequest {

    @SerializedName("result")
    @Expose
    private BulletinMyListResponse result;

    public BulletinMyListResponse getResult() {
        return result;
    }

    public void setResult(BulletinMyListResponse result) {
        this.result = result;
    }

}
