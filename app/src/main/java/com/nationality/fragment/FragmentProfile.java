package com.nationality.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nationality.LoginActivity;
import com.nationality.R;
import com.nationality.adapter.MutualConectionsAdapter;
import com.nationality.model.AddUserRequest;
import com.nationality.model.DeleteMeetupRequest;
import com.nationality.model.GetChatTokenRequest;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.model.ProfileDetailsConnectionList;
import com.nationality.model.ProfileDetailsRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment implements View.OnClickListener,RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String facebookToken="";
    private String receiverId="";
    private String to_user_id="";
    MutualConectionsAdapter mutualConectionsAdapter;
    ImageView img_back,img_profile;
    View default_view;
    RestHandler rest;
    private List<ProfileDetailsConnectionList> arrAllMutualFriend;

    private OnFragmentInteractionListener mListener;

    RecyclerView my_recycler_view;

    TextView tv_header,tv_name,tv_desc,tv_tagme_hdr,tv_intrst_hdr,
            tv_lang_hdr,tv_lbl_mutual,tv_all,tv_connect;
    LinearLayout user_msg;
    TwoWayView lv_friends;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    private ArrayList<MeetupDetailsAttendeesList> arrMeetup;
    private int id=0;
    LinearLayout user_connect;
    String interest="",tag="";
    ProgressDialog pDialog;
    ProfileDetailsRequest req_obj;
    TextView tv_tag_dtls,tv_interest_dtls,tv_languages_dtls;
    private ScrollView scrollview;

    public FragmentProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(int param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
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

       View view=inflater.inflate(R.layout.fragment_profile_dtls, container, false);

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            id = bundle.getInt("id", 0);
            interest = bundle.getString("interest");
            tag = bundle.getString("tag");
        }
        arrMeetup=new ArrayList<>();
