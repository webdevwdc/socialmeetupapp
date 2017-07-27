package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class ChatLastMessageStoreRequest {

    @SerializedName("result")
    @Expose
    private ChatLastMessageStoreResponse result;

    public ChatLastMessageStoreResponse getResult() {
        return result;
    }

    public void setResult(ChatLastMessageStoreResponse result) {
        this.result = result;
    }
}
