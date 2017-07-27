package com.nationality.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.nationality.R;
import com.nationality.adapter.ImageMeetupInviteeListAdapter;
import com.nationality.adapter.InviteUsersAdapter;
import com.nationality.adapter.MyMeetupAdapterList;
import com.nationality.model.AddMeetupRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.EditMeetupRequest;
import com.nationality.model.InviteMeetupUserRequest;
import com.nationality.model.SearchUserConnectionDetails;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;


public class FragmentAddMeetup extends Fragment implements View.OnClickListener, RetrofitListener, View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GestureDetector gestureDetector;

//    @Bind(R.id.my_recycler_view)
    RecyclerView recycler_view_bulletins;
    static EditText clicked_view;
    CheckBox chk_meetup_type;

    public static String meetup_event_type="add", meetup_event_type_edited = "add";
    String meetup_privacy_type="private";

    ImageView img_back;

    //private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    Button btnCreateEvent;
    TextView txt_chk_meetup_type;

    EditText edt_address,edt_date,edt_time,edt_title,edt_description;

    RestHandler rest;
    ProgressDialog pDialog;
    private String latitude="",longitude="", address="", placename="";
    private String id="";
    private LinearLayout ll_invite;
    private RecyclerView mRecyclerView;
    private RecyclerViewFastScroller fastScroller;
    private TextView tv_header;
    private TextView txt_reqst,txt_meetup_header;

    ImageMeetupInviteeListAdapter adapter;

    private List<ConnectionListData> mDataArray;

    public static List<ConnectionListData> friendArray;
    static InviteUsersAdapter mAdapter;
    TwoWayView lv_invited_friends;

    public static String position = "";
    private EditText edt_place;


    public static ArrayList<HashMap<String, String>> friendArrayToShow = new ArrayList<HashMap<String, String>>();
    public static String key_id = "id";
    public static String key_name = "name";
    public static String key_pic = "pic_url";

    public FragmentAddMeetup() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentAddMeetup newInstance(String param1, String param2) {
        FragmentAddMeetup fragment = new FragmentAddMeetup();
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

        View view=inflater.inflate(R.layout.fragment_add_meetup, container, false);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        rest=new RestHandler(getActivity(),this);

        gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());

        btnCreateEvent=(Button)view.findViewById(R.id.btnCreateEvent);
        txt_meetup_header=(TextView)view.findViewById(R.id.txt_meetup_header);

        chk_meetup_type=(CheckBox)view.findViewById(R.id.chk_meetup_type);
        chk_meetup_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_meetup_type.isChecked())
                {
                    chk_meetup_type.setText(" Posting as public");
                }else {
                    chk_meetup_type.setText(" Posting as private");
                }
            }
        });
        txt_chk_meetup_type=(TextView)view.findViewById(R.id.txt_chk_meetup_type);
