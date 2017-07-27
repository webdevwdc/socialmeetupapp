package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
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

import retrofit2.Call;
import retrofit2.Response;


public class FragmentBulletinMessageRespond extends Fragment implements View.OnClickListener, RetrofitListener{
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
    private TextView txt_title,txt_date, txt_time, txt_msg,tv_header;
    private EditText ext_msg;
    private Button btn_respond;

    ImageView img_create_bulletin, img_my_bulletin;

    RestHandler rest;
    ProgressDialog pDialog;
    private Typeface typeFaceOpenSansBold,typeFaceOpenSansReg;

    int bulletin_id=0, bulletin_parent_id=0;

    public FragmentBulletinMessageRespond() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentBulletinMessageRespond newInstance(String param1, String param2) {
        FragmentBulletinMessageRespond fragment = new FragmentBulletinMessageRespond();
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
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");
        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());


        View view=inflater.inflate(R.layout.fragment_bulletin_message_respond, container, false);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        txt_title=(TextView)view.findViewById(R.id.txt_title);
        txt_title.setTypeface(typeFaceOpenSansReg);
        txt_date=(TextView)view.findViewById(R.id.txt_date);
        txt_date.setTypeface(typeFaceOpenSansReg);
        txt_time=(TextView)view.findViewById(R.id.txt_time);
        txt_time.setTypeface(typeFaceOpenSansReg);
        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
//        txt_title=(TextView)view.findViewById(R.id.txt_time);
//        txt_title.setTypeface(typeFaceOpenSansReg);
        txt_msg=(TextView)view.findViewById(R.id.txt_msg);
        txt_msg.setTypeface(typeFaceOpenSansReg);
        ext_msg=(EditText)view.findViewById(R.id.ext_msg);
        ext_msg.setTypeface(typeFaceOpenSansReg);
        ext_msg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ext_msg.setSingleLine(false);
        ext_msg.setMaxLines(5);
        btn_respond=(Button)view.findViewById(R.id.btn_respond);
        btn_respond.setTypeface(typeFaceOpenSansBold);
        btn_respond.setOnClickListener(this);

        Bundle extrBundle=this.getArguments();
        if(extrBundle!=null){
            txt_title.setText(extrBundle.getString("title"));
            txt_msg.setText(extrBundle.getString("comment"));
            txt_date.setText(Constants.changeDateFormat(extrBundle.getString("date"),Constants.web_date_only_format,Constants.app_display_date_format));

            txt_time.setText(Constants.changeDateFormat(extrBundle.getString("time"),Constants.web_time_only_format,Constants.app_display_time_format));
            bulletin_id=Integer.parseInt(extrBundle.getString("bulletin_id"));
            bulletin_parent_id=extrBundle.getInt("parent_bulletin_id");
        }


//        ButterKnife.bind(getActivity());

        //initialiseData();
        initialiseUI();
        //rest=new RestHandler(getActivity(),this);
        //pDialog=new ProgressDialog(getActivity());
        //pDialog.setMessage(getString(R.string.dialog_msg));
        //pDialog.setCancelable(false);
        //getBulletinList(4);

        return view;
    }
    private void getBulletinCommentReply(int bulleting_id,int user_id, String comment, String parent_bulletin_id) {

        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
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

            case R.id.btn_respond:

                if(ext_msg.getText().toString().equalsIgnoreCase("")||ext_msg.getText().toString().isEmpty()){
                    Constants.showAlert(getActivity(),"Please write your comment.");
                }else{
                    getBulletinCommentReply(bulletin_id,Constants.getUserId(getActivity()),ext_msg.getText().toString(), ""+bulletin_parent_id);
                }

                break;

        }

    }


    @Override
    public void onSuccess(Call call, Response response, String method) {


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
