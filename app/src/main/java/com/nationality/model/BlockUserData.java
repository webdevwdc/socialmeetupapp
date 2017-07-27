package com.nationality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/17.
 */

public class BlockUserData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("to_userid")
    @Expose
    private String toUserid;
    @SerializedName("from_userid")
    @Expose
    private String fromUserid;
    @SerializedName("is_accept")
    @Expose
    private String isAccept;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("accept_date_time")
    @Expose
    private String acceptDateTime;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToUserid() {
        return toUserid;
    }

    public void setToUserid(String toUserid) {
        this.toUserid = toUserid;
    }

    public String getFromUserid() {
        return fromUserid;
    }

    public void setFromUserid(String fromUserid) {
        this.fromUserid = fromUserid;
    }

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAcceptDateTime() {
        return acceptDateTime;
    }

    public void setAcceptDateTime(String acceptDateTime) {
        this.acceptDateTime = acceptDateTime;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
