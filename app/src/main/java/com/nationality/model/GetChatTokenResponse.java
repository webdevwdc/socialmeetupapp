package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/5/17.
 */

public class GetChatTokenResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private GetChatTokenData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public GetChatTokenData getData() {
        return data;
    }

    public void setData(GetChatTokenData data) {
        this.data = data;
    }

}
