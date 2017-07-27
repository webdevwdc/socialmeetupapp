package com.nationality;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nationality.LocationUtils.LocationHandlerService;
import com.nationality.LocationUtils.LocationReceiver;
import com.nationality.adapter.SettingsAdapter;
import com.nationality.crashreport.ExceptionHandler;
import com.nationality.fragment.FragmentBulletin;
import com.nationality.fragment.FragmentBulletinDetails;
import com.nationality.fragment.FragmentChat;
import com.nationality.fragment.FragmentConnection;
import com.nationality.fragment.FragmentConnectionRequest;
import com.nationality.fragment.FragmentCreateBulletin;
import com.nationality.fragment.FragmentLanding;
import com.nationality.fragment.FragmentMeetProfileDetails;
import com.nationality.fragment.FragmentMeetup;
import com.nationality.fragment.FragmentMeetupRequest;
import com.nationality.fragment.FragmentMyBulletin;
import com.nationality.fragment.FragmentProfile;
import com.nationality.fragment.FragmentRecentMessage;
import com.nationality.fragment.FragmentSettings;
import com.nationality.fragment.FragmentLanding.OnFragmentInteractionListener;
import com.nationality.model.BadgeRemoveRequest;
import com.nationality.model.FooterCountRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener,
        FragmentConnection.OnFragmentInteractionListener, FragmentProfile.OnFragmentInteractionListener,
        FragmentConnectionRequest.OnFragmentInteractionListener, FragmentBulletin.OnFragmentInteractionListener,
        FragmentMeetup.OnFragmentInteractionListener, FragmentMeetProfileDetails.OnFragmentInteractionListener,
        FragmentMyBulletin.OnFragmentInteractionListener, FragmentBulletinDetails.OnFragmentInteractionListener,
        FragmentCreateBulletin.OnFragmentInteractionListener, FragmentSettings.OnFragmentInteractionListener,
        RetrofitListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

//    Button btn_create_account, btn_forgor_pass;
//    ImageView img_go;

    FragmentManager fManager;
    TextView txt_reqst_counter_connection, txt_reqst_counter_meetup, txt_reqst_counter_chat, txt_reqst_counter_bulletin;
    FragmentTransaction fTransaction;
    private String from_user_id="";
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    RelativeLayout btm_connection, btm_meetups, btm_msgs, btm_bulletin, btm_me;
    FragmentConnection fragConnection;
    FragmentLanding fragmentLanding;
    FragmentBulletin fragBulletin;
    FragmentMeetup fragMeetups;
//    private FirebaseAuth mFirebaseAuth;
    FragmentRecentMessage fragmentRecentMessage;
    TextView tv_btm_connections, tv_btm_meetups, tv_btm_msg, tv_btm_bulletins, tv_btm_me;
//    private FirebaseUser mFirebaseUser;
//    private String strUserName;
    String push_type = "";

    RestHandler rest;

    // For Location

    public static LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder builder;
    private long INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2 * 1000;


    public static GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;


    static SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);
