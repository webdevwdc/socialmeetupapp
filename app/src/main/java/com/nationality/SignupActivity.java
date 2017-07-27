package com.nationality;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.nationality.crashreport.ExceptionHandler;
import com.nationality.model.CityRequest;
import com.nationality.model.NationalityRequest;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 4/4/2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener,RetrofitListener,
        View.OnTouchListener, GoogleApiClient.ConnectionCallbacks,LocationListener,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    EditText edt_first_name, edt_last_name, edt_email, edt_pwd, edtAddress;
    Button btnSubmit;
    ImageView img_back,btn_fb_login;
    List<String> al_homecity;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    private AutoCompleteTextView edt_nationality,edt_city;
    TextView txt_or;
    ImageView img_hide_show_pass;
    RestHandler rest;
    ProgressDialog pDialog;
    String selected_language="",selected_city="";
    ArrayList<String> al_nationality,al_city;
    NationalityRequest req_Obj;
    CityRequest req_cityObj;
    private GestureDetector gestureDetector;
    ProgressBar nat_prog_bar,city_prog_bar;
    private String id="";
//    private String fb_id;
    private String fb_profile_pic_url;

    FrameLayout pwd_container;

    private CallbackManager callbackManager;

    String user_type=Constants.registration_type_nrml;
    Location mCurrentLocation;
    String latitude="",longitude="",facebookId="";
    String first_name="",last_name="",email="";



    // For Location

    public static LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder builder;
    private long INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2 * 1000;

    TextView txt_header_name_signupView;

    public static GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private String TAG="GPS";

    String mLastUpdateTime;
    private String gps_address="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            user_type=extras.getString("user_type");
            first_name=extras.getString("first_name");
            last_name=extras.getString("last_name");
            email=extras.getString("email");
            facebookId=extras.getString("fbid");
        }

        initField();

    }

    private void initField() {

        txt_header_name_signupView=(TextView)findViewById(R.id.txt_header_name_signupView);

        typeFaceOpenSansBold = Typeface.createFromAsset(getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getAssets(), "OPENSANS-REGULAR.TTF");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        btn_fb_login=(ImageView) findViewById(R.id.btn_fb_login);
        txt_or=(TextView)findViewById(R.id.txt_or);
        txt_or.setTypeface(typeFaceOpenSansReg);
        img_hide_show_pass=(ImageView)findViewById(R.id.img_hide_show_pass);
        img_hide_show_pass.setOnClickListener(this);
        edt_first_name = (EditText) findViewById(R.id.editTextFirstName);
        edt_first_name.setTypeface(typeFaceOpenSansReg);
        edt_last_name = (EditText) findViewById(R.id.editTextLastName);
        edt_last_name.setTypeface(typeFaceOpenSansReg);
        edt_email = (EditText) findViewById(R.id.editEmail);
        edt_email.setTypeface(typeFaceOpenSansReg);
        edt_pwd = (EditText) findViewById(R.id.editPassword);
        edt_pwd.setTypeface(typeFaceOpenSansReg);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtAddress.setTypeface(typeFaceOpenSansReg);
        edt_city = (AutoCompleteTextView) findViewById(R.id.edt_city);
        edt_city.setTypeface(typeFaceOpenSansReg);
        edt_nationality = (AutoCompleteTextView) findViewById(R.id.edt_nationality);
        edt_nationality.setTypeface(typeFaceOpenSansReg);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnSubmit.setTypeface(typeFaceOpenSansBold);
        nat_prog_bar=(ProgressBar)findViewById(R.id.nat_prog_bar);
        city_prog_bar=(ProgressBar)findViewById(R.id.city_prog_bar);
        pwd_container=(FrameLayout)findViewById(R.id.pwd_container);

        al_homecity = new ArrayList<>();

        img_back=(ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        btn_fb_login.setOnClickListener(this);

        if(user_type.equalsIgnoreCase(Constants.registration_type_fb))
            pwd_container.setVisibility(View.GONE);
//            edt_pwd.setVisibility(View.GONE);

        rest=new RestHandler(this,this);
        pDialog=new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        edtAddress.setOnTouchListener(this);
//        getLanguages();

        if(!facebookId.equalsIgnoreCase("")) {
            btn_fb_login.setVisibility(View.GONE);
            txt_or.setVisibility(View.GONE);
            edt_first_name.setText(first_name);
            edt_last_name.setText(last_name);
            edt_email.setText(email);
        }


        txt_header_name_signupView.setTypeface(typeFaceOpenSansBold);

        edt_nationality.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2) {
//

                    if (edt_nationality.isPerformingCompletion()) {
                        // An item has been selected from the list. Ignore.
                        return;
                    }
                    else
                        getNationalityListing(s);

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_nationality.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selected_language= parent.getItemAtPosition(pos).toString();
//                Toast.makeText(SignupActivity.this," selected"+pos, Toast.LENGTH_LONG).show();
//                getLanguageId();
                getNationalityId();

            }
        });


        edt_city.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2) {
//

                    if (edt_city.isPerformingCompletion()) {
                        // An item has been selected from the list. Ignore.
                        return;
                    }
                    else
                        getCityListing(s);

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selected_city= parent.getItemAtPosition(pos).toString();
            }
        });



        // GPS
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);*/



        edt_first_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_first_name.setSingleLine(false);

        edt_last_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_last_name.setSingleLine(false);




        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        settingsrequest();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }




    private void getNationalityListing(CharSequence s) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getNationalityList(s.toString()),"getNationality");
       nat_prog_bar.setVisibility(View.VISIBLE);

    }

    private void getCityListing(CharSequence s) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getCityList(s.toString()),"getCity");
        city_prog_bar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

             case R.id.img_back:

                 hideSoftKeyboard();

                 Intent intentBack=new Intent(SignupActivity.this, LoginActivity.class);
                 startActivity(intentBack);
                 overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                 finish();
                break;

            case R.id.btnSubmit:
                if(validate()) {
                    hideSoftKeyboard();
                    doRegistration(1);
                }
