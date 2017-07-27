package com.nationality.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nationality.LandingActivity;
import com.nationality.R;
import com.nationality.adapter.AttendeesListAdapter;
import com.nationality.model.JoinMeetupRequest;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.model.MeetupDetailsRequest;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.TrackAppliation;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMeetProfileDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMeetProfileDetails extends Fragment implements View.OnClickListener,RetrofitListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AttendeesListAdapter friendListAdapter;
//    TwoWayView lv_friends;
    ArrayList<MeetupDetailsAttendeesList> arrMeetup=new ArrayList<>();
//    ImageView img_back;
    LinearLayout meetup_comment,lin_edit,lin_back,meetup_like,meet_cmnt_like,meet_comment_reply;
    RestHandler rest;
    MeetupDetailsRequest meetup_Obj;
    TextView txt_meetup_title,txt_people_added,tv_all;

    ImageView ico_join_meetup,ico_un_join_meetup;
    NestedScrollView mScroll;
    RelativeLayout relative_pic;
    MeetupCommentLikeRequest meet_comt_like;
    ImageView img_profile,profile_pic,img_edit;
    String creator_name="";

    int meetup_id=0;

    View no_view;

    RecyclerView my_recycler_view;

    RelativeLayout rel_meetup_address;

    TextView txt_title_header,txt_people_added_count,txt_date,txt_time,txt_view_count,
            txt_comment_count, txt_like_count,txt_name,txt_user_location,
            txt_desc_text,txt_desc,txt_response_name,txt_latest_comment,
            txt_latest_comment_like_count,txt_latest_comment_reply_count,
            txt_latest_comment_date_time,txt_total_attendies;

    private OnFragmentInteractionListener mListener;
    private ProgressDialog pDialog;
    private RelativeLayout rel_respond_comment;
    private Integer count;
    private Integer comment_like_count;
    private String from_frag="";
    private ImageView img_msg;

    public FragmentMeetProfileDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMeetProfileDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMeetProfileDetails newInstance(String param1, String param2) {
        FragmentMeetProfileDetails fragment = new FragmentMeetProfileDetails();
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
        View view= inflater.inflate(R.layout.fragment_meet_profile_details, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
//        lv_friends=(TwoWayView)view.findViewById(R.id.lv_friend_list);

        meetup_comment=(LinearLayout)view.findViewById(R.id.meetup_comment);
        meetup_like=(LinearLayout)view.findViewById(R.id.meetup_like);

        my_recycler_view=(RecyclerView)view.findViewById(R.id.my_recycler_view);

        ico_un_join_meetup=(ImageView)view.findViewById(R.id.ico_un_join_meetup);

        no_view=(View)view.findViewById(R.id.no_view);

        txt_meetup_title=(TextView)view.findViewById(R.id.txt_meetup_title);
        ico_join_meetup=(ImageView) view.findViewById(R.id.ico_join_meetup);
        tv_all=(TextView)view.findViewById(R.id.tv_all);

        rel_meetup_address=(RelativeLayout)view.findViewById(R.id.rel_meetup_address);
        rel_respond_comment=(RelativeLayout)view.findViewById(R.id.rel_respond_comment);
        lin_back=(LinearLayout)view.findViewById(R.id.lin_back);
        lin_edit=(LinearLayout)view.findViewById(R.id.lin_edit);
        meet_cmnt_like=(LinearLayout)view.findViewById(R.id.meet_cmnt_like);
        meet_comment_reply=(LinearLayout)view.findViewById(R.id.meet_comment_reply);

        mScroll=(NestedScrollView)view.findViewById(R.id.mScroll);

        txt_people_added=(TextView)view.findViewById(R.id.txt_people_added);

        relative_pic=(RelativeLayout)view.findViewById(R.id.relative_pic);

        img_profile=(ImageView)view.findViewById(R.id.img_profile);
        profile_pic=(ImageView)view.findViewById(R.id.profile_pic);
        img_edit=(ImageView)view.findViewById(R.id.img_edit);

        txt_desc=(TextView)view.findViewById(R.id.txt_desc);
        txt_title_header=(TextView)view.findViewById(R.id.txt_title_header);
        txt_people_added_count=(TextView)view.findViewById(R.id.txt_people_added_count);
        txt_date=(TextView)view.findViewById(R.id.txt_date);
        txt_time=(TextView)view.findViewById(R.id.txt_time);
        txt_view_count=(TextView)view.findViewById(R.id.txt_view_count);
        txt_comment_count=(TextView)view.findViewById(R.id.txt_comment_count);
        txt_like_count =(TextView)view.findViewById(R.id.txt_like_count);
        txt_name=(TextView)view.findViewById(R.id.txt_name);
        txt_user_location=(TextView)view.findViewById(R.id.txt_user_location);
        img_msg=(ImageView)view.findViewById(R.id.img_msg);
//        txt_address=(TextView)view.findViewById(R.id.txt_address);
        txt_desc_text=(TextView)view.findViewById(R.id.txt_desc_text);
        txt_response_name=(TextView)view.findViewById(R.id.txt_response_name);
        txt_latest_comment=(TextView)view.findViewById(R.id.txt_latest_comment);
        txt_latest_comment_reply_count=(TextView)view.findViewById(R.id.txt_latest_comment_reply_count);
        txt_latest_comment_like_count=(TextView)view.findViewById(R.id.txt_latest_comment_like_count);
        txt_latest_comment_date_time=(TextView)view.findViewById(R.id.txt_latest_comment_date_time);
        txt_total_attendies=(TextView)view.findViewById(R.id.txt_total_attendies);

        ico_join_meetup.setOnClickListener(this);
        ico_un_join_meetup.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        profile_pic.setOnClickListener(this);

        setFonts();
        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);


        rel_meetup_address.setOnClickListener(this);
        lin_edit.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        txt_latest_comment_reply_count.setOnClickListener(this);
//        txt_user_location.setOnClickListener(this);
//        img_msg.setOnClickListener(this);

        tv_all.setOnClickListener(this);
        meet_cmnt_like.setOnClickListener(this);
        meet_comment_reply.setOnClickListener(this);


//        lv_friends.setAdapter(friendListAdapter);

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            meetup_id=bundle.getInt("meetup_id");
            from_frag=bundle.getString("fragment");
            getMeetupDetails(meetup_id);
        }

        if(from_frag!=null && from_frag.equalsIgnoreCase("Meetup_request")){
            meetup_like.setVisibility(View.GONE);
            meetup_comment.setVisibility(View.GONE);
            meet_cmnt_like.setVisibility(View.GONE);
            meet_comment_reply.setVisibility(View.GONE);
        }

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

    private void setFonts() {
//        txt_title_header,txt_people_added_count,txt_date,txt_time,txt_view_count,
//                txt_comment_count,txt_like_count,txt_name,txt_user_location,txt_address,txt_desc_text;

        txt_desc.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_title_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_people_added_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_date.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_time.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_view_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_comment_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_like_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_total_attendies.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        txt_response_name.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_latest_comment.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_latest_comment_reply_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_latest_comment_like_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_name.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_user_location.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
//        txt_address.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_desc_text.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getMeetupDetails(int meetup_id)
    {
        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getMeetupDetails(meetup_id,Constants.getUserId(getActivity())),"getMeetupDetails");
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
        switch (v.getId()){
            case R.id.img_back:
                getActivity().onBackPressed();
                break;

            case R.id.meetup_comment:

                TrackAppliation trackApp=(TrackAppliation)getActivity().getApplication();
                trackApp.setMeet_resp(meetup_Obj.getResult().getData().getMeetupRespond());

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        FragmentCommetListing.newInstance(meetup_id,creator_name), Constants.TAG_FRAGMENT_COMMENT_LISTING)
                        .addToBackStack( Constants.TAG_FRAGMENT_COMMENT_LISTING)
                        .commit();

                break;

            case R.id.lin_edit:

                FragmentAddMeetup mFragAddMeetup=  new FragmentAddMeetup();
                Bundle b=new Bundle();
                b.putString("type", meetup_Obj.getResult().getData().getType());
                b.putString("meetup_id","3");
                b.putString("place", meetup_Obj.getResult().getData().getPlace());
                b.putString("title",meetup_Obj.getResult().getData().getTitle());
                b.putString("date",Constants.changeDateFormat(meetup_Obj.getResult().getData().getDateTime(),
                        Constants.web_date_format,Constants.app_display_date_format));
                b.putString("time",Constants.changeDateFormat(meetup_Obj.getResult().getData().getDateTime(),
                        Constants.web_date_format,Constants.app_display_time_format));
                b.putString("address",meetup_Obj.getResult().getData().getLocation());
                b.putString("comments",meetup_Obj.getResult().getData().getComment());
                mFragAddMeetup.setArguments(b);


                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        mFragAddMeetup, Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .addToBackStack( Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .commit();

                break;

            case R.id.lin_back:

                getActivity().onBackPressed();

                break;

            case R.id.meetup_like:


                if(meetup_Obj!=null && meetup_Obj.getResult().getData().getIsLike().equalsIgnoreCase(Constants.LIKE_NO)
                        && meetup_Obj.getResult().getData().getIsCreator().equalsIgnoreCase("No")) {
                    count = meetup_Obj.getResult().getData().getTotalLike();
                    postLiketoServer();

                }
                break;

            case R.id.meet_cmnt_like:


                if(meetup_Obj!=null && meetup_Obj.getResult().getData().
                        getMeetupRespond().get(0).getIs_like().equalsIgnoreCase("no") &&
                        !String.valueOf(meetup_Obj.getResult().getData().getMeetupRespond().get(0).getUserId())
                                .equalsIgnoreCase(""+Constants.getUserId(getActivity()))) {
                    comment_like_count=meetup_Obj.getResult().getData().getMeetupRespond().get(0).getRespondLike();
                    likeMeetupComment();
                }

                break;

            case R.id.meet_comment_reply:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentReplyComment().newInstance(meetup_Obj.getResult().getData().getMeetupRespond().get(0).getId(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getName(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getComment(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getUpdatedAt(),
                                meetup_id,meetup_Obj.getResult().getData().getMeetupRespond().get(0).getRespondLike(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getRespondReply(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getProfilePic(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getUserId(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getIs_like()),

                        Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                        .addToBackStack( Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                        .commit();

                break;

            case R.id.txt_latest_comment_reply_count:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentReplyComment().newInstance(meetup_Obj.getResult().getData().getMeetupRespond().get(0).getId(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getName(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getComment(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getUpdatedAt(),
                                meetup_id,meetup_Obj.getResult().getData().getMeetupRespond().get(0).getRespondLike(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getRespondReply(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getProfilePic(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getUserId(),
                                meetup_Obj.getResult().getData().getMeetupRespond().get(0).getIs_like()),

                        Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                        .addToBackStack( Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                        .commit();

                break;

            case R.id.tv_all:

                Bundle bundle=new Bundle();
                bundle.putSerializable("arrMeetup", (Serializable) arrMeetup);
                bundle.putString("header", "Attendees");
                FragmentSeeAllMeetupFriend fragment=new FragmentSeeAllMeetupFriend();
                fragment.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment,
                        Constants.TAG_ALL_MEETUPFRIENDLIST)
                        .addToBackStack(Constants.TAG_ALL_MEETUPFRIENDLIST)
                        .commit();
                break;

            case R.id.txt_user_location:

                showMap();

                break;

            case R.id.rel_meetup_address:

                showMap();

                break;


            case R.id.ico_join_meetup:

                showConfirmDialog("Do you want to join this meetup?","join");

                break;

            case R.id.ico_un_join_meetup:

                showConfirmDialog("Do you want to unjoin this meetup?","unjoin");

                break;

            case R.id.img_profile:

                meetup_Obj.getResult().getData().getUserId();

                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id",meetup_Obj.getResult().getData().getUserId());

                FragmentProfile fragment1 = new FragmentProfile();
                fragment1.setArguments(arguments);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment1, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();

                break;


            case R.id.profile_pic:

                Bundle arguments1 = new Bundle();
                arguments1.putString("tag", "");
                arguments1.putString("interest", "");
                arguments1.putInt("id", Integer.parseInt(meetup_Obj.getResult().getData().
                        getMeetupRespond().get(0).getUserId()));

                FragmentProfile fragment2 = new FragmentProfile();
                fragment2.setArguments(arguments1);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment2, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();

                break;

        }
    }

    private void joinMeetup() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                joinMeetup(Constants.getUserId(getActivity()),
                        meetup_Obj.getResult().getData().getId(),"yes"),"join_meetup");
    }

    private void unjoinMeetup() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                unJoinMeetup(Constants.getUserId(getActivity()),
                        meetup_Obj.getResult().getData().getId()),"unjoin_meetup");
    }

    private void likeMeetupComment() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupCommentLike(Constants.getUserId(getActivity()),Constants.LIKE_YES,
                        meetup_Obj.getResult().getData().getMeetupRespond().get(0).getId()),"likeCommentMeetup");

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("getMeetupDetails")) {

                meetup_Obj=(MeetupDetailsRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    setData(meetup_Obj);
                }

            }
           else if (method.equalsIgnoreCase("likeMeetup")) {

                MeetupLikeRequest meetup_Obj=(MeetupLikeRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    //
                    count=count+1;
                    if(count>1)
                        txt_like_count.setText(count+" Likes");
                    else
                        txt_like_count.setText(count+" Like");
                    meetup_like.setBackgroundResource(R.drawable.grey_rounded_bg);

                    meetup_like.setEnabled(false);
                }

            }
            else if(method.equalsIgnoreCase("likeCommentMeetup"))
            {

                meet_comt_like=(MeetupCommentLikeRequest)response.body();

//                comment_like_count=comment_like_count+1;
//                if(comment_like_count>1)
                    txt_latest_comment_like_count.setText(String.valueOf(++comment_like_count));
//                else
//                    txt_latest_comment_like_count.setText(comment_like_count);

                if(!meet_comt_like.getResult().getError())
                {
                    meet_cmnt_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                    meet_cmnt_like.setEnabled(false);
                }

            }
            else if(method.equalsIgnoreCase("join_meetup"))
            {

              JoinMeetupRequest meet_comt_like=(JoinMeetupRequest)response.body();

//

                if(!meet_comt_like.getResult().getError())
                {

                    ico_join_meetup.setVisibility(View.GONE);
                    ico_un_join_meetup.setVisibility(View.VISIBLE);
                }
                Constants.showAlert(getActivity(),meet_comt_like.getResult().getMessage());
            }
            else if(method.equalsIgnoreCase("unjoin_meetup"))
            {

                JoinMeetupRequest meet_comt_like=(JoinMeetupRequest)response.body();

//

                if(!meet_comt_like.getResult().getError())
                {

                    ico_join_meetup.setVisibility(View.VISIBLE);
                    ico_un_join_meetup.setVisibility(View.GONE);
                }
                Constants.showAlert(getActivity(),meet_comt_like.getResult().getMessage());
            }
            //join_meetup unjoin_meetup
        }
        else {
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(getActivity(), response.code() + " " + response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            } catch (NullPointerException e) {
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    private void postLiketoServer() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupLikeRequest(Constants.getUserId(getActivity()),Constants.LIKE_YES,
                        meetup_id),"likeMeetup");
    }

//    private void postCommentLiketoServer() {
//
//        pDialog.show();
//        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
//                meetupCommentLike(Constants.getUserId(getActivity()),Constants.LIKE_YES,
//                        meetup_Obj.getResult().getData().getMeetupRespond().get(0).getId()),"likeMeetup");
//    }


    private void setData(MeetupDetailsRequest meetup_obj) {

//        meetup_obj.getResult().getData().getMeetupRespond()

        no_view.setVisibility(View.GONE);

        creator_name=meetup_obj.getResult().getData().getUserName();

        if(Constants.getUserId(getActivity())==meetup_obj.getResult().getData().getUserId())
            img_edit.setVisibility(View.INVISIBLE);
        else
            img_edit.setVisibility(View.VISIBLE);


        if(Constants.getUserId(getActivity())!=meetup_obj.getResult().getData().getUserId() &&
                meetup_obj.getResult().getData().getType().equalsIgnoreCase(Constants.PRIVACY_PUBLIC))
        {
            img_edit.setVisibility(View.INVISIBLE);
            if(meetup_obj.getResult().getData().getIs_join().equalsIgnoreCase("no")) {
                ico_join_meetup.setVisibility(View.VISIBLE);
                ico_un_join_meetup.setVisibility(View.GONE);
            }
            else
            {
                ico_join_meetup.setVisibility(View.GONE);
                ico_un_join_meetup.setVisibility(View.VISIBLE);
            }

        }


        meetup_like.setOnClickListener(this);
        meetup_comment.setOnClickListener(this);

        txt_meetup_title.setText(meetup_obj.getResult().getData().getTitle());

        txt_title_header.setText(/*meetup_obj.getResult().getData().getTitle()+" @ "+*/meetup_obj.getResult().getData().getPlace());
        txt_people_added_count.setText(String.valueOf(meetup_obj.getResult().getData().getInvitedUserCount()));

        if(meetup_obj.getResult().getData().getInvitedUserCount()<=1)
            txt_people_added.setText("Person is added");
        else
            txt_people_added.setText("People are added");

        if(meetup_obj.getResult().getData().getTotalView()>1)
            txt_view_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalView())+" Views");
        else
            txt_view_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalView())+" View");
        if(meetup_obj.getResult().getData().getTotalReply()>1)
            txt_comment_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalReply())+" Replies");
        else
            txt_comment_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalReply())+" Reply");

        if(meetup_obj.getResult().getData().getTotalLike()>1)

            txt_like_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalLike())+" Likes");
        else
            txt_like_count.setText(String.valueOf(meetup_obj.getResult().getData().getTotalLike())+" Like");
        txt_name.setText(meetup_obj.getResult().getData().getUserName());
        txt_user_location.setText(meetup_obj.getResult().getData().getLocation());
        txt_desc_text.setText(meetup_obj.getResult().getData().getComment());
        txt_date.setText(Constants.changeDateFormat(meetup_obj.getResult().getData().getDateTime(),
                Constants.web_date_format,Constants.app_display_date_format));
        txt_time.setText(Constants.changeDateFormat(meetup_obj.getResult().getData().getDateTime(),
                Constants.web_date_format,Constants.app_display_time_format));
