package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nationality.R;
import com.nationality.model.ChangePasswordRequest;
import com.nationality.model.CmsRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 3/5/17.
 */

public class FragmentCms extends Fragment implements View.OnClickListener,RetrofitListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String cms_type = "";

    TextView tv_header;



    EditText et_old_password,et_new_password,et_confirm_password;


    Button btn_update;

    RestHandler rest;
    ProgressDialog pDialog;

    ImageView img_back;
    WebView webview;

    private FragmentChangePassword.OnFragmentInteractionListener mListener;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    private ProgressBar progressBar;


    public FragmentCms() {
        // Required empty public constructor
    }


    public static FragmentCms newInstance(String param1, String param2) {
        FragmentCms fragment = new FragmentCms();
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

        View view=inflater.inflate(R.layout.fragment_cms, container, false);

        rest=new RestHandler(getActivity(),this);

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar1);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);


        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        img_back=(ImageView)view.findViewById(R.id.img_back);
        webview=(WebView)view.findViewById(R.id.wv_cms_desc);
        img_back.setOnClickListener(this);

        Bundle bundle=getArguments();
        if(bundle!=null && bundle.containsKey("cms_type"))
            cms_type=bundle.getString("cms_type");

        if (cms_type.equalsIgnoreCase("privacy-policy")){
            tv_header.setText("Privacy Policy");
        }else if (cms_type.equalsIgnoreCase("terms-of-services")){
            tv_header.setText("Terms of Services");
        }

        getCmsData();

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



        }
    }


    private void getCmsData() {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getCmsData(cms_type),"getCms");
        pDialog.show();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getCms")) {
                CmsRequest req_Obj = (CmsRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    String description = req_Obj.getResult().getData().getCmsDesc();
                    setWebview(description);

                }

                else {
                    Toast.makeText(getActivity(), "Server Response Error.. ", Toast.LENGTH_LONG).show();
                    // Constants.showAlert(getActivity(),req_Obj.getResult().getData().getCmsDesc());

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
        Constants.showAlert(getActivity(),errorMessage);
    }


    private void setWebview(String webContent){
        webview.setBackgroundColor(Color.TRANSPARENT);

        WebSettings settings = webview.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        DisplayMetrics dm = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        webview.setMinimumHeight(dm.heightPixels);
        webview.setMinimumWidth(dm.widthPixels);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        String pHtml = "<html><head><style> p { color: #000;} body { color: #000;} ol li{color: #000;} ul li{color: #000;}</style></head><body>"+webContent+"</body></html>";


        webview.loadDataWithBaseURL("", pHtml, mimeType, encoding, "");
        progressBar.setVisibility(View.GONE);
    }
}
