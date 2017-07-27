package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class BlockUserRequest {

    @SerializedName("result")
    @Expose
    private BlockUserResponse result;

    public BlockUserResponse getResult() {
        return result;
    }

    public void setResult(BlockUserResponse result) {
        this.result = result;
    }


}

