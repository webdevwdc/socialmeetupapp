package com.nationality.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nationality.R;
import com.nationality.firebaseChat.CodelabPreferences;
import com.nationality.firebaseChat.FriendlyMessage;
import com.nationality.model.BadgeRemoveRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 4/10/2017.
 */

public class FragmentChat extends Fragment implements RetrofitListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_header;

    String strUserName = "test@webskitters.com";
    String strPwd = "123456";
    public static final String MESSAGES_CHILD = "Messages/";
    public static String MESSAGES_CHILD_unique_users;
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    //private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    //private EditText mMessageEditText;
    private EmojiconEditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG="error";

    private View rootView;
    private ImageView emojiImageView;
    private EmojIconActions emojIcon;
    private FirebaseUser mFirebaseUser;
    private String uniqueChatId="",receiverId="",senderId="";
    ImageView img_back;

    RestHandler rest;
    ProgressDialog pDialog;

    private String lastMessage="";
    private String to_userID;
    private String show_loader;

    public FragmentChat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMeetup.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMeetup newInstance(String param1, String param2) {
        FragmentMeetup fragment = new FragmentMeetup();
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

        initFields();
    }

    private void initFields() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();
        strUserName= Constants.getUserEmailId(getActivity());

        mUsername = strUserName;
        //login(strUserName, strPwd);
        /*if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity

            login(strUserName, strPwd);
//            return;
        } else {
            if (mFirebaseUser.getDisplayName()!=null) {
                mUsername = mFirebaseUser.getDisplayName();
            }
            //Uri profileUri = mFirebaseUser.getPhotoUrl();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }*/

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            uniqueChatId = bundle.getString("uniqueChatId");
            receiverId=bundle.getString("receiverId");
            senderId = Constants.getUserFBId(getActivity());
            to_userID=bundle.getString("to_userID");
            show_loader=bundle.getString("show_loader");

            Constants.to_user_id=to_userID;

        }

        MESSAGES_CHILD_unique_users = MESSAGES_CHILD+uniqueChatId;

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.dialog_msg));
        pDialog.setCancelable(false);

        rest=new RestHandler(getActivity(),this);
//        removeBadges("connection_badge");
    }

    private void removeBadges(String type) {

//        badge_type ['connection_badge' , 'meetup_badge' ,'message_badge' ,'bulletin_badge']

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                removeBadgeCount(Constants.getUserId(getActivity()),type),type);

//        pDialog.show();

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

        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        img_back=(ImageView)view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();
                //lastMessageStore(to_userID,Constants.getUserId(getActivity()),uniqueChatId, lastMessage);
