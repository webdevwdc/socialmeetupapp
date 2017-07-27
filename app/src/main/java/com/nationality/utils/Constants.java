package com.nationality.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.nationality.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by webskitters on 4/6/2017.
 */

public class Constants {
    public static boolean isLocationUpdated = false;

//    public static String base_url="http://app.nationality.dedicatedresource.net/api/v2/";

    public static String base_url="http://api.nationalityapp.com/api/v2/";

//

//    public static String base_url="192.168.2.60/api/v2/";
//    public static String image_url="http://api.nationalityapp.com/upload/profile_image/thumbs/";
    public static String image_url="http://nationalityapp.com/upload/profile_image/thumbs/";
    public static String SHARED_PREF_NAME="NationalityData";



    public static String registration_type_nrml="Normal";
    public static String registration_type_fb="Fb";
    public static String device_type="android";

    public static String TAG_FRAGMENT_CONNECTION="mFrag_Connection_main";
    public static String TAG_FRAGMENT_CONNECTION_PROF_DTLS="mFrag_Connection_main_prof_dtls";
    public static String TAG_FRAGMENT_CONNECTION_REQUESTS="mFrag_Connection_requests";
    public static String TAG_FRAGMENT_ME="mFrag_me";
    public static String TAG_FRAGMENT_BULLETIN="mFrag_bulletin";
    public static String TAG_FRAGMENT_MY_BULLETIN="mFrag_my_bulletin";
    public static String TAG_FRAGMENT_CREATE_BULLETIN="mFrag_create_bulletin";
    public static String TAG_FRAGMENT_BULLETIN_DETAILS="mFrag_bulletin_details";
    public static String TAG_FRAGMENT_MEETUPS="mFrag_meetups";
    public static String TAG_FRAGMENT_MEETUPS_DETAILS="mFrag_meetup_details";
    public static String TAG_FRAGMENT_MSG="mFrag_msg";
    public static String TAG_FRAGMENT_SETTINGS="mFrag_settings";
    public static String TAG_FRAGMENT_PRIVACY="mFrag_privacy";
    public static String TAG_FRAGMENT_EDITPROFILE="mFrag_privacy";
    public static String TAG_FRAGMENT_RPORTUSER="mFrag_report_user";
    public static String TAG_FRAGMENT_BULLETIN_MSG_RESPOND="mFrag_bulletin_msg_respond";
    public static String TAG_FRAGMENT_BULLETIN_COMMENT_RESPOND="mFrag_bulletin_comment_respond";
    public static String TAG_FRAGMENT_BULLETIN_REPLY_COMMENTS="mFrag_bulletin_comment_reply";
    public static String TAG_FRAGMENT_ADD_MEETUP="mFrag_bulletin_add_meetup";
    public static String TAG_FRAGMENT_MY_MEETUP="mFrag_bulletin_my_meetup";
    public static String TAG_FRAGMENT_COMMENT_LISTING="mFrag_comment_listing";
    public static String TAG_FRAGMENT_MEETUP_REPLY_COMMENTS="mFrag_meetup_comment_reply";
    public static String TAG_FRAGMENT_BULLETIN_REPLY_LIST="mFrag_bulletin_reply_list";
    public static String TAG_CHANGE_PASSWORD="mFrag_change_pwd";
    public static String TAG_FRAGMENT_MSG_CONN="mFrag_msg_conn";
    public static String TAG_FRAGMENT_RECENT_MSG="mFrag_recent_msg";

    public static String TAG_FRAGMENT_SUGGESTIONS="mFrag_suggestions";

    public static String TAG_ALL_MUTUALFRIENDLIST="mFrag_see_all_mutual_friend_list";
    public static String TAG_ALL_CONNECTION="mFrag_see_all_connection_list";

    public static String TAG_ALL_MEETUPFRIENDLIST="mFrag_see_all_meetup_friend_list";

    public static String TAG_CMS="mFrag_cms";
    public static String TAG_FRAGMENT_BLOCKUSER="mFrag_block_user";
    public static String TAG_FRAGMENT_BLOCKUSERLIST="mFrag_block_user_list";

    public static String TAG_FRAGMENT_MEETUP_REQUEST="mFrag_meetup_req";
    //TAG_FRAGMENT_REPORT_POST
    public static String TAG_FRAGMENT_REPORT_POST="mFrag_report_post";
    public static String strShPrefUserType="user_type";
    public static String strShPrefUserLocationSet="No";
    public static String strShPrefUserloginStatus="login_status";
    public static String strShPrefUserFirstName="first_name";
    public static String strShPrefUserLastName="last_name";
    public static String strShPrefUserEmail="email";
    public static String strShPrefUserZip="zip_code";
    public static String strShPrefUserCity="city";
    public static String strShPrefUserLanguage="language_id";
    public static String strShPrefUserLanguageTEXTS="language_texts";
    public static String strShPrefUserLRegType="registration_type";
    public static String strShPrefUserID="id";
    public static String strShPrefUserShortBio="short_bio";
    public static String strShPrefProfilePic="profile_pic";
    public static String strShPrefPhoneNo="phone_no";

    public static String strShPrefUserTag="user_tag";

    public static String strShPrefUserNationalityID="nationality_id";
    public static String strShPrefUserLatitude="user_latitude";
    public static String strShPrefUserLongitude="user_longitude";
    public static String strShPrefFCMTokenID="fcm_token";

    public static String strShPrefMeetupBadges="meetup_badges_count";
    public static String strShPrefBulletinBadges="bulletin_badges_count";
    public static String strShPrefConnectionBadges="connection_badges_count";
    public static String strShPrefChatupBadges="chat_badges_count";


