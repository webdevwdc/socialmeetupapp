package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 27/4/17.
 */

public class SearchUserConnectionRequest {
    @SerializedName("result")
    @Expose
    private SearchUserConnectionResponse result;

    public SearchUserConnectionResponse getResult() {
        return result;
    }

    public void setResult(SearchUserConnectionResponse result) {
        this.result = result;
    }
}
