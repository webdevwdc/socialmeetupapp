package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class RecentMessageListRequest {

    @SerializedName("result")
    @Expose
    private RecentMessageListResponse result;

    public RecentMessageListResponse getResult() {
        return result;
    }

    public void setResult(RecentMessageListResponse result) {
        this.result = result;
    }
}