//                Intent intentLanding=new Intent(SignupActivity.this, LandingActivity.class);
//                startActivity(intentLanding);
//                finish();
                break;

            case R.id.img_hide_show_pass:

                if(edt_pwd.getText().toString().length()>0)
                    if(Integer.parseInt(img_hide_show_pass.getTag().toString())==0){
                        edt_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        img_hide_show_pass.setTag(1);
                        img_hide_show_pass.setImageResource(R.drawable.hide_icon_inactive_reg);
                        edt_pwd.setSelection(edt_pwd.getText().toString().length());
                    }else{
                        edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_hide_show_pass.setTag(0);
                        img_hide_show_pass.setImageResource(R.drawable.hide_icon_active_reg);
                        edt_pwd.setSelection(edt_pwd.getText().toString().length());
                    }
                break;

            case R.id.edtAddress:

//               callPlacePicker();

                break;


            case R.id.btn_fb_login:

                hideSoftKeyboard();
                onFblogin();

                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if (requestCode == 007) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                LatLng l=place.getLatLng();

//                Toast.makeText(this, toastMsg+l.toString(), Toast.LENGTH_LONG).show();
                latitude=place.getLatLng().latitude+"";
                longitude=place.getLatLng().longitude+"";
                edtAddress.setText(place.getAddress());
