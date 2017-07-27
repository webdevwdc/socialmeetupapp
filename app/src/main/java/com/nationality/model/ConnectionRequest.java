
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionRequest {

    @SerializedName("result")
    @Expose
    private ConnectionListResult result;

    public ConnectionListResult getResult() {
        return result;
    }

    public void setResult(ConnectionListResult result) {
        this.result = result;
    }

}