//        txt_address.setText(meetup_obj.getResult().getData().getUserLocation());

        Constants.setImage(img_profile,meetup_obj.getResult().getData().getUserPhoto(),getActivity());
        if(meetup_obj.getResult().getData().getIsLike().equalsIgnoreCase(Constants.LIKE_YES)||meetup_Obj.getResult().getData().getIsCreator().equalsIgnoreCase("Yes"))
        {
            meetup_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            meetup_like.setEnabled(false);
        }
        else{
            meetup_like.setBackgroundResource(R.drawable.green_rounded_bg);
            meetup_like.setEnabled(true);
        }
        if(meetup_obj.getResult().getData().getIsCreator().equalsIgnoreCase(Constants.LIKE_YES)){
            img_edit.setVisibility(View.VISIBLE);
        }else{
            img_edit.setVisibility(View.GONE);
        }


        if(meetup_obj.getResult().getData().getMeetupRespond()!=null &&
                meetup_obj.getResult().getData().getMeetupRespond().size()>0) {

            rel_respond_comment.setVisibility(View.VISIBLE);
            if(meetup_obj.getResult().getData().getMeetupRespond().get(0).getIs_like().equalsIgnoreCase("yes")||meetup_obj.getResult().getData().getMeetupRespond().get(0).getUserId().equalsIgnoreCase(""+Constants.getUserId(getActivity())))
            {
                meet_cmnt_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            }

            txt_response_name.setText(meetup_obj.getResult().getData().getMeetupRespond().get(0).getName());
            Constants.setImage(profile_pic,meetup_obj.getResult().getData().getMeetupRespond().get(0).getProfilePic(),getActivity());

            txt_latest_comment.setText(meetup_obj.getResult().getData().getMeetupRespond().get(0).getComment());
            txt_latest_comment_like_count.setText(String.valueOf(meetup_obj.getResult().getData().getMeetupRespond().get(0).getRespondLike()));
            comment_like_count=meetup_obj.getResult().getData().getMeetupRespond().get(0).getRespondLike();
            if(meetup_obj.getResult().getData().getMeetupRespond().get(0).getRespondReply()>1)
                txt_latest_comment_reply_count.setText(String.valueOf(meetup_obj.getResult().getData().getMeetupRespond().get(0).getRespondReply())+" Replies");
            else
                txt_latest_comment_reply_count.setText(String.valueOf(meetup_obj.getResult().getData().getMeetupRespond().get(0).getRespondReply())+" Reply");
            txt_latest_comment_date_time.setText(Constants.changeDateFormat(meetup_obj.getResult().getData().getMeetupRespond().get(0).getUpdatedAt(),
                    Constants.web_date_format,Constants.app_display_date_format)+" at "+
                    Constants.changeDateFormat(meetup_obj.getResult().getData().getMeetupRespond().get(0).getUpdatedAt(),
                            Constants.web_date_format,Constants.app_display_time_format)
            );
        }
        else{
            rel_respond_comment.setVisibility(View.GONE);
        }

        if(meetup_obj.getResult().getData().getAttendeesList()!=null &&
                meetup_obj.getResult().getData().getAttendeesList().size()>0)
        {

            arrMeetup.clear();
            arrMeetup.addAll(meetup_obj.getResult().getData().getAttendeesList());
//            friendListAdapter.notifyDataSetChanged();

            friendListAdapter=new AttendeesListAdapter(getActivity(), arrMeetup,mScroll);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            my_recycler_view.setLayoutManager(mLayoutManager);
            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
            my_recycler_view.setAdapter(friendListAdapter);

            OverScrollDecoratorHelper.setUpOverScroll(my_recycler_view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

            txt_total_attendies.setText("Attendees ("+arrMeetup.size()+ ")");

            /*mScroll.fullScroll(NestedScrollView.FOCUS_UP);
            relative_pic.requestFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    relative_pic.requestFocus();
                    //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }, 2000);

            txt_name.requestFocus();*/
        }

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


    private void showMap()
            //geo:0,0?q=" + location
    {
        if(meetup_Obj.getResult().getData().getLocation()!=null &&
                meetup_Obj.getResult().getData().getLocation().length()>0){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+meetup_Obj.getResult().getData().getLocation());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
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



    private void showConfirmDialog(String msg, final String event)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        if(event.equalsIgnoreCase("join"))
                            joinMeetup();
                        else if(event.equalsIgnoreCase("unjoin"))
                            unjoinMeetup();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
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

}
