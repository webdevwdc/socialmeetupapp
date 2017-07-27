package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinLikeRequest {

    @SerializedName("result")
    @Expose
    private BulletinLikeResponse result;

    public BulletinLikeResponse getResult() {
        return result;
    }

    public void setResult(BulletinLikeResponse result) {
        this.result = result;
    }

}
