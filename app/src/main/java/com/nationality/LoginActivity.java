package com.nationality;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nationality.crashreport.ExceptionHandler;
import com.nationality.model.ForgotPasswordRequest;
import com.nationality.model.LoginRequest;
import com.nationality.model.SignupRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        RetrofitListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Button btn_create_account, btn_forgot_pass;
    ImageView img_go,btn_fb_login;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    EditText etUserName, etPass;
    TextView txt_how_to_use;
    ImageView img_hide_show_pass;
    RestHandler rest;
    ProgressDialog pDialog;
    private CallbackManager callbackManager;
    private String fb_id;
    private String fb_profile_pic_url;
    private String id="";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String first_name="",last_name="",email="";
    private EditText edt_email;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Button btnFusedLocation;
    TextView tvLocation,tv_header;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private String gps_address="",latitude="",longitude="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        initView();
        settingsrequest();
//        storagePermission();
    }

    private void initView(){


//            if (AccessToken.getCurrentAccessToken() == null) {
//                Toast.makeText(this," Logged Out", Toast.LENGTH_LONG).show();
//
//                return; // already logged out
//            }
//
//            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                    .Callback() {
//                @Override
//                public void onCompleted(GraphResponse graphResponse) {
//
//                    LoginManager.getInstance().logOut();
//                    Toast.makeText(LoginActivity.this," Logged Out", Toast.LENGTH_LONG).show();
////                details.setText("Logged Out");
//
//                }
//            }).executeAsync();


        FacebookSdk.sdkInitialize(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        typeFaceOpenSansBold = Typeface.createFromAsset(getAssets(),
                "OPENSANS-BOLD.TTF");

        btn_fb_login=(ImageView)findViewById(R.id.btn_fb_login);

        typeFaceOpenSansReg=Typeface.createFromAsset(getAssets(), "OPENSANS-REGULAR.TTF");
        img_hide_show_pass=(ImageView)findViewById(R.id.img_hide_show_pass);
        img_hide_show_pass.setOnClickListener(this);
        btn_create_account=(Button)findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(this);
        btn_create_account.setTypeface(typeFaceOpenSansReg);
        btn_forgot_pass =(Button)findViewById(R.id.btn_forgot_pass);
        btn_forgot_pass.setTypeface(typeFaceOpenSansReg);
        btn_forgot_pass.setOnClickListener(this);
        img_go=(ImageView) findViewById(R.id.img_go);
        img_go.setOnClickListener(this);


        etUserName=(EditText)findViewById(R.id.ext_userName);
        etUserName.setTypeface(typeFaceOpenSansReg);
        etPass=(EditText)findViewById(R.id.ext_pass);
        etPass.setTypeface(typeFaceOpenSansReg);
        txt_how_to_use=(TextView)findViewById(R.id.txt_how_to_use);
        txt_how_to_use.setTypeface(typeFaceOpenSansReg);

//        etUserName.setText("uzair.hossain@webskitters.com");
//        etPass.setText("123456");

        rest=new RestHandler(this,this);

        SpannableString content = new SpannableString(txt_how_to_use.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, txt_how_to_use.getText().toString().length(), 0);
        txt_how_to_use.setText(content);

        btn_fb_login.setOnClickListener(this);
        btn_forgot_pass.setOnClickListener(this);

        pDialog=new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);


        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_account:

                hideSoftKeyboard();

                Intent intentCreateAccount=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentCreateAccount);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
                finish();

                break;

            case R.id.img_go:

                if(validate())
                    doLogin();

