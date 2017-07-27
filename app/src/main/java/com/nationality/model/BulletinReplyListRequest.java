package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 26/4/17.
 */

public class BulletinReplyListRequest {

    @SerializedName("result")
    @Expose
    private BulletinReplyListResponse result;

    public BulletinReplyListResponse getResult() {
        return result;
    }

    public void setResult(BulletinReplyListResponse result) {
        this.result = result;
    }
}
