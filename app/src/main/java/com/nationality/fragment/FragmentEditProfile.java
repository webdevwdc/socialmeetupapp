package com.nationality.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.nationality.R;
import com.nationality.model.CityRequest;
import com.nationality.model.EditProfileRequest;
import com.nationality.model.LanguageData;
import com.nationality.model.LanguageRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by android on 12/5/17.
 */

public class FragmentEditProfile extends Fragment implements View.OnClickListener, RetrofitListener,
        View.OnTouchListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_CAMERA = 10;
    private static final int REQUEST_CODE_GALLERY = 20;

    public static String profileIMAGEBASE64="";

//    private Uri fileUri; // file url to store image/video
    private static final int IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 7;
    private static final String IMAGE_DIRECTORY_NAME = "Nationality";

    public static Bitmap profile_image_bmp=null;
    String gps_address="",saved_address="";
    boolean flag=true;
    Uri photoURI=null;
    byte[] image1;
    private String latitude,longitude;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Button btnFusedLocation;
    TextView tvLocation,tv_header;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;


    EditText editTextInsertBio;
    AutoCompleteTextView edt_home_city;

    ImageView img_back;
    ImageView iv_edit_image;
    static ImageView imageProfile;

    Button btn_edit_profile_finish;
    TextView tv_languages;

    ProgressBar city_prog_bar;
    CityRequest req_cityObj;
    ArrayList<String> al_nationality,al_city;

    RestHandler rest;
    ProgressDialog pDialog;

    private OnFragmentInteractionListener mListener;

    private GestureDetector gestureDetector;
    private String saved_lang;
    private String lang_txt="";

    List<LanguageData> al_mst_languages=new ArrayList<>();
    ArrayList<String> al_txt_languages = new ArrayList<>();


    public FragmentEditProfile() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentEditProfile newInstance(String param1, String param2) {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_edit_profile, container, false);

        tv_languages=(TextView)view.findViewById(R.id.tv_languages);
        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        img_back = (ImageView) view.findViewById(R.id.img_back);
        btn_edit_profile_finish = (Button) view.findViewById(R.id.btnEditProfileFinish);
//        edt_address = (EditText)view.findViewById(R.id.edt_address);
        edt_home_city = (AutoCompleteTextView) view.findViewById(R.id.edt_home_city);
//        editTextTag = (EditText)view.findViewById(R.id.editTextTag);
        //editTextLanguage = (EditText)view.findViewById(R.id.editTextLanguage);
        editTextInsertBio = (EditText)view.findViewById(R.id.editTextInsertBio);
        iv_edit_image = (ImageView) view.findViewById(R.id.iv_edit_image);
        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
        city_prog_bar=(ProgressBar)view.findViewById(R.id.city_prog_bar);
        img_back.setOnClickListener(this);
        btn_edit_profile_finish.setOnClickListener(this);
        tv_languages.setOnClickListener(this);
        rest=new RestHandler(getActivity(),this);
        iv_edit_image.setOnClickListener(this);
//        edt_address.setOnTouchListener(this);
        profileIMAGEBASE64="";

        editTextInsertBio.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextInsertBio.setSingleLine(false);

        gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setProfileDetails();
        getlanguageListing();

        return view;
    }

    private void getlanguageListing()
    {
        flag=false;
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getLanguageList(),"getLanguage");
        pDialog.show();
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


    private void callPlacePicker()
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        pDialog.show();

        try {
            startActivityForResult(builder.build(getActivity()), Constants.PlacePicker_Requeest);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    private void getCityListing(CharSequence s) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getCityList(s.toString()),"getCity");
        city_prog_bar.setVisibility(View.VISIBLE);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_image:

                insertDummyCameraWrapperPhoto();

                break;

            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.btnEditProfileFinish:

                if (chkValidation()){

                    postDataToServer();
                }

                break;

            case R.id.tv_languages:

                if(al_mst_languages.size()>0)
                    showMultiChoice();

                break;
        }

    }


    private void showMultiChoice(){

        final MaterialDialog.Builder multi_dialog=   new MaterialDialog.Builder(getActivity());
        multi_dialog.title("Select Languages")
                .items(al_txt_languages)
                .itemsCallbackMultiChoice(getSelectedIndices(), new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, final Integer[] which, CharSequence[] text) {

                        Log.e("Poly ","");
                        saveLanguageSelections(which);

                        return true;
                    }
                })

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .neutralText("Cancel")
                .positiveText("Submit")
                .cancelable(false);
