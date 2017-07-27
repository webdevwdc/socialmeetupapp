
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BadgeRemoveRequest {

    @SerializedName("result")
    @Expose
    private BadgeRemoveResult result;

    public BadgeRemoveResult getResult() {
        return result;
    }

    public void setResult(BadgeRemoveResult result) {
        this.result = result;
    }

}
