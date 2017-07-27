package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 4/5/17.
 */

public class RecentMessageListResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<RecentMessageListData> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<RecentMessageListData> getData() {
        return data;
    }

    public void setData(List<RecentMessageListData> data) {
        this.data = data;
    }
}