//                Intent intentGo=new Intent(LoginActivity.this, LandingActivity.class);
//                finish();
//                startActivity(intentGo);
//                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
                break;

            case R.id.img_hide_show_pass:

                if(etPass.getText().toString().length()>0)
                if(Integer.parseInt(img_hide_show_pass.getTag().toString())==0){
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    img_hide_show_pass.setTag(1);
                    img_hide_show_pass.setImageResource(R.drawable.hide_icon_pass_inactive);
                    etPass.setSelection(etPass.getText().toString().length());
                }else{
                    etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    img_hide_show_pass.setTag(0);
                    img_hide_show_pass.setImageResource(R.drawable.hide_icon_pass_active);
                    etPass.setSelection(etPass.getText().toString().length());
                }
                break;


            case R.id.btn_fb_login:

                hideSoftKeyboard();
                onFblogin();
                break;

            case R.id.btn_forgot_pass:

                showResetPwdDialog();

                break;
        }
    }

    private boolean validate() {
        Boolean validResp = true;
        if (etUserName.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "First Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this, "Please enter email address.");
            etUserName.requestFocus();
            etUserName.setText("");

        }
       else if (!emailValidator(etUserName.getText().toString().trim())) {
            validResp = false;
//            Toast.makeText(this, "First Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this, "Please enter valid email address.");
            etUserName.requestFocus();
            etUserName.setText("");

        } else if (etPass.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this, "Password cannot be blank");
            etPass.requestFocus();
            etPass.setText("");
            return validResp;
        }
        return validResp;
    }

    private void doLogin()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                login(Constants.getFCMToken(this),etUserName.getText().toString().trim(),
                        etPass.getText().toString().trim(),Constants.device_type,
                        gps_address,latitude,longitude),"login");

        pDialog.show();
    }
    @Override
    public void onSuccess(Call call, Response response, String method) {

            if(pDialog.isShowing())
                pDialog.dismiss();

            if(response!=null && response.code()==200){

                if(method.equalsIgnoreCase("login"))
                {
                    LoginRequest signup_obj=(LoginRequest)response.body();
                    if(!signup_obj.getResult().getError())
                    {
                        // login succesfull
                        hideSoftKeyboard();
                        storeUserDetails(signup_obj);
                        Intent intentLanding=new Intent(LoginActivity.this, LandingActivity.class);
                        startActivity(intentLanding);
                        finish();
                    }
                    else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                        Constants.showAlert(LoginActivity.this,signup_obj.getResult().getMessage());
                    }
                }
//                else if(method.equalsIgnoreCase("registration"))
//                {
//                    SignupRequest signup_obj=(SignupRequest)response.body();
//                    processResponse(signup_obj);
//                }

                else if(method.equalsIgnoreCase("Fbregistration"))
                {
                    SignupRequest signup_obj=(SignupRequest)response.body();
                    if(!signup_obj.getResult().getError())
                    {

                        // signup succesfull
                        FbSignUpRequest(signup_obj);
//                        Intent intentLanding=new Intent(LoginActivity.this, LandingActivity.class);
//                        startActivity(intentLanding);
//                        finish();
                    }
                    else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                        Constants.showAlert(LoginActivity.this,signup_obj.getResult().getMessage());
                    }
                }
                else if(method.equalsIgnoreCase("forgot_pwd"))
                {
                    ForgotPasswordRequest forgot_obj=(ForgotPasswordRequest)response.body();

//                    if(!forgot_obj.getResult().getError())
//                    {

                        Constants.showAlert(LoginActivity.this,forgot_obj.getResult().getMessage());

//                    }
                }
            }
            else{
                try {
                    Constants.showAlert(this,response.code()+" "+response.message());
                } catch (Exception e) {
//                showAlertDialog("Alert","Server Response Error..");
                    e.printStackTrace();
                }

            }
    }

    private void FbSignUpRequest(SignupRequest signup_obj) {

        Log.d("test","");

        if(signup_obj.getResult().getData().getCreatedAt()==null || signup_obj.getResult().getData().getCreatedAt()=="" ||
                signup_obj.getResult().getData().getCreatedAt().isEmpty()) {


//        ==================

        Intent intent=new Intent(this,SignupActivity.class);
        intent.putExtra("user_type",Constants.registration_type_fb);
        intent.putExtra("fbid",fb_id);
        intent.putExtra("first_name",first_name);
        intent.putExtra("last_name",last_name);
        intent.putExtra("email",email);
        startActivity(intent);

        }
        else {
            //======================

            //parse data and store response in shared pref & go go dashboard

            storeFbUserDetails(signup_obj);
            Intent intentLanding=new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intentLanding);
            finish();
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(this,errorMessage);

    }

    private void storeUserDetails(LoginRequest req)
    {

       SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

//        editor.putString(Constants.strShUserType, "parent");
        editor.putBoolean(Constants.strShPrefUserloginStatus, true);
        editor.putString(Constants.strShPrefUserType, Constants.registration_type_nrml);
        editor.putString(Constants.strShPrefUserLocationSet,"Yes");
        editor.putString(Constants.strShPrefUserFirstName, req.getResult().getData().getFirstName());
        editor.putString(Constants.strShPrefUserLastName, req.getResult().getData().getLastName());
        editor.putString(Constants.strShPrefUserEmail, req.getResult().getData().getEmail());
        editor.putString(Constants.strShPrefUserZip,req.getResult().getData().getZipCode());
        editor.putString(Constants.strShPrefUserCity,req.getResult().getData().getHomeCity());
        editor.putString(Constants.strShPrefUserLanguage,req.getResult().getData().getLanguageId());
        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());

        editor.putString(Constants.strShPrefUserFBID,req.getResult().getData().getFacebookId());


        editor.putString(Constants.strShPrefUserNationalityID,req.getResult().getData().getNationalityId());
        editor.putString(Constants.strShPrefUserLatitude,req.getResult().getData().getLatitude());
        editor.putString(Constants.strShPrefUserLongitude,req.getResult().getData().getLongitude());

        editor.putString(Constants.strShPrefAddress,req.getResult().getData().getAddress());
        editor.putString(Constants.strShPrefUserShortBio,req.getResult().getData().getShortBio());

        editor.putString(Constants.strShPrefUserTag,req.getResult().getData().getTag());

        editor.putString(Constants.strShPrefProfilePic,req.getResult().getData().getProfilePic());
        editor.putString(Constants.strShPrefPhoneNo,req.getResult().getData().getPhone());


        editor.commit();

    }


    private void storeFbUserDetails(SignupRequest req)
    {

       SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

//        editor.putString(Constants.strShUserType, "parent");
        editor.putBoolean(Constants.strShPrefUserloginStatus, true);
        editor.putString(Constants.strShPrefUserType, Constants.registration_type_fb);
        editor.putString(Constants.strShPrefUserLocationSet,"Yes");
        editor.putString(Constants.strShPrefUserFirstName, req.getResult().getData().getFirstName());
        editor.putString(Constants.strShPrefUserLastName, req.getResult().getData().getLastName());
        editor.putString(Constants.strShPrefUserEmail, req.getResult().getData().getEmail());
        editor.putString(Constants.strShPrefUserZip,req.getResult().getData().getZipCode());
        editor.putString(Constants.strShPrefUserCity,req.getResult().getData().getHomeCity());
        editor.putString(Constants.strShPrefUserLanguage,req.getResult().getData().getLanguageId());
        editor.putString(Constants.strShPrefUserShortBio,req.getResult().getData().getShort_bio());
        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());

        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());
        editor.putString(Constants.strShPrefUserFBID,req.getResult().getData().getFacebookId());

        //Added on 28.04.2017