//        arrMeetup.add("1");
//        arrMeetup.add("2");

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        arrAllMutualFriend=new ArrayList<>();

        scrollview=(ScrollView)view.findViewById(R.id.scr);

        my_recycler_view=(RecyclerView)view.findViewById(R.id.my_recycler_view);

        tv_all=(TextView)view.findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);

        default_view=(View)view.findViewById(R.id.default_view);

        user_connect=(LinearLayout)view.findViewById(R.id.user_connect);
        lv_friends=(TwoWayView)view.findViewById(R.id.lv_friend_list);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_profile=(ImageView)view.findViewById(R.id.img_profile);

        tv_tag_dtls=(TextView)view.findViewById(R.id.tv_tag_dtls);
        tv_interest_dtls=(TextView)view.findViewById(R.id.tv_interest_dtls);
        tv_languages_dtls=(TextView)view.findViewById(R.id.tv_languages_dtls);
        tv_connect=(TextView)view.findViewById(R.id.tv_connect);

        user_msg=(LinearLayout)view.findViewById(R.id.user_msg);
        user_msg.setOnClickListener(this);
        //tv_header,tv_name,tv_desc,tv_tagme_hdr,tv_intrst_hdr,tv_lang_hdr,tv_lbl_mutual,tv_all;
        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_desc=(TextView)view.findViewById(R.id.tv_desc);
        tv_tagme_hdr=(TextView)view.findViewById(R.id.tv_tagme_hdr);
        tv_intrst_hdr=(TextView)view.findViewById(R.id.tv_intrst_hdr);
        tv_lang_hdr=(TextView)view.findViewById(R.id.tv_lang_hdr);
        tv_lbl_mutual=(TextView)view.findViewById(R.id.tv_lbl_mutual);
        tv_all=(TextView)view.findViewById(R.id.tv_all);

        tv_tag_dtls.setText(tag);
        tv_interest_dtls.setText(interest);

        img_back.setOnClickListener(this);

        user_connect.setOnClickListener(this);


        setFonts();

        getProfileDetails();

        return view;
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

    private void getProfileDetails()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getProfileDetails(id,Constants.getUserId(getActivity())),"get_profile_dtls");

        pDialog.show();
    }

    private void sendFriendRequest()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                userAddFriend(Constants.getUserId(getActivity()),id),"sendFriendReq");

        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("get_profile_dtls"))
            {
                req_obj=(ProfileDetailsRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    setData(req_obj);
//                  req
//                    req_obj.getResult().getData().
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),"Error while fetching data..");
                }
            }
            else if(method.equalsIgnoreCase("sendFriendReq"))
            {
                AddUserRequest req_obj=(AddUserRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                    user_connect.setBackgroundColor(Color.parseColor("#7d7d7d"));
                    tv_connect.setText("Request Sent");
                    user_connect.setOnClickListener(null);
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                }
            }
            else if(method.equalsIgnoreCase("getChatToken")) {

                GetChatTokenRequest meetup_Obj=(GetChatTokenRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    String strUniqueChatId=meetup_Obj.getResult().getData().getUniqueChatid().toString().trim();

                    Bundle arguments = new Bundle();
                    arguments.putString("uniqueChatId", strUniqueChatId);
                    arguments.putString("receiverId", receiverId);
                    arguments.putString("to_userID", to_user_id);
                    arguments.putString("show_loader", meetup_Obj.getResult().getData().getIsMessageDone());
                    FragmentChat fragment = new FragmentChat();
                    fragment.setArguments(arguments);

                    getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_MSG)
                            .addToBackStack( Constants.TAG_FRAGMENT_MSG)
                            .commit();
                }

            }

            else if(method.equalsIgnoreCase("unfriend_user"))
            {
                DeleteMeetupRequest del_req_obj=(DeleteMeetupRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    Constants.showAlert(getActivity(),del_req_obj.getResult().getMessage());
                    user_connect.setBackgroundColor(Color.parseColor("#048BCD"));
                    tv_connect.setText("Connect");
                    req_obj.getResult().getData().setConnection_status("");
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),del_req_obj.getResult().getMessage());
                }
            }
        }
        else{
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(getActivity(),response.code()+" "+response.message());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    private void setData(ProfileDetailsRequest req_obj) {


        if(req_obj.getResult().getData().getProfileAccess().equalsIgnoreCase("No")){
            scrollview.setVisibility(View.GONE);
        }
        if(req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("Accept"))//already friends


        if(req_obj.getResult().getData().getTag().equalsIgnoreCase("")||req_obj.getResult().getData().getTag().toString().isEmpty()){
            tv_tag_dtls.setText("N/A");
        }else
        tv_tag_dtls.setText(req_obj.getResult().getData().getTag());

        if(req_obj.getResult().getData().getShortBio().equalsIgnoreCase("")||req_obj.getResult().getData().getShortBio().toString().isEmpty()){
            tv_interest_dtls.setText("N/A");
        }else
        tv_interest_dtls.setText(req_obj.getResult().getData().getShortBio());

        if(req_obj.getResult().getData().getLanguage().equalsIgnoreCase("")||req_obj.getResult().getData().getLanguage().toString().isEmpty()){
            tv_languages_dtls.setText("N/A");
        }else
        tv_languages_dtls.setText(req_obj.getResult().getData().getLanguage());



        tv_name.setText(req_obj.getResult().getData().getFirstName()+" "+
        req_obj.getResult().getData().getLastName());
        tv_desc.setText(req_obj.getResult().getData().getHomeCity());

        receiverId=req_obj.getResult().getData().getFacebookId();
        to_user_id=req_obj.getResult().getData().getId().toString();
        facebookToken=req_obj.getResult().getData().getFacebookId();

        mutualConectionsAdapter =new MutualConectionsAdapter(getActivity(), req_obj.getResult().getData().getConnectionLists(),scrollview);
//        lv_friends.setAdapter(mutualConectionsAdapter);

//        lv_friends.setAdapter(mutualConectionsAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        my_recycler_view.setLayoutManager(mLayoutManager);
        my_recycler_view.setItemAnimator(new DefaultItemAnimator());
        my_recycler_view.setAdapter(mutualConectionsAdapter);

        OverScrollDecoratorHelper.setUpOverScroll(my_recycler_view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);



        arrAllMutualFriend.addAll(req_obj.getResult().getData().getConnectionLists());

        if(req_obj.getResult().getData().getConnectionLists()!=null)
        tv_lbl_mutual.setText("Connections("+req_obj.getResult().getData().getConnectionLists().size()+")");

        Constants.setImage(img_profile,req_obj.getResult().getData().getProfilePic(),getActivity());

//        Activity activity = (Activity) user_connect.getContext();
//        if(req_obj.getResult().getData().getStatus().equalsIgnoreCase("active"))
//            user_connect.setBackgroundColor(ContextCompat.getColor(activity, R.color.btn_blue_color));
//        else
//            user_connect.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_text_color));

        if(Constants.getUserId(getActivity()) != req_obj.getResult().getData().getId()) {

            if (req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("accept")) {
                user_connect.setBackgroundColor(Color.parseColor("#2CC393"));
                tv_connect.setText("Connected");
                user_connect.setOnClickListener(this);
//            user_connect.setOnClickListener(null);
            } else if (req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("pending")) {
                user_connect.setBackgroundColor(Color.parseColor("#7d7d7d"));
                tv_connect.setText("Request Sent");
                user_connect.setOnClickListener(null);
            }
            else if (req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("block")) {
                user_connect.setBackgroundColor(Color.parseColor("#E01818"));
                tv_connect.setText("Blocked");
                user_connect.setOnClickListener(null);
                user_msg.setVisibility(View.INVISIBLE);
            }

            else {
                user_connect.setBackgroundColor(Color.parseColor("#048BCD"));
                tv_connect.setText("Connect");
                user_connect.setOnClickListener(this);
            }
        }
        else{
            user_connect.setBackgroundColor(Color.parseColor("#7F007F"));
            tv_connect.setText("My Profile");
            user_msg.setVisibility(View.INVISIBLE);
            user_connect.setOnClickListener(null);
        }

        default_view.setVisibility(View.GONE);

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(getActivity(),errorMessage);

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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.user_connect:

                if(req_obj!=null && req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("accept"))
                    showAlert();
                else if(req_obj!=null && req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("") ||
                        req_obj!=null && req_obj.getResult().getData().getConnection_status().equalsIgnoreCase("reject"))
                sendFriendRequest();

                break;

            case R.id.tv_all:

                Bundle bundle=new Bundle();
//                bundle.putInt("id", id);

                bundle.putSerializable("arrAllMutualFriend", (Serializable) arrAllMutualFriend);

                FragmentSeeAllMutualConnection fragment=new FragmentSeeAllMutualConnection();

                fragment.setArguments(bundle);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment,
                        Constants.TAG_ALL_MUTUALFRIENDLIST)
                        .addToBackStack(Constants.TAG_ALL_MUTUALFRIENDLIST)
                        .commit();

                break;

            case R.id.user_msg:

                getChatToken(facebookToken);
        }
    }

    private void showAlert() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Alert");
        alertBuilder.setMessage("Are you sure you want to remove this user ?");
        alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UnfriendUser();

            }
        });

        alertBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();

    }

    private void UnfriendUser() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).unfriendUser(id,Constants.getUserId(getActivity())),"unfriend_user");

    }

    private void getChatToken(String facebookToken) {

        pDialog.show();
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String sender_id = prefs.getString(Constants.strShPrefUserFBID, "");
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getChatToken(sender_id,facebookToken, "Private"),"getChatToken");
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        //tv_header,tv_name,tv_desc,tv_tagme_hdr,tv_intrst_hdr,tv_lang_hdr,tv_lbl_mutual,tv_all;

        tv_header.setTypeface(typeFaceOpenSansBold);
        tv_name.setTypeface(typeFaceOpenSansBold);
        tv_desc.setTypeface(typeFaceOpenSansReg);
        tv_tagme_hdr.setTypeface(typeFaceOpenSansBold);
        tv_intrst_hdr.setTypeface(typeFaceOpenSansBold);
        tv_lang_hdr.setTypeface(typeFaceOpenSansBold);
        tv_lbl_mutual.setTypeface(typeFaceOpenSansReg);
        tv_all.setTypeface(typeFaceOpenSansReg);
        tv_interest_dtls.setTypeface(typeFaceOpenSansReg);
        tv_languages_dtls.setTypeface(typeFaceOpenSansReg);

        //tv_interest_dtls,tv_languages_dtls;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(true);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);
    }


}