//                .show();


        MaterialDialog dialog = multi_dialog.build();
//        dialog.setSelectedIndices(selected_indices);
        dialog.show();
    }

    private Integer[] getSelectedIndices()
    {

        if(saved_lang==null || saved_lang.length()==0)
            return null;

        int ndx=0;
        String[] as=saved_lang.split(",");

        Integer[] selected_indices=new Integer[as.length];

        for(int i=0;i<as.length;i++)
        {
            int y=getIndex(as[i]);
            if(y>=0) {
                selected_indices[ndx]=y;
                ndx++;
            }

        }

        return selected_indices;
    }
    private void saveLanguageSelections(Integer[] which)
    {
        lang_txt="";
        saved_lang="";

        if(which.length==0){
            tv_languages.setText("");
            saved_lang="";
        }
        else{
            for(int i=0;i<which.length;i++)
            {
                lang_txt=lang_txt+ al_mst_languages.get(which[i]).getName()+",";
                saved_lang=saved_lang+al_mst_languages.get(which[i]).getId()+",";
            }

            lang_txt=lang_txt.replaceAll(",$", "");
            saved_lang=saved_lang.replaceAll(",$", "");
            tv_languages.setText(lang_txt);
        }
    }




    private boolean chkValidation() {

       /* if (editTextCurrentCity.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Current City");
            return false;

        }else*/
      /*  if (edt_address.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Address");
            return false;

        }*/
        if (edt_home_city.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Current City");
            return false;

        }/*else if (editTextTag.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Tag");
            return false;

        }*/
       /* else if (editTextLanguage.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Language");
            return false;

        }*/else if (editTextInsertBio.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Short Bio");
            return false;

        }/*else if (profileIMAGEBASE64.equalsIgnoreCase("")){


            Constants.showAlert(getActivity(),"Select Profile Image");
            return false;


        }*/ else {

            return true;
        }
    }
    private void postDataToServer()
    {
        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                editProfile(Constants.getUserId(getActivity()),Constants.getData(getActivity(),Constants.strShPrefUserFirstName),
                        Constants.getData(getActivity(),Constants.strShPrefUserLastName),
                        edt_home_city.getText().toString(),
                        Constants.getData(getActivity(),Constants.strShPrefUserNationalityID),
                        saved_lang,
                        gps_address.length()>0 ? gps_address : saved_address,
                        latitude,longitude,"",
                        "",editTextInsertBio.getText().toString().trim(),profileIMAGEBASE64),"update_profile");

        Log.d("UpdateProfile",Constants.getUserId(getActivity())+" "+Constants.getData(getActivity(),Constants.strShPrefUserFirstName)+" "+
                Constants.getData(getActivity(),Constants.strShPrefUserLastName)+" "+
                Constants.getData(getActivity(),Constants.strShPrefUserCity)+" "+
                Constants.getData(getActivity(),Constants.strShPrefUserNationalityID)+" "+
                Constants.getData(getActivity(),saved_lang+" "+
                        " ddress"+
                        latitude+" "+longitude+" "+
                        ""+""+editTextInsertBio.getText().toString().trim()));
    }



    @Override
    public void onSuccess(Call call, Response response, String method) {
        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getCity")) {
                city_prog_bar.setVisibility(View.INVISIBLE);
                req_cityObj = (CityRequest) response.body();
                if(!req_cityObj.getResult().getError())
//                    Log.d("Ddsad","");
                    setCity(req_cityObj);
                else
                    Constants.showAlert(getActivity(),"Server Response Error.. ");
//                    Toast.makeText(SignupActivity.this,"Server Response Error.. ",Toast.LENGTH_LONG).show();

            }
            else if (method.equalsIgnoreCase("update_profile")) {

                EditProfileRequest req_cityObj = (EditProfileRequest) response.body();
                if(!req_cityObj.getResult().getError()) {
//                    Log.d("Ddsad","");

                    storeUserDetails(req_cityObj);
                    //Constants.showAlert(getActivity(), req_cityObj.getResult().getMessage());

                    showConfirmDialog(req_cityObj.getResult().getMessage());


                }
                else
                    Constants.showAlert(getActivity(),req_cityObj.getResult().getMessage());
//                    Toast.makeText(SignupActivity.this,"Server Response Error.. ",Toast.LENGTH_LONG).show();

            }
            else if(method.equalsIgnoreCase("getLanguage"))
            {
                flag=true;
                LanguageRequest langObj=(LanguageRequest)response.body();

                if(!langObj.getResult().getError())
                {

                    setLanguage(langObj.getResult().getData());
                }

            }
        }
        else{
            try {
                response.errorBody().string();
                Constants.showAlert(getActivity(),response.code()+" "+response.message());
//                label.setText(response.code()+" "+response.message());
//                showAlertDialog("Alert",response.code()+" "+response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            }
            catch(NullPointerException e){
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(getActivity(),errorMessage);
    }

    public class OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri) {
        }
    }



    private void setLanguage(List<LanguageData> langs)
    {
        al_mst_languages.addAll(langs);
        for(int i=0;i<langs.size();i++)
        {
            al_txt_languages.add(langs.get(i).getName());
        }

        setTextLangFromPref();//neeed to set saved languages from language id
    }

    private void setTextLangFromPref()
    {
        String[] as=saved_lang.split(",");

        String saved_lan_txt="";

        if(as.length>0 && saved_lang.length()>0)
        {
            for(int i=0;i<as.length;i++)
            {
                String id=as[i];

//                saved_lan_txt=al_mst_languages.get(getIndex(id)).getName();
                saved_lan_txt=saved_lan_txt+al_mst_languages.get(getIndex(id)).getName()+",";

            }
            saved_lan_txt=saved_lan_txt.replaceAll(",$", "");
            tv_languages.setText(saved_lan_txt);
        }
    }


    private int getIndex(String id)
    {
        int isSelected=-1;

        for(int i=0;i<al_mst_languages.size();i++)
        {

            String mst_id=String.valueOf(al_mst_languages.get(i).getId());
            if(mst_id.equals(id))
            {
                isSelected=i;
                break;
            }
        }
        return isSelected;
    }

    @TargetApi(23)
    private void insertDummyCameraWrapperPhoto() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(getActivity(), message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_CAMERA);
                                }
                            }
                        });
                return;
            }