//                place.getName()
            }
        }
        else {
            if(callbackManager!=null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
                //startLocationUpdates();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void doRegistration(int register_done) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                register(Constants.getFCMToken(this),
//                        facebookId,
                       facebookId,
                        edt_first_name.getText().toString().trim(),
                        edt_last_name.getText().toString().trim(),
                        edt_email.getText().toString().trim(),
                        edt_pwd.getText().toString().trim(),
                        edt_city.getText().toString().trim(),
//                        edtAddress.getText().toString().trim(),
                        user_type,
//                        latitude, //latitude
//                        longitude, //longitude
                        Constants.device_type,
                        getNationalityId(),register_done),"registration");

        pDialog.show();
    }

    private boolean validate() {
        Boolean validResp = true;
        if (edt_first_name.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "First Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"First Name cannot be blank!");
            edt_first_name.requestFocus();
            edt_first_name.setText("");

        }
        else if (edt_last_name.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Last name cannot be blank!");
            edt_last_name.requestFocus();
            edt_last_name.setText("");
            return validResp;
        }
        else if (edt_email.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Email cannot be blank!");
            edt_email.requestFocus();
            edt_email.setText("");
            return validResp;
        }
        else if (!emailValidator(edt_email.getText().toString().trim())) {
            validResp = false;
//            Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Enter valid email address!");
            edt_email.requestFocus();
//            edt_email.setText("");
            return validResp;
        }
        else if (user_type.equalsIgnoreCase(Constants.registration_type_nrml) &&
                edt_pwd.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Password cannot be blank!");
            edt_pwd.requestFocus();
            edt_pwd.setText("");
            return validResp;
        }
        else if (user_type.equalsIgnoreCase(Constants.registration_type_nrml) &&
                edt_pwd.getText().toString().trim().length()<5) {
            validResp = false;
//            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Password should be of atleast 5 characters!");
            edt_pwd.requestFocus();
            return validResp;
        }
        /*else if (edt_city.getText().toString().equalsIgnoreCase("")||edt_city.getText().toString().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Please enter city name!");
            edt_city.requestFocus();
            return validResp;
        }*/
        else if ((!edt_city.getText().toString().equalsIgnoreCase("")&&!edt_city.getText().toString().isEmpty())&&selected_city.length()==0) {
            validResp = false;
//            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Please enter valid city name!");
            edt_city.requestFocus();
            return validResp;
        }
        else if (edt_nationality.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Language cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Nationality cannot be blank!");
            edt_nationality.requestFocus();
            edt_nationality.setText("");
            return validResp;
        }
        else if(getNationalityId()==-1)
        {
            validResp = false;
//            Toast.makeText(this, "Language cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(this,"Please select valid nationality!");
            edt_nationality.requestFocus();
//            edt_nationality.setText("");
            return validResp;
        }
        return validResp;
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getNationality")) {
                nat_prog_bar.setVisibility(View.INVISIBLE);
                req_Obj = (NationalityRequest) response.body();
                if(!req_Obj.getResult().getError())
                    setNationalityListing(req_Obj);
                /*else
                    Constants.showAlert(this,req_Obj.getResult().getMessage());*/
//                    Toast.makeText(SignupActivity.this,"Server Response Error.. ",Toast.LENGTH_LONG).show();

            }
           else if (method.equalsIgnoreCase("getCity")) {
                city_prog_bar.setVisibility(View.INVISIBLE);
                req_cityObj = (CityRequest) response.body();
                if(!req_cityObj.getResult().getError())
//                    Log.d("Ddsad","");
                    setCity(req_cityObj);
                else {
                    selected_city="";
                }
                    //Constants.showAlert(this,req_cityObj.getResult().getMessage());
//                    Toast.makeText(SignupActivity.this,"Server Response Error.. ",Toast.LENGTH_LONG).show();

            }
            else if(method.equalsIgnoreCase("registration"))
            {
                SignupRequest signup_obj=(SignupRequest)response.body();
                if(!signup_obj.getResult().getError())
                {

                    hideSoftKeyboard();
                    // signup succesfull
                    storeUserDetails(signup_obj);
                    Intent intentLanding=new Intent(SignupActivity.this, LandingActivity.class);
                    startActivity(intentLanding);
                    finish();
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(SignupActivity.this,signup_obj.getResult().getMessage());
                }
            }

            else if(method.equalsIgnoreCase("Fbregistration"))
            {
                SignupRequest signup_obj=(SignupRequest)response.body();
                if(!signup_obj.getResult().getError())
                {
                    //now check whether data is empty. if empty means new user, proceed to step2
                    FbSignUpRequest(signup_obj);
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(SignupActivity.this,signup_obj.getResult().getMessage());
                }
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

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(this,errorMessage);

    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void FbSignUpRequest(SignupRequest signup_obj) {

        Log.d("test","");


        if(signup_obj.getResult().getData().getCreatedAt()==null || signup_obj.getResult().getData().getCreatedAt()=="" ||
                signup_obj.getResult().getData().getCreatedAt().isEmpty()) {

            //if new user
//        ==================

//            Intent intent=new Intent(this,SignupActivity.class);
//            intent.putExtra("user_type",Constants.registration_type_fb);
//            intent.putExtra("fbid","165345445546547593654454458");
//            intent.putExtra("first_name",first_name);
//            intent.putExtra("last_name",last_name);
//            intent.putExtra("email",email);
//            startActivity(intent);


            btn_fb_login.setVisibility(View.INVISIBLE);
            txt_or.setVisibility(View.INVISIBLE);
            edt_first_name.setText(first_name);
            edt_last_name.setText(last_name);
            edt_email.setText(email);
            edt_pwd.setVisibility(View.GONE);
            pwd_container.setVisibility(View.GONE);
            user_type=Constants.registration_type_fb;

        }
        else {
            //======================

            //parse data and store response in shared pref & go go dashboard

            storeFbUserDetails(signup_obj);
            Intent intentLanding=new Intent(SignupActivity.this, LandingActivity.class);
            Toast.makeText(SignupActivity.this,"Successfully Logged in..",Toast.LENGTH_LONG).show();
            startActivity(intentLanding);
            finish();
        }

    }

    private void setNationalityListing(NationalityRequest req_data)
    {
        if(al_nationality ==null)
        al_nationality =new ArrayList<>(req_data.getResult().getData().size());
        else
            al_nationality.clear();

        for(int i=0;i<req_data.getResult().getData().size();i++)
        al_nationality.add(req_data.getResult().getData().get(i).getName());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, al_nationality);

        edt_nationality.setAdapter(adapter);
        edt_nationality.setThreshold(3);
        edt_nationality.showDropDown();


    }


    private void setCity(CityRequest req_data)
    {
        if(al_city ==null)
            al_city =new ArrayList<>(req_data.getResult().getData().size());
        else
            al_city.clear();

        for(int i=0;i<req_data.getResult().getData().size();i++)
            al_city.add(req_data.getResult().getData().get(i).getName());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, al_city);

        edt_city.setAdapter(adapter);
        edt_city.setThreshold(3);
        edt_city.showDropDown();


        if(al_city.size()==0)
            selected_city="";
    }


    private void storeUserDetails(SignupRequest req)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.strShPrefUserType,user_type);
        editor.putString(Constants.strShPrefUserType,user_type);
        editor.putString(Constants.strShPrefUserLocationSet,"No");
        editor.putBoolean(Constants.strShPrefUserloginStatus, true);
        editor.putString(Constants.strShPrefUserFirstName, edt_first_name.getText().toString().trim());
        editor.putString(Constants.strShPrefUserLastName, edt_last_name.getText().toString().trim());
        editor.putString(Constants.strShPrefUserEmail, edt_email.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserZip, edtAddress.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserCity,edt_city.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserLanguage,req.getResult().getData().getLanguageId());
        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());

        editor.putString(Constants.strShPrefUserFBID,req.getResult().getData().getFacebookId());

        editor.putString(Constants.strShPrefUserNationalityID,req.getResult().getData().getNationality_id());
        editor.putString(Constants.strShPrefUserLatitude,req.getResult().getData().getLatitude());
        editor.putString(Constants.strShPrefUserLongitude,req.getResult().getData().getLongitude());

        editor.commit();

    }


    private void storeFbUserDetails(SignupRequest req)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.strShPrefUserType,Constants.registration_type_fb);
        editor.putBoolean(Constants.strShPrefUserloginStatus, true);
        editor.putString(Constants.strShPrefUserFirstName, edt_first_name.getText().toString().trim());
        editor.putString(Constants.strShPrefUserLastName, edt_last_name.getText().toString().trim());
        editor.putString(Constants.strShPrefUserEmail, edt_email.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserZip, edtAddress.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserCity,edt_city.getEditableText().toString().trim());
        editor.putString(Constants.strShPrefUserLanguage,req.getResult().getData().getLanguageId());
        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());
        editor.putString(Constants.strShPrefUserShortBio,req.getResult().getData().getShort_bio());
        editor.putString(Constants.strShPrefUserFBID,req.getResult().getData().getFacebookId());

        editor.putString(Constants.strShPrefUserNationalityID,req.getResult().getData().getNationality_id());
        editor.putString(Constants.strShPrefUserLatitude,req.getResult().getData().getLatitude());
        editor.putString(Constants.strShPrefUserLongitude,req.getResult().getData().getLongitude());

        editor.commit();

    }

    private int getNationalityId()
    {
        int nat_id=-1;

        if(al_nationality==null || al_nationality.size()==0)
            return nat_id;

       int pos= al_nationality.indexOf(edt_nationality.getText().toString());

        if(pos!=-1)
        nat_id=req_Obj.getResult().getData().get(pos).getId();

        return nat_id;

    }

    private int getCityId()
    {
        int nat_id=-1;

        if(al_city.size()==0)
            return nat_id;

        int pos= al_city.indexOf(edt_city.getText().toString());

        if(pos!=-1)
            nat_id=req_cityObj.getResult().getData().get(pos).getId();

        return nat_id;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            // single tap

           callPlacePicker();
            return true;
        } else {
            // your code for move and drag
        }

        return false;
    }



    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    private void callPlacePicker()
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        pDialog.show();

        try {
            startActivityForResult(builder.build(this), Constants.PlacePicker_Requeest);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

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
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

                        if(pDialog.isShowing())
                            pDialog.dismiss();



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
                            LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("email", "user_friends", "public_profile"));
