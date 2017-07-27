
package com.nationality.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyMetupListingResult {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<MyMeetupListingDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<MyMeetupListingDatum> getData() {
        return data;
    }

    public void setData(List<MyMeetupListingDatum> data) {
        this.data = data;
    }

}
