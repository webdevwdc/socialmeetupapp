package com.nationality.retrofit;

import android.app.Activity;
import android.content.Context;

import com.nationality.R;
import com.nationality.model.AddBulletinRequest;
import com.nationality.model.AddFriendRequest;
import com.nationality.model.AddMeetupRequest;
import com.nationality.model.AddUserRequest;
import com.nationality.model.AllMeetupRequest;
import com.nationality.model.BadgeRemoveRequest;
import com.nationality.model.BlockUserListRequest;
import com.nationality.model.BlockUserRequest;
import com.nationality.model.BulletinCommentLikeRequest;
import com.nationality.model.BulletinCommentReplyRequest;
import com.nationality.model.BulletinDetailsRequest;
import com.nationality.model.BulletinLikeRequest;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.BulletinMyListRequest;
import com.nationality.model.BulletinReplyListRequest;
import com.nationality.model.ChangePasswordRequest;
import com.nationality.model.ChatLastMessageStoreRequest;
import com.nationality.model.CityRequest;
import com.nationality.model.CmsRequest;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.DashboardRequest;
import com.nationality.model.DeleteBulletinRequest;
import com.nationality.model.DeleteMeetupRequest;
import com.nationality.model.EditBulletinRequest;
import com.nationality.model.EditMeetupRequest;
import com.nationality.model.EditProfileRequest;
import com.nationality.model.FbInviteFriendRequest;
import com.nationality.model.FooterCountRequest;
import com.nationality.model.ForgotPasswordRequest;
import com.nationality.model.FriendAcceptRejectRequest;
import com.nationality.model.FriendListRequest;
import com.nationality.model.GetChatTokenRequest;
import com.nationality.model.GetPrivacySettingsRequest;
import com.nationality.model.InsertPrivacySettingsRequest;
import com.nationality.model.InviteMeetupUserRequest;
import com.nationality.model.JoinMeetupRequest;
import com.nationality.model.LanguageRequest;
import com.nationality.model.LoginRequest;
import com.nationality.model.LogoutRequest;
import com.nationality.model.MeetupAcceptRejectRequest;
import com.nationality.model.MeetupCommentDtlsRequest;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.model.MeetupDetailsRequest;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.model.MessageRemoveBadgeCountRequest;
import com.nationality.model.NationalityRequest;
import com.nationality.model.NewMeetupRequestList;
import com.nationality.model.ProfileDetailsRequest;
import com.nationality.model.RecentMessageListRequest;
import com.nationality.model.ReportPostRequest;
import com.nationality.model.SearchUserConnectionRequest;
import com.nationality.model.SignupRequest;
import com.nationality.model.SuggestionRequest;
import com.nationality.model.UnblockUserRequest;
import com.nationality.model.UpdateLocationRequest;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Uzibaba on 1/8/2017.
 */

public class RestHandler {

   public Retrofit retrofit;
    RetrofitListener retroListener;
    String method_name;
    Context mContext;

