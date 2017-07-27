package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nationality.R;
import com.nationality.model.SuggestionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class FragmentSuggestions extends Fragment implements View.OnClickListener, RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match

    RestHandler rest;
    ProgressDialog pDialog;

    EditText edt_suggestions;

    Button btn_submit;
    ImageView img_back;

    public FragmentSuggestions() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentSuggestions newInstance(String param1, String param2) {
        FragmentSuggestions fragment = new FragmentSuggestions();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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

        View view=inflater.inflate(R.layout.fragment_suggestions, container, false);

        edt_suggestions=(EditText)view.findViewById(R.id.edt_suggestions);

        btn_submit=(Button)view.findViewById(R.id.btn_submit);
        img_back=(ImageView)view.findViewById(R.id.img_back);

        btn_submit.setOnClickListener(this);
        img_back.setOnClickListener(this);


        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        edt_suggestions.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_suggestions.setSingleLine(false);

        return view;
    }

    private void postSuggestions()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                postSuggestions(Constants.getUserId(getActivity()),
                        edt_suggestions.getText().toString().trim()),"post_suggestions");
        pDialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
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
//        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_back:

                getActivity().onBackPressed();
                break;

            case R.id.btn_submit:

                if(edt_suggestions.getText().toString().trim().length()>0) {
                    postSuggestions();
                    if (getActivity() != null) {
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                }
                else
                    Constants.showAlert(getActivity(),"Please enter comment!");

                break;

        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){
            if(method.equalsIgnoreCase("post_suggestions"))
            {
                SuggestionRequest sug_obj=(SuggestionRequest)response.body();

//                if(!sug_obj.getResult().getError())
//                    Toast.makeText(getActivity(),sug_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                showAlert(getActivity(),sug_obj.getResult().getMessage());


                edt_suggestions.setText("");



            }
        }
        else{
            try {
                response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(NullPointerException e){
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


    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btm_me).setSelected(true);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

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
                        // if this button is clicked, just close
                        // the dialog box and do nothing
//                        dialog.cancel();

                        getActivity().getFragmentManager().popBackStack();
                        Fragment frag =new FragmentLanding();

                        getActivity().getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.contentContainer, frag, Constants.TAG_FRAGMENT_ME)
                                .addToBackStack(Constants.TAG_FRAGMENT_ME)
                                .commit();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
