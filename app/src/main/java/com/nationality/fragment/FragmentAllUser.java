package com.nationality.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.AllUserListAdapter;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.adapter.ConnectionLIstAdapter;
import com.nationality.adapter.ReportLIstAdapter;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.SearchUserConnectionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 5/5/17.
 */

public class FragmentAllUser extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    GridView gallery_grid;


    ImageView img_back;
    TextView tv_header,tv_no_msg;

    /* private List<String> mDataArray;*/
    private List<ConnectionListData> mDataArray;


    RestHandler rest;
    ProgressDialog pDialog;


    public FragmentAllUser() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentAllUser newInstance(String param1, String param2) {
        FragmentAllUser fragment = new FragmentAllUser();
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

        View view=inflater.inflate(R.layout.fragment_all_user, container, false);

        tv_no_msg=(TextView)view.findViewById(R.id.tv_no_msg);
        tv_header=(TextView)view.findViewById(R.id.tv_header);
        gallery_grid=(GridView) view.findViewById(R.id.gallery_grid);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);


        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        tv_no_msg.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        mDataArray=new ArrayList<>();
        img_back.setOnClickListener(this);

        getAllUserList();

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
        }

    }

    private void getAllUserList()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getConnectionList(Constants.getUserId(getActivity())),"get_all_user");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("get_all_user"))
            {
                ConnectionRequest req_obj=(ConnectionRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    ConnectionListResult conn=req_obj.getResult();


                    mDataArray.clear();
                    mDataArray.addAll(conn.getData());
                    initialiseUI();

                    if(mDataArray.size()==0) {
                        tv_no_msg.setVisibility(View.VISIBLE);
                        gallery_grid.setVisibility(View.GONE);
                    }
                }
                else{

                    tv_no_msg.setVisibility(View.VISIBLE);
                    gallery_grid.setVisibility(View.GONE);
//                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
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
                e.printStackTrace();
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

    protected void initialiseUI() {
        //gallery_grid.setLayoutManager(new LinearLayoutManager(getActivity()));
        gallery_grid.setAdapter(new AllUserListAdapter(mDataArray,getActivity()));

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
}