//            ActivityCompat.requestPermissions(getActivity(), permissionsList.toArray(new String[permissionsList.size()]),
//                    REQUEST_CODE_CAMERA);
            return;
        }else{

            callingDialog();
        }

    }


    private void callingDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_picker);
        dialog.setCanceledOnTouchOutside(false);
        Button dialogFromGallary = (Button) dialog.findViewById(R.id.bt_photo);
        dialogFromGallary.setText("Gallery");
        dialogFromGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*boolean result = Utility.checkPermission(PostOfferActivity.this);
                if (result) {*/
                profileIMAGEBASE64="";
                shareImageFromGallery();


                dialog.dismiss();

            }
        });

        Button dialogFromCamera = (Button) dialog.findViewById(R.id.bt_library);
        dialogFromCamera.setText("Camera");
        dialogFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                profileIMAGEBASE64="";
                captureImage();
                dialog.dismiss();
            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.bt_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog_main.dismiss();
                dialog.dismiss();
                //dialog_main.dismiss();
                // quickActionPopup1.dismiss();
            }
        });

        dialog.show();
    }

    // Share image...................
    public void shareImageFromGallery() {
       /* Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMAGE);*/


        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, IMAGE);

    }
    private void captureImage() {
       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        Uri imageUri = Uri.fromFile(photo);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

            dispatchTakePictureIntent();

    }
   /* public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
        else {
            return null;
        }

        return mediaFile;
    }*/



    public static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission))
                return false;
        }
        return true;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(pDialog.isShowing())
            pDialog.dismiss();
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    float heightRatio = (float) Math.ceil(options.outHeight / (float) 200);
                    options.inSampleSize = (int) heightRatio;
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
                            options);
                    try {
                        ExifInterface exif = new ExifInterface(picturePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
                        Bitmap bitmap1 = scaleDown(bitmap, 300, true);
                        imageProfile.setImageBitmap(bitmap1);
                        try {
                            profile_image_bmp = bitmap;

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            profile_image_bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            image1 = stream.toByteArray();
                            profileIMAGEBASE64 = encodeToBase64(profile_image_bmp);
                            profileIMAGEBASE64 = Base64.encodeToString(image1, 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        // successfully captured the image
                        // display it in image view
//                    captureImage();
//                    if (checkPermission2())
                        previewCapturedImage();
                    } else
                    if (resultCode == RESULT_CANCELED) {
                        // user cancelled Image capture
                        Toast.makeText(getActivity(),
                                "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // failed to capture image

                        Toast.makeText(getActivity(),
                                "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                                .show();

                    }
                } else if (requestCode == 007) {

                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(getActivity(), data);
                        String toastMsg = String.format("Place: %s", place.getName());
                        LatLng l = place.getLatLng();

//                Toast.makeText(this, toastMsg+l.toString(), Toast.LENGTH_LONG).show();
                        latitude = place.getLatLng().latitude + "";
                        longitude = place.getLatLng().longitude + "";
//                        edt_address.setText(place.getAddress());
                    }
                }
            }
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }


    private void previewCapturedImage() {
        try {

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 4;

//            String path=photoURI.getPath();
//            Log.d("Ddd",path);

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,
                    options);
//            Toast.makeText(getApplicationContext(), " Capture image bitmap " + " " + bitmap, Toast.LENGTH_SHORT).show();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();


            // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bao);
//            byte[] ba = bao.toByteArray();
//                Constants.IMAGE_PATH=bm.toString();
            //String encodedImage = Base64.encodeToString(ba, 0);
            try {
                ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
                 Bitmap bitmap1 = scaleDown(bitmap, 300, true);
                imageProfile.setImageBitmap(bitmap);
                try {
                    profile_image_bmp = bitmap;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    profile_image_bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    image1 = stream.toByteArray();
                    profileIMAGEBASE64 = encodeToBase64(profile_image_bmp);
                    profileIMAGEBASE64 = Base64.encodeToString(image1, 0);

                    File curr_file=new File(mCurrentPhotoPath);
                    if(curr_file!=null) {
                        boolean ff = curr_file.delete();
                        Log.d("Dddd",ff+"");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void setProfileDetails()
    {

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);

        saved_address=prefs.getString(Constants.strShPrefAddress,"");

//        edt_address.setText(prefs.getString(Constants.strShPrefAddress,""));
        edt_home_city.setText(prefs.getString(Constants.strShPrefUserCity,""));
//        editTextTag.setText(prefs.getString(Constants.strShPrefUserTag,""));
        editTextInsertBio.setText(prefs.getString(Constants.strShPrefUserShortBio,""));

        latitude=prefs.getString(Constants.strShPrefUserLatitude,"");
        longitude=prefs.getString(Constants.strShPrefUserLongitude,"");
        //Constants.setImage(imageProfile,prefs.getString(Constants.strShPrefProfilePic,""),getActivity());
        Constants.setSquareImage(imageProfile,prefs.getString(Constants.strShPrefProfilePic,""),getActivity());

        saved_lang=prefs.getString(Constants.strShPrefUserLanguage,"");
//        tv_languages.setText(prefs.getString(Constants.strShPrefUserLanguageTEXTS,""));


//        else
//            spinnSelectLanguage



    }

    private void setCity(CityRequest req_data)
    {
        if(al_city ==null)
            al_city =new ArrayList<>(req_data.getResult().getData().size());
        else
            al_city.clear();

        for(int i=0;i<req_data.getResult().getData().size();i++)
            al_city.add(req_data.getResult().getData().get(i).getName());

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, al_city);

        edt_home_city.setAdapter(adapter);
        edt_home_city.setThreshold(3);
        edt_home_city.showDropDown();


    }


    private void storeUserDetails(EditProfileRequest req)
    {

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

//        editor.putString(Constants.strShUserType, "parent");
//        editor.putBoolean(Constants.strShPrefUserloginStatus, true);
//        editor.putString(Constants.strShPrefUserType, Constants.registration_type_nrml);
//        editor.putString(Constants.strShPrefUserFirstName, req.getResult().getData().getFirstName());
//        editor.putString(Constants.strShPrefUserLastName, req.getResult().getData().getLastName());
//        editor.putString(Constants.strShPrefUserEmail, req.getResult().getData().getEmail());
//        editor.putString(Constants.strShPrefUserZip,req.getResult().getData().getZipCode());
        editor.putString(Constants.strShPrefUserCity,req.getResult().getData().getHomeCity());
        editor.putString(Constants.strShPrefUserLanguage,req.getResult().getData().getLanguageId());
        editor.putString(Constants.strShPrefUserLanguageTEXTS,lang_txt);
//        editor.putInt(Constants.strShPrefUserID,req.getResult().getData().getId());


//        editor.putString(Constants.strShPrefUserNationalityID,req.getResult().getData().getNationalityId());
        editor.putString(Constants.strShPrefUserLatitude,req.getResult().getData().getLatitude());
        editor.putString(Constants.strShPrefUserLongitude,req.getResult().getData().getLongitude());

        editor.putString(Constants.strShPrefAddress,req.getResult().getData().getAddress());
        editor.putString(Constants.strShPrefUserShortBio,req.getResult().getData().getShortBio());

        editor.putString(Constants.strShPrefUserTag,req.getResult().getData().getTag());

        editor.putString(Constants.strShPrefProfilePic,req.getResult().getData().getProfilePic());
//        editor.putString(Constants.strShPrefPhoneNo,req.getResult().getData().getPhone());


        editor.commit();

    }

    //1,3
    //


    private void showConfirmDialog(final String message)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        //MainActivity.this.finish();

                        getActivity().getFragmentManager().popBackStack();
                        Fragment frag =new FragmentLanding();

                        getActivity().getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.contentContainer, frag, Constants.TAG_FRAGMENT_ME)
                                .addToBackStack(Constants.TAG_FRAGMENT_ME)
                                .commit();



                        /*Fragment fragment = getFragmentManager().findFragmentByTag(Constants.TAG_FRAGMENT_EDITPROFILE);
                        if(fragment != null) {
                            getFragmentManager().beginTransaction().remove(fragment).commit();
                        }

                        // Create new fragment and transaction
                        Fragment newFragment = new FragmentLanding();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack if needed
                        transaction.replace(R.id.contentContainer, newFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();*/

                       /* getActivity().onBackPressed();*/






                    }
                });
               /* .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();


        // show it
        alertDialog.show();
    }



    ///////////////////////////



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

       String geo_add= getAddress(location.getLatitude(),location.getLongitude());
        Log.d("geo_addddddd",geo_add);
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
        if(getActivity()!=null) {
            if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getActivity(), getActivity())) {
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "Location update started ..............: ");
            } else {
                requestPermission1(android.Manifest.permission.ACCESS_FINE_LOCATION, 5444, getActivity(), getActivity());
            }
            Log.d(TAG, "Location update started ..............: ");
        }
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

    public void requestPermission1(String strPermission, int perCode, Context _c, Activity _a) {
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
                    startLocationUpdates();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                    //splashHandlar(SPLASH_TIME_OUT);
                }
                //splashHandlar(SPLASH_TIME_OUT);
                break;

            case REQUEST_CODE_CAMERA:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    insertDummyCameraWrapperPhoto();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access Camera.", Toast.LENGTH_LONG).show();
                    //splashHandlar(SPLASH_TIME_OUT);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private String getAddress(double mLatitude,double mLongitude)
    {

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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



    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
//            ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.nationality.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }
}