//                onDetach();

            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        if(show_loader.equalsIgnoreCase("No")){
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        tv_header=(TextView)view.findViewById(R.id.tv_header);
        tv_header.setTypeface(Constants.typeFaceOpenSansBold(getActivity()));

        mLinearLayoutManager.setStackFromEnd(true);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
                FriendlyMessage.class,
                R.layout.row_my_message,
                MessageViewHolder.class,
                //mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
                mFirebaseDatabaseReference.child(MESSAGES_CHILD_unique_users)) {

            @Override
            protected FriendlyMessage parseSnapshot(DataSnapshot snapshot) {
                FriendlyMessage friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setsender_id(snapshot.getKey());
                }
                return friendlyMessage;
            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, FriendlyMessage friendlyMessage, int position) {

                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                //viewHolder.userNmaeTextView.setText(friendlyMessage.getName());
                String datetime=convertDateFromMillis(friendlyMessage.getsenddate().toString());
                String[] date=datetime.split(",");
                viewHolder.dateTextView.setText(date[0]);
                viewHolder.timeTextView.setText(date[1]);
                //viewHolder.messageTextView.setText(friendlyMessage.getmessage());
                viewHolder.messageTextView.setText(friendlyMessage.getmessage());
                viewHolder.messageTextView.setTextColor(Color.BLACK);

                viewHolder.userNmaeTextView.setText(friendlyMessage.getName());

                viewHolder.message1TextView.setText(friendlyMessage.getmessage());
                viewHolder.message1TextView.setTextColor(Color.WHITE);
                //viewHolder.message1TextView.setBackgroundResource(R.drawable.grytalk);

                //viewHolder.messageTextView.setVisibility(View.GONE);
                viewHolder.userName1TextView.setText(friendlyMessage.getName());
                //viewHolder.userNmaeTextView.setVisibility(View.INVISIBLE);


                if(!friendlyMessage.getreceiver_id().equalsIgnoreCase(senderId)){
                    viewHolder.message1TextView.setVisibility(View.VISIBLE);
                    viewHolder.userName1TextView.setVisibility(View.VISIBLE);
                    viewHolder.messageTextView.setVisibility(View.GONE);
                    viewHolder.userNmaeTextView.setVisibility(View.INVISIBLE);

                }else{

                    viewHolder.messageTextView.setVisibility(View.VISIBLE);

                    viewHolder.userNmaeTextView.setVisibility(View.VISIBLE);

                    viewHolder.message1TextView.setVisibility(View.GONE);
                    viewHolder.userName1TextView.setVisibility(View.INVISIBLE);
                }


                if (friendlyMessage.getmessage() != null) {
                    // write this message to the on-device index
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
                }

                // log a view action on it
                FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));
            }
        };

       /*else
            mProgressBar.setVisibility(ProgressBar.VISIBLE);*/

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(mMessageRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);


        // Initialize Firebase Measurement.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        // Define default config values. Defaults are used when fetched config values are not
        // available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 1000L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config.
        fetchConfig();

        mMessageEditText = (EmojiconEditText) view.findViewById(R.id.et_emojicon);

        rootView = (RelativeLayout) view.findViewById(R.id.root_view);

        emojiImageView = (ImageView) view.findViewById(R.id.emoji_btn);
        emojIcon = new EmojIconActions(getActivity(), rootView, mMessageEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });

        //mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(senderId, receiverId,mMessageEditText.getText().toString(), Constants.getUserName(getActivity()),
                        mPhotoUrl, null, String.valueOf(System.currentTimeMillis()));
                mFirebaseDatabaseReference.child(MESSAGES_CHILD_unique_users).push().setValue(friendlyMessage);
                lastMessage=mMessageEditText.getText().toString().trim();
                mMessageEditText.setText("");
                mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
                if(lastMessage!=null&&!lastMessage.isEmpty())
                    lastMessageStore(to_userID,Constants.getUserId(getActivity()), lastMessage, String.valueOf(System.currentTimeMillis()));
            }
        });

        return view;
    }


    public static String convertDateFromMillis(String millis) {

        //long val = 1346524199000l;
        long val = Long.parseLong(millis);

        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat(Constants.app_display_date_format+","+Constants.app_display_time_format);
        String dateText = df2.format(date);
        System.out.println(dateText);
        return dateText;
    }

    private void login(String uName, String pwd) {
        mFirebaseAuth.createUserWithEmailAndPassword(uName, pwd)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(getActivity(), "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Authentication Succeeded
                            mFirebaseAuth = FirebaseAuth.getInstance();
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            mUsername = mFirebaseUser.getDisplayName();
                            //mPhotoUrl= mFirebaseUser.getPhotoUrl().toString();
                        }
                    }
                });
    }

    private Action getMessageViewAction(FriendlyMessage friendlyMessage) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(friendlyMessage.getName(), MESSAGE_URL.concat(friendlyMessage.getsender_id()))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(FriendlyMessage friendlyMessage) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(mUsername == friendlyMessage.getName())
                .setName(friendlyMessage.getName())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getsender_id() + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(mUsername)
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getreceiver_id() + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(friendlyMessage.getmessage())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getsender_id()))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
    }

    public String randomUserId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }



    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView, message1TextView;
        public TextView userNmaeTextView,userName1TextView, timeTextView, dateTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.txt_msg);
            message1TextView = (TextView) itemView.findViewById(R.id.txt_msg1);
            userNmaeTextView = (TextView) itemView.findViewById(R.id.txt_name);
            userName1TextView = (TextView) itemView.findViewById(R.id.txt_name_right);
            timeTextView = (TextView) itemView.findViewById(R.id.txt_time);
            dateTextView = (TextView) itemView.findViewById(R.id.txt_date);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        /*if(lastMessage!=null&&!lastMessage.isEmpty())
        lastMessageStore(to_userID,Constants.getUserId(getActivity()),uniqueChatId, lastMessage);*/

    }

    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config", e);
                        applyRetrievedLengthLimit();
                    }
                });
    }


    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong("friendly_msg_length");
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, "FML is: " + friendly_msg_length);
    }

    private void lastMessageStore(String to_userid, int from_userid, String message, String time) {

//        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                recentMessageStore(to_userid,from_userid,  message, time),"recentMessageStore");

        //getActivity().onBackPressed();

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if(method.equalsIgnoreCase("recentMessageStore")) {


            }

            else if(method.contains("badge"))
            {
                BadgeRemoveRequest req_obj=(BadgeRemoveRequest)response.body();
//                req_obj.getResult().getData().get
                if(!req_obj.getResult().getError())
                {
                    getActivity().findViewById(R.id.txt_reqst_counter_chat).setVisibility(View.GONE);
//                    i
//                    if(method.equalsIgnoreCase("connection_badge"))
//                    {
//                        txt_reqst_counter_connection.setVisibility(View.GONE);
//                    }
//
//                    else if(method.equalsIgnoreCase("meetup_badge"))
//                    {
//                        txt_reqst_counter_meetup.setVisibility(View.GONE);
//                    }
//
//                    else if(method.equalsIgnoreCase("message_badge"))
//                    {
//                        txt_reqst_counter_chat.setVisibility(View.GONE);
//                    }
//
//                    else if(method.equalsIgnoreCase("bulletin_badge"))
//                    {
//                        txt_reqst_counter_bulletin.setVisibility(View.GONE);
//                    }

                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(getActivity(),req_obj.getResult().getMessage());
                }
            }

        }
        else {
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(getActivity(), response.code() + " " + response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            } catch (NullPointerException e) {
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


}