//        btn_create_account=(Button)findViewById(R.id.btn_create_account);
//        btn_create_account.setOnClickListener(this);
//        btn_forgot_pass=(Button)findViewById(R.id.btn_forgot_pass);
//        btn_forgot_pass.setOnClickListener(this);
//        img_go=(ImageView) findViewById(R.id.img_go);
//        img_go.setOnClickListener(this);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        push_type = "";

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            push_type = extras.getString("type");

        }

        init();

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras=intent.getExtras();
            if(extras!=null && extras.containsKey("from_user_id"))
            {
                from_user_id=extras.getString("from_user_id");
            }
            getFooterCounter();
            refreshFragment();
        }
    };

    private void init() {

        rest = new RestHandler(this, this);
        btm_connection = (RelativeLayout) findViewById(R.id.btm_connection);
        btm_meetups = (RelativeLayout) findViewById(R.id.btm_meetups);
        btm_msgs = (RelativeLayout) findViewById(R.id.btm_msgs);
        btm_bulletin = (RelativeLayout) findViewById(R.id.btm_bulletin);
        btm_me = (RelativeLayout) findViewById(R.id.btm_me);

        tv_btm_connections = (TextView) findViewById(R.id.tv_btm_connections);
        tv_btm_meetups = (TextView) findViewById(R.id.tv_btm_meetups);
        tv_btm_msg = (TextView) findViewById(R.id.tv_btm_msg);
        tv_btm_bulletins = (TextView) findViewById(R.id.tv_btm_bulletins);
        tv_btm_me = (TextView) findViewById(R.id.tv_btm_me);

        txt_reqst_counter_connection = (TextView) findViewById(R.id.txt_reqst_counter_connection);
        txt_reqst_counter_meetup = (TextView) findViewById(R.id.txt_reqst_counter_meetup);
        txt_reqst_counter_chat = (TextView) findViewById(R.id.txt_reqst_counter_chat);
        txt_reqst_counter_bulletin = (TextView) findViewById(R.id.txt_reqst_counter_bulletin);

        typeFaceOpenSansBold = Typeface.createFromAsset(getAssets(), "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg = Typeface.createFromAsset(getAssets(), "OPENSANS-REGULAR.TTF");

        tv_btm_connections.setTypeface(typeFaceOpenSansReg);
        tv_btm_meetups.setTypeface(typeFaceOpenSansReg);
        tv_btm_msg.setTypeface(typeFaceOpenSansReg);
        tv_btm_bulletins.setTypeface(typeFaceOpenSansReg);
        tv_btm_me.setTypeface(typeFaceOpenSansReg);

        btm_connection.setOnClickListener(this);
        btm_me.setOnClickListener(this);
        btm_bulletin.setOnClickListener(this);
        btm_meetups.setOnClickListener(this);
        btm_msgs.setOnClickListener(this);

//        btm_meetups.setSelected(true);

        btm_me.setSelected(true);

        fragConnection = new FragmentConnection();
        fragmentLanding = new FragmentLanding();
        fragBulletin = new FragmentBulletin();
        fragMeetups = new FragmentMeetup();
        fragmentRecentMessage = new FragmentRecentMessage();


//        initFirebase();


        openLandingFragment();

        if(Constants.getLocationStatus(LandingActivity.this).equalsIgnoreCase("No"))
        showLocatinAlert();
        else if(Constants.getUserAddress(this).trim().length()==0)
            fetchLocation();
//        else
//            storagePermission();
    }

    private void storagePermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

            // Permission Already Given
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Storage Permission");
            alertBuilder.setMessage("Please provide storage access to improve app performance.");
            alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(LandingActivity.this,
                            new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }
            });

            AlertDialog alert = alertBuilder.create();
            //alert.setCancelable(false);
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFooterCounter();
        registerReceiver(mMessageReceiver, new IntentFilter("fcm_push"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    /*private void initFirebase()
    {
        // Initialize Firebase Auth
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            strUserName = randomUserId()+"@gmail.com";
            login(strUserName, "123456");
            return;
        } else {
            if (mFirebaseUser.getDisplayName()!=null) {
                //mUsername = mFirebaseUser.getDisplayName();
            }
            //Uri profileUri = mFirebaseUser.getPhotoUrl();
            if (mFirebaseUser.getPhotoUrl() != null) {
               // mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }*/

    public String randomUserId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    /*private void login(String uName, String pwd) {
        mFirebaseAuth.createUserWithEmailAndPassword(uName, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(LandingActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(LandingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Authentication Succeeded
                        }
                    }
                });
    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btm_connection:

                Fragment frag = fManager.findFragmentById(R.id.contentContainer);

                removeBadges("connection_badge");

                if (!(frag instanceof FragmentConnection)) {

//                btm_connection.setSelected(true);
//                btm_meetups.setSelected(false);
//                btm_me.setSelected(false);
//                btm_bulletin.setSelected(false);
//                btm_msgs.setSelected(false);

//                BlankFragment1 newFragment = new BlankFragment1();
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();

                    fManager.addOnBackStackChangedListener(getListener());


                    int pos = searchFragmentfromStack(Constants.TAG_FRAGMENT_CONNECTION);
                    if (pos != -1) {//fragment found, need to pop  all fragments from stack from the current search position incrementally

                        removeFragmentsFromStack(pos + 1);//method for pop fragment from stack

                    } else {//fragment not in stack,add new

                        fTransaction.replace(R.id.contentContainer, fragConnection, Constants.TAG_FRAGMENT_CONNECTION)
                                .addToBackStack(Constants.TAG_FRAGMENT_CONNECTION)
                                .commit();
                    }

                }
                break;


            case R.id.btm_me:

                Fragment frag2 = fManager.findFragmentById(R.id.contentContainer);

                if (!(frag2 instanceof FragmentLanding)) {

//                    btm_connection.setSelected(false);
//                    btm_meetups.setSelected(false);
//                    btm_me.setSelected(true);
//                    btm_bulletin.setSelected(false);
//                    btm_msgs.setSelected(false);

                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();

                    fManager.addOnBackStackChangedListener(getListener());

                    int pos = searchFragmentfromStack(Constants.TAG_FRAGMENT_ME);//search fragment & find stack position if it exists in stack
                    if (pos != -1) {//fragment found

                        removeFragmentsFromStack(pos + 1);//+1 because searched fragment will not be popped up, it will remain in stack for reuse
                    } else {

                        fTransaction
                                .replace(R.id.contentContainer, fragmentLanding, Constants.TAG_FRAGMENT_ME)
                                .addToBackStack(Constants.TAG_FRAGMENT_ME)
                                .commit();
                    }
                }
                break;


            case R.id.btm_bulletin:

                Fragment frag3 = fManager.findFragmentById(R.id.contentContainer);

                removeBadges("bulletin_badge");

                if (!(frag3 instanceof FragmentBulletin)) {
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();

                    fManager.addOnBackStackChangedListener(getListener());

                    int pos = searchFragmentfromStack(Constants.TAG_FRAGMENT_BULLETIN);//search fragment & find stack position if it exists in stack
                    if (pos != -1) {//fragment found

                        removeFragmentsFromStack(pos + 1);//+1 because searched fragment will not be popped up, it will remain in stack for reuse
                    } else {

                        fTransaction
                                .replace(R.id.contentContainer, fragBulletin, Constants.TAG_FRAGMENT_BULLETIN)
                                .addToBackStack(Constants.TAG_FRAGMENT_BULLETIN)
                                .commit();
                    }
                }
                break;

            case R.id.btm_meetups:

                Fragment frag4 = fManager.findFragmentById(R.id.contentContainer);

                removeBadges("meetup_badge");

                if (!(frag4 instanceof FragmentMeetup)) {
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();

                    fManager.addOnBackStackChangedListener(getListener());

                    int pos = searchFragmentfromStack(Constants.TAG_FRAGMENT_MEETUPS);//search fragment & find stack position if it exists in stack
                    if (pos != -1) {//fragment found

                        removeFragmentsFromStack(pos + 1);//+1 because searched fragment will not be popped up, it will remain in stack for reuse
                    } else {

                        fTransaction
                                .replace(R.id.contentContainer, fragMeetups, Constants.TAG_FRAGMENT_MEETUPS)
                                .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS)
                                .commit();
                    }
                }
                break;


            case R.id.btm_msgs:

                Fragment frag5 = fManager.findFragmentById(R.id.contentContainer);

//                removeBadges("message_badge");


                if (!(frag5 instanceof FragmentRecentMessage))

//                    ((FragmentRecentMessage)frag5).getConnectionList();

                {
                    fManager = getFragmentManager();
                    fTransaction = fManager.beginTransaction();
                    fManager.addOnBackStackChangedListener(getListener());
                    int pos = searchFragmentfromStack(Constants.TAG_FRAGMENT_RECENT_MSG);

                    //search fragment & find stack position if it exists in stack
                    if (pos != -1) {//fragment found

                        removeFragmentsFromStack(pos + 1);//+1 because searched fragment will not be popped up, it will remain in stack for reuse
                    } else {
                        fTransaction.replace(R.id.contentContainer, fragmentRecentMessage, Constants.TAG_FRAGMENT_RECENT_MSG)
                                .addToBackStack(Constants.TAG_FRAGMENT_RECENT_MSG)
                                .commit();
                    }
                }
                break;

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

//    public static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
//        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
//            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
//
        int count = fManager.getBackStackEntryCount();

        Log.d("onBackkkkkk", count + "");

        if (count < 2)
            showConfirmDialog();
        else {
            Fragment frag5 = fManager.findFragmentById(R.id.contentContainer);
            if (frag5 instanceof FragmentChat) {
//                txt_reqst_counter_chat.setVisibility(View.GONE);
//                removeBadges("message_badge");
                Constants.to_user_id="";
            }
            super.onBackPressed();
        }

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                Fragment currentFragment = fManager.findFragmentById(R.id.contentContainer);
                if (currentFragment != null)
                    currentFragment.onResume();
//                }
            }
        };

        return result;
    }

    private void openLandingFragment() {
        FragmentLanding newFragment = new FragmentLanding();
        fManager = getFragmentManager();
        fTransaction = fManager.beginTransaction();

        Bundle b = new Bundle();
        b.putString("type", push_type);
        newFragment.setArguments(b);

        fManager.addOnBackStackChangedListener(getListener());

        fTransaction
                .add(R.id.contentContainer, newFragment, Constants.TAG_FRAGMENT_ME)
                .addToBackStack(Constants.TAG_FRAGMENT_ME)
                .commit();
//                }
//
    }

    private int searchFragmentfromStack(String fragmentTag) {
        int pos = -1;
        for (int i = 0; i < fManager.getBackStackEntryCount(); ++i) {

            String name = fManager.getBackStackEntryAt(i).getName();

            Log.d("Fragment_Name", name);
            if (name.equalsIgnoreCase(fragmentTag)) {
                pos = i;
                break;
            }
//                fManager.popBackStack();
        }

        return pos;
    }

    private void removeFragmentsFromStack(int startPos) {
        for (int i = startPos; i < fManager.getBackStackEntryCount(); i++) {
            fManager.popBackStack();
        }

    }

    private void showConfirmDialog() {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
//                        MainActivity.this.finish();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FragmentSettings.callbackManager.onActivityResult(requestCode, resultCode, data);

        if (FragmentSettings.callbackManager != null) {
            FragmentSettings.callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (SettingsAdapter.callbackManager != null) {
            SettingsAdapter.callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
                startLocationUpdates();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void getFooterCounter() {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getFooterCountSection(Constants.getUserId(this)), "get_footer_count");
//        pDialog.show();
    }


    @Override
    public void onSuccess(Call call, Response response, String method) {

        if (response != null && response.code() == 200) {

            if (method.equalsIgnoreCase("get_footer_count")) {
//                getFooterCounter();
                if(response.body() instanceof FooterCountRequest) {
                    FooterCountRequest req_obj = (FooterCountRequest) response.body();
                    if (!req_obj.getResult().getError()) {
//                    refreshFragment();
                        if (req_obj.getResult().getData().getMeetupRequest() > 0) {
                            txt_reqst_counter_meetup.setVisibility(View.VISIBLE);
                            txt_reqst_counter_meetup.setText(String.valueOf(req_obj.getResult().getData().getMeetupRequest()));
                        }

                        if (req_obj.getResult().getData().getConnectionRequest() > 0) {
                            txt_reqst_counter_connection.setVisibility(View.VISIBLE);
                            txt_reqst_counter_connection.setText(String.valueOf(req_obj.getResult().getData().getConnectionRequest()));
                        }

                        if (req_obj.getResult().getData().getBulletinRequest() > 0) {
                            txt_reqst_counter_bulletin.setVisibility(View.VISIBLE);
                            txt_reqst_counter_bulletin.setText(String.valueOf(req_obj.getResult().getData().getBulletinRequest()));
                        }

                        if (req_obj.getResult().getData().getMessageRequest() > 0) {

                            Fragment frag = fManager.findFragmentById(R.id.contentContainer);

                            ///////////////////////////
                            if (frag instanceof FragmentChat && from_user_id != null &&
                                    from_user_id.equalsIgnoreCase(Constants.to_user_id)) {

                                // not needed to increase counter

                                //remove counter from server
                                removeCounterFromServer();


                            } else {
//                            (!(frag instanceof FragmentChat) && !from_user_id.equalsIgnoreCase(Constants.to_user_id)) {
                                txt_reqst_counter_chat.setVisibility(View.VISIBLE);
                                txt_reqst_counter_chat.setText(String.valueOf(req_obj.getResult().getData().getMessageRequest()));
                            }
                        }
//
                    } else {
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
//                    Constants.showAlert(this,req_obj.getResult().getMessage());
                    }
                }
            } else if (method.contains("badge")) {

                if(response.body() instanceof  BadgeRemoveRequest) {
                    BadgeRemoveRequest req_obj = (BadgeRemoveRequest) response.body();
//                req_obj.getResult().getData().get
                    if (!req_obj.getResult().getError()) {
//                    i
                        if (method.equalsIgnoreCase("connection_badge")) {
                            txt_reqst_counter_connection.setVisibility(View.GONE);
                        } else if (method.equalsIgnoreCase("meetup_badge")) {
                            txt_reqst_counter_meetup.setVisibility(View.GONE);
                        } else if (method.equalsIgnoreCase("message_badge")) {
                            txt_reqst_counter_chat.setVisibility(View.GONE);
                        } else if (method.equalsIgnoreCase("bulletin_badge")) {
                            txt_reqst_counter_bulletin.setVisibility(View.GONE);
                        }

                    } else {
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                        Constants.showAlert(this, req_obj.getResult().getMessage());
                    }
                }
            } else if (method.equalsIgnoreCase("set location to server")) {
                // Do ur code. Unregister the receiver.
            }
        } else {
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(this, response.code() + " " + response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            } catch (NullPointerException e) {
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    private void removeCounterFromServer() {

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
       int from_user_id= prefs.getInt(Constants.strShPrefUserFromChat, 0);
        int to_user_id= prefs.getInt(Constants.strShPrefUserToChat,0);

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                messageRemoveBadge(to_user_id,from_user_id,1, Constants.getUserId(this)),"messageRemoveBadge");


    }

    @Override
    public void onFailure(String errorMessage) {

    }


    private void removeBadges(String type) {

//        badge_type ['connection_badge' , 'meetup_badge' ,'message_badge' ,'bulletin_badge']

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                removeBadgeCount(Constants.getUserId(this), type), type);

//        pDialog.show();

    }

    private void refreshFragment() {
        Fragment frag = fManager.findFragmentById(R.id.contentContainer);

        if (frag instanceof FragmentRecentMessage)
            ((FragmentRecentMessage) frag).getConnectionList();


        else if (frag instanceof FragmentMeetup)
            ((FragmentMeetup) frag).getMeetupRequest();

        else if (frag instanceof FragmentBulletin)
            ((FragmentBulletin) frag).getBulletinList(Constants.getUserId(this));

        else if (frag instanceof FragmentConnection)
            ((FragmentConnection) frag).getConnectionList();

        else if (frag instanceof FragmentConnectionRequest)
            ((FragmentConnectionRequest) frag).getFriendList();

        else if (frag instanceof FragmentMeetupRequest)
            ((FragmentMeetupRequest) frag).addMeetUp();

    }




    // Location alert
    void showLocatinAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LandingActivity.this);

        // set title
        alertDialogBuilder.setTitle("Nationality");

        // set dialog message
        alertDialogBuilder
                .setMessage("In order to connect you, Nationality needs your location.")
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                        fetchLocation();

                        pref= getSharedPreferences("Location", Context.MODE_PRIVATE);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void fetchLocation() {
        // GPS
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setInterval(30 * 1000);
        //locationRequest.setFastestInterval(5 * 1000);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;

                //Toast.makeText(LandingActivity.this, "success", Toast.LENGTH_LONG).show();

                // Starts to fetch location continueously
                startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog

                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().

                    status.startResolutionForResult(LandingActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    private void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        Intent intent = new Intent(LandingActivity.this, LocationReceiver.class);
        PendingIntent locationIntent = PendingIntent.getBroadcast(LandingActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationIntent);

    }


/*

    public static class LocationReceiverInner extends BroadcastReceiver
    {
        Location location;
        Intent intent;
        SharedPreferences.Editor editor;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
            *//*intent = new Intent(context, LocationHandlerService.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("location", location);
            intent.putExtras(mBundle);
            context.startService(intent);*//*

            if(location!=null)
            {
                editor = pref.edit();
                editor.putString("Lat", location.getLatitude()+"");
                editor.putString("Long", location.getLongitude() + "");
                editor.putString("Time", getDate(location.getTime(), "yyyy-MM-dd HH:mm:ss"));
                editor.commit();

                updateLocationAtServer();

            }
        }

        public String getDate(long milliSeconds, String dateFormat)
        {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }
    }

    private static void updateLocationAtServer() {

        rest1 = new RestHandler(LandingActivity.this, this);
        rest1.makeHttpRequest(rest1.retrofit.create(RestHandler.RestInterface.class).
                getPrivacySettings(Constants.getUserId(getApplicationContext())),"getPrivacySettingsData");

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }*/
}
