package com.nationality.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentChat;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.FooterCountRequest;
import com.nationality.model.GetChatTokenRequest;
import com.nationality.model.MessageRemoveBadgeCountRequest;
import com.nationality.model.RecentMessageListData;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by webskitters on 3/29/2017.
 */

public class RecentMessageAdapter extends RecyclerView.Adapter<RecentMessageAdapter.ViewHolder> implements RetrofitListener
{

    ProgressDialog pDialog;
    RestHandler rest;
    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<RecentMessageListData> mDataArray;
    private String receiverId="";
    private String to_user_id="";
    private String chat_token="";

    public MyAdapterListener onCustomClickListener;

    public RecentMessageAdapter(List<RecentMessageListData> dataset, Context con, MyAdapterListener listener) {
        if(con!=null) {
            mDataArray = new ArrayList<>(dataset);
            mContext = con;
            onCustomClickListener = listener;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recent_message, parent, false);
        ViewHolder vh = new ViewHolder(v);

        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage("Please wait..");
        pDialog.setCancelable(false);
        rest=new RestHandler(mContext,this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txt_msg.setText(mDataArray.get(position).getMessage().toString());
        /*String[] datetime=mDataArray.get(position).getCreatedAt().split(" ");
        holder.txt_date.setText(datetime[0]);
        holder.txt_time.setText(datetime[1]);*/

        Constants.setImage(holder.profile_pic, mDataArray.get(position).getImage(),mContext);

        holder.txt_user_name.setText(mDataArray.get(position).getName());

        /*holder.txt_date.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_date_format));
        holder.txt_time.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_time_format));
*/

        String datetime=convertDateFromMillis(mDataArray.get(position).getChat_date_time());
        holder.txt_time.setText(datetime);
        holder.txt_date.setVisibility(View.GONE);


        if(mDataArray.get(position).getBadgeCount()>0){
            holder.txt_reqst_counter.setText(""+mDataArray.get(position).getBadgeCount());
            holder.txt_reqst_counter.setVisibility(View.VISIBLE);
        }else{
            holder.txt_reqst_counter.setText("0");
            holder.txt_reqst_counter.setVisibility(View.GONE);
        }

    }

    /*@Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= mDataArray.size())
            return null;

        String name = mDataArray.get(pos).getFirstName();
        if (name == null || name.length() < 1)
            return null;

        return  mDataArray.get(pos).getFirstName().substring(0, 1);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_date)
        TextView txt_date;

        @Bind(R.id.txt_time)
        TextView txt_time;

        @Bind(R.id.txt_msg)
        TextView txt_msg;

        @Bind(R.id.txt_reqst_counter)
        TextView txt_reqst_counter;

        @Bind(R.id.txt_user_name)
        TextView txt_user_name;

        //profile_pic
        @Bind(R.id.profile_pic)
        ImageView profile_pic;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-REGULAR.TTF");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    storeUserChatDetails(
                            Integer.parseInt(mDataArray.get(getAdapterPosition()).getFromUserid()),
                            Integer.parseInt(mDataArray.get(getAdapterPosition()).getToUserid()));


                    receiverId=mDataArray.get(getAdapterPosition()).getFacebookId();
                    //to_user_id=mDataArray.get(getAdapterPosition()).getToUserid();
                    to_user_id=mDataArray.get(getAdapterPosition()).getToUserid();
                    if(to_user_id.equalsIgnoreCase(""+Constants.getUserId(mContext))){
                        to_user_id=mDataArray.get(getAdapterPosition()).getFromUserid();
                    }else{
                        to_user_id=mDataArray.get(getAdapterPosition()).getToUserid();
                    }

                    chat_token=mDataArray.get(getAdapterPosition()).getChatToken();

                    if(mDataArray.get(getAdapterPosition()).getBadgeCount()>0){



                        messageRemoveBadge(Integer.parseInt(mDataArray.get(getAdapterPosition()).getToUserid()),
                                Integer.parseInt(mDataArray.get(getAdapterPosition()).getFromUserid()),
                                mDataArray.get(getAdapterPosition()).getBadgeCount());

                    }

                    if(chat_token.equalsIgnoreCase("")||chat_token.toString().trim().isEmpty())
                        getChatToken(mDataArray.get(getAdapterPosition()).getFacebookId());

                    else{
                        Bundle arguments = new Bundle();
                        arguments.putString("uniqueChatId", chat_token);
                        arguments.putString("receiverId", receiverId);
                        arguments.putString("to_userID", to_user_id);
                        arguments.putString("show_loader", "Yes");
                        FragmentChat fragment = new FragmentChat();
                        fragment.setArguments(arguments);

                        ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                                fragment, Constants.TAG_FRAGMENT_MSG)
                                .addToBackStack( Constants.TAG_FRAGMENT_MSG)
                                .commit();

                    }



                }
            });




            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCustomClickListener!=null){
                        onCustomClickListener.iconProfileOnClick(profile_pic, getAdapterPosition());
                    }

                  /*  RecentMessageListData col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getToUserid()));

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();*/


                    if(mDataArray.get(getAdapterPosition()).getToUserid().equalsIgnoreCase(""+Constants.getUserId(mContext))){
                        to_user_id=mDataArray.get(getAdapterPosition()).getFromUserid();
                    }else{
                        to_user_id=mDataArray.get(getAdapterPosition()).getToUserid();
                    }
                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(to_user_id));
                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);
                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();

                }
            });

            txt_date.setTypeface(typeFaceOpenSansReg);
            txt_time.setTypeface(typeFaceOpenSansReg);
            txt_msg.setTypeface(typeFaceOpenSansReg);
            txt_user_name.setTypeface(typeFaceOpenSansReg);
        }



    }
    private void getChatToken(String facebookToken) {

        pDialog.show();
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String sender_id = prefs.getString(Constants.strShPrefUserFBID, "");
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getChatToken(sender_id,facebookToken, "Private"),"getChatToken");
    }
    private void messageRemoveBadge(int to_user_id, int from_userid, int badgeCount) {

        //pDialog.show();
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String sender_id = prefs.getString(Constants.strShPrefUserFBID, "");
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                messageRemoveBadge(to_user_id,from_userid, badgeCount, Constants.getUserId(mContext)),"messageRemoveBadge");
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("getChatToken")) {

                GetChatTokenRequest meetup_Obj=(GetChatTokenRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    String strUniqueChatId=meetup_Obj.getResult().getData().getUniqueChatid().toString().trim();

                    Bundle arguments = new Bundle();
                    arguments.putString("uniqueChatId", strUniqueChatId);
                    arguments.putString("receiverId", receiverId);
                    arguments.putString("to_userID", to_user_id);
                    arguments.putString("show_loader", meetup_Obj.getResult().getData().getIsMessageDone());
                    FragmentChat fragment = new FragmentChat();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_MSG)
                            .addToBackStack( Constants.TAG_FRAGMENT_MSG)
                            .commit();
                }

            }else if(method.equalsIgnoreCase("messageRemoveBadge")){

                MessageRemoveBadgeCountRequest req=(MessageRemoveBadgeCountRequest)response.body();
                if(!req.getResult().getError()){
//                    Toast.makeText(mContext, "badge count removed successfully", Toast.LENGTH_SHORT).show();

                  //  ((Activity)mContext).findViewById(R.id.)
                    getFooterCount();

                }
            }

            else if (method.equalsIgnoreCase("get_footer_count")) {
//                getFooterCounter();
                FooterCountRequest req_obj = (FooterCountRequest) response.body();
                if (!req_obj.getResult().getError()) {
//                    refreshFragment();
                    if (req_obj.getResult().getData().getMeetupRequest() > 0) {
                        ((Activity) mContext).findViewById(R.id.txt_reqst_counter_meetup).setVisibility(View.VISIBLE);
//                        ((Activity)mContext).findViewById(R.id.txt_reqst_counter_meetup).setText(String.valueOf(req_obj.getResult().getData().getMeetupRequest()));

                        TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_meetup);
                        textView.setText(String.valueOf(req_obj.getResult().getData().getMeetupRequest()));

                    }
                    else{
                        TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_meetup);
                        textView.setText("0");


                        textView.setVisibility(View.GONE);
                    }

                    if (req_obj.getResult().getData().getConnectionRequest() > 0) {
//                        txt_reqst_counter_connection.setVisibility(View.VISIBLE);
//                        txt_reqst_counter_connection.setText(String.valueOf(req_obj.getResult().getData().getConnectionRequest()));


                        TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_connection);
                        textView.setText(String.valueOf(req_obj.getResult().getData().getConnectionRequest()));

                    }

                    if (req_obj.getResult().getData().getBulletinRequest() > 0) {
//                        txt_reqst_counter_bulletin.setVisibility(View.VISIBLE);
//                        txt_reqst_counter_bulletin.setText(String.valueOf(req_obj.getResult().getData().getBulletinRequest()));

                        TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_bulletin);
                        textView.setText(String.valueOf(req_obj.getResult().getData().getBulletinRequest()));

                    }

                    if (req_obj.getResult().getData().getMessageRequest() > 0) {

                        Fragment frag = ((Activity) mContext).getFragmentManager().findFragmentById(R.id.contentContainer);

//                        if (!(frag instanceof FragmentChat)) {
//                            txt_reqst_counter_chat.setVisibility(View.VISIBLE);
//                            txt_reqst_counter_chat.setText(String.valueOf(req_obj.getResult().getData().getMessageRequest()));

                            TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_chat);
                            textView.setText(String.valueOf(req_obj.getResult().getData().getMessageRequest()));
