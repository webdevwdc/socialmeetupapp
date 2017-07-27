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

import com.nationality.R;
import com.nationality.model.ChangePasswordRequest;
import com.nationality.model.ReportPostRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 26/4/17.
 */

public class FragmentChangePassword extends Fragment implements View.OnClickListener,RetrofitListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText et_old_password,et_new_password,et_confirm_password;
    ImageView img_hide_show_pass_old,img_hide_show_pass_new,img_hide_show_pass_confirm;

    TextView tv_header;

    Button btn_update;

    RestHandler rest;
    ProgressDialog pDialog;

    ImageView img_back;

    private OnFragmentInteractionListener mListener;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;



    public FragmentChangePassword() {
        // Required empty public constructor
    }


    public static FragmentChangePassword newInstance(String param1, String param2) {
        FragmentChangePassword fragment = new FragmentChangePassword();
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

        View view=inflater.inflate(R.layout.fragment_change_password, container, false);

        rest=new RestHandler(getActivity(),this);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        btn_update = (Button) view.findViewById(R.id.btn_update);
        et_old_password = (EditText) view.findViewById(R.id.et_old_password);
        et_new_password = (EditText) view.findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) view.findViewById(R.id.et_confirm_password);
        img_hide_show_pass_old=(ImageView)view.findViewById(R.id.img_hide_show_pass_old);
        img_hide_show_pass_new=(ImageView)view.findViewById(R.id.img_hide_show_pass_new);
        img_hide_show_pass_confirm=(ImageView)view.findViewById(R.id.img_hide_show_pass_confirm);

        img_hide_show_pass_old.setOnClickListener(this);
        img_hide_show_pass_new.setOnClickListener(this);
        img_hide_show_pass_confirm.setOnClickListener(this);

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        setFonts();



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

            case R.id.btn_update:

                if (chkValidation()){

                    changePassword();

                }

                break;

            case R.id.img_hide_show_pass_old:

                if(et_old_password.getText().toString().length()>0)
                    if(Integer.parseInt(img_hide_show_pass_old.getTag().toString())==0){
                        et_old_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        img_hide_show_pass_old.setTag(1);
                        img_hide_show_pass_old.setImageResource(R.drawable.hide_icon_inactive_reg);
                        et_old_password.setSelection(et_old_password.getText().toString().length());
                    }else{
                        et_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_hide_show_pass_old.setTag(0);
                        img_hide_show_pass_old.setImageResource(R.drawable.hide_icon_active_reg);
                        et_old_password.setSelection(et_old_password.getText().toString().length());
                    }
                break;

            case R.id.img_hide_show_pass_new:

                if(et_new_password.getText().toString().length()>0)
                    if(Integer.parseInt(img_hide_show_pass_new.getTag().toString())==0){
                        et_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        img_hide_show_pass_new.setTag(1);
                        img_hide_show_pass_new.setImageResource(R.drawable.hide_icon_inactive_reg);
                        et_new_password.setSelection(et_new_password.getText().toString().length());
                    }else{
                        et_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_hide_show_pass_new.setTag(0);
                        img_hide_show_pass_new.setImageResource(R.drawable.hide_icon_active_reg);
                        et_new_password.setSelection(et_new_password.getText().toString().length());
                    }
                break;

            case R.id.img_hide_show_pass_confirm:

                if(et_confirm_password.getText().toString().length()>0)
                    if(Integer.parseInt(img_hide_show_pass_confirm.getTag().toString())==0){
                        et_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        img_hide_show_pass_confirm.setTag(1);
                        img_hide_show_pass_confirm.setImageResource(R.drawable.hide_icon_inactive_reg);
                        et_confirm_password.setSelection(et_confirm_password.getText().toString().length());
                    }else{
                        et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_hide_show_pass_confirm.setTag(0);
                        img_hide_show_pass_confirm.setImageResource(R.drawable.hide_icon_active_reg);
                        et_confirm_password.setSelection(et_confirm_password.getText().toString().length());
                    }
                break;

        }
    }

    private boolean chkValidation() {

        if (et_old_password.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Old Password");
            return false;

        }else if (et_new_password.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter New Password");
            return false;

        }
        else if (et_new_password.getText().toString().trim().length()<5) {

            Constants.showAlert(getActivity(), "New Password should be of atleast 5 characters !");
            return false;
        }
        else if (et_confirm_password.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Confirm Password");
            return false;

        }else if (!et_confirm_password.getText().toString().trim().equalsIgnoreCase(et_new_password.getText().toString().trim())){

            Constants.showAlert(getActivity(),"Confirm Password Not Match With New Password");
            return false;

        } else {

            return true;
        }
    }

    private void changePassword() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                changePasswordRequest(Constants.getUserId(getActivity()),et_new_password.getText().toString().trim(),
                        et_old_password.getText().toString().trim()),"changePassword");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("changePassword")) {
                ChangePasswordRequest req_Obj = (ChangePasswordRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    Toast.makeText(getActivity(), req_Obj.getResult().getMessage(), Toast.LENGTH_LONG).show();
//                    getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .commit();

                    getActivity().onBackPressed();

                }

                else
                    /*Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();*/
                    Constants.showAlert(getActivity(),req_Obj.getResult().getMessage());



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
        Constants.showAlert(getActivity(),errorMessage);
    }


}
