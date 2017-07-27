package com.nationality.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.GetPrivacySettingsRequest;
import com.nationality.model.InsertPrivacySettingsRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class FragmentPrivacy extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

//    @Bind(R.id.my_recycler_view)
    RecyclerView recycler_view_bulletins;


    ImageView img_back;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    TextView tv_header;

    ImageView img_create_bulletin, img_my_bulletin;
    RangeBar rangeBar;

    RestHandler rest;
    ProgressDialog pDialog;
    Button btnSubmit;
    private CheckBox chk_anyone_find,chk_anyone_see_my_pro_con,chk_evone_see_my_blletin;
    String evone_see_my_blletin,anyone_see_my_pro_con,anyone_find;
    private TextView txt_mileage;

    public FragmentPrivacy() {
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

        View view=inflater.inflate(R.layout.fragment_privacy_settings, container, false);

        chk_anyone_find = (CheckBox)view.findViewById(R.id.chk_anyone_find);
        chk_anyone_see_my_pro_con = (CheckBox)view.findViewById(R.id.chk_anyone_see_my_pro_con);
        chk_evone_see_my_blletin = (CheckBox)view.findViewById(R.id.chk_evone_see_my_blletin);
        rangeBar = (RangeBar) view.findViewById(R.id.rangebar);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        //rangeBar.setTickEnd(25);
        rangeBar.setPinRadius(50);
        //rangeBar.setRangePinsByIndices(22, 43);
        rangeBar.setSeekPinByIndex(50);
        txt_mileage = (TextView) view.findViewById(R.id.txt_mileage);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        img_back = (ImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                txt_mileage.setText(rightPinValue);
            }
        });



        getPrivacySettings();

//        ButterKnife.bind(getActivity());

        //initialiseData();

//        getBulletinList(4);

        return view;
    }

    private void getBulletinList(int user_id) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getBulletinList(user_id),"getBulletinData");
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.btn_create_bulletin:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentCreateBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                        .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                        .commit();

                break;

            case R.id.btn_my_bulletin:
                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .addToBackStack( Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .commit();
                break;

            case R.id.btnSubmit:



                if (chk_anyone_find.isChecked()){
                    anyone_find = "Yes";
                }else {
                    anyone_find = "No";
                }

                if (chk_anyone_see_my_pro_con.isChecked()){

                    anyone_see_my_pro_con = "Yes";
                }else {
                    anyone_see_my_pro_con ="No";
                }

                if (chk_evone_see_my_blletin.isChecked()){
                    evone_see_my_blletin ="Yes";
                }else {
                    evone_see_my_blletin = "No";
                }

                //int leftPin = rangeBar.getLeftIndex();
                //String leftVal = rangeBar.getLeftPinValue();
                String rightVal = rangeBar.getRightPinValue();

                submitPrivacySettings(anyone_find,anyone_see_my_pro_con,evone_see_my_blletin, rightVal);
        }

    }

    private void getPrivacySettings() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getPrivacySettings(Constants.getUserId(getActivity())),"getPrivacySettingsData");
        pDialog.show();
    }


    private void submitPrivacySettings(String anyone_find, String anyone_see_my_pro_con, String evone_see_my_blletin, String rightVal) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                insertPrivacySettings(Constants.getUserId(getActivity()),anyone_find,anyone_see_my_pro_con,
                        evone_see_my_blletin, rightVal),"insertPrivacySettings");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinData")) {
                BulletinListRequest req_Obj = (BulletinListRequest) response.body();
                if(!req_Obj.getResult().getError())

                recycler_view_bulletins.setAdapter(new BulletinListAdapter(req_Obj.getResult().getData(),getActivity()));
                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();

//                getAllTeeTimes();


            }else if (method.equalsIgnoreCase("insertPrivacySettings")) {
                InsertPrivacySettingsRequest req_Obj = (InsertPrivacySettingsRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    Toast.makeText(getActivity(), "Submitted successfully", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }

                else {
                    Toast.makeText(getActivity(), "Server Response Error.. ", Toast.LENGTH_LONG).show();
                }

//                getAllTeeTimes();


            }else if (method.equalsIgnoreCase("getPrivacySettingsData")) {
                GetPrivacySettingsRequest req_Obj = (GetPrivacySettingsRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    if (req_Obj.getResult().getData().getIsAnyoneFindMe().equalsIgnoreCase("Yes")){

                        chk_anyone_find.setChecked(true);
                    }

                    if (req_Obj.getResult().getData().getIsAnyoneSeeMyProfile().equalsIgnoreCase("Yes")){
                        chk_anyone_see_my_pro_con.setChecked(true);
                    }

                    if (req_Obj.getResult().getData().getIsEveryoneSeeMyBulletin().equalsIgnoreCase("Yes")){
                        chk_evone_see_my_blletin.setChecked(true);
                    }
                    rangeBar.setSeekPinByIndex(Integer.parseInt(req_Obj.getResult().getData().getMile()));
                    txt_mileage.setText(req_Obj.getResult().getData().getMile());
                }



                else {
                    //Toast.makeText(getActivity(), "Server Response Error.. ", Toast.LENGTH_LONG).show();

                    chk_anyone_find.setChecked(true);
                    chk_anyone_see_my_pro_con.setChecked(true);
                    chk_evone_see_my_blletin.setChecked(true);
                }

//                getAllTeeTimes();


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
        recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));

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