//        txt_meetup=(TextView)view.findViewById(R.id.txt_meetup);

        edt_address=(EditText)view.findViewById(R.id.edt_address);
        edt_place=(EditText)view.findViewById(R.id.edt_place);
        edt_date=(EditText)view.findViewById(R.id.edt_date);
        edt_time=(EditText)view.findViewById(R.id.edt_time);
        edt_title=(EditText)view.findViewById(R.id.edt_title);
        edt_description=(EditText)view.findViewById(R.id.edt_description);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        ll_invite = (LinearLayout) view.findViewById(R.id.ll_invite);

        lv_invited_friends=(TwoWayView)view.findViewById(R.id.lv_friend_list);

        txt_meetup_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        edt_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_title.setSingleLine(false);

        edt_description.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_description.setSingleLine(false);

        edt_place.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_place.setSingleLine(false);

        edt_address.setOnTouchListener(this);
        edt_time.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        btnCreateEvent.setOnClickListener(this);
        ll_invite.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            id = bundle.getString("meetup_id");
            meetup_privacy_type=bundle.getString("type");
            latitude= bundle.getString("latitude");  //private or public
            longitude= bundle.getString("longitude");

            meetup_event_type="edit";
            meetup_event_type_edited = meetup_event_type;

            if (bundle.containsKey("position")){
                position = bundle.getString("position");
            }

            edt_title.setText(bundle.getString("title"));
            edt_date.setText(bundle.getString("date"));
            edt_time.setText(bundle.getString("time"));
            edt_address.setText(bundle.getString("address"));
            edt_place.setText(bundle.getString("place"));
            edt_description.setText(bundle.getString("comments"));
            txt_meetup_header.setText("Edit Meetup");
            btnCreateEvent.setText("Update Meetup");

            if(meetup_privacy_type.equalsIgnoreCase("public")) {
                chk_meetup_type.setChecked(true);
                chk_meetup_type.setText(" Posting as public");
//                ll_invite.setVisibility(View.INVISIBLE);
            }
            else {
                chk_meetup_type.setChecked(false);
                chk_meetup_type.setText(" Posting as private");

            }
        } else {
            meetup_event_type = "add";
            meetup_event_type_edited = meetup_event_type;
            chk_meetup_type.setChecked(true);
        }
        friendArray=new ArrayList<>();
        adapter=new ImageMeetupInviteeListAdapter(getActivity(), meetup_event_type);//insert or update
        lv_invited_friends.setAdapter(adapter);

        addInvitedFriend(true);

        return view;
    }

    private void hideSoftKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    private void addMeetup() {

        if(chk_meetup_type.isChecked())
        {
            meetup_privacy_type=Constants.PRIVACY_PUBLIC;
        }else {
            meetup_privacy_type=Constants.PRIVACY_PRIVATE;
        }

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                insertMeetup(Constants.getUserId(getActivity()),
                        edt_title.getText().toString().trim(),
                        Constants.changeDateFormat(edt_date.getText().toString().trim()+" "+edt_time.getText().toString(),
                                Constants.app_display_date_format+" "+Constants.app_display_time_format,Constants.web_date_format),
                       edt_place.getText().toString().trim(), edt_address.getText().toString().trim(),
                        latitude,
                        longitude,
                        edt_description.getText().toString().trim(),
                        meetup_privacy_type),"add_meetups");

        pDialog.show();
    }

    private void updateMeetup() {

        if(chk_meetup_type.isChecked())
        {
            meetup_privacy_type=Constants.PRIVACY_PUBLIC;
        }else {
            meetup_privacy_type=Constants.PRIVACY_PRIVATE;
        }


        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                editMeetup(id,
                        edt_title.getText().toString().trim(),
                        Constants.changeDateFormat(edt_date.getText().toString().trim()+" "+edt_time.getText().toString(),
                                Constants.app_display_date_format+" "+Constants.app_display_time_format,Constants.web_date_format),
                        edt_address.getText().toString().trim(),edt_place.getText().toString().trim(),
                        latitude,
                        longitude,
                        edt_description.getText().toString().trim(),
                        meetup_privacy_type),"update_meetups");

        pDialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

        if(friendArray!=null)
            friendArray.clear();

        if(friendArrayToShow!=null)
            friendArrayToShow.clear();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                friendArray.clear();
                friendArrayToShow.clear();

                break;

            case R.id.edt_date:

                clicked_view=edt_date;
                hideSoftKeyBoard(edt_date);
                showDatePickerDialog();
                break;

            case R.id.edt_time:

                clicked_view=edt_time;
                hideSoftKeyBoard(edt_time);
                showTimePickerDialog();

                break;

            case R.id.btnCreateEvent:

                if(validate()) {

                    if (meetup_event_type.equalsIgnoreCase("add"))
                        addMeetup();
                    else
                        updateMeetup();
                }

                break;

            case R.id.ll_invite:


                if(edt_address.getText().toString().trim().equalsIgnoreCase("")||edt_address.getText().toString().isEmpty())
                {
                    Constants.showAlert(getActivity(), "Please select address before inviting friends.");
                }else {
                    getDialogInviteUsers();
                }

                break;

           /* case R.id.chk_meetup_type:
                if(chk_meetup_type.isChecked())
                {
                    chk_meetup_type.setText("Public");
                }else {
                    chk_meetup_type.setText("Private");
                }
                break;*/

        }

    }


    Dialog dialog;
    private void getDialogInviteUsers() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_invite_users);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(false);
        mRecyclerView=(RecyclerView)dialog.findViewById(R.id.my_recycler_view);
        fastScroller=(RecyclerViewFastScroller)dialog.findViewById(R.id.fast_scroller);
        ImageView img_back_dialog=(ImageView)dialog.findViewById(R.id.img_back_dialog);

        tv_header=(TextView)dialog.findViewById(R.id.tv_header);
        TextView txt_done=(TextView)dialog.findViewById(R.id.txt_done);
        EditText et_search = (EditText) dialog.findViewById(R.id.et_search);
        ViewGroup flowContainer = (ViewGroup) dialog.findViewById(R.id.flow_container);

        mDataArray=new ArrayList<>();

        mAdapter=new InviteUsersAdapter(mDataArray, flowContainer, getActivity(), meetup_event_type_edited);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mAdapter.getFilter().filter(s.toString());
            }
        });
        img_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetup_event_type_edited = "add";
                addInvitedFriend(false);
                showInvitedUsers();
                dialog.dismiss();
            }
        });
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetup_event_type_edited = "add";
                addInvitedFriend(false);
                showInvitedUsers();
                dialog.dismiss();
            }
        });

        getConnectionList(latitude,longitude);

        dialog.show();
    }

    private void addInvitedFriend(Boolean isFirstEdit){
        friendArrayToShow.clear();
        if (/*isFirstEdit && */meetup_event_type.equalsIgnoreCase("edit")) {
            String pos = FragmentAddMeetup.position;
            int attendee_size = 0;
            if (!pos.equalsIgnoreCase("")) {
                attendee_size = MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().size();
            }
            for (int i=0; i<attendee_size; i++){
                HashMap<String, String> mapFriend = new HashMap<String, String>();

                mapFriend.put(key_id, MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().get(i).getId().toString());
                mapFriend.put(key_name, MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().get(i).getAttendeeName());
                mapFriend.put(key_pic, MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().get(i).getAttendeePicture());

                friendArrayToShow.add(mapFriend);
            }
        }
        for (int i=0; i<friendArray.size(); i++){
            if (!getStat(i)) {
                HashMap<String, String> mapFriend = new HashMap<String, String>();

                mapFriend.put(key_id, friendArray.get(i).getId().toString());
                mapFriend.put(key_name, friendArray.get(i).getFirstName().toString() + " " + friendArray.get(i).getLastName());
                mapFriend.put(key_pic, friendArray.get(i).getProfilePic().toString());

                friendArrayToShow.add(mapFriend);
            }
        }
    }


    public boolean getStat(int pos){
        boolean isCheck = false;

        if (friendArrayToShow.size()>0){
            for (int i=0; i<friendArrayToShow.size(); i++) {
                if (friendArray.get(pos).getId().toString().equalsIgnoreCase(friendArrayToShow.get(i).get(key_id))) {
                    isCheck = true;
                    break;
                }
            }
        }
        else {
        }

        return  isCheck;
    }


    private void showInvitedUsers() {


        adapter.notifyDataSetChanged();
        //adapter=new ImageMeetupInviteeListAdapter(getActivity());
        //lv_invited_friends.setAdapter(adapter);


    }


    public static void addToFlowLayout(Context con, ViewGroup flowContainer, List<ConnectionListData> list){
        flowContainer.removeAllViews();
        for (int i=0; i<list.size(); i++){
            flowContainer.addView(createDummyTextView(con, flowContainer, list, i),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private static View createDummyTextView(Context context, final ViewGroup flowContainer, final List<ConnectionListData> list, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View child = inflater.inflate(R.layout.item_flow_layout, null);
        //View child = LayoutInflater.from(get).inflate(R.layout.item_flow_layout, null);
        TextView txt_more = (TextView) child.findViewById(R.id.txt_more);;
        txt_more.setText(list.get(position).getFirstName()+" "+list.get(position).getLastName());
        final ImageView img_view = (ImageView) child.findViewById(R.id.img_view);
        ImageView img_cross = (ImageView) child.findViewById(R.id.img_cross);
        img_cross.setId(position);
        img_cross.setTag(list.get(position).getId());
        //txt_more.setAdjustViewBounds(true);
        //img_view.setAdjustViewBounds(true);
        //layout.addView(child);

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curId = v.getId();
            }
        });

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                String tag = v.getTag().toString();
                for (int i = 0; i < friendArray.size(); i++){
                    if (friendArray.get(i).getId().toString().equalsIgnoreCase(tag)){
                        flowContainer.removeViewAt(i);
                        friendArray.remove(friendArray.get(i));
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });


        /*TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.com_facebook_button_background);*/

        return child;
    }

    private void getConnectionList(String lat, String lng)
    {
        String meetup_id = "";
        String pos = FragmentAddMeetup.position;
        if (!pos.equalsIgnoreCase("")) {
            meetup_id = MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getId().toString();
        }
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getAllConnectionList("", Constants.getUserId(getActivity()), meetup_id, lat, lng),"get_connection");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("add_meetups")) {
                AddMeetupRequest req_Obj = (AddMeetupRequest) response.body();
                if(!req_Obj.getResult().getError())
                {


//                    Constants.showAlert(getActivity(),req_Obj.getResult().get);

                    address="";
                    placename="";
                    if (friendArray.size()>0) {
                        postInviteUsers(req_Obj.getResult().getData().getId());
                    }
                    else{
//                        Toast.makeText(getActivity(),meet_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                        getActivity().getFragmentManager().popBackStack();
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                                new FragmentMyMeetup(), Constants.TAG_FRAGMENT_MY_MEETUP)
                                .addToBackStack( Constants.TAG_FRAGMENT_MY_MEETUP)
                                .commit();
                        onDetach();
                        friendArray.clear();
                        friendArrayToShow.clear();
                        meetup_event_type_edited = meetup_event_type;
                    }

                }
            }
            else if (method.equalsIgnoreCase("update_meetups")) {
                EditMeetupRequest req_Obj = (EditMeetupRequest) response.body();
                if(!req_Obj.getResult().getError())
                {
                    address="";
                    placename="";
                    if (friendArray.size()>0) {
                        postInviteUsers(req_Obj.getResult().getData().getId());
                    }else {
                        Toast.makeText(getActivity(), req_Obj.getResult().getMessage(), Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }

                }
            }

            else if(method.equalsIgnoreCase("get_connection"))
            {
                ConnectionRequest req_obj=(ConnectionRequest)response.body();
                if(!req_obj.getResult().getError())
                {
//                    showdata(req_obj);
                    ConnectionListResult conn=req_obj.getResult();

//                    if(conn.getData().size()>0 && conn.getData().get(0).getFriendRequestNumber()>1)
//                        txt_reqst_counter.setText(String.valueOf( conn.getData().get(0).getFriendRequestNumber()));
//                    else
//                        txt_reqst_counter.setVisibility(View.GONE);
//                    List<ConnectionListData> cpp=conn.getData();
//                    cpp.get()
                    mDataArray.clear();
                    mDataArray.addAll(conn.getData());
                    mAdapter.notifyDataSetChanged();


                    initialiseData();
                    //initialiseUI();


                    /*mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    //mRecyclerView.setAdapter(new InviteUsersAdapter(mDataArray, friendArray, getActivity()));
                    mRecyclerView.setAdapter(new InviteUsersAdapter(mDataArray, getActivity()));*/

                    fastScroller.setRecyclerView(mRecyclerView);
                    fastScroller.setUpAlphabet(mAlphabetItems);


                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                }
            }
            else if(method.equalsIgnoreCase("invite_meetup_users"))
            {

                InviteMeetupUserRequest meet_Obj=(InviteMeetupUserRequest)response.body();



                if(!meet_Obj.getResult().getError())
                {
                    Toast.makeText(getActivity(),meet_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            new FragmentMyMeetup(), Constants.TAG_FRAGMENT_MY_MEETUP)
                            .addToBackStack( Constants.TAG_FRAGMENT_MY_MEETUP)
                            .commit();
                    onDetach();
                    friendArray.clear();
                    friendArrayToShow.clear();
                    meetup_event_type_edited = meetup_event_type;

                }else{
                    Constants.showAlert(getActivity(),meet_Obj.getResult().getMessage());
                }

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

    protected void initialiseData() {
        //Recycler view data
//        mDataArray = DataHelper.getAlphabetData();
        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();

        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i).getFirstName();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1).toUpperCase();
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word.toUpperCase());
                mAlphabetItems.add(new AlphabetItem(i, word.toUpperCase(), false));
            }
        }
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

            Constants.showAlert(getActivity(),e.getMessage());
            pDialog.dismiss();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            pDialog.dismiss();
            Constants.showAlert(getActivity(),e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(pDialog.isShowing())
            pDialog.dismiss();

        if (requestCode == 007) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);

                String toastMsg = String.format("Place: %s", place.getName());
                LatLng l=place.getLatLng();

