
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteMeetupRequest {

    @SerializedName("result")
    @Expose
    private DeleteMeetupResult result;

    public DeleteMeetupResult getResult() {
        return result;
    }

    public void setResult(DeleteMeetupResult result) {
        this.result = result;
    }

}
