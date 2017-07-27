package com.nationality.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.adapter.BulletinReplyListAdapter;
import com.nationality.adapter.ReplyCommentsAdapter;
import com.nationality.model.AllMeetupData;
import com.nationality.model.BulletinCommentLikeRequest;
import com.nationality.model.BulletinListData;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.BulletinReplyListRequest;
import com.nationality.model.MeetupCommentDtlsRequest;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;


public class FragmentBulletinReplyList extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COMMENT_ID = "comment_id";
    private static final String ARG_NAME = "name";
    private static final String ARG_COMMENT = "comment";
    private static final String ARG_DATE = "date";
    private static final String ARG_MEETUP_ID = "meetup_id";

    // TODO: Rename and change types of parameters
    private int mParam1=-1,meetup_id;
    private String name,comment,date;

    private OnFragmentInteractionListener mListener;

//    @Bind(R.id.my_recycler_view)
    RecyclerView mRecycler;

    View view_white;

    EditText edt_comment_box;
    ImageView img_back,btn_post;
    TextView tv_header,txt_name,txt_comment,tv_date_time,tv_likes,tv_reply;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<BulletinListData> mDataArray=new ArrayList<>();
    private List<AlphabetItem> mAlphabetItems;

    ReplyCommentsAdapter mReplyAdapter;

    RestHandler rest;
    ProgressDialog pDialog;
    int comment_id=0;
    private ImageView img_profile_pic;
    private ImageView img_like;
    private Integer like_count=0;

    BulletinReplyListRequest req_Obj;

    public FragmentBulletinReplyList() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBulletinReplyList newInstance(int comment_id, String name, String comment, String date_time, int meetup_id) {
        FragmentBulletinReplyList fragment = new FragmentBulletinReplyList();
        Bundle args = new Bundle();
        args.putInt(ARG_COMMENT_ID, comment_id);
        args.putString(ARG_NAME,name);
        args.putString(ARG_COMMENT,comment);
        args.putString(ARG_DATE,date_time);
        args.putInt(ARG_MEETUP_ID,meetup_id);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_COMMENT_ID);/*
            name=getArguments().getString(ARG_NAME);
            comment=getArguments().getString(ARG_COMMENT);
            date=getArguments().getString(ARG_DATE);
            meetup_id=getArguments().getInt(ARG_MEETUP_ID);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_bulletin_reply_list, container, false);
        init(view);

        Bundle bundle=getArguments();
        if(bundle!=null){
            comment_id=bundle.getInt("comment_id");
        }

        if(mParam1!=-1)
            getBulletinReplyList();

        return view;
    }



    /*private void postComment() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                postMeetupComment(meetup_id,Constants.getUserId(getActivity()),mParam1,
                        edt_comment_box.getText().toString().trim()),"postMeetupComment");

    }*/


    private void init(View view)
    {

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        img_profile_pic=(ImageView)view.findViewById(R.id.img_profile_pic);

        view_white=view.findViewById(R.id.view_white);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        txt_name=(TextView)view.findViewById(R.id.txt_name);
        txt_comment=(TextView)view.findViewById(R.id.txt_comment);
        tv_date_time=(TextView)view.findViewById(R.id.tv_date_time);
        tv_likes=(TextView)view.findViewById(R.id.tv_likes);
        tv_likes.setOnClickListener(this);

        img_like=(ImageView)view.findViewById(R.id.like_btn);
        img_like.setOnClickListener(this);

        tv_reply=(TextView)view.findViewById(R.id.tv_reply);

        edt_comment_box=(EditText)view.findViewById(R.id.edt_comment_box);
        btn_post=(ImageView)view.findViewById(R.id.btn_post);

        btn_post.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        mRecycler=(RecyclerView)view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        OverScrollDecoratorHelper.setUpOverScroll(mRecycler, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
//        mReplyAdapter=new ReplyCommentsAdapter(mDataArray,getActivity());
//        mRecycler.setAdapter(mReplyAdapter);

        txt_name.setText(name);
        txt_comment.setText(comment);
        tv_date_time.setText(Constants.changeDateFormat(date,Constants.web_date_format,Constants.app_display_date_format) +" at "+
        Constants.changeDateFormat(date,Constants.web_date_format,Constants.app_display_time_format));

        img_profile_pic.setOnClickListener(this);

        setFonts();
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        txt_name.setTypeface(typeFaceOpenSansBold);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_comment.setTypeface(typeFaceOpenSansReg);
        tv_date_time.setTypeface(typeFaceOpenSansReg);
        tv_likes.setTypeface(typeFaceOpenSansReg);
        tv_reply.setTypeface(typeFaceOpenSansReg);

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

            case R.id.tv_like_count:

                commentLike(comment_id);
                break;

            case R.id.like_btn:


                commentLike(comment_id);

                break;

            case R.id.img_profile_pic:


                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id", Integer.parseInt(req_Obj.getResult().getData().getUserId()));

                FragmentProfile fragment = new FragmentProfile();
                fragment.setArguments(arguments);

               getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();


                break;

//            case R.id.img_create_bulletin:
//
//                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentCreateBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                        .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                        .commit();
//
//                break;
//
//            case R.id.img_my_bulletin:
//                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_MY_BULLETIN)
//                        .addToBackStack( Constants.TAG_FRAGMENT_MY_BULLETIN)
//                        .commit();
//                break;
        }

    }

    private void getBulletinReplyList() {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getBulletinReplyList(comment_id,Constants.getUserId(getActivity())),"getBulletinReplyList");
        pDialog.show();
    }

    private void commentLike(int comment_id) {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinCommentLikeRequest(Constants.getUserId(getActivity()), "Yes", comment_id),"getBulletinCommentLike");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinReplyList")) {
                req_Obj = (BulletinReplyListRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    view_white.setVisibility(View.GONE);

                    Constants.setImage(img_profile_pic, req_Obj.getResult().getData().getProfilePic(), getActivity());
                    txt_name.setText(req_Obj.getResult().getData().getName());
                    txt_comment.setText(req_Obj.getResult().getData().getComment());
                    tv_date_time.setText(Constants.changeDateFormat(req_Obj.getResult().getData().getUpdatedAt(),
                            Constants.web_date_format,Constants.app_display_date_format)+" at "+
                            Constants.changeDateFormat(req_Obj.getResult().getData().getUpdatedAt(),
                                    Constants.web_date_format,Constants.app_display_time_format));

                    if(req_Obj.getResult().getData().getRespondLike()>1)
                        tv_likes.setText(req_Obj.getResult().getData().getRespondLike()+" Likes");
                    else
                        tv_likes.setText(req_Obj.getResult().getData().getRespondLike()+" Like");


                    like_count=req_Obj.getResult().getData().getRespondLike();

                    if(req_Obj.getResult().getData().getRespondChild().size()>1)
                        tv_reply.setText(req_Obj.getResult().getData().getRespondChild().size()+" Replies");
                    else
                        tv_reply.setText(req_Obj.getResult().getData().getRespondChild().size()+" Reply");

                    if(req_Obj.getResult().getData().getIsLike().equalsIgnoreCase(Constants.LIKE_YES)){

                        img_like.setImageResource(R.drawable.imgpsh_fullsize_grey);
                        img_like.setEnabled(false);
                        tv_likes.setEnabled(false);
                    }

                    if(req_Obj.getResult().getData().getUserId().equalsIgnoreCase(""+Constants.getUserId(getActivity()))){

                        img_like.setImageResource(R.drawable.imgpsh_fullsize_grey);
                        img_like.setEnabled(false);
                        tv_likes.setEnabled(false);
                    }
                    boolean myBulletin=false;
                    if(req_Obj.getResult().getData().getUserId().equalsIgnoreCase(""+Constants.getUserId(getActivity()))){
                       myBulletin=true;
                    }

                    mRecycler.setAdapter(new BulletinReplyListAdapter(req_Obj.getResult().getData().getRespondChild(), getActivity(), myBulletin));
                }
                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();

            } else if(method.equalsIgnoreCase("getBulletinCommentLike")){
                BulletinCommentLikeRequest req_Obj = (BulletinCommentLikeRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    img_like.setImageResource(R.drawable.imgpsh_fullsize_grey);
                    img_like.setEnabled(false);
                    like_count++;
                    if(like_count>1){
                        tv_likes.setText(like_count+" Likes");
                    }else
                        tv_likes.setText(like_count+" Like");
                    tv_likes.setEnabled(false);


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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(true);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

    }
}
