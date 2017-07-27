package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinCommentLikeRequest {

    @SerializedName("result")
    @Expose
    private BulletinCommentLikeResponse result;

    public BulletinCommentLikeResponse getResult() {
        return result;
    }

    public void setResult(BulletinCommentLikeResponse result) {
        this.result = result;
    }
}