    public RestHandler(Context con, RetrofitListener retroListener ){

        this.retroListener=retroListener;
        mContext=con;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface RestInterface {

        @GET("languages")
        Call<LanguageRequest> getLanguageList();

        @FormUrlEncoded
        @POST("nationalities")
        Call<NationalityRequest> getNationalityList(@Field("keyword") String keyword);

        @FormUrlEncoded
        @POST("cities")
        Call<CityRequest> getCityList(@Field("keyword") String keyword);

        @FormUrlEncoded
        @POST("user/signup")
        Call<SignupRequest> register(@Field("token_id") String token_id, @Field("facebookId") String facebookId,
                                       @Field("first_name") String first_name, @Field("last_name") String last_name,
                                       @Field("email") String email, @Field("password") String password, @Field("home_city") String home_city,
                                      /* @Field("address") String address,*/ @Field("registration_type") String registration_type,
                                       /*@Field("latitude") String latitude, @Field("longitude") String longitude,*/
                                       @Field("device_type") String device_type, @Field("nationality_id") int nationality_id
                , @Field("register_done") int register_done);


        @FormUrlEncoded
        @POST("user/signup")
        Call<SignupRequest> fbLoginStep1(@Field("token_id") String token_id, @Field("facebookId") String facebookId,
                                     @Field("first_name") String first_name, @Field("last_name") String last_name,
                                     @Field("email") String email, @Field("password") String password, @Field("home_city") String home_city,
                                       @Field("address") String address, @Field("registration_type") String registration_type,
                                       @Field("latitude") String latitude, @Field("longitude") String longitude,
                                     @Field("device_type") String device_type, @Field("nationality_id") int nationality_id
                , @Field("register_done") int register_done);

//        @FormUrlEncoded
//        @POST("user/signup")
//        Call<FbSignUpRequest> fbregisterStep1(@Field("token_id") String token_id, @Field("facebookId") String facebookId,
//                                     @Field("first_name") String first_name, @Field("last_name") String last_name,
//                                     @Field("email") String email, @Field("password") String password, @Field("home_city") String home_city,
//                                     @Field("address") String address, @Field("registration_type") String registration_type,
//                                     @Field("latitude") String latitude, @Field("longitude") String longitude,
//                                     @Field("device_type") String device_type, @Field("nationality_id") int nationality_id
//                , @Field("register_done") int register_done);

        @FormUrlEncoded
        @POST("user/signin")
        Call<LoginRequest> login(@Field("token_id") String token_id,
                                 @Field("email") String email, @Field("password") String password,
                                 @Field("device_type") String device_type,
                                 @Field("address") String address,
                                 @Field("latitude") String latitude,
                                 @Field("longitude") String longitude);

        @FormUrlEncoded
        @POST("user/facebook_add_friend")
        Call<FbInviteFriendRequest> facebook_add_friend(@Field("facebookId") String facebookId,
                                                        @Field("from_userid") int from_userid);


        @GET("user/dashboard/{user_id}")
        Call<DashboardRequest> getDashboard(@Path("user_id") int user_id);

        @GET("user/{user_id}/{loggedin_userid}")
        Call<ProfileDetailsRequest> getProfileDetails(@Path("user_id") int user_id,@Path("loggedin_userid") int loggedin_userid);

        @GET("user/friends/{user_id}")
        Call<ConnectionRequest> getConnectionList(@Path("user_id") int user_id);

        @FormUrlEncoded
        @POST("user/all_user")
        Call<ConnectionRequest> getAllConnectionList(@Field("keyword") String keyword,
                                                     @Field("user_id") int user_id,
                                                     @Field("meetup_id") String meetup_id,
                                                     @Field("latitude") String latitude,
                                                     @Field("longitude") String longitude);

        @FormUrlEncoded
        @POST("bulletin/add")
        Call<AddBulletinRequest> AddBulletinRequest(@Field("bulletin_title") String bulletin_title,
                                                    @Field("bulletin_content") String bulletin_content,
                                                    @Field("user_id") int user_id);


//        @GET("bulletin/{bulletin_id}")
//        Call<BulletinDetailsRequest> getBulletinDetails(@Path("bulletin_id") int bulletin_id);

        @FormUrlEncoded
        @POST("meetup/insert")
        Call<AddMeetupRequest> insertMeetup(@Field("user_id") int user_id,
                                            @Field("title") String title, @Field("date_time") String date_time,@Field("meetup_place") String meetup_place,
                                            @Field("location") String location, @Field("meetup_lat") String meetup_lat,
                                            @Field("meetup_long") String meetup_long, @Field("comment") String comment,
                                            @Field("type") String type);

        @GET("meetup/{meetup_id}/{user_id}")
        Call<MeetupDetailsRequest> getMeetupDetails(@Path("meetup_id") int meetup_id, @Path("user_id") int user_id);

        @FormUrlEncoded
        @PUT("meetup/{meetup_id}")
        Call<EditMeetupRequest> editMeetup(@Path("meetup_id") String meetup_id,@Field("title") String title,
                                           @Field("date_time") String date_time, @Field("location") String location,
                                           @Field("meetup_place") String meetup_place,
                                           @Field("meetup_lat") String meetup_lat,
                                           @Field("meetup_long") String meetup_long, @Field("comment") String comment,
                                           @Field("type") String type);


        @FormUrlEncoded
        @POST("meetup/like")
        Call<MeetupLikeRequest>
        meetupLikeRequest(@Field("user_id") int user_id,
                            @Field("is_like") String is_like, @Field("meetup_id") int meetup_id);

        @GET("meetup/all/{user_id}")
        Call<AllMeetupRequest> getAllMeetupList(@Path("user_id") int user_id);

        @GET("meetup/my/{user_id}")
        Call<AllMeetupRequest> getMyMeetupList(@Path("user_id") int user_id);

        @GET("meetup/meetup_request/{user_id}")
        Call<NewMeetupRequestList> getMeetupRequestList(@Path("user_id") int user_id);

        @GET("meetup/all/{user_id}")
        Call<AllMeetupRequest> getAllFilteredMeetupList(@Path("user_id") int user_id, @Query("filter") String filter);

        //

        @GET("bulletin/all/{user_id}")
        Call<BulletinListRequest> getAllFilteredBulletinList(@Path("user_id") int user_id, @Query("filter") String filter);



        //meetup/comment/like

        @FormUrlEncoded
        @POST("meetup/comment/like")
        Call<MeetupCommentLikeRequest>
        meetupCommentLike(@Field("user_id") int user_id,
                          @Field("is_like") String is_like, @Field("comment_id") int comment_id);

        @FormUrlEncoded
        @POST("meetup/meetup_accept_reject")
        Call<MeetupAcceptRejectRequest>
        sendMeetupAcceptReject(@Field("user_id") int user_id,
                          @Field("is_attend") String is_attend, @Field("meetup_id") int meetup_id);

        //user/logout/{user_id}

        @GET("user/logout/{user_id}")
        Call<LogoutRequest> logour(@Path("user_id") int user_id);

        //meetup/comment_post/{meetup_id}

        @FormUrlEncoded
        @POST("meetup/comment_post/{meetup_id}")
        Call<MeetupCommentLikeRequest>
        postMeetupComment(@Path("meetup_id") int meetup_id,@Field("user_id") int user_id,
                           @Field("parent_meetup_id") int parent_meetup_id , @Field("comment") String comment);



        //meetup/comment/{comment_id}/{user_id}

        @GET("meetup/comment/{comment_id}/{user_id}")
        Call<MeetupCommentDtlsRequest> getMeetupCommentDetails(@Path("comment_id") int comment_id, @Path("user_id") int user_id);




        @FormUrlEncoded
        @POST("post_report")
        Call<ReportPostRequest>
        reportPostRequest(@Field("from_user_id") int from_user_id,
                          @Field("to_user_id") int to_user_id, @Field("comment") String comment);


        //http://app.nationality.dedicatedresource.net/api/v2/user/friend_request/{user_id}

        @GET("user/friend_request/{user_id}")
        Call<FriendListRequest> getNewFriendRequests(@Path("user_id") int user_id);

        //user/friend_accept_reject

        @FormUrlEncoded
        @POST("user/friend_accept_reject")
        Call<FriendAcceptRejectRequest>
        userAcceptReject(@Field("from_userid") String from_userid,
                                   @Field("to_userid") String to_userid, @Field("is_accept") String is_accept);




        //////////////////////////////////////////

        @FormUrlEncoded
        @POST("bulletin/add")
        Call<AddBulletinRequest> AddBulletinRequest(@Field("bulletin_title") String bulletin_title,
                                                    @Field("bulletin_content") String bulletin_content,
                                                    @Field("bulletin_type") String bulletin_type,
                                                    @Field("user_id") int user_id,
                                                    @Field("bulletin_date_time") String bulletin_date_time);
        @GET("bulletin/all/{user_id}")
        Call<BulletinListRequest> getBulletinList(@Path("user_id") int user_id);

        @GET("bulletin/my/{user_id}")
        Call<BulletinMyListRequest> getMyBulletinList(@Path("user_id") int user_id);


        @FormUrlEncoded
        @PUT("bulletin/{bulletin_id}")
        Call<EditBulletinRequest> EditBulletinRequest(@Path("bulletin_id") int bulletin_id ,
                                                      @Field("bulletin_title") String bulletin_title,
                                                      @Field("bulletin_content") String bulletin_content,
                                                      @Field("bulletin_type") String bulletin_type,
                                                      @Field("user_id") int user_id,
                                                      @Field("bulletin_date_time") String bulletin_date_time);



        @FormUrlEncoded
        @POST("bulletin/like")
        Call<BulletinLikeRequest>
        bulletinLikeRequest(@Field("user_id") int user_id,
                            @Field("is_like") String is_like, @Field("bulletin_id") int bulletin_id);


        @FormUrlEncoded
        @POST("bulletin/comment_post/{bulletin_id}")
        Call<BulletinCommentReplyRequest> bulletinCommentReplyRequest(@Path("bulletin_id") int bulletin_id ,
                                                                      @Field("comment") String comment,
                                                                      @Field("parent_bulletin_id") String parent_bulletin_id,
                                                                      @Field("user_id") int user_id);


        @GET("bulletin/{bulletin_id}/{user_id}")
        Call<BulletinDetailsRequest> getBulletinDetails(@Path("bulletin_id") int bulletin_id, @Path("user_id") int user_id);

        @GET("bulletin/comment/{comment_id}/{user_id}")
        Call<BulletinReplyListRequest> getBulletinReplyList(@Path("comment_id") int comment_id, @Path("user_id") int user_id);


        @FormUrlEncoded
        @POST("bulletin/comment/like")
        Call<BulletinCommentLikeRequest>
        bulletinCommentLikeRequest(@Field("user_id") int user_id,
                                   @Field("is_like") String is_like, @Field("comment_id") int comment_id);
        @DELETE("bulletin/{bulletin_id}")
        Call<DeleteBulletinRequest> delBulletin(@Path("bulletin_id") int bulletin_id);

        ////////////////////////////////////////


        @FormUrlEncoded
        @POST("user/search_user/{user_id}")
        Call<SearchUserConnectionRequest>
        searchUserConnection(@Path("user_id") int user_id, @Field("keyword") String keyword);


        @FormUrlEncoded
        @POST("user/add_friend")
        Call<AddFriendRequest>
        addFriend(@Field("from_userid") int from_userid, @Field("to_userid") int to_userid);

        @FormUrlEncoded
        @PUT("user/{user_id}")
        Call<EditProfileRequest> editProfile(@Path("user_id") int user_id, @Field("first_name") String first_name,
                                             @Field("last_name") String last_name, @Field("home_city") String home_city,
                                             @Field("nationality_id") String nationality_id,
                                             @Field("language_id") String language_id, @Field("address") String address,
                                             @Field("latitude") String latitude, @Field("longitude") String longitude,
                                             @Field("tag") String tag,
                                             @Field("interest") String interest,
                                             @Field("short_bio") String short_bio,@Field("profileImage") String profileImage);


        @FormUrlEncoded
        @POST("meetup/invite_user")
        Call<InviteMeetupUserRequest>
        inviteMeetupUsers(@Field("meetup_id") int meetup_id, @Field("user_id") String user_id);

        @FormUrlEncoded
        @POST("user/change_password")
        Call<ChangePasswordRequest>
        changePasswordRequest(@Field("id") int user_id,
                              @Field("new_password") String new_password,
                              @Field("old_password") String old_password);

        @FormUrlEncoded
        @POST("user/forgot_password")
        Call<ForgotPasswordRequest>
        forgotPassword(@Field("email") String email);

        @FormUrlEncoded
        @POST("chat-token")
        Call<GetChatTokenRequest>
        getChatToken(@Field("senderfbid") String senderfbid,
                     @Field("recieverfbid") String recieverfbid,
                     @Field("chat_type") String chat_type);

        @FormUrlEncoded
        @POST("user/insert_message")
        Call<ChatLastMessageStoreRequest>
        recentMessageStore(@Field("to_userid") String to_userid, @Field("from_userid") int from_userid, @Field("message") String message, @Field("message_date_time") String message_date_time);

        @GET("user/recent_message/{user_id}")
        Call<RecentMessageListRequest> getRecentMessageList(@Path("user_id") int user_id);


        @GET("cms/{cms}")
        Call<CmsRequest> getCmsData(@Path("cms") String cms);

        @FormUrlEncoded
        @POST("user/block_user")
        Call<BlockUserRequest>
        blockUser(@Field("from_userid") int from_userid, @Field("to_userid") int to_userid);

        @FormUrlEncoded
        @POST("user/unblock_user")
        Call<UnblockUserRequest>
        unblockUser(@Field("from_userid") int from_userid, @Field("to_userid") int to_userid);

        @GET("user/block/{user_id}")
        Call<BlockUserListRequest> blockUserList(@Path("user_id") int user_id);

        @FormUrlEncoded
        @POST("appsetting/insert")
        Call<InsertPrivacySettingsRequest>
        insertPrivacySettings(@Field("user_id") int userid,
                              @Field("is_anyone_find_me") String is_anyone_find_me,
                              @Field("is_anyone_see_my_profile") String is_anyone_see_my_profile,
                              @Field("is_everyone_see_my_bulletin") String is_everyone_see_my_bulletin,
                              @Field("mile") String mile);

        @GET("appsetting/{user_id}")
        Call<GetPrivacySettingsRequest> getPrivacySettings(@Path("user_id") int user_id);


        //user/request_count/{user_id}

        @GET("user/request_count/{user_id}")
        Call<FooterCountRequest> getFooterCountSection(@Path("user_id") int user_id);


        //userAddFriend
        @FormUrlEncoded
        @POST("user/add_friend")
        Call<AddUserRequest>
        userAddFriend(@Field("from_userid") int from_userid,
                              @Field("to_userid") int to_userid);

        //user/remove_push_badge

        @FormUrlEncoded
        @POST("user/remove_push_badge")
        Call<BadgeRemoveRequest>
        removeBadgeCount(@Field("user_id") int user_id,
                      @Field("badge_type") String badge_type);


        @DELETE("meetup/{meetup_id}")
        Call<DeleteMeetupRequest> delMeetup(@Path("meetup_id") int meetup_id);

        //user/friend_remove

        @FormUrlEncoded
        @POST("user/friend_remove")
        Call<DeleteMeetupRequest> // since same response and model
        unfriendUser(@Field("to_userid") int to_userid,
                         @Field("from_userid") int from_userid);


        @FormUrlEncoded
        @POST("meetup/meetup_join")
        Call<JoinMeetupRequest>
         joinMeetup(@Field("user_id") int user_id,@Field("meetup_id") int meetup_id,
                    @Field("is_attend") String is_attend);


        @FormUrlEncoded
        @POST("meetup/meetup_unjoin")
        Call<JoinMeetupRequest>
        unJoinMeetup(@Field("user_id") int user_id,@Field("meetup_id") int meetup_id);

        @FormUrlEncoded
        @PUT("user/location_update/{user_id}")
        Call<UpdateLocationRequest> setLocation(@Path("user_id") int user_id, @Field("address") String address,
                                                @Field("latitude") String latitude, @Field("longitude") String longitude);


        @FormUrlEncoded
        @POST("user/remove_message_push_badge")
        Call<MessageRemoveBadgeCountRequest>
        messageRemoveBadge(@Field("to_userid") int to_userid,@Field("from_userid") int from_userid,
                           @Field("badge_number") int badge_number, @Field("user_id") int user_id);


        @FormUrlEncoded
        @POST("suggestions")
        Call<SuggestionRequest>
        postSuggestions(@Field("user_id") int user_id,@Field("comment") String comment);

    }

    public void makeHttpRequest(Call call, final String method)
    {
        this.method_name =method;
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                // do something with the response
//                if(mContext instanceof  Activity && !((Activity)mContext).isFinishing())

                if(method.equalsIgnoreCase("getNationality") || method.equalsIgnoreCase("getCity"))
                {
                    if(!((Activity)mContext).isFinishing())
                    retroListener.onSuccess(call,response,method_name);

                }
                else
                retroListener.onSuccess(call,response, method_name);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                    if (t instanceof NoRouteToHostException) {
                        retroListener.onFailure(mContext.getString(R.string.server_unreachable));
                    } else if (t instanceof SocketTimeoutException) {
                        retroListener.onFailure(mContext.getString(R.string.timed_out));
                    } else if (t instanceof IOException) {
                        retroListener.onFailure(mContext.getString(R.string.no_internet));
                    } else
                        retroListener.onFailure(t.getMessage());
                }



        });
    }




}
