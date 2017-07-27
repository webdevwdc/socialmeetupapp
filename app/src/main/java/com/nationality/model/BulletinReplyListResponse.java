package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 26/4/17.
 */

public class BulletinReplyListResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("data")
    @Expose
    private BulletinReplyListData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public BulletinReplyListData getData() { return data; }

    public void setData(BulletinReplyListData data) {
        this.data = data;
    }
}