//        editor.putString(Constants.strShPrefUserNationalityID,req.getResult().getData().getNationalityId());
        editor.putString(Constants.strShPrefUserLatitude,req.getResult().getData().getLatitude());
        editor.putString(Constants.strShPrefUserLongitude,req.getResult().getData().getLongitude());

        editor.putString(Constants.strShPrefProfilePic,req.getResult().getData().getProfile_pic());
        editor.putString(Constants.strShPrefPhoneNo,req.getResult().getData().getPhone());

        //28.04.2017

//        editor.putString(Constants.strShUserAddress,edt_email.getEditableText().toString().trim());
//        editor.putString(Constants.strShUserId,id);
//        editor.putBoolean(Constants.strShUserLoginStatus,true);
        editor.commit();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(callbackManager!=null)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onFblogin()
    {
        pDialog.show();
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_friends", "public_profile"/*, "user_birthday"*/));
//        LoginManager.logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("email", "user_friends", "public_profile"/*, "user_birthday"*/));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");
//                       ProgressDialog progressDialog = new ProgressDialog(FbLoginActivity.this);
//                        progressDialog.setMessage("Procesando datos...");
//                        progressDialog.show();
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
//                                details.setText(object.toString());

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, " +
                                "email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();

                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");

                        if(pDialog.isShowing())
                            pDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        if(exception!=null) {

                            AccessToken.setCurrentAccessToken(null);
                            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_friends", "public_profile"));
                        }
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle=null;

        try {
            bundle = new Bundle();
            fb_id = object.getString("id");

//            Toast.makeText(this,object.toString(),Toast.LENGTH_LONG).show();

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + fb_id + "/picture?width=200&height=200");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

                fb_profile_pic_url=profile_pic.toString();

//                new ImageUrlToBase64().execute(fb_profile_pic_url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
               first_name=object.getString("first_name");
            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
                last_name=object.getString("last_name");
            }
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                email=object.getString("email");
            }
            if (object.has("gender")) {
                bundle.putString("gender", object.getString("gender"));
            }
            if (object.has("birthday")) {
                bundle.putString("birthday", object.getString("birthday"));
            }
            if (object.has("location")) {
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            }

            doFbRegistrationStep1(0);