//                Toast.makeText(this, toastMsg+l.toString(), Toast.LENGTH_LONG).show();
                latitude=place.getLatLng().latitude+"";
                longitude=place.getLatLng().longitude+"";
                if(place.getAddress()!=null)
                address=place.getAddress().toString();


                String[] s=place.getName().toString().split(" ");
                if(place.getName().toString().contains("°")){
                    edt_address.setText(place.getAddress());
                    edt_place.setText("");
                    placename="";
                }else{
                    edt_address.setText(place.getAddress());
                    edt_place.setText(place.getName());
                    placename=place.getName().toString();
                }

            }
        }
    }


   /* public boolean latLongValidator(String latlong) {
        Pattern pattern;
        Matcher matcher;

        final String LATITUDE_PATTERN="^(\\+?(?:90(??:\\.0{1,6})?)?:[0-9]|[1-8][0-9])(??:\\.[0-9]{1,6})?))$";
        final String LONGITUDE_PATTERN="^(\\+?(?:180(??:\\.0{1,6})?)?:[0-9]|[1-9][0-9]|1[0-7][0-9])(??:\\.[0-9]{1,6})?))$";
        final String EMAIL_PATTERN = LATITUDE_PATTERN+LONGITUDE_PATTERN;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(latlong);
        return matcher.matches();
    }*/

    /*private boolean isValidLAT(String LAT) {
        String LAT_PATTERN = "([0-8][0-9]|[9][0])°' '[0-9][0-9]\\.[0-9]´' '[NS]";
        //String LONG_PATTERN = "(-?([1]?[0-7][1-9]|[1-9]?[0-9])?(\\.\\d*)?)|-?180(\\.[0]*)?";

        Pattern pattern = Pattern.compile(LAT_PATTERN);
        Matcher matcher = pattern.matcher(LAT);
        return matcher.matches();
    }*/
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

    protected void initialiseUI() {

        recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(true);

    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getChildFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            clicked_view.setText(Constants.changeDateFormat(hourOfDay+":"+minute,"HH:mm","hh:mm a"));
        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String selected_date="";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog=new DatePickerDialog(getActivity(), this, year, month, day);

            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, +1);
