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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.adapter.ReplyCommentsAdapter;
import com.nationality.model.BulletinListData;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.MeetupCommentDtlsRequest;
import com.nationality.model.MeetupCommentDtlsRespondChild;
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


public class FragmentReplyComment extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COMMENT_ID = "comment_id";
    private static final String ARG_NAME = "name";
    private static final String ARG_COMMENT = "comment";
    private static final String ARG_DATE = "date";
    private static final String ARG_MEETUP_ID = "meetup_id";
    String image_name;

    // TODO: Rename and change types of parameters
    private int mParam1=-1,meetup_id,respond_like,respond_comment;
    private String name,comment,date;

    private OnFragmentInteractionListener mListener;

//    @Bind(R.id.my_recycler_view)
    RecyclerView mRecycler;


    EditText edt_comment_box;
    ImageView img_back,btn_post,profile_pic;
    TextView tv_header,txt_name,txt_comment,tv_date_time,tv_likes,tv_reply;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    LinearLayout img_back2;

    private List<MeetupCommentDtlsRespondChild> mDataArray=new ArrayList<>();
    private List<AlphabetItem> mAlphabetItems;

    ReplyCommentsAdapter mReplyAdapter;

    RestHandler rest;
    ProgressDialog pDialog;
    private TextView tv_replying_to;
    private String user_id;
    private ImageView like_btn;
    private String is_like="";
    private Integer count=0;

    public FragmentReplyComment () {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentReplyComment newInstance(int comment_id,String name,String comment,
                                                   String date_time,int meetup_id,int respond_like,
                                                   int respond_reply,String image_name, String user_id, String is_like) {
        FragmentReplyComment fragment = new FragmentReplyComment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMMENT_ID, comment_id);
        args.putString(ARG_NAME,name);
        args.putString(ARG_COMMENT,comment);
        args.putString(ARG_DATE,date_time);
        args.putInt(ARG_MEETUP_ID,meetup_id);
        args.putInt("respond_like",respond_like);
        args.putInt("respond_reply",respond_reply);
        args.putString("image_name",image_name);
        args.putString("image_name",image_name);
        args.putString("user_id",user_id);
        args.putString("is_like",is_like);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_COMMENT_ID);
            name=getArguments().getString(ARG_NAME);
            comment=getArguments().getString(ARG_COMMENT);
            date=getArguments().getString(ARG_DATE);
            meetup_id=getArguments().getInt(ARG_MEETUP_ID);
            respond_like=getArguments().getInt("respond_like");
            respond_comment=getArguments().getInt("respond_reply");
            image_name=getArguments().getString("image_name");
            user_id=getArguments().getString("user_id");
            is_like=getArguments().getString("is_like");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_reply_comment, container, false);
        init(view);

        if(mParam1!=-1)
            getCommentDetails();

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

    private void getCommentDetails() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getMeetupCommentDetails(mParam1,Constants.getUserId(getActivity())),"getCommentDetails");
        pDialog.show();

    }

    private void postComment() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                postMeetupComment(meetup_id,Constants.getUserId(getActivity()),mParam1,
                        edt_comment_box.getText().toString().trim()),"postMeetupComment");

    }


    private void init(View view)
    {
        tv_replying_to=(TextView)view.findViewById(R.id.tv_replying_to);


        img_back2=(LinearLayout)view.findViewById(R.id.img_back2);

        like_btn=(ImageView)view.findViewById(R.id.like_btn);
        like_btn.setOnClickListener(this);
        if(user_id.equalsIgnoreCase(""+Constants.getUserId(getActivity())) ||
                is_like.equalsIgnoreCase("yes")){
            like_btn.setImageResource(R.drawable.like_grey);
            like_btn.setEnabled(false);
        }

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        txt_name=(TextView)view.findViewById(R.id.txt_name);
        txt_comment=(TextView)view.findViewById(R.id.txt_comment);
        tv_date_time=(TextView)view.findViewById(R.id.tv_date_time);
        tv_likes=(TextView)view.findViewById(R.id.tv_likes);
        tv_likes.setOnClickListener(this);
        tv_reply=(TextView)view.findViewById(R.id.tv_reply);

        profile_pic=(ImageView)view.findViewById(R.id.profile_pic);

        edt_comment_box=(EditText)view.findViewById(R.id.edt_comment_box);
        btn_post=(ImageView)view.findViewById(R.id.btn_post);

        btn_post.setOnClickListener(this);
        profile_pic.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        img_back2.setOnClickListener(this);

        mRecycler=(RecyclerView)view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        OverScrollDecoratorHelper.setUpOverScroll(mRecycler, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mReplyAdapter=new ReplyCommentsAdapter(mDataArray,getActivity());
        mRecycler.setAdapter(mReplyAdapter);

        txt_name.setText(name);
        tv_replying_to.setText("Replying to "+name);
        txt_comment.setText(comment);
        tv_date_time.setText(Constants.changeDateFormat(date,Constants.web_date_format,Constants.app_display_date_format) +" at "+
        Constants.changeDateFormat(date,Constants.web_date_format,Constants.app_display_time_format));
        if(respond_like>1)
        tv_likes.setText(String.valueOf(respond_like)+" Likes");
        else
            tv_likes.setText(String.valueOf(respond_like)+" Like");

        if(respond_comment>1)
        tv_reply.setText(String.valueOf(respond_comment)+" Replies");
        else
            tv_reply.setText(String.valueOf(respond_comment)+" Reply");

        Constants.setImage(profile_pic,image_name,getActivity());

        setFonts();
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        txt_name.setTypeface(typeFaceOpenSansBold);
        txt_name.setTypeface(typeFaceOpenSansReg);
        tv_header.setTypeface(typeFaceOpenSansBold);
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();/*
        mListener = null;*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.btn_post:

                if(edt_comment_box.getText().toString().isEmpty()){
                    Constants.showAlert(getActivity(),"Enter Comment!");
                    edt_comment_box.requestFocus();
                }else{
                postComment();
                }

                break;

            case R.id.img_back2:

                getActivity().onBackPressed();

                break;

            case R.id.tv_like:
                if(is_like.equalsIgnoreCase("no") && !user_id.equalsIgnoreCase(""+Constants.getUserId(getActivity()))) {
                    postLiketoServer(mParam1);
                    count=respond_like;

                }
                break;

            case R.id.like_btn:

                if(is_like.equalsIgnoreCase("no") && !user_id.equalsIgnoreCase(""+Constants.getUserId(getActivity()))) {
                    postLiketoServer(mParam1);
                    count=respond_like;

                }

                break;

            case R.id.profile_pic:


                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id", Integer.parseInt(user_id));

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
    private void postLiketoServer(int comment_id) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupCommentLike(Constants.getUserId(getActivity()),Constants.LIKE_YES,
                        comment_id),"likeComment");
    }


    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinData")) {
                BulletinListRequest req_Obj = (BulletinListRequest) response.body();
                if(!req_Obj.getResult().getError())

                    mRecycler.setAdapter(new BulletinListAdapter(req_Obj.getResult().getData(),getActivity()));
                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();

//                getAllTeeTimes();


            }
            else  if (method.equalsIgnoreCase("likeComment")) {

                MeetupCommentLikeRequest meetup_Obj=(MeetupCommentLikeRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    count=count+1;

                    if(count>1)
                        tv_likes.setText(String.valueOf(count)+" Likes");
                    else
                        tv_likes.setText(String.valueOf(count)+" Like");

                    like_btn.setImageResource(R.drawable.like_grey);
                    like_btn.setEnabled(false);
                }

            }
            else if(method.equalsIgnoreCase("getCommentDetails"))
            {
                MeetupCommentDtlsRequest meetObj=(MeetupCommentDtlsRequest)response.body();

                if(!meetObj.getResult().getError())
                {
                    mDataArray.clear();
                    mDataArray.addAll(meetObj.getResult().getData().getRespondChild());
                    mReplyAdapter.notifyDataSetChanged();

                }

            }
            else if(method.equalsIgnoreCase("postMeetupComment"))
            {

                MeetupCommentLikeRequest meetLikeObj=(MeetupCommentLikeRequest)response.body();

                if(!meetLikeObj.getResult().getError())
                {
                    Toast.makeText(getActivity(),"Comment posted succesfully",Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }else{
                    Toast.makeText(getActivity(),"False",Toast.LENGTH_LONG).show();
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
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(true);

    }
}
