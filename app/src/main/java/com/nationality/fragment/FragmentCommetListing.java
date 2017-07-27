package com.nationality.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nationality.adapter.MeetupAllCommentListingAdapter;
import com.nationality.model.MeetupModelList;
import com.nationality.R;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.TrackAppliation;

import java.io.IOException;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMeetup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommetListing extends Fragment implements View.OnClickListener, RetrofitListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "meetup_id";
    private static final String ARG_PARAM2 = "creator_name";

    // TODO: Rename and change types of parameters
    private int meetup_id;
    private String meetup_creator_name;

    private OnFragmentInteractionListener mListener;

    EditText edt_comment_box;
    RecyclerView mRecyclerView;
    ImageView img_back;
    MeetupAllCommentListingAdapter adapterList;
    ArrayList<MeetupModelList>all_meet;
    LinearLayoutManager mLayoutManager;
    ImageView img_filter,btn_send;
//    LinearLayout lin_bulletin_parent;
//    RelativeLayout rel_bulletin_parent;

    TextView tv_header,tv_add_meetup;
    EditText edt_search;
    TrackAppliation trackApp;
    RestHandler rest;
    private ProgressDialog pDialog;
    private TextView tv_replying_to;


    public FragmentCommetListing() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentCommetListing newInstance(int meetup_id,String creator_name) {
        FragmentCommetListing fragment = new FragmentCommetListing();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, meetup_id);
        args.putString(ARG_PARAM2, creator_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meetup_id = getArguments().getInt(ARG_PARAM1);
            meetup_creator_name = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_comment_listing, container, false);

        trackApp=(TrackAppliation)getActivity().getApplication();
//        trackApp.setMeet_resp(meetup_Obj.getResult().getData().getMeetupRespond());

        tv_replying_to=(TextView)view.findViewById(R.id.tv_replying_to);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.my_recycler_view);

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_filter=(ImageView)view.findViewById(R.id.img_filter);
        btn_send=(ImageView)view.findViewById(R.id.btn_send);
        edt_comment_box=(EditText)view.findViewById(R.id.edt_comment_box);


        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest=new RestHandler(getActivity(),this);

//        lin_bulletin_parent=(LinearLayout)view.findViewById(R.id.lin_bulletin_parent);
//        rel_bulletin_parent=(RelativeLayout)view.findViewById(R.id.rel_bulletin_parent);

        tv_header =(TextView)view.findViewById(R.id.tv_header);
        tv_add_meetup=(TextView)view.findViewById(R.id.tv_add_meetup);


        all_meet=new ArrayList<>();
        addMeetUp();
        adapterList = new MeetupAllCommentListingAdapter(trackApp.getMeet_resp(), getActivity(),meetup_id);
        if(trackApp.getMeet_resp()!=null&&trackApp.getMeet_resp().size()>0){
            tv_replying_to.setText("Replying to "+trackApp.getMeet_resp().get(0).getName());
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mRecyclerView.setAdapter(adapterList);

        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
//        tv_add_meetup.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));

        img_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
//        img_filter.setOnClickListener(this);
//        lin_bulletin_parent.setOnClickListener(this);


        tv_replying_to.setText("Replying to "+meetup_creator_name);


        return view;
    }

    private void addMeetUp() {
        for(int i=0;i<15;i++)
        {
            MeetupModelList meet=new MeetupModelList();
            all_meet.add(meet);
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

            case R.id.img_filter:
                showAlertDialog();
                break;

            case R.id.lin_bulletin_parent:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentAddMeetup(), Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .addToBackStack( Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .commit();

                break;

            case R.id.btn_send:

                if(edt_comment_box.getText().toString().trim().length()>0)
                postComment();
                else
                    Constants.showAlert(getActivity(),"Please type some comment.");

                break;
        }
    }

    private void postComment() {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                postMeetupComment(meetup_id,Constants.getUserId(getActivity()),0,
                        edt_comment_box.getText().toString().trim()),"postMeetupComment");

    }

    private void showAlertDialog() {
        final Dialog dialog = new Dialog(getActivity());
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.filter_dialog);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        TextView txt_cancel=(TextView)dialog.findViewById(R.id.txt_cancel);

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Succesfully Submited",Toast.LENGTH_LONG).show();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("postMeetupComment")) {

                getActivity().onBackPressed();

            }

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
    public void onResume() {
        super.onResume();
        Log.d("Dddd","");
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(true);

    }
}
