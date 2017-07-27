package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentChat;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AddFriendRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.GetChatTokenRequest;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class MessageConnectionListAdapter extends RecyclerView.Adapter<MessageConnectionListAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter, RetrofitListener {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<ConnectionListData> mDataArray;
    ProgressDialog pDialog;
    RestHandler rest;
    String senderid="", receiverId="", to_user_id="";
    private String chat_token="";

    public MessageConnectionListAdapter(List<ConnectionListData> dataset, Context con, String senderToken) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
            senderid = senderToken;
            pDialog = new ProgressDialog(con);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            rest = new RestHandler(con, this);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_connection_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
                mDataArray.get(position).getLastName());

        holder.txt_subheader.setText(mDataArray.get(position).getEmail());

        Constants.setImage(holder.profile_pic, mDataArray.get(position).getProfilePic(),mContext);

        holder.rel_main.setTag(position);
    }


    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= mDataArray.size())
            return null;

        String name = mDataArray.get(pos).getFirstName();
        if (name == null || name.length() < 1)
            return null;

        return  mDataArray.get(pos).getFirstName().substring(0, 1);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.mTextCity)
        TextView txt_subheader;

        @Bind(R.id.profile_pic)
       ImageView profile_pic;

        @Bind(R.id.rel_main)
        RelativeLayout rel_main;

        @Bind(R.id.img_msg)
        ImageView img_msg;

        /*@Bind(R.id.btn_add)
        Button btn_add;*/

        public ViewHolder(View itemView) {
            super(itemView);
           ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            rel_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos=Integer.parseInt(v.getTag().toString());
                    receiverId=mDataArray.get(pos).getFacebookId();
                    to_user_id=mDataArray.get(pos).getId().toString();
                    chat_token=mDataArray.get(pos).getTokenId();
                    //if(chat_token.equalsIgnoreCase("")||chat_token.toString().trim().isEmpty())
                    getChatToken(mDataArray.get(pos).getFacebookId());
                    /*else
                    {
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
                    }*/
                    //SearchUserConnectionRequest col_data=mDataArray.get(getAdapterPosition());

                }
            });

            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    to_user_id=mDataArray.get(getAdapterPosition()).getId().toString();
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


            img_msg.setVisibility(View.GONE);
        }



    }

    private void getChatToken(String facebookToken) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getChatToken(senderid,facebookToken, "Private"),"getChatToken");
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

            }

        }
        else{
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(mContext,response.code()+" "+response.message());
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
    public void onFailure(String errorMsg) {

        if(pDialog.isShowing())
            pDialog.dismiss();


        Constants.showAlert(mContext,errorMsg);

    }
}
