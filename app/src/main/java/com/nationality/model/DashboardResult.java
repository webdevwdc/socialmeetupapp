
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardResult {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private DashboardData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public DashboardData getData() {
        return data;
    }

    public void setData(DashboardData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
