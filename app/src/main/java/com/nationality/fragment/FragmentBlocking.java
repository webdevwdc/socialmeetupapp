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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.adapter.ConnectionLIstAdapter;
import com.nationality.adapter.ConnectionListBlockingLIstAdapter;
import com.nationality.adapter.ReportLIstAdapter;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 3/5/17.
 */

public class FragmentBlocking extends Fragment implements View.OnClickListener, RetrofitListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //    @Bind(R.id.my_recycler_view)
    EditText edt_search;
    RecyclerView recycler_view_bulletins;
    RelativeLayout rl_block_user_list;


    ImageView img_back;

    /* private List<String> mDataArray;*/
    private List<ConnectionListData> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    ImageView img_create_bulletin, img_my_bulletin;
    TextView tv_header,tv_no_message;

    RestHandler rest;
    ProgressDialog pDialog;

    RecyclerView mRecyclerView;
    ConnectionListBlockingLIstAdapter connAdapter;

    public FragmentBlocking() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBlocking newInstance(String param1, String param2) {
        FragmentBlocking fragment = new FragmentBlocking();
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

        View view=inflater.inflate(R.layout.fragment_blocking, container, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.my_recycler_view);


        tv_header=(TextView)view.findViewById(R.id.tv_header);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        rl_block_user_list = (RelativeLayout) view.findViewById(R.id.rl_block_user_list);
        edt_search = (EditText)view.findViewById(R.id.edt_search);

        tv_no_message=(TextView)view.findViewById(R.id.tv_no_message);
        tv_no_message.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));


//        ButterKnife.bind(getActivity());

        //initialiseData();
//        initialiseUI();
//        rest=new RestHandler(getActivity(),this);
//        pDialog=new ProgressDialog(getActivity());
//        pDialog.setMessage(getString(R.string.dialog_msg));
//        pDialog.setCancelable(false);
//        getBulletinList(4);


        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        mDataArray=new ArrayList<>();
        img_back.setOnClickListener(this);
        rl_block_user_list.setOnClickListener(this);

//        ButterKnife.bind(getActivity());

//        initialiseData();
//        initialiseUI();

        getConnectionList();

        edt_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {// data set changed

                if(connAdapter!=null ) {
                    if(s!=null) {
                        connAdapter.getFilter().filter(s);
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        return view;
    }




    private void getConnectionList()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getConnectionList(Constants.getUserId(getActivity())),"get_connection");
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

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.rl_block_user_list:

                (getActivity()).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentBlockUserList(), Constants.TAG_FRAGMENT_BLOCKUSERLIST)
                        .addToBackStack( Constants.TAG_FRAGMENT_BLOCKUSERLIST)
                        .commit();

                break;


        }

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("get_connection"))
            {
                ConnectionRequest req_obj=(ConnectionRequest)response.body();
                if(!req_obj.getResult().getError())
                {
//                    showdata(req_obj);
                    ConnectionListResult conn=req_obj.getResult();
//                    List<ConnectionListData> cpp=conn.getData();
//                    cpp.get()
                    mDataArray.clear();
                    mDataArray.addAll(conn.getData());
                    initialiseData();
                    initialiseUI();

                    if(mDataArray.size()==0)
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        tv_no_message.setVisibility(View.VISIBLE);
                    }
                    else{
                        mRecyclerView.setVisibility(View.VISIBLE);
                        tv_no_message.setVisibility(View.GONE);
                    }
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
//                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_message.setVisibility(View.VISIBLE);
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
//        mDataArray = DataHelper.getAlphabetData();
        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i).getFirstName();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word.toUpperCase());
                mAlphabetItems.add(new AlphabetItem(i, word.toUpperCase(), false));
            }
        }
    }

    protected void initialiseUI() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        connAdapter = new ConnectionListBlockingLIstAdapter(mDataArray,getActivity());
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mRecyclerView.setAdapter(connAdapter);

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