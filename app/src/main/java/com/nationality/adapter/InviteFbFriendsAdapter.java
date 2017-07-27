package com.nationality.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.model.BulletinListRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.FbInviteFriendRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.CircleTransform;
import com.nationality.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by android on 4/5/17.
 */

public class InviteFbFriendsAdapter extends RecyclerView.Adapter<InviteFbFriendsAdapter.ViewHolder> implements RetrofitListener {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<ConnectionListData> filter_list=new ArrayList<>();

    private List<HashMap<String, String>> mDataArray;
    private ProgressDialog pDialog;

    RestHandler rest;

    public InviteFbFriendsAdapter(List<HashMap<String, String>> dataset, Context con) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
        }
        rest=new RestHandler(mContext,this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_fb_users, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).get("name"));

        Picasso.with(mContext)
                .load("https://graph.facebook.com/" + mDataArray.get(position).get("id")+ "/picture?type=large")
                .fit().placeholder(R.drawable.mtup_small_otherimg).centerInside().transform(new CircleTransform())
                .into(holder.profile_pic);

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(mContext);
                pDialog.setMessage(mContext.getString(R.string.dialog_msg));
                pDialog.setCancelable(false);

                rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                        facebook_add_friend(mDataArray.get(position).get("id"),Constants.getUserId(mContext)),"facebook_add_friend");

                pDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.mTextCity)
        TextView mTextCity;

        //profile_pic
        @Bind(R.id.profile_pic)
        ImageView profile_pic;

        @Bind(R.id.btn_add)
        Button btn_add;


        public ViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e){
                e.printStackTrace();
            }

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {


        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("facebook_add_friend")) {
                FbInviteFriendRequest req_Obj = (FbInviteFriendRequest) response.body();
                if(!req_Obj.getResult().getError())

                    Constants.showAlert(mContext, req_Obj.getResult().getMessage());
                else
                    Toast.makeText(mContext, req_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();

//                getAllTeeTimes();


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

        Constants.showAlert(mContext,errorMessage);

    }

}
