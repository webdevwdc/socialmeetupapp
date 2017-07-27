package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinCommentReplyRequest {

    @SerializedName("result")
    @Expose
    private BulletinCommentReplyResponse result;

    public BulletinCommentReplyResponse getResult() {
        return result;
    }

    public void setResult(BulletinCommentReplyResponse result) {
        this.result = result;
    }


}
