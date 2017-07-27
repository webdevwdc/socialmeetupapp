package com.nationality.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nationality.adapter.MeetupRequestAdapterList;
import com.nationality.R;
import com.nationality.model.NewMeetupRequestDatum;
import com.nationality.model.NewMeetupRequestList;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.OnClickCallBack;

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
public class FragmentMeetupRequest extends Fragment implements View.OnClickListener,RetrofitListener,OnClickCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    ImageView img_back;
    MeetupRequestAdapterList adapterList;
    ArrayList<NewMeetupRequestDatum>all_meet;
    LinearLayoutManager mLayoutManager;
    ImageView img_filter;
    LinearLayout lin_bulletin_parent;
    RelativeLayout rel_bulletin_parent;

    RestHandler rest;

    TextView btn_my_meetup,tv_no_msg;

    TextView tv_header,tv_add_meetup;
    EditText edt_search;
    private ProgressDialog pDialog;


    public FragmentMeetupRequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMeetup.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMeetupRequest newInstance(String param1, String param2) {
        FragmentMeetupRequest fragment = new FragmentMeetupRequest();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_meetup_request, container, false);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.my_recycler_view);


        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_filter=(ImageView)view.findViewById(R.id.img_filter);

        btn_my_meetup=(TextView)view.findViewById(R.id.btn_my_meetup);
        tv_no_msg=(TextView)view.findViewById(R.id.tv_no_msg);
        tv_no_msg.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        edt_search=(EditText)view.findViewById(R.id.edt_search);

        lin_bulletin_parent=(LinearLayout)view.findViewById(R.id.lin_bulletin_parent);
        rel_bulletin_parent=(RelativeLayout)view.findViewById(R.id.rel_bulletin_parent);

        tv_header =(TextView)view.findViewById(R.id.tv_header);
        tv_add_meetup=(TextView)view.findViewById(R.id.tv_add_meetup);

        rest=new RestHandler(getActivity(),this);


        all_meet=new ArrayList<>();
        addMeetUp();
        adapterList = new MeetupRequestAdapterList(all_meet, getActivity(),false,this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mRecyclerView.setAdapter(adapterList);

        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        tv_add_meetup.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        btn_my_meetup.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        lin_bulletin_parent.setOnClickListener(this);
        btn_my_meetup.setOnClickListener(this);

        addTextListener();

        return view;
    }

    public void addMeetUp() {
//        for(int i=0;i<15;i++)
//        {
//            MeetupModelList meet=new MeetupModelList();
//            all_meet.add(meet);
//        }

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getMeetupRequestList(Constants.getUserId(getActivity())),"get_meetup_req");

        pDialog.show();

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
//                showAlertDialog();

                filterMeetup();

                break;

            case R.id.lin_bulletin_parent:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentAddMeetup(), Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .addToBackStack( Constants.TAG_FRAGMENT_ADD_MEETUP)
                        .commit();

                break;

            case R.id.btn_my_meetup:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentMyMeetup(), Constants.TAG_FRAGMENT_MY_MEETUP)
                        .addToBackStack( Constants.TAG_FRAGMENT_MY_MEETUP)
                        .commit();


                break;
        }
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


    private void filterMeetup()
    {
        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getAllFilteredMeetupList(Constants.getUserId(getActivity()),"Yes"),"filter_meetup");
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        Log.d("Ddd","");

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("get_meetup_req"))
            {
                NewMeetupRequestList req_obj=(NewMeetupRequestList)response.body();
//                req_obj.getResult().getData().get
                if(!req_obj.getResult().getError())
                {
                    showdata(req_obj);
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
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

    private void showdata(NewMeetupRequestList req_obj) {

        if(req_obj.getResult().getData()!=null)
        {
            all_meet.clear();
            all_meet.addAll(req_obj.getResult().getData());
            adapterList.notifyDataSetChanged();

            if(all_meet.size()==0)
            {
                tv_no_msg.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            else{
                tv_no_msg.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

        }
    }
    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(getActivity(),errorMessage);

    }

    @Override
    public void onUIupdate() {
        tv_no_msg.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
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

        edt_search.setText("");

    }


    public void addTextListener(){

        edt_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

               // data set changed

//                if(s!=null && s.length()>0)
//                adapterList.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}