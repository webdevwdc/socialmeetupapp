package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class BlockUserListRequest {

    @SerializedName("result")
    @Expose
    private BlockUserListResponse result;

    public BlockUserListResponse getResult() {
        return result;
    }

    public void setResult(BlockUserListResponse result) {
        this.result = result;
    }
}
