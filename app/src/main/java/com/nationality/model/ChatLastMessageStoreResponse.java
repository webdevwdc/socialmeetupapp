package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class ChatLastMessageStoreResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private ChatLastMessageStoreData data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    /*public ChatLastMessageStoreData getData() {
        return data;
    }

    public void setData(ChatLastMessageStoreData data) {
        this.data = data;
    }*/

}
