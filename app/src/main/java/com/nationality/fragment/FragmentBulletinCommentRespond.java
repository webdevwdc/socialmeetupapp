package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.nationality.model.BulletinCommentReplyRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import retrofit2.Call;


public class FragmentBulletinCommentRespond extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageView img_back;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    private TextView txt_name, txt_msg, txt_time,tv_header;

    private EditText ext_msg;

    private Button btn_respond;

    RestHandler rest;
    ProgressDialog pDialog;

    Typeface typeFaceOpenSansReg, typeFaceOpenSansBold;

    public FragmentBulletinCommentRespond() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBulletinCommentRespond newInstance(String param1, String param2) {
        FragmentBulletinCommentRespond fragment = new FragmentBulletinCommentRespond();
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
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");


        View view=inflater.inflate(R.layout.fragment_bulletin_comment_respond, container, false);

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        txt_name=(TextView)view.findViewById(R.id.txt_name2);
        txt_name.setTypeface(typeFaceOpenSansReg);
        txt_time=(TextView)view.findViewById(R.id.txt_time);
        txt_time.setTypeface(typeFaceOpenSansReg);
        txt_msg=(TextView)view.findViewById(R.id.txt_msg);
        txt_msg.setTypeface(typeFaceOpenSansReg);
        ext_msg=(EditText)view.findViewById(R.id.ext_msg);
        ext_msg.setTypeface(typeFaceOpenSansReg);
        btn_respond=(Button)view.findViewById(R.id.btn_respond);
        btn_respond.setTypeface(typeFaceOpenSansBold);
        btn_respond.setOnClickListener(this);
//        ButterKnife.bind(getActivity());

        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        //initialiseData();
        initialiseUI();
        //rest=new RestHandler(getActivity(),this);
        //pDialog=new ProgressDialog(getActivity());
        //pDialog.setMessage(getString(R.string.dialog_msg));
        //pDialog.setCancelable(false);
        //getBulletinList(Constants.getUserId(getActivity()));

        return view;
    }

    private void getBulletinCommentReply(int bulleting_id,int user_id, String comment, String parent_bulletin_id) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinCommentReplyRequest(bulleting_id,comment,parent_bulletin_id, user_id),"bulletinCommentReplyRequest");
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

            case R.id.btn_respond:

                if(ext_msg.getText().toString().equalsIgnoreCase("")||ext_msg.getText().toString().isEmpty()){
                    Constants.showAlert(getActivity(),"Please write your comment.");
                }else{
                    getBulletinCommentReply(4,4,"Testing app for Comment and Reply", "4");
                }

                break;


        }

    }


    @Override
    public void onSuccess(Call call, retrofit2.Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("bulletinCommentReplyRequest")) {
                BulletinCommentReplyRequest req_Obj = (BulletinCommentReplyRequest) response.body();
                if(!req_Obj.getResult().getError()){
                    Toast.makeText(getActivity(),"Comment posted successfully.",Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();
            }
        }
        else{
            try {
                response.errorBody().string();
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
        //recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));

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
}
