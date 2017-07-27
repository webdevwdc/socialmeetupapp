package com.nationality.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.ConnectionLIstAdapter;
import com.nationality.adapter.ConnectionSearchLIstAdapter;
import com.nationality.adapter.MessageConnectionListAdapter;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.SearchUserConnectionDetails;
import com.nationality.model.SearchUserConnectionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

public class FragmentMessageConnection extends Fragment implements View.OnClickListener,RetrofitListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

//    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

//    @Bind(R.id.fast_scroller)
    RecyclerViewFastScroller fastScroller;
    RestHandler rest;
    ImageView img_back;

    LinearLayout lin_requests;

    TextView tv_header,tv_no_msg,txt_reqst,txt_add,txt_reqst_counter;

    private List<ConnectionListData> mDataArray;
    private List<SearchUserConnectionDetails> searchConnectionList;
    private List<AlphabetItem> mAlphabetItems;
    ProgressDialog pDialog;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    RecyclerView recycler_search;
    MessageConnectionListAdapter mMessageConnectionListAdapter;

    public FragmentMessageConnection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConnection.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMessageConnection newInstance(String param1, String param2) {
        FragmentMessageConnection fragment = new FragmentMessageConnection();
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

        View view=inflater.inflate(R.layout.fragment_message_connection, container, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.my_recycler_view);


        fastScroller=(RecyclerViewFastScroller)view.findViewById(R.id.fast_scroller);
        img_back=(ImageView)view.findViewById(R.id.img_back);

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_no_msg=(TextView)view.findViewById(R.id.tv_no_msg);

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        mDataArray=new ArrayList<>();
        searchConnectionList = new ArrayList<>();
        img_back.setOnClickListener(this);
       // lin_requests.setOnClickListener(this);
        //txt_add.setOnClickListener(this);

//        ButterKnife.bind(getActivity());

//        initialiseData();
//        initialiseUI();

        getConnectionList();

        setFonts();

        return view;
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

    private void getConnectionList()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getConnectionList(Constants.getUserId(getActivity())),"get_connection");
        pDialog.show();
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

            case R.id.txt_add:

                //getDialogSearch();

                break;

            case R.id.lin_requests:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentConnectionRequest(),
                        Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                        .commit();
                break;
        }

    }

    Dialog dialog;
    /*private void getDialogSearch() {
        //dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_search);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView img_search=(ImageView)dialog.findViewById(R.id.img_search);

        //ImageView img_back=(ImageView)dialog.findViewById(R.id.img_back_search);
        //final ListView lv=(ListView)dialog.findViewById(R.id.listview);
        recycler_search=(RecyclerView)dialog.findViewById(R.id.recycler_search);
        connectionSearchLIstAdapter = new ConnectionSearchLIstAdapter(searchConnectionList, getActivity(), dialog);
        recycler_search.setAdapter(connectionSearchLIstAdapter);
        final EditText et_search = (EditText) dialog.findViewById(R.id.et_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSOftKeyboard(getActivity(), et_search);
            }
        });
        ImageView img_clear = (ImageView) dialog.findViewById(R.id.img_clear);
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strSearch = et_search.getText().toString().trim();
                if (strSearch.length() >= 4) {
                    getAutoSearchKey(strSearch);
                }else{
                    //getAutoSearchKey("");
                    searchConnectionList.clear();
                    synchronized (getActivity()) {
                        recycler_search.invalidate();
                        connectionSearchLIstAdapter.notifyDataSetChanged();
                        //recycler_search.notifyAll();
                    }
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Search button from keyboard clicked
                    String strSearch = et_search.getText().toString().trim();
                    getAutoSearchKey(strSearch);
                    return true;
                }
                return false;
            }
        });
        dialog.show();
    }*/

    public static void openSOftKeyboard(Activity activity, EditText et_search){

        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.SHOW_FORCED, 0);
        et_search.setFocusable(true);
        et_search.requestFocus();
    }

    private void getAutoSearchKey(final String strSearch) {

        // Call web service for auto search
        int userId = Constants.getUserId(getActivity());
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                searchUserConnection(userId, strSearch),"searchUserConnection");
        pDialog.show();
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

//                    if(conn.getData().size()>0 && conn.getData().get(0).getFriendRequestNumber()>1)
//                        txt_reqst_counter.setText(String.valueOf( conn.getData().get(0).getFriendRequestNumber()));
//                    else
//                        txt_reqst_counter.setVisibility(View.GONE);
//                    List<ConnectionListData> cpp=conn.getData();
//                    cpp.get()
                   /* mDataArray.clear();
                    mDataArray.addAll(conn.getData());
                    initialiseData();
                    initialiseUI();*/
                    mMessageConnectionListAdapter=new MessageConnectionListAdapter(conn.getData(), getActivity(), Constants.getUserFBId(getActivity()));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                    mRecyclerView.setAdapter(mMessageConnectionListAdapter);

                    if(conn.getData()!=null && conn.getData().size()>0)
                    {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        tv_no_msg.setVisibility(View.GONE);
                    }
                    else{
                        mRecyclerView.setVisibility(View.GONE);
                        tv_no_msg.setVisibility(View.VISIBLE);
                    }

                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),"No friend list found...");
                }
            }
            // For auto search
            else if (method.equalsIgnoreCase("searchUserConnection")){
                /*SearchUserConnectionRequest request = (SearchUserConnectionRequest)response.body();
                if (!request.getResult().getError()) {
                    searchConnectionList.clear();
                    searchConnectionList.addAll(request.getResult().getData());
                    recycler_search.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recycler_search.setAdapter(connectionSearchLIstAdapter);

                    // To hide keyboard after successfully fetching the response
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }*/
            }
        }
        else{
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(getActivity(),response.code()+" "+response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            }
            catch(NullPointerException e){
                e.printStackTrace();
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    @Override
    public void onFailure(String errorMsg) {


        if(pDialog.isShowing())
            pDialog.dismiss();


            Constants.showAlert(getActivity(),errorMsg);

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

            String word = name.substring(0, 1).toUpperCase();
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word.toUpperCase());
                mAlphabetItems.add(new AlphabetItem(i, word.toUpperCase(), false));
            }
        }
    }

    protected void initialiseUI() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ConnectionLIstAdapter(mDataArray,getActivity()));

        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setUpAlphabet(mAlphabetItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Dddd","");
        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(true);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        //tv_header,tv_name,tv_desc,tv_tagme_hdr,tv_intrst_hdr,tv_lang_hdr,tv_lbl_mutual,tv_all;

        tv_header.setTypeface(typeFaceOpenSansBold);
        tv_no_msg.setTypeface(typeFaceOpenSansBold);
        //txt_add.setTypeface(typeFaceOpenSansReg);
        //txt_reqst.setTypeface(typeFaceOpenSansReg);

    }

}