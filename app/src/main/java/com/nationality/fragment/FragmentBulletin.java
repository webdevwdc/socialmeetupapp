package com.nationality.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.model.BulletinListData;
import com.nationality.model.BulletinListRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;


public class FragmentBulletin extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<BulletinListData> li_bullletin=new ArrayList<>();

//    @Bind(R.id.my_recycler_view)
    RecyclerView recycler_view_bulletins;

    ImageView img_filter;
    BulletinListAdapter mAdapter;
    EditText edt_search;


    ImageView img_back;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    Button btn_create_bulletin, btn_my_bulletin;
    TextView txt_user,txt_topic,txt_date_time,tv_header,tv_no_msg;

    RestHandler rest;
    ProgressDialog pDialog;

    public FragmentBulletin() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBulletin newInstance(String param1, String param2) {
        FragmentBulletin fragment = new FragmentBulletin();
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

        View view=inflater.inflate(R.layout.fragment_bulletin, container, false);

        recycler_view_bulletins=(RecyclerView)view.findViewById(R.id.recycler_view_bulletins);


        tv_header=(TextView)view.findViewById(R.id.tv_header);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        tv_no_msg=(TextView)view.findViewById(R.id.tv_no_msg);
//        tv_topbar_header=(TextView)view.findViewById(R.id.tv_header);
        img_back.setOnClickListener(this);
        btn_create_bulletin=(Button)view.findViewById(R.id.btn_create_bulletin);
        btn_create_bulletin.setOnClickListener(this);
        btn_my_bulletin=(Button)view.findViewById(R.id.btn_my_bulletin);
        btn_my_bulletin.setOnClickListener(this);

        img_filter=(ImageView)view.findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        mAdapter=new BulletinListAdapter(li_bullletin,getActivity());
        recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));
        OverScrollDecoratorHelper.setUpOverScroll(recycler_view_bulletins, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        recycler_view_bulletins.setAdapter(mAdapter);

        edt_search=(EditText)view.findViewById(R.id.edt_search);

        txt_user=(TextView)view.findViewById(R.id.txt_user);
        txt_topic=(TextView)view.findViewById(R.id.txt_topic);
        txt_date_time=(TextView)view.findViewById(R.id.txt_date_time);
//        ButterKnife.bind(getActivity());

        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        tv_no_msg.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        //initialiseData();
        initialiseUI();
        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

//        tv_topbar_header.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        btn_create_bulletin.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        btn_my_bulletin.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
//        tv_topbar_header.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_user.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_topic.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_date_time.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        addTextListener();

        getBulletinList(Constants.getUserId(getActivity()));

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

    public void getBulletinList(int user_id) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getBulletinList(user_id),"getBulletinData");
        pDialog.show();
    }

    private void getFilteredBulletinList(int user_id) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getAllFilteredBulletinList(user_id,"yes"),"getFilteredBulletinData");
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

            case R.id.btn_create_bulletin:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentCreateBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                        .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                        .commit();

                break;

            case R.id.btn_my_bulletin:
                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentMyBulletin(), Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .addToBackStack(Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .commit();
                break;

            case R.id.img_filter:

                getFilteredBulletinList(Constants.getUserId(getActivity()));

                break;
        }

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinData")) {
                BulletinListRequest req_Obj = (BulletinListRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    li_bullletin.clear();
                    li_bullletin.addAll(req_Obj.getResult().getData());
                    mAdapter.notifyDataSetChanged();
//                    recycler_view_bulletins.setAdapter(new BulletinListAdapter(req_Obj.getResult().getData(), getActivity()));

//                else
//                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();

//                getAllTeeTimes();


                    if (li_bullletin.size() == 0) {
                        tv_no_msg.setVisibility(View.VISIBLE);
                        recycler_view_bulletins.setVisibility(View.GONE);
                    } else {
                        tv_no_msg.setVisibility(View.GONE);
                        recycler_view_bulletins.setVisibility(View.VISIBLE);
                    }
                }
                else{

                    tv_no_msg.setVisibility(View.VISIBLE);
                    recycler_view_bulletins.setVisibility(View.GONE);

//                    Toast.makeText(getActivity(),req_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
//                    Constants.showAlert(getActivity(),req_Obj.getResult().getMessage());
                }


            }
            else  if (method.equalsIgnoreCase("getFilteredBulletinData")) {
                BulletinListRequest req_Obj = (BulletinListRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    li_bullletin.clear();
                    li_bullletin.addAll(req_Obj.getResult().getData());
                    mAdapter.notifyDataSetChanged();
//                    recycler_view_bulletins.setAdapter(new BulletinListAdapter(req_Obj.getResult().getData(), getActivity()));
                }
                else {
                    Toast.makeText(getActivity(), req_Obj.getResult().getMessage(), Toast.LENGTH_LONG).show();
                    tv_no_msg.setVisibility(View.VISIBLE);
                    recycler_view_bulletins.setVisibility(View.GONE);

                }

//                getAllTeeTimes();


            }

            //getFilteredBulletinData
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
        mDataArray = DataHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {


        //fastScroller.setRecyclerView(mRecyclerView);
        //fastScroller.setUpAlphabet(mAlphabetItems);
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



    public void addTextListener(){

        edt_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // data set changed

//                if(s!=null && s.length()>0)
                mAdapter.getFilter().filter(s.toString());
            }
        });
    }
}
