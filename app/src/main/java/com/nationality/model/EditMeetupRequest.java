
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditMeetupRequest {

    @SerializedName("result")
    @Expose
    private EditMeetupResult result;

    public EditMeetupResult getResult() {
        return result;
    }

    public void setResult(EditMeetupResult result) {
        this.result = result;
    }

}
