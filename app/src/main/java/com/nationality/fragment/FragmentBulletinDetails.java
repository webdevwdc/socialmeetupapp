package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.models.AlphabetItem;
import com.nationality.R;
import com.nationality.adapter.BulletinDetailsAdapter;
import com.nationality.model.BulletinCommentReplyRequest;
import com.nationality.model.BulletinDetailsRequest;
import com.nationality.model.BulletinLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.DataHelper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import okhttp3.Response;
import retrofit2.Call;


public class FragmentBulletinDetails extends Fragment implements View.OnClickListener, RetrofitListener, Filterable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //    @Bind(R.id.my_recycler_view)
    LinearLayout meetup_reply,bulletin_like;
    RecyclerView recycler_view_bulletins;
    TextView  txt_name,txt_date, txt_time, txt_title, txt_view_count, txt_reply_count, txt_likes_count, btn_respond, txt_resp_count,
            tv_like, tv_reply,tv_header;

    ImageView img_back;

    View view_white;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;
    private RestHandler rest;
    private ProgressDialog pDialog;
    private TextView  txt_comment;
    private Integer bulletin_parent_id=0, bulletin_id=0;
    private String bulletin_title="";
    private String comment="";
    private String date="";
    private String title="";
    private String time="";
    private ImageView img_profile_pic;
    private Integer likeCount=0;

    BulletinDetailsRequest req_Obj;

    public FragmentBulletinDetails() {
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
    public static FragmentBulletinDetails newInstance(String param1, String param2) {
        FragmentBulletinDetails fragment = new FragmentBulletinDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bulletin_details, container, false);
        init(view);

        return view;
    }

    private void init(View view) {

        Bundle bundle=getArguments();
        if(bundle!=null)
            bulletin_id=bundle.getInt("bulletin_id");


        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        view_white=(View)view.findViewById(R.id.view_white);

        rest=new RestHandler(getActivity(),this);
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        initialiseUI(view);

        getBulletinDetails(bulletin_id);


    }

    private void getBulletinDetails(int bulletin_id) {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getBulletinDetails(bulletin_id, Constants.getUserId(getActivity())),"getBulletinDetails");
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

            case R.id.meetup_reply:

                Bundle arguments = new Bundle();
                arguments.putString("parent_bulletin_id", ""+bulletin_parent_id);
                arguments.putString("bulletin_id", ""+bulletin_id);
                arguments.putString("comment", comment);
                arguments.putString("date", date);
                arguments.putString("time", time);
                arguments.putString("title", title);

                FragmentBulletinMessageRespond fragment = new FragmentBulletinMessageRespond();
                fragment.setArguments(arguments);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment, Constants.TAG_FRAGMENT_BULLETIN_MSG_RESPOND)
                        .addToBackStack(Constants.TAG_FRAGMENT_BULLETIN_MSG_RESPOND).commit();

                break;

            case R.id.bulletin_like:

                likeBulletin(bulletin_id);

                break;

            case R.id.img_profile_pic:



                Bundle arguments1 = new Bundle();
                arguments1.putString("tag", "");
                arguments1.putString("interest", "");
                arguments1.putInt("id", Integer.parseInt(req_Obj.getResult().getData().getUserId()));

                FragmentProfile fragment2 = new FragmentProfile();
                fragment2.setArguments(arguments1);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment2, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();


                break;
        }

    }


    @Override
    public void onSuccess(Call call, retrofit2.Response response, String method) {
        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("getBulletinDetails")) {
                req_Obj = (BulletinDetailsRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    view_white.setVisibility(View.GONE);
                    bulletin_parent_id = 0;
                    bulletin_id = req_Obj.getResult().getData().getId();
                    bulletin_title = req_Obj.getResult().getData().getTitle();
                    comment = req_Obj.getResult().getData().getContent();
                    String[] strDateTime = req_Obj.getResult().getData().getCreatedAt().split(" ");
                    date = strDateTime[0];
                    time = strDateTime[1];
                    title = req_Obj.getResult().getData().getTitle();

                    txt_name.setText(req_Obj.getResult().getData().getCreatorName());
                    txt_comment.setText(req_Obj.getResult().getData().getContent());
                    tv_header.setText(req_Obj.getResult().getData().getTitle());
                    txt_title.setText(req_Obj.getResult().getData().getTitle());
                    String[] datetime = req_Obj.getResult().getData().getCreatedAt().split(" ");
                    txt_date.setText(Constants.changeDateFormat(req_Obj.getResult().getData().getCreatedAt(),
                            Constants.web_date_format,Constants.app_display_date_format));
                    txt_time.setText(Constants.changeDateFormat(req_Obj.getResult().getData().getCreatedAt(),
                            Constants.web_date_format,Constants.app_display_time_format));

                    if (req_Obj.getResult().getData().getTotalView() > 1)
                        txt_view_count.setText(req_Obj.getResult().getData().getTotalView() + " Views ");
                    else
                        txt_view_count.setText(req_Obj.getResult().getData().getTotalView() + " View ");

                    if (req_Obj.getResult().getData().getTotalLike() > 1)
                        txt_likes_count.setText(" "+req_Obj.getResult().getData().getTotalLike() + " Likes");
                    else
                        txt_likes_count.setText(" "+req_Obj.getResult().getData().getTotalLike() + " Like ");

                    likeCount=req_Obj.getResult().getData().getTotalLike();

                    if (req_Obj.getResult().getData().getTotalReply() > 1)
                        txt_reply_count.setText(" "+req_Obj.getResult().getData().getTotalReply() + " Replies ");
                    else
                        txt_reply_count.setText(" "+req_Obj.getResult().getData().getTotalReply() + " Reply ");

                    if (req_Obj.getResult().getData().getIsLike().equalsIgnoreCase(Constants.LIKE_YES)) {
                        bulletin_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                        bulletin_like.setEnabled(false);
                    }

                    if (req_Obj.getResult().getData().getUserId().equalsIgnoreCase("" + Constants.getUserId(getActivity()))) {
                        bulletin_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                        bulletin_like.setEnabled(false);
                    }
                    Constants.setImage(img_profile_pic, req_Obj.getResult().getData().getCreatorProfilePic(), getActivity());
                /*else{
                    bulletin_like.setBackgroundResource(R.drawable.green_rounded_bg);
                    bulletin_like.setEnabled(true);
                }*/

                    recycler_view_bulletins.setAdapter(new BulletinDetailsAdapter(req_Obj.getResult().getData().getRespond(), title, getActivity()));

                }
                else{
                    Constants.showAlert(getActivity(),req_Obj.getResult().getMessage());
                }
            }

            else if(method.equalsIgnoreCase("getLikeBulletin"))
            {

                BulletinLikeRequest req_Obj = (BulletinLikeRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    likeCount=likeCount+1;
                    if (likeCount > 1)
                        txt_likes_count.setText(" "+likeCount + " Likes");
                    else
                        txt_likes_count.setText(" "+likeCount + " Like");

                    bulletin_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                    bulletin_like.setEnabled(false);

                }
                else
                    Constants.showAlert(getActivity(),req_Obj.getResult().getMessage());

            }

        }else{
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

    @Override
    public Filter getFilter() {
        return null;
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

    private void likeBulletin(int bulletin_id) {

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinLikeRequest(Constants.getUserId(getActivity()),"Yes",bulletin_id),"getLikeBulletin");
        pDialog.show();

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

    protected void initialiseUI(View view) {
        recycler_view_bulletins=(RecyclerView)view.findViewById(R.id.recycler_view_bulletins);
        recycler_view_bulletins.setLayoutManager(new LinearLayoutManager(getActivity()));
        OverScrollDecoratorHelper.setUpOverScroll(recycler_view_bulletins, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        ///////////////View Imitiasiation/////////////////
        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

//        tv_header_txt=(TextView)view.findViewById(R.id.tv_header_txt);
        txt_name=(TextView)view.findViewById(R.id.txt_name);
        txt_date=(TextView)view.findViewById(R.id.txt_date);
        txt_time=(TextView)view.findViewById(R.id.txt_time);
        txt_comment=(TextView)view.findViewById(R.id.txt_comment);

        txt_title=(TextView)view.findViewById(R.id.txt_title);
        txt_view_count=(TextView)view.findViewById(R.id.txt_view_count);
        txt_reply_count=(TextView)view.findViewById(R.id.txt_reply_count);
        txt_likes_count=(TextView)view.findViewById(R.id.txt_likes_count);
        txt_comment=(TextView)view.findViewById(R.id.txt_comment);

        img_profile_pic=(ImageView)view.findViewById(R.id.img_profile_pic);

        bulletin_like=(LinearLayout)view.findViewById(R.id.bulletin_like);
        bulletin_like.setOnClickListener(this);

        tv_like =(TextView)view.findViewById(R.id.tv_like);
        tv_reply=(TextView)view.findViewById(R.id.tv_reply);

        meetup_reply=(LinearLayout) view.findViewById(R.id.meetup_reply);
        bulletin_like=(LinearLayout) view.findViewById(R.id.bulletin_like);

        meetup_reply.setOnClickListener(this);
        bulletin_like.setOnClickListener(this);
        img_profile_pic.setOnClickListener(this);

        txt_name.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_date.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_time.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_comment.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_title.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));
        txt_view_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_reply_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_likes_count.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        txt_comment.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        tv_like.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));
        tv_reply.setTypeface(Constants.typeFaceOpenSansReg(getActivity()));


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
