package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinListAdapter;
import com.nationality.model.AddBulletinRequest;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.EditBulletinRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FragmentCreateBulletin extends Fragment implements View.OnClickListener, RetrofitListener{
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

    ImageView img_create_bulletin, img_my_bulletin;
    private RestHandler rest;
    private  ProgressDialog pDialog;
    private EditText ext_title, ext_description;
    Button btn_post_now;

    TextView txt_chk_bulletin_type,tv_header;
    CheckBox chk_bulletin_type;

    //TextView txt_meetup;
    String bulletinTitle="",bulletinContent="",bulletinType="";
    int bulletinId = 0;

    public FragmentCreateBulletin() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentCreateBulletin newInstance(String param1, String param2) {
        FragmentCreateBulletin fragment = new FragmentCreateBulletin();
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

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                bulletinId = bundle.getInt("bulletin_id");
                bulletinTitle = bundle.getString("title");
                bulletinContent = bundle.getString("content");
                bulletinType = bundle.getString("bulletin_type");


            }
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

        View view=inflater.inflate(R.layout.fragment_add_bulletin, container, false);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        ext_title=(EditText)view.findViewById(R.id.ext_title);
        ext_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ext_description=(EditText)view.findViewById(R.id.ext_description);
        ext_description.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ext_description.setSingleLine(false);
        ext_description.setMaxLines(5);
        btn_post_now=(Button)view.findViewById(R.id.btn_post_now);
        chk_bulletin_type=(CheckBox)view.findViewById(R.id.chk_bulletin_type);
        txt_chk_bulletin_type = (TextView) view.findViewById(R.id.txt_chk_bulletin_type);
//        txt_meetup = (TextView) view.findViewById(R.id.txt_meetup);
        btn_post_now.setOnClickListener(this);
        chk_bulletin_type.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);


        if(bulletinType.contains("private")) {
            chk_bulletin_type.setText(" Posting as private");
            chk_bulletin_type.setChecked(false);
        }
        else {
            chk_bulletin_type.setText(" Posting as public");
            chk_bulletin_type.setChecked(true);
        }


        initialiseUI();

        //addBulletin(4);

        return view;
    }

    private void addBulletin(String bulletin_title, String bulletin_content, int user_id) {

        String bulletin_type="";
        if(chk_bulletin_type.isChecked())
        {
            bulletin_type="Public";
        }else {
            bulletin_type="Private";
        }

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                AddBulletinRequest(bulletin_title,bulletin_content,bulletin_type,user_id,getDateTime()),"addBulletin");
        pDialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void EditBulletin() {

        String bulletin_type="";
        if(chk_bulletin_type.isChecked())
        {
            bulletin_type="Public";
        }else {
            bulletin_type="Private";
        }

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                EditBulletinRequest(bulletinId,ext_title.getText().toString().trim(),
                        ext_description.getText().toString().trim(),bulletin_type,Constants.getUserId(getActivity()),getDateTime()),"editBulletin");
        pDialog.show();
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

            case R.id.btn_post_now:


                if(chkValidation()) {

                    if (bulletinId != 0) {

                        EditBulletin();

                    } else {
                        addBulletin(ext_title.getText().toString(), ext_description.getText().toString(), Constants.getUserId(getActivity()));
                    }
                }

                break;

            case R.id.btn_my_bulletin:
                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .addToBackStack( Constants.TAG_FRAGMENT_MY_BULLETIN)
                        .commit();
                break;

            case R.id.chk_bulletin_type:
                if(chk_bulletin_type.isChecked())
                {
                    chk_bulletin_type.setText(" Posting as public");
                }else {
                    chk_bulletin_type.setText(" Posting as private");
                }
                break;
        }

    }

    private boolean chkValidation() {

        if (ext_title.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Title");
            return false;

        }else if (ext_description.getText().toString().trim().equalsIgnoreCase("")){

            Constants.showAlert(getActivity(),"Enter Description");
            return false;

        }else {

            return true;
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("addBulletin")) {
                AddBulletinRequest req_Obj = (AddBulletinRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    Toast.makeText(getActivity(), "Bulletin Added successfully. ", Toast.LENGTH_LONG).show();
//                    getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .commit();

                    getActivity().onBackPressed();

                }

                else
                    Toast.makeText(getActivity(),"Server Response Error.. ",Toast.LENGTH_LONG).show();



            }
            else if (method.equalsIgnoreCase("editBulletin")){

                EditBulletinRequest req_Obj = (EditBulletinRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    Toast.makeText(getActivity(),  ""+req_Obj.getResult().getMessage(), Toast.LENGTH_LONG).show();
//                    getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentMyBulletin(), Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .addToBackStack( Constants.TAG_FRAGMENT_CREATE_BULLETIN)
//                            .commit();
                    getActivity().onBackPressed();

                }

                else
                    Toast.makeText(getActivity(),req_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();


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

        if (bulletinId != 0){

            tv_header.setText("Edit Bulletin");
            ext_title.setText(bulletinTitle);
            ext_description.setText(bulletinContent);
            btn_post_now.setText("Update");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Dddd","");
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(true);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

    }


    private String getDateTime()
    {
        Calendar c=Calendar.getInstance();

        SimpleDateFormat df=new SimpleDateFormat(Constants.web_date_format);

        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }
}