//                            Log.v("LoginActivity", exception.getCause().toString());
                            Toast.makeText(SignupActivity.this, "Facebook Login Error. Please try again.", Toast.LENGTH_LONG).show();
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
            facebookId = object.getString("id");

//            Toast.makeText(this,object.toString(),Toast.LENGTH_LONG).show();

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + facebookId + "/picture?width=200&height=200");
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
                        facebookId,
                        first_name,
                        last_name,
                        email,
                        "",
                        "",
//                        "",
                        Constants.registration_type_fb,
//                        "", //latitude
//                        "", //longitude
                        Constants.device_type,
                        0,register_done),"Fbregistration");

        pDialog.show();
    }





   /* private void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        Intent intent = new Intent(SignupActivity.this, LocationReceiver.class);
        PendingIntent locationIntent = PendingIntent.getBroadcast(SignupActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (ActivityCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    }*/

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
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;

                Toast.makeText(SignupActivity.this, "success", Toast.LENGTH_LONG).show();
                //startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog

                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(SignupActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
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
                            status.startResolutionForResult(SignupActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            Log.e("Applicationsett",e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //Log.e("Application","Button Clicked2");
                        Toast.makeText(SignupActivity.this, "Location is Enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        latitude=location.getLatitude()+"";
        longitude=location.getLongitude()+"";

        String geo_add= getAddress(location.getLatitude(),location.getLongitude());
        Log.d("geo_addddddd",geo_add);
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


}