//            if(email.length()>0){
//
//                showEmailDialog();
//
//            }
//            validateFbToken(fb_id);


//            new Constants(this,this).disconnectFromFacebook();
        }
        catch(JSONException e) {
            Log.d("Dsdsd","Error parsing JSON");
        }
        return bundle;
    }



    private void doFbRegistrationStep1(int register_done) {

        pDialog.show();
//        Parameters :
//        token_id
//                first_name
//        last_name
//                email
//        password
//                home_city
//        address
//                latitude
//        longitude
//        registration_type [Normal , Fb]
//        device_type [android, ios]

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                register(Constants.getFCMToken(this),
//                        fb_id,
                        fb_id,
                        first_name,
                        last_name,
                        email,
                        "",
                        "",
//                        gps_address,
                        Constants.registration_type_fb,
//                        latitude, //latitude
//                        longitude, //longitude
                        Constants.device_type,
                        0,register_done),"Fbregistration");

        pDialog.show();
    }

    private void showResetPwdDialog() {
        String keyword="";
        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_reset);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView header2=(TextView)dialog.findViewById(R.id.header2);
        edt_email=(EditText) dialog.findViewById(R.id.edt_email);
        TextView btn_search=(TextView)dialog.findViewById(R.id.btn_search);
        TextView btn_cancel=(TextView)dialog.findViewById(R.id.btn_cancel);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_email.getText().toString().trim().length()==0)
                    Constants.showAlert(LoginActivity.this,"Please enter email");
                else {
                    submitRequest(edt_email.getText().toString());
                    dialog.dismiss();

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void submitRequest(String email) {


        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                forgotPassword(email),"forgot_pwd");

        pDialog.show();
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void setPermision()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Storage Permission");
                alertBuilder.setMessage("Please provide storage access to improve app performance.");
                alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                1000);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        latitude=location.getLatitude()+"";
        longitude=location.getLongitude()+"";

        String geo_add= getAddress(location.getLatitude(),location.getLongitude());
        Log.d("geo_addddddd",geo_add);
    }




    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        }
    }


    protected void startLocationUpdates() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, this, this)) {

            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
        } else {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 5444, this, this);
        }
        Log.d(TAG, "Location update started ..............: ");
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    public static boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
            //Toast.makeText(VisitorHomePageActivity.this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5444:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    settingsrequest();
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                    //splashHandlar(SPLASH_TIME_OUT);
                }
                //splashHandlar(SPLASH_TIME_OUT);
                break;
           /* case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                } else {
                    checkPermission();
                    // Permission Denied
                }
                //splashHandlar(SPLASH_TIME_OUT);
                break;*/
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private String getAddress(double mLatitude,double mLongitude)
    {


//        if (isLocationAvailable) {


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Get the current location from the input parameter list
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
				/*
				 * Return 1 address.
				 */
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
            return ("IO Exception trying to get address:" + e1);
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments "
                    + Double.toString(mLatitude) + " , "
                    + Double.toString(mLongitude)
                    + " passed to address service";
            e2.printStackTrace();
            return errorString;
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
            gps_address = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ? address
                            .getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            // Return the text
            Log.d("adddddreessss",gps_address);
            return gps_address;
        } else {
            Log.d("adddddreessss",gps_address);
            return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it. :(";
        }
//        } else {
//            return "Location Not available";
//        }


    }


    public void settingsrequest()
    {
        Log.e("settingsrequest","Comes");


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // Log.e("Application","Button Clicked");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        // Log.e("Application","Button Clicked1");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            Log.e("Applicationsett",e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //Log.e("Application","Button Clicked2");
                        Toast.makeText(LoginActivity.this, "Location is Enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }
            });

            AlertDialog alert = alertBuilder.create();
            //alert.setCancelable(false);
            alert.show();
        }
    }


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}


