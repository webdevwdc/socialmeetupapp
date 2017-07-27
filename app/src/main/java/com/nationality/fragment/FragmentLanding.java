package com.nationality.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.adapter.AttendeesListAdapter;
import com.nationality.adapter.ImageListAdapter;
import com.nationality.adapter.LatestMeetupAdapter;
import com.nationality.adapter.MeetupAdapterList;
import com.nationality.adapter.MyFriendListsAdapter;
import com.nationality.adapter.UpcomingEventAdapter;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.DashboardNextMeetup;
import com.nationality.model.DashboardRequest;
import com.nationality.model.DashboardYourMeetup;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.ExpandableHeightListView;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLanding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLanding extends Fragment implements RetrofitListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1="";
    private String mParam2;


    ArrayList<DashboardYourMeetup> arrMeetup;
    ArrayList<DashboardNextMeetup> arrUpcomingEvents;
    LatestMeetupAdapter latestMeetupAdapter;
    UpcomingEventAdapter upcomingEventAdapter;

    RecyclerView my_recycler_view;

    DashboardRequest req_obj;

    TextView tv_all,tv_name,tv_desc,tv_desc2,tv_header_meetup,bulletin_header,tv_welcome_bck,
            tv_welcome_bck_lbl,tv_settings,tv_upcoming_event,tv_conected_to;

    ExpandableHeightListView lv_latest_meetup, lv_upcoming_event;
    RestHandler rest;
    LinearLayout lin_settings;
    RelativeLayout bulletin_contents;
    ImageView img_profile;
    ProgressDialog pDialog;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    LinearLayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

