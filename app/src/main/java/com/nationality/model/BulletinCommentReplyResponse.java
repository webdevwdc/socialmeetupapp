package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class BulletinCommentReplyResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private BulletinCommentReplyData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public BulletinCommentReplyData getData() {
        return data;
    }

    public void setData(BulletinCommentReplyData data) {
        this.data = data;
    }
}
