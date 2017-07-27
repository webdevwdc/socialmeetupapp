package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/5/17.
 */

public class GetChatTokenRequest {

    @SerializedName("result")
    @Expose
    private GetChatTokenResponse result;

    public GetChatTokenResponse getResult() {
        return result;
    }

    public void setResult(GetChatTokenResponse result) {
        this.result = result;
    }
}
