package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 27/4/17.
 */

public class SearchUserConnectionResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<SearchUserConnectionDetails> data = null;
    /*@SerializedName("message")
    @Expose
    private String message;*/

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<SearchUserConnectionDetails> getData() {
        return data;
    }

    public void setData(List<SearchUserConnectionDetails> data) {
        this.data = data;
    }
    /*public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/
}