//    TwoWayView lv_connected_friends;
    int latestBulletinId;

    public FragmentLanding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLanding.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLanding newInstance(String param1, String param2) {
        FragmentLanding fragment = new FragmentLanding();
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
            mParam1 = getArguments().getString("type");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_landing, container, false);
        init(view);
        return view;
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


    private void init(View view)
    {

        lv_latest_meetup=(ExpandableHeightListView)view.findViewById(R.id.lv_latestmeetup);
        lv_upcoming_event=(ExpandableHeightListView)view.findViewById(R.id.lv_upcoming_event);
        tv_all=(TextView)view.findViewById(R.id.tv_all);

        my_recycler_view=(RecyclerView)view.findViewById(R.id.my_recycler_view);


        lin_settings=(LinearLayout)view.findViewById(R.id.lin_settings);

        img_profile=(ImageView)view.findViewById(R.id.img_profile);

        tv_welcome_bck=(TextView)view.findViewById(R.id.tv_welcome_bck);
        tv_welcome_bck_lbl=(TextView)view.findViewById(R.id.tv_welcome_bck_lbl);
        tv_settings=(TextView)view.findViewById(R.id.tv_settings);
        tv_upcoming_event=(TextView)view.findViewById(R.id.tv_upcoming_event);
        tv_conected_to=(TextView)view.findViewById(R.id.tv_conected_to);


        bulletin_contents=(RelativeLayout)view.findViewById(R.id.bulletin_contents);
        bulletin_contents.setOnClickListener(this);
        bulletin_header=(TextView)view.findViewById(R.id.bulletin_header);
        tv_header_meetup=(TextView)view.findViewById(R.id.tv_header_meetup);

        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_desc=(TextView)view.findViewById(R.id.tv_desc);
        tv_desc2=(TextView)view.findViewById(R.id.tv_desc2);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        lin_settings.setOnClickListener(this);
        arrMeetup=new ArrayList<>();
        arrUpcomingEvents=new ArrayList<>();
//        arrAttendies=new ArrayList<>();

        latestMeetupAdapter=new LatestMeetupAdapter(getActivity(), arrMeetup);
        lv_latest_meetup.setAdapter(latestMeetupAdapter);
        lv_latest_meetup.setExpanded(true);

        upcomingEventAdapter=new UpcomingEventAdapter(getActivity(), arrUpcomingEvents);
        lv_upcoming_event.setAdapter(upcomingEventAdapter);
        lv_upcoming_event.setExpanded(true);

//        friendListAdapter=new AttendeesListAdapter(getActivity(), arrAttendies,mScroll);
//        lv_friends.setAdapter(friendListAdapter);


        tv_all.setOnClickListener(this);
        img_profile.setOnClickListener(this);

        rest=new RestHandler(getActivity(),this);

        setFonts();
        getDashboardDtls();
       // getConnectionList();

        if(mParam1.equals("meetup_request"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentMeetup(), Constants.TAG_FRAGMENT_MEETUPS)
                    .addToBackStack( Constants.TAG_FRAGMENT_MEETUPS)
                    .commit();

        }

       else if(mParam1.equals("meetup_request_accept"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentMeetup(), Constants.TAG_FRAGMENT_MEETUPS)
                    .addToBackStack( Constants.TAG_FRAGMENT_MEETUPS)
                    .commit();
        }

       else if(mParam1.equals("meetup_comment"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentMeetup(), Constants.TAG_FRAGMENT_MEETUPS)
                    .addToBackStack( Constants.TAG_FRAGMENT_MEETUPS)
                    .commit();

        }
        //connection request accept
        else if(mParam1.equals("connection request accept"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentConnection(),
                    Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                    .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                    .commit();

        }
        else if(mParam1.equals(" connection_request"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentConnectionRequest(),
                    Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                    .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_REQUESTS)
                    .commit();

        }
        else if(mParam1.equals("messaging"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentRecentMessage(), Constants.TAG_FRAGMENT_RECENT_MSG)
                    .addToBackStack( Constants.TAG_FRAGMENT_RECENT_MSG)
                    .commit();


        }
        else if(mParam1.equals(" bulletin_comment"))
        {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                    new FragmentBulletin(), Constants.TAG_FRAGMENT_BULLETIN)
                    .addToBackStack( Constants.TAG_FRAGMENT_BULLETIN)
                    .commit();

        }
        mParam1="";

     /*   lv_latest_meetup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentMeetProfileDetails mFrag= new FragmentMeetProfileDetails();
                Bundle b=new Bundle();
                b.putInt("meetup_id", arrMeetup.get(position).getId());
                b.putString("fragment","");
                mFrag.setArguments(b);

                //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        mFrag,Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .commit();
            }
        });
        lv_upcoming_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentMeetProfileDetails mFrag= new FragmentMeetProfileDetails();
                Bundle b=new Bundle();
                b.putInt("meetup_id", arrUpcomingEvents.get(position).getId());
                b.putString("fragment","");
                mFrag.setArguments(b);

                //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        mFrag,Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .commit();
            }
        });*/

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
    public void onSuccess(Call call, Response response, String method) {
        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("get_dashboard"))
            {
                req_obj=(DashboardRequest)response.body();

                if(!req_obj.getResult().getError())
                {
                    showdata(req_obj);

                    if (req_obj.getResult().getData().getBulletin()!=null && req_obj.getResult().getData().getBulletin().size()>0) {
                        latestBulletinId = req_obj.getResult().getData().getBulletin().get(0).getId();
                    }
                    getConnectionList();
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                }
            }
            else if(method.equalsIgnoreCase("get_connection"))
            {
                ConnectionRequest req_obj=(ConnectionRequest)response.body();
                if(!req_obj.getResult().getError())
                {
                    ConnectionListResult conn=req_obj.getResult();
                    SpannableStringBuilder spannable = new SpannableStringBuilder("See All ("+conn.getData().size()+")");
                    spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.green)), 9, 11, 0);
                    tv_all.setText(spannable);

                    MyFriendListsAdapter  adapterList = new MyFriendListsAdapter(getActivity(),conn.getData());
                    mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    my_recycler_view.setLayoutManager(mLayoutManager);
                    my_recycler_view.setItemAnimator(new DefaultItemAnimator());
                    my_recycler_view.setAdapter(adapterList);

                    OverScrollDecoratorHelper.setUpOverScroll(my_recycler_view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);


                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                }


            }

        }
        else{
            if(pDialog.isShowing())
                pDialog.dismiss();
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(getActivity(),response.code()+" "+response.message());
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
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.lin_settings:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentSettings(), Constants.TAG_FRAGMENT_SETTINGS)
                        .addToBackStack( Constants.TAG_FRAGMENT_SETTINGS)
                        .commit();

                break;

            case R.id.bulletin_contents:

                Bundle bundle=new Bundle();
                bundle.putInt("bulletin_id", latestBulletinId);

                FragmentBulletinDetails fragment=new FragmentBulletinDetails();

                fragment.setArguments(bundle);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment,
                        Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                        .addToBackStack(Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                        .commit();

                break;


            case R.id.tv_all:

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        new FragmentAllUser(), Constants.TAG_FRAGMENT_CONNECTION)
                        .addToBackStack( Constants.TAG_ALL_CONNECTION)
                        .commit();

                break;

            case R.id.img_profile:

                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id", Integer.parseInt(req_obj.getResult().getData()
                        .getBulletin().get(0).getUserId()));

                FragmentProfile fragment1 = new FragmentProfile();
                fragment1.setArguments(arguments);

                getActivity().getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment1, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();

                break;


        }

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
        Log.d("Dddd","");
        getActivity().findViewById(R.id.btm_me).setSelected(true);
        getActivity().findViewById(R.id.btm_connection).setSelected(false);
        getActivity().findViewById(R.id.btm_bulletin).setSelected(false);
        getActivity().findViewById(R.id.btm_msgs).setSelected(false);
        getActivity().findViewById(R.id.btm_meetups).setSelected(false);

    }


    private void getDashboardDtls()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getDashboard(Constants.getUserId(getActivity())),"get_dashboard");

        pDialog.show();
    }

    private void getConnectionList()
    {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getConnectionList(Constants.getUserId(getActivity())),"get_connection");
        pDialog.show();
    }


    private void showdata(DashboardRequest req)
    {
//        req.getData().getBulletin().get(0).getId();
        tv_welcome_bck.setText(req.getResult().getData().getFirstName()+" !");

        if(req.getResult().getData().getBulletin()!=null &&
                req.getResult().getData().getBulletin().size()>0) {
            tv_name.setText(req.getResult().getData().getBulletin().get(0).getTitle());

            Constants.setSquareImage(img_profile,req.getResult().getData().getBulletin().get(0).getBulletinCreatorPic(),getActivity());

            tv_desc.setText(req.getResult().getData().getBulletin().get(0).getBulletinCreator()+", "+
            Constants.changeDateFormat(req.getResult().getData().getBulletin().get(0).getUpdatedAt(),
                    Constants.web_date_format,Constants.app_display_date_format));
            tv_desc2.setText(req.getResult().getData().getBulletin().get(0).getContent());
        }
        else{
            bulletin_contents.setVisibility(View.GONE);
//            bulletin_header.setVisibility(View.GONE);
        }

        //latest meetups
        if(req.getResult().getData().getYourMeetup()!=null &&
                req.getResult().getData().getYourMeetup().size()>0)
        {
            arrMeetup.clear();
            arrMeetup.addAll(req.getResult().getData().getYourMeetup());
            latestMeetupAdapter.notifyDataSetChanged();
        }

        if(req.getResult().getData().getNextMeetup()!=null &&
                req.getResult().getData().getNextMeetup().size()>0)
        {
            arrUpcomingEvents.clear();
            arrUpcomingEvents.addAll(req.getResult().getData().getNextMeetup());
            upcomingEventAdapter.notifyDataSetChanged();
        }

//        if(req.getResult().getData().getMeetup().size()>0){
//
//        }
//        else{
//            tv_header_meetup.setVisibility(View.GONE);
//        }
    }

    private void setFonts()
    {
        typeFaceOpenSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "OPENSANS-BOLD.TTF");
        typeFaceOpenSansReg=Typeface.createFromAsset(getActivity().getAssets(), "OPENSANS-REGULAR.TTF");

        tv_welcome_bck_lbl.setTypeface(typeFaceOpenSansReg);
        tv_welcome_bck.setTypeface(typeFaceOpenSansBold);
        tv_name.setTypeface(typeFaceOpenSansReg);
        bulletin_header.setTypeface(typeFaceOpenSansBold);
        tv_desc.setTypeface(typeFaceOpenSansReg);
        tv_desc2.setTypeface(typeFaceOpenSansReg);
        tv_header_meetup.setTypeface(typeFaceOpenSansBold);
        tv_upcoming_event.setTypeface(typeFaceOpenSansBold);
        tv_conected_to.setTypeface(typeFaceOpenSansReg);
        tv_all.setTypeface(typeFaceOpenSansReg);

    }
}