//                        }
                    }
                    else{
                        Fragment frag = ((Activity) mContext).getFragmentManager().findFragmentById(R.id.contentContainer);

//                        if (!(frag instanceof FragmentChat)) {
//                            txt_reqst_counter_chat.setVisibility(View.VISIBLE);
//                            txt_reqst_counter_chat.setText(String.valueOf(req_obj.getResult().getData().getMessageRequest()));

                            TextView textView = (TextView) ((Activity) mContext).findViewById(R.id.txt_reqst_counter_chat);
                            textView.setText("0");
                            textView.setVisibility(View.GONE);
//                        }
                    }
//
                }
            }

            }
    }

    private void getFooterCount() {


        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getFooterCountSection(Constants.getUserId(mContext)), "get_footer_count");
//        pDialog.show();

    }



    @Override
    public void onFailure(String errorMsg) {
        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(mContext,errorMsg);
    }

    public interface MyAdapterListener {

        void iconProfileOnClick(View v, int position);
        //void iconImageViewOnClick(View v, int position);
    }


    public void animateTo(List<RecentMessageListData> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<RecentMessageListData> newModels) {
        if(mDataArray!=null) {
            for (int i = mDataArray.size() - 1; i >= 0; i--) {
                final RecentMessageListData model = mDataArray.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }
    }

    private void applyAndAnimateAdditions(List<RecentMessageListData> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final RecentMessageListData model = newModels.get(i);
            if (mDataArray!=null && !mDataArray.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<RecentMessageListData> newModels) {
        if(mDataArray!=null) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final RecentMessageListData model = newModels.get(toPosition);
                final int fromPosition = mDataArray.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }
    }


    public RecentMessageListData removeItem(int position) {
        final RecentMessageListData model = mDataArray.remove(position);

        notifyItemRemoved(position);
        return model;

    }

    public void addItem(int position, RecentMessageListData model) {
        mDataArray.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final RecentMessageListData model = mDataArray.remove(fromPosition);
        mDataArray.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void storeUserChatDetails(int from_user_id,int to_user_id)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();

        editor.putInt(Constants.strShPrefUserToChat, to_user_id);
        editor.putInt(Constants.strShPrefUserFromChat,from_user_id);


        editor.commit();
    }



    public static String convertDateFromMillis(String millis) {
        //long val = 1346524199000l;
          long val = Long.parseLong(millis);
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat(Constants.app_display_date_format+" "
                +Constants.app_display_time_format);

        String dateText = df2.format(date);

        System.out.println(dateText);

        return dateText;    }

}
