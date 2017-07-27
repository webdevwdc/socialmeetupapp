
package com.nationality.model;

import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nationality.utils.Constants;

public class ConnectionListData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("interest")
    @Expose
    private String interest;
    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("short_bio")
    @Expose
    private String shortBio;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("home_city")
    @Expose
    private String homeCity;
    @SerializedName("facebook_token")
    @Expose
    private String facebookToken;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("token_id")
    @Expose
    private String tokenId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("registration_type")
    @Expose
    private String registrationType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("friend_request_number")
    @Expose
    private Integer friendRequestNumber;

    @SerializedName("facebookId")
    @Expose
    private String facebookId;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFriendRequestNumber() {
        return friendRequestNumber;
    }

    public void setFriendRequestNumber(Integer friendRequestNumber) {
        this.friendRequestNumber = friendRequestNumber;
    }


  /*  Bundle bundle = this.getArguments();
        if(bundle!=null) {
        uniqueChatId = bundle.getString("uniqueChatId");
        receiverId=bundle.getString("receiverId");
        senderId = Constants.getUserFBId(getActivity());

    }*/
}
