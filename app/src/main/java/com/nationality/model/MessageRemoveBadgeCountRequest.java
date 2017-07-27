
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageRemoveBadgeCountRequest {

    @SerializedName("result")
    @Expose
    private MessageRemoveBadgeCountResponse result;

    public MessageRemoveBadgeCountResponse getResult() {
        return result;
    }

    public void setResult(MessageRemoveBadgeCountResponse result) {
        this.result = result;
    }
}