//            c1.set(2000, 0, 1);//Year,Mounth -1,Day
            dialog.getDatePicker().setMinDate(c1.getTimeInMillis());

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;


            Calendar newDate = Calendar.getInstance();
//            newDate.set(year, month, day);
//            Long dtSelected = newDate.getTimeInMillis();
//
//           int mYear=newDate.get(Calendar.YEAR);
//           int mMonth=newDate.get(Calendar.MONTH);
//           int mDay=newDate.get(Calendar.DAY_OF_MONTH);


            DateFormat dateFormat = new SimpleDateFormat(Constants.app_display_date_format);
            Date today = newDate.getTime();
            String todays_date = dateFormat.format(today);


//            String todays_date=mDay+"/"+mMonth+"/"+mYear;

            if(validateDate(todays_date, month+"/"+day+"/"+year ))
            {
                Log.d("date",year+month+day+"");
                clicked_view.setText(month+"/"+day+"/"+year);
                selected_date=Constants.changeDateFormat(month+"/"+day+"/"+year,Constants.app_display_date_format,Constants.web_date_format);
                Log.d("fateesssss",selected_date);
            }
            else{
                Toast.makeText(getActivity(),"Meetup date should not be past date or today's date. ",Toast.LENGTH_LONG).show();
                clicked_view.setText("");
                selected_date="";
            }




        }
    }

    private boolean validate() {
        Boolean validResp = true;
        if (edt_title.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "First Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please Enter Title..");
            edt_title.requestFocus();
            edt_title.setText("");

        } else if (edt_date.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please select date");
            edt_date.requestFocus();
            edt_date.setText("");
            return validResp;
        }
        else if (edt_time.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please select time");
            edt_time.requestFocus();
            edt_time.setText("");
            return validResp;
        }
        else if (edt_address.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please select address");
            edt_address.requestFocus();
            edt_address.setText("");
            return validResp;
        }
        else if (edt_place.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please provide meetup place");
            edt_place.requestFocus();
            edt_place.setText("");
            return validResp;
        }
        else if (edt_description.getText().toString().trim().isEmpty()) {
            validResp = false;
//            Toast.makeText(this, "Last Name cannot be blank", Toast.LENGTH_SHORT).show();
            Constants.showAlert(getActivity(), "Please provide Description..");
            edt_description.requestFocus();
            edt_description.setText("");
            return validResp;
        }

        else if (meetup_event_type.equalsIgnoreCase("add")){
            if(friendArray==null||friendArray.size()==0){
                if(!chk_meetup_type.isChecked()) {
                    Constants.showAlert(getActivity(), "Please invite some friends..");
                    validResp = false;
                }
            }
        } /*else if (meetup_event_type.equalsIgnoreCase("edit")){

            String pos = FragmentAddMeetup.position;
            if (!pos.equalsIgnoreCase("")) {
                if (MyMeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().size()>0){}
                else {
                    Constants.showAlert(getActivity(), "Please invite some friends..");
                    validResp = false;
                }
            }

        }*/

        /*else if(friendArray==null||friendArray.size()==0){
            Constants.showAlert(getActivity(), "Please invite some friends..");
            validResp = false;
        }*/
        return validResp;
    }

    private void postInviteUsers(int meetup_id)
    {

        pDialog.show();
        StringBuilder sb=new StringBuilder();

        for(int i=0;i<friendArray.size();i++)
        {
            if(i==friendArray.size()-1)
                sb.append(friendArray.get(i).getId());
            else
                sb.append(friendArray.get(i).getId()+",");
        }

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class)
                .inviteMeetupUsers(meetup_id,sb.toString()),"invite_meetup_users");


    }

    private static boolean validateDate(String todays_date,String selected_date) {
        boolean flag=false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.app_display_date_format);

//            String str1 = "9/10/2015";
            Date date1 = formatter.parse(todays_date);


//            String str2 = "10/10/2015";
            Date date2 = formatter.parse(selected_date);

            if (date1.compareTo(date2) < 0) {
                System.out.println("date2 is Greater than my date1");
                flag = true;
            }
            else
                flag=false;

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return flag;
    }
}
