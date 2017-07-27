package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.adapter.SeeAllMeetupFriendListAdapter;
import com.nationality.adapter.SeeAllMutualConnectionListAdapter;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.model.ProfileDetailsConnectionList;
import com.nationality.model.ProfileDetailsRequest;
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

public class FragmentSeeAllMeetupFriend extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView tv_header;

    GridView gallery_grid;


    ImageView img_back;

    String header="";

    /* private List<String> mDataArray;*/


    RestHandler rest;
    ProgressDialog pDialog;

    private int id=0;

    private List<MeetupDetailsAttendeesList> arrAllMutualFriend;


    public FragmentSeeAllMeetupFriend() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentSeeAllMeetupFriend newInstance(String param1, String param2) {
        FragmentSeeAllMeetupFriend fragment = new FragmentSeeAllMeetupFriend();
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

        View view=inflater.inflate(R.layout.fragment_all_mutual_friend, container, false);

        gallery_grid=(GridView) view.findViewById(R.id.gallery_grid);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        img_back.setOnClickListener(this);

        arrAllMutualFriend = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            /*id = bundle.getInt("id", 0);*/
            arrAllMutualFriend =  (List<MeetupDetailsAttendeesList>) bundle.getSerializable("arrMeetup");
            header =bundle.getString("header");

        }
        tv_header.setText(header);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        img_back.setOnClickListener(this);

        /*mDataArray.addAll(arrAllMutualFriend) ;*/

        //getAllUserList();

        initialiseUI();

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





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    protected void initialiseUI() {
        //gallery_grid.setLayoutManager(new LinearLayoutManager(getActivity()));
        gallery_grid.setAdapter(new SeeAllMeetupFriendListAdapter(arrAllMutualFriend,getActivity()));

        //fastScroller.setRecyclerView(mRecyclerView);
        //fastScroller.setUpAlphabet(mAlphabetItems);
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