    public static String strShPrefUserToChat="to_user_id";
    public static String strShPrefUserFromChat="from_user_id";

    public static String strShPrefUserFBID="fb_id";
    public static String strShPrefAddress="adress";

    public static int PlacePicker_Requeest=007;


    public static String web_date_format="yyyy-MM-dd HH:mm:ss";
    public static String app_display_date_format="MM/dd/yy";
    public static String app_display_time_format="hh:mm a";

    public static String app_bulletin_display_date_format="dd MMM yyyy";

    public static String PRIVACY_PUBLIC="public";
    public static String PRIVACY_PRIVATE="private";

    public static String LIKE_YES="Yes";
    public static String LIKE_NO="No";

    public static String web_date_only_format="yyyy-MM-dd";
    public static String web_time_only_format="HH:mm:ss";


    public static String ACCEPT="Accept";
    public static String REJECT="Reject";
    public static String to_user_id="";

    //for date--meetups
//        Constants.changeDateFormat("2017-04-20 00:13:15","yyyy-MM-dd HH:mm:ss","dd/MM/yy");

    //for time--meetups
//        Constants.changeDateFormat("2017-04-20 00:13:15","yyyy-MM-dd HH:mm:ss","hh:mm aa");

    public static void showAlert(Context con, String msg)
    {

        if(con==null)
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
//                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//                        // if this button is clicked, close
//                        // current activity
////                        MainActivity.this.finish();
//                    }
//                })
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static boolean getLoggedinStatus(Context con)
    {
        boolean status=false;

        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        status = prefs.getBoolean(Constants.strShPrefUserloginStatus, false);

        return status;

    }

    public static String getUserType(Context con)
    {
        String type="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        type = prefs.getString(Constants.strShPrefUserType, "");

        return type;

    }
    public static String getLocationStatus(Context con)
    {
        String type="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        type = prefs.getString(Constants.strShPrefUserLocationSet, "");

        return type;

    }

    public static String getUserLat(Context con)
    {
        String lat="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        lat = prefs.getString(Constants.strShPrefUserLatitude, "");

        return lat;

    }

    public static String getUserLon(Context con)
    {
        String lon="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        lon = prefs.getString(Constants.strShPrefUserLongitude, "");

        return lon;

    }

    public static int getUserNationalityID(Context con)
    {
        int nat_id=0;
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        nat_id = prefs.getInt(Constants.strShPrefUserNationalityID, 0);

        return nat_id;

    }

    public static String getFCMToken(Context con)
    {

        if(con==null)
            return "";

        String fcm="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        fcm = prefs.getString(Constants.strShPrefFCMTokenID, "");

        return fcm;
    }

    public static int getUserId(Context con)
    {
        if(con==null)
            return 0;

        int id=0;

        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        id = prefs.getInt(Constants.strShPrefUserID, 0);

        return id;

    }


    public static String changeDateFormat(String inp_date,String inpFormat,String outFormat) {

        String outputDateStr = "";
        DateFormat outputFormat = new SimpleDateFormat(outFormat);
        DateFormat inputFormat = new SimpleDateFormat(inpFormat);
//        String inputDateStr= null;
        try {
//            inputDateStr = "2013-06-24";

            Date date = inputFormat.parse(inp_date);
            outputDateStr = outputFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }

    public static Typeface typeFaceOpenSansBold(Context con)
    {
        Typeface typeFaceOpenSansBold;
        typeFaceOpenSansBold = Typeface.createFromAsset(con.getAssets(),
                "OPENSANS-BOLD.TTF");

        return typeFaceOpenSansBold;
    }

    public static Typeface typeFaceOpenSansReg(Context con)
    {
        Typeface  typeFaceOpenSansReg;
        typeFaceOpenSansReg=Typeface.createFromAsset(con.getAssets(), "OPENSANS-REGULAR.TTF");

        return typeFaceOpenSansReg;
    }



    public static void setImage(ImageView image, String image_name, Context c)
    {
        if(image_name!=null && image_name.length()>0) {

            String url= Constants.image_url+image_name;

            Picasso.with(c)
                    .load(url).centerCrop().fit().transform(new CircleTransform())
                    .into(image);
        }
                else{
                    Picasso.with(c)
                            .load(R.drawable.no_image).fit().centerInside().transform(new CircleTransform())
                            .into(image);
                }
    }

    public static void setSquareImage(ImageView image, String image_name, Context c)
    {
        if(image_name!=null && image_name.length()>0) {

            String url= Constants.image_url+image_name;

            Picasso.with(c)
                    .load(url).centerCrop().fit()
                    .into(image);
        }
        else{
            Picasso.with(c)
                    .load(R.drawable.no_image).fit().centerInside()
                    .into(image);
        }
    }



    public static String getData(Context con,String parameter)
    {
        String data="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        data = prefs.getString(parameter, "");

        return data;
    }

    public static void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    public static String getUserFBId(Context con)
    {        String id="";

        if(con==null)
            return "";

        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        id = prefs.getString(Constants.strShPrefUserFBID, "");
        return id;    }


    public static String getUserEmailId(Context con)
    {        String email="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        email = prefs.getString(Constants.strShPrefUserEmail, "");
        return email;
    }


    public static String getUserAddress(Context con)
    {        String email="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        email = prefs.getString(Constants.strShPrefAddress, "");
        return email;
    }

    public static String getUserName(Context con)
    {
        String firstName="", lastName="";
        SharedPreferences prefs = con.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        firstName = prefs.getString(Constants.strShPrefUserFirstName, "");
        lastName = prefs.getString(Constants.strShPrefUserLastName,"");
        return firstName+" "+lastName;
    }


}
