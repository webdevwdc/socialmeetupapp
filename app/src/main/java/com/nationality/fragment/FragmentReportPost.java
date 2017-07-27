package com.nationality.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.nationality.R;
import com.nationality.adapter.AttendeesListAdapter;
import com.nationality.model.AddBulletinRequest;
import com.nationality.model.EditBulletinRequest;
import com.nationality.model.ReportPostRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 26/4/17.
 */

public class FragmentReportPost extends Fragment implements View.OnClickListener,RetrofitListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_name,tv_location,tv_report,tv_private,tv_header;
    EditText et_comment;
    ImageView imgProfile;

    String user_name,location,image;

    Button btn_submit,btn_back;

    int to_user_id;

    RestHandler rest;
    ProgressDialog pDialog;

    ImageView img_back;

    private OnFragmentInteractionListener mListener;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;



    public FragmentReportPost() {
        // Required empty public constructor
    }


    public static FragmentReportPost newInstance(String param1, String param2) {
        FragmentReportPost fragment = new FragmentReportPost();
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

        View view=inflater.inflate(R.layout.fragment_report_post, container, false);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            to_user_id = bundle.getInt("to_user_id", 0);
            user_name = bundle.getString("user_name");
            location = bundle.getString("location");
            image = bundle.getString("image");

        }

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        tv_name = (TextView) view.findViewById(R.id.textViewName);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_report = (TextView) view.findViewById(R.id.textViewReport);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        tv_private = (TextView) view.findViewById(R.id.tv_private);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);

        btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        setFonts();

        tv_name.setText(user_name);
        tv_location.setText(location);
        Constants.setImage(imgProfile,image,getActivity());

        imgProfile.setOnClickListener(this);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        tv_name.setTypeface(typeFaceOpenSansReg);
        tv_report.setTypeface(typeFaceOpenSansBold);
        tv_location.setTypeface(typeFaceOpenSansReg);
        tv_private.setTypeface(typeFaceOpenSansReg);



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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();

                break;

            case R.id.btn_submit:

                if (chkValidation()){

                    reportPost();
                    if(getActivity()!=null) {
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }

                }

                break;

            case R.id.btn_back:

                getActivity().onBackPressed();

                break;


            case R.id.imgProfile:


                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id", to_user_id);

                FragmentProfile fragment = new FragmentProfile();
                fragment.setArguments(arguments);

                ((Activity) getActivity()).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();

                break;
        }
    }

    private boolean chkValidation() {

        if (et_comment.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Comment");
            return false;

        }else {

            return true;
        }
    }

    private void reportPost() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                reportPostRequest(Constants.getUserId(getActivity()),to_user_id,
                        et_comment.getText().toString().trim()),"postReport");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("postReport")) {
                ReportPostRequest req_Obj = (ReportPostRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    showAlert(getActivity(),"Report Added successfully. ");

                }

                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();



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
    public void showAlert(Context con, String msg)
    {

        if(con==null)
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        getActivity().onBackPressed();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(getActivity(),errorMessage);

    }
}