
package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteBulletinRequest {

    @SerializedName("result")
    @Expose
    private DeleteBulletinResult result;

    public DeleteBulletinResult getResult() {
        return result;
    }

    public void setResult(DeleteBulletinResult result) {
        this.result = result;
    }

}
