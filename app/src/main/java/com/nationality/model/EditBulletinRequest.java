package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 19/4/17.
 */

public class EditBulletinRequest {

    @SerializedName("result")
    @Expose
    private EditBulletinResponse result;

    public EditBulletinResponse getResult() {
        return result;
    }

    public void setResult(EditBulletinResponse result) {
        this.result = result;
    }


}
