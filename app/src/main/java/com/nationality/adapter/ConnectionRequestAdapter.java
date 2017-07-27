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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.FriendAcceptRejectRequest;
import com.nationality.model.FriendListDatum;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.OnClickCallBack;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class ConnectionRequestAdapter extends RecyclerView.Adapter<ConnectionRequestAdapter.ViewHolder>
        implements RetrofitListener {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    RestHandler rest;
    int posi_del=-1;

    private List<FriendListDatum> mDataArray;
    private ProgressDialog pDialog;
    private String event_name;
    OnClickCallBack mCallback;

    public ConnectionRequestAdapter(List<FriendListDatum> dataset, Context con, OnClickCallBack mCallback) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;

            rest = new RestHandler(con, this);

            event_name="";

            pDialog = new ProgressDialog(con);
            pDialog.setMessage(con.getString(R.string.dialog_msg));
            pDialog.setCancelable(false);
            this.mCallback=mCallback;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        else
        return mDataArray.size();
    }

    @Override
    public ConnectionRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_connection_requests, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.mTextView.setText(mDataArray.get(position));

        holder.tv_name.setText(mDataArray.get(position).getName());
        holder.tv_location.setText(mDataArray.get(position).getHomeCity());
        Constants.setImage(holder.image_profile,mDataArray.get(position).getProfilePic(),mContext);

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tv_name;

        @Bind(R.id.btn_accept)
        ImageView btn_accept;

        @Bind(R.id.btn_reject)
        ImageView btn_reject;


        @Bind(R.id.tv_location)
        TextView tv_location;

        //image_profile

        @Bind(R.id.image_profile)
        ImageView image_profile;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tv_name.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_location.setTypeface(Constants.typeFaceOpenSansReg(mContext));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendListDatum col_data = mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getFromUserid()));

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack(Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();
                }
            });

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    posi_del=getAdapterPosition();

                    event_name=Constants.ACCEPT;
                    requestServer(Constants.ACCEPT,getAdapterPosition());

                }
            });

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    posi_del=getAdapterPosition();
                    event_name=Constants.REJECT;
                    requestServer(Constants.REJECT,getAdapterPosition());

                }
            });
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("accept_reject")) {

                FriendAcceptRejectRequest meetup_Obj=(FriendAcceptRejectRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    //
//                    current_btn_like.setBackgroundResource(R.drawable.grey_rounded_bg);

                    //remove row
                    if(event_name.equalsIgnoreCase(Constants.REJECT))
                    Toast.makeText(mContext,"Request rejected succesfully",Toast.LENGTH_LONG).show();
                    else if(event_name.equals(Constants.ACCEPT))
                        Toast.makeText(mContext,"Request accepted succesfully",Toast.LENGTH_LONG).show();

                    if(posi_del!=-1)
                        removeAt(posi_del);

                }
            }
        }
        else {
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(mContext, response.code() + " " + response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            } catch (NullPointerException e) {
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(mContext,errorMessage);

    }


    private void requestServer(String action,int position)
    {

        /*{
"result": {
"error": false,
"data": [
{
"id": 33,
"to_userid": "3",
"from_userid": "8",
"is_accept": "Pending",
"created_at": "2017-05-10 09:43:21",
"accept_date_time": "2017-05-10 09:43:21",
"updated_at": "2017-05-10 09:43:21",
"name": "Jeeban Mondal",
"profile_pic": "no_image.jpeg",
"home_city": "Kolkata"
}
]
}
}*/

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                userAcceptReject(mDataArray.get(position).getFromUserid(),
                        mDataArray.get(position).getToUserid(),action),"accept_reject");


    }

    public void removeAt(int position) {
        mDataArray.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataArray.size());

        if(mDataArray.size()==0)
            mCallback.onUIupdate();
    }

}
