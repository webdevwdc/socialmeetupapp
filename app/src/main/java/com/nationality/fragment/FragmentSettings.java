package com.nationality.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nationality.R;
import com.nationality.SignupActivity;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.adapter.ConnectionLIstAdapter;
import com.nationality.adapter.InviteFbFriendsAdapter;
import com.nationality.adapter.InviteUsersAdapter;
import com.nationality.adapter.SettingsAdapter;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.LanguageRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class FragmentSettings extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static RecyclerView mDialogRecyclerView;
    private static InviteFbFriendsAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recycler_view_bulletins;


    ImageView img_back;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;
    TextView tv_header;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    static RecyclerView mRecyclerView;
    LinearLayout img_back2;

    ImageView img_create_bulletin, img_my_bulletin;

    RestHandler rest;
    ProgressDialog pDialog;


    public static List<HashMap<String, String>> fbFriendList = new ArrayList();
    public static CallbackManager callbackManager;

    public static String currentFbId = "";

    public FragmentSettings() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBulletin newInstance(String param1, String param2) {
        FragmentBulletin fragment = new FragmentBulletin();
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

        View view=inflater.inflate(R.layout.fragment_settings, container, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.mRecyclerView);

        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        if(Constants.getUserType(getActivity()).equalsIgnoreCase(Constants.registration_type_nrml))
        mDataArray= Arrays.asList(getResources().getStringArray(R.array.settings_items));
        else
            mDataArray= Arrays.asList(getResources().getStringArray(R.array.settings_items_fb));


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new SettingsAdapter(mDataArray,getActivity()));

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(typeFaceOpenSansBold);

        img_back2=(LinearLayout)view.findViewById(R.id.img_back2);
        img_back2.setOnClickListener(this);

//        ButterKnife.bind(getActivity());

        //initialiseData();
//        initialiseUI();
        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
//        getBulletinList(4);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;



            case R.id.img_back2:

                getActivity().onBackPressed();

                break;
        }

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinData")) {
                BulletinListRequest req_Obj = (BulletinListRequest) response.body();
                if(!req_Obj.getResult().getError())

                recycler_view_bulletins.setAdapter(new BulletinListAdapter(req_Obj.getResult().getData(),getActivity()));
                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();

//                getAllTeeTimes();


            }
        }
        else{
            try {
                response.errorBody().string();
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

    static ProgressDialog progressDialog;
    public static void getFbFriendList(){

        final Activity activity = (Activity)mRecyclerView.getContext();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        callbackManager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "user_friends", "public_profile"/*, "user_birthday"*/));
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
                                //Bundle bFacebookData = getFacebookData(object);

                                try {
                                    currentFbId = object.get("id").toString();
                                    JSONArray jsonArrayFriends = object.getJSONObject("friends").getJSONArray("data");
                                    String countFriends = object.getJSONObject("friends").getJSONObject("summary").get("total_count").toString();
                                    if (jsonArrayFriends!=null){
                                        for (int i=0; i<jsonArrayFriends.length();i++) {
                                            JSONObject friendlistObject = jsonArrayFriends.getJSONObject(i);
                                            String friendListID = friendlistObject.getString("id");
                                            fbFriendDtl(friendListID);
                                        }
                                        //if (fbFriendList.size()>0){
                                            dialogFbFriend(activity);
                                        //}
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location, friends"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();



                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                        Toast.makeText(activity, "You have cancelled the authentication.", Toast.LENGTH_LONG).show();

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        if(exception!=null) {
                            System.out.println("onError");
//                            Log.v("LoginActivity", exception.getCause().toString());
                            Toast.makeText(activity, "Facebook Login Error. "+exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }


    public static void getFbFriends() {
        getFbFriendList();
    }

    private static void fbFriendDtl(String friendlistId) {
        final String graphPath = "/"+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();

        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                //"/"+friendlistId+"/friends",
                "/"+friendlistId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        JSONObject object = response.getJSONObject();

                        try {

                            HashMap<String, String> map = new HashMap<>();
                            String id = "";
                            if (object.has("id")) {
                                id = object.getString("id");
                            }
                            map.put("id",id);
                            String name = "";
                            if (object.has("name")) {
                                name = object.getString("name");
                            }
                            map.put("name", name);
                            String fName = "";
                            if (object.has("first_name")) {
                                fName = object.getString("first_name");
                            }
                            map.put("first_name", fName);
                            String lName = "";
                            if (object.has("last_name")) {
                                lName = object.getString("last_name");
                            }
                            map.put("last_name", lName);
                            String mail = "";
                            if (object.has("email")) {
                                mail = object.getString("email");
                            }
                            map.put("email", mail);
                            String gender = "";
                            if (object.has("gender")) {
                                gender = object.getString("gender");
                            }
                            map.put("gender", gender);
                            if (object.has("birthday")) {
                                String bDay = object.getString("birthday");
                            }
                            if (object.has("location")) {
                                String location = object.getString("location");
                            }

                            fbFriendList.add(map);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        Bundle param = new Bundle();
        param.putString("fields", "name, id, first_name, last_name, email,gender, birthday");
        request.setParameters(param);
        request.executeAsync();


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    protected void initialiseData() {
        //Recycler view data
        mDataArray = DataHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
//        recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));

        //fastScroller.setRecyclerView(mRecyclerView);
        //fastScroller.setUpAlphabet(mAlphabetItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btm_me).setSelected(true);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

    }

    private static void dialogFbFriend(Activity activity) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_invite_fb_users);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(false);
        mDialogRecyclerView=(RecyclerView)dialog.findViewById(R.id.my_recycler_view);
        ImageView img_back_dialog=(ImageView)dialog.findViewById(R.id.img_back_dialog);

        TextView txt_done=(TextView)dialog.findViewById(R.id.txt_done);
        EditText et_search = (EditText) dialog.findViewById(R.id.et_search);

        mAdapter=new InviteFbFriendsAdapter(fbFriendList, activity);
        mDialogRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mDialogRecyclerView.setAdapter(mAdapter);

        img_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fbFriendList.clear();
            }
        });
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fbFriendList.clear();
            }
        });

        dialog.show();
    }
}
