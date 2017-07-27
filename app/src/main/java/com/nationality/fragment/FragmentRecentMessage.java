package com.nationality.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.ConnectionSearchLIstAdapter;
import com.nationality.adapter.RecentMessageAdapter;
import com.nationality.model.ConnectionListData;
import com.nationality.model.RecentMessageListData;
import com.nationality.model.RecentMessageListRequest;
import com.nationality.model.RecentMessageListResponse;
import com.nationality.model.SearchUserConnectionDetails;
import com.nationality.model.SearchUserConnectionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRecentMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecentMessage extends Fragment implements View.OnClickListener,RetrofitListener{
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
    //RecyclerViewFastScroller fastScroller;
    RestHandler rest;
    ImageView img_back;

    LinearLayout lin_requests;

    TextView tv_header,tv_no_msg,txt_reqst,txt_add,txt_reqst_counter;

    RecentMessageAdapter mAdapter;

    private List<ConnectionListData> mDataArray;
    private List<SearchUserConnectionDetails> searchConnectionList;
    private List<AlphabetItem> mAlphabetItems;
    ProgressDialog pDialog;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    RecyclerView recycler_search;
    ConnectionSearchLIstAdapter connectionSearchLIstAdapter;
    RelativeLayout rel_create_msg;

    List<RecentMessageListData> dataset;

    // For Images
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    Rect startBounds;
    Rect finalBounds;
    Point globalOffset;
    float startScaleFinal;

    ImageView expandedImageView;
    RelativeLayout rl_container;

    private List<RecentMessageListData> list_recentChat;
    private EditText edt_search;
    View view_thumb;


    public FragmentRecentMessage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentRecentMessage newInstance(String param1, String param2) {
        FragmentRecentMessage fragment = new FragmentRecentMessage();
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

        View view=inflater.inflate(R.layout.fragment_recent_message, container, false);


        tv_no_msg=(TextView)view.findViewById(R.id.tv_no_msg);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        edt_search=(EditText)view.findViewById(R.id.edt_search);
        tv_header=(TextView)view.findViewById(R.id.tv_header);

        rel_create_msg=(RelativeLayout)view.findViewById(R.id.rel_create_msg);
        rel_create_msg.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);


        expandedImageView = (ImageView) view.findViewById(R.id.expanded_image);
        rl_container = (RelativeLayout) view.findViewById(R.id.container);

        startBounds = new Rect();
        finalBounds = new Rect();
        globalOffset = new Point();

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        tv_no_msg.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));



        mDataArray=new ArrayList<>();
        searchConnectionList = new ArrayList<>();
        img_back.setOnClickListener(this);

        //lin_requests.setOnClickListener(this);
        //txt_add.setOnClickListener(this);



        setFonts();
        getConnectionList();
        addTextListener();
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

//                getActivity().onBackPressed();
                int isShowing = expandedImageView.getVisibility();
                if (isShowing == View.VISIBLE){
                    int pos=expandedImageView.getId();
                    hideExpandedImage(view_thumb, pos);
                }else {
                    //super.onBackPressed();
                    getActivity().onBackPressed();
                }

                break;

            case R.id.txt_add:

                getDialogSearch();

                break;



            case R.id.rel_create_msg:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentMessageConnection(),
                        Constants.TAG_FRAGMENT_MSG_CONN)
                        .addToBackStack( Constants.TAG_FRAGMENT_MSG_CONN)
                        .commit();
                break;
        }

    }

    Dialog dialog;
    private void getDialogSearch() {
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
    }

    public static void openSOftKeyboard(Activity activity, EditText et_search){

        InputMethodManager inputMethodManager =
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
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

    public void getConnectionList()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getRecentMessageList(Constants.getUserId(getActivity())),"getRecentMessageList");
        pDialog.show();
    }


    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("getRecentMessageList"))
            {
                RecentMessageListRequest req_obj=(RecentMessageListRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    RecentMessageListResponse conn=req_obj.getResult();

                    if (list_recentChat!=null){
                        list_recentChat.clear();
                    }

                    dataset=new ArrayList<>();
                    dataset.addAll(conn.getData());

                    if(dataset.size()==0)
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        tv_no_msg.setVisibility(View.VISIBLE);
                    }

                    list_recentChat = conn.getData();

                    mAdapter=new RecentMessageAdapter(dataset,getActivity(), new RecentMessageAdapter.MyAdapterListener() {
                        @Override
                        public void iconProfileOnClick(View v, int position) {
                            zoomImageFromThumb(v, position);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);

                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
//                    Constants.showAlert(getActivity(),"No recent message found...");
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_msg.setVisibility(View.VISIBLE);
                }
            }
            // For auto search
            else if (method.equalsIgnoreCase("searchUserConnection")){
                SearchUserConnectionRequest request = (SearchUserConnectionRequest)response.body();
                if (!request.getResult().getError()) {
                    searchConnectionList.clear();
                    searchConnectionList.addAll(request.getResult().getData());
                    recycler_search.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recycler_search.setAdapter(connectionSearchLIstAdapter);

                    // To hide keyboard after successfully fetching the response
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
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
    public void onFailure(String errMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();


            Constants.showAlert(getActivity(),errMessage);

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        //tv_header,tv_name,tv_desc,tv_tagme_hdr,tv_intrst_hdr,tv_lang_hdr,tv_lbl_mutual,tv_all;

        tv_header.setTypeface(typeFaceOpenSansBold);
       // txt_add.setTypeface(typeFaceOpenSansReg);
       // txt_reqst.setTypeface(typeFaceOpenSansReg);

    }


    // Profile Pics
    private void zoomImageFromThumb(final View thumbView, final int position) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        //expandedImageView.setImageResource(imageResId);
//        Constants.setSquareImage(expandedImageView, list_recentChat.get(position).getImage(), getActivity());

        Glide.with(getActivity()).load(Constants.image_url+list_recentChat.get(position).
                getImage()).centerCrop().placeholder(R.drawable.no_image).crossFade().into(expandedImageView);

        expandedImageView.setId(position);

        view_thumb=thumbView;

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rl_container
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide expanded Image and show thumb image.
                hideExpandedImage(thumbView, position);
            }
        });
    }

    private void hideExpandedImage(final View thumb1View, int position) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(expandedImageView, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.Y,startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());

        Constants.setImage(expandedImageView, list_recentChat.get(position).getImage(), getActivity());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumb1View.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumb1View.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }



    @Override
    public void onResume() {
        super.onResume();



        getActivity().findViewById(R.id.btm_me).setSelected(false);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(true);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    int isShowing = expandedImageView.getVisibility();

                    if (isShowing == View.VISIBLE){
                        int pos=expandedImageView.getId();
                        hideExpandedImage(view_thumb, pos);
                    }else {
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    public void addTextListener(){

        edt_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // data set changed

             /*   if(s!=null && s.length()>0)
                adapterList.getFilter().filter(s.toString());*/

                if(s!=null && s.length()>=0 && mAdapter!=null) {
                    final List<RecentMessageListData> filteredModelList = filter(dataset, s.toString());
                    mAdapter.animateTo(filteredModelList);
                    mRecyclerView.scrollToPosition(0);
                }

            }
        });
    }

    private List<RecentMessageListData> filter(List<RecentMessageListData> models, String query) {
        query = query.toLowerCase();

        final List<RecentMessageListData> filteredModelList = new ArrayList<>();
        for (RecentMessageListData model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
