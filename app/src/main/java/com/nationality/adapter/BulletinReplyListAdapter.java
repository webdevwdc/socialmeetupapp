package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.BulletinCommentLikeRequest;
import com.nationality.model.BulletinListData;
import com.nationality.model.BulletinReplyListData;
import com.nationality.model.BulletinReplyListReplyChild;
import com.nationality.model.BulletinReplyListRequest;
import com.nationality.model.MeetupCommentDtlsRespondChild;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

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

public class BulletinReplyListAdapter extends RecyclerView.Adapter<BulletinReplyListAdapter.ViewHolder> implements RetrofitListener
{
    Activity mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<BulletinReplyListReplyChild> mDataArray;
    RestHandler rest;
    ProgressDialog pDialog;

    TextView tvLikes;
    int like_count=0;
    ImageView imgLike;

    int itemPos=0;

    boolean isMyBulletin=false;

    public BulletinReplyListAdapter(List<BulletinReplyListReplyChild> dataset, Activity con, boolean myBulletin) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
            this.isMyBulletin=myBulletin;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bulletin_reply_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        rest=new RestHandler(mContext,this);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txt_name.setText(mDataArray.get(position).getName());
        holder.txt_comment.setText(mDataArray.get(position).getComment());

        holder.tv_date_time.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_date_format)+" at "+
                Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                        Constants.web_date_format,Constants.app_display_time_format));

        if(mDataArray.get(position).getRespondLike()>1)
            holder.tv_likes.setText(mDataArray.get(position).getRespondLike()+" Likes");
        else
            holder.tv_likes.setText(mDataArray.get(position).getRespondLike()+" Like");

        if(mDataArray.get(position).getRespondReply()>1)
            holder.tv_reply.setText(mDataArray.get(position).getRespondReply()+" Replies");
        else
            holder.tv_reply.setText(mDataArray.get(position).getRespondReply()+" Reply");

        holder.tv_likes.setTag(position);
        holder.img_like.setTag(position);
        if(mDataArray.get(position).getIsLike().equalsIgnoreCase(Constants.LIKE_YES)){
            holder.img_like.setImageResource(R.drawable.like_grey);
            holder.img_like.setEnabled(false);
            holder.tv_likes.setEnabled(false);

        }
        if(mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))){
            holder.img_like.setImageResource(R.drawable.like_grey);
            holder.img_like.setEnabled(false);
            holder.tv_likes.setEnabled(false);
        }


        Constants.setImage(holder.img_profile_pic, mDataArray.get(position).getProfilePic(), mContext);

    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {
            BulletinCommentLikeRequest req_Obj = (BulletinCommentLikeRequest) response.body();
            if (!req_Obj.getResult().getError()) {
                like_count=like_count+1;
                mDataArray.get(itemPos).setIsLike(Constants.LIKE_YES);
                mDataArray.get(itemPos).setRespondLike(like_count);
                notifyDataSetChanged();

            } else {
                try {
                    response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
//                showAlertDialog("Alert",response.code()+" "+response.message());
                } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                    e.printStackTrace();
                } catch (NullPointerException e) {
//                showAlertDialog("Alert","Server Response Error..");
                }
            }
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(mContext,errorMessage);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_name)
        TextView txt_name;
        @Bind(R.id.txt_comment)
        TextView txt_comment;
        @Bind(R.id.tv_date_time)
        TextView tv_date_time;
        @Bind(R.id.tv_likes)
        TextView tv_likes;
        @Bind(R.id.tv_reply)
        TextView tv_reply;

        @Bind(R.id.img_like)
        ImageView img_like;

        @Bind(R.id.img_profile_pic)
        ImageView img_profile_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tv_likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!mDataArray.get(getAdapterPosition()).getIsLike().equalsIgnoreCase(Constants.LIKE_YES)&&!isMyBulletin) {
                        itemPos = Integer.parseInt(v.getTag().toString());

                        LinearLayout parent = (LinearLayout) v.getParent();
                        imgLike=(ImageView)parent.findViewById(R.id.img_like);
                        tvLikes = (TextView) parent.findViewById(R.id.tv_likes);
                        like_count = mDataArray.get(itemPos).getRespondLike();
                        commentLike(mDataArray.get(itemPos).getId());
                    }
                }
            });
            img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mDataArray.get(getAdapterPosition()).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))) {
                        itemPos = Integer.parseInt(v.getTag().toString());

                        LinearLayout parent = (LinearLayout) v.getParent();
                        imgLike=(ImageView)parent.findViewById(R.id.img_like);
                        tvLikes = (TextView) parent.findViewById(R.id.tv_likes);
                        like_count = mDataArray.get(itemPos).getRespondLike();
                        commentLike(mDataArray.get(itemPos).getId());
                    }
                }
            });

            img_profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    BulletinReplyListReplyChild col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getUserId()));

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();

                }
            });

            txt_name.setTypeface(typeFaceOpenSansBold);
            txt_comment.setTypeface(typeFaceOpenSansReg);
            tv_date_time.setTypeface(typeFaceOpenSansReg);
            tv_likes.setTypeface(typeFaceOpenSansReg);
            tv_reply.setTypeface(typeFaceOpenSansReg);
        }



    }

    private void commentLike(int comment_id) {
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinCommentLikeRequest(Constants.getUserId(mContext), "Yes", comment_id),"getBulletinCommentLike");
        pDialog.show();
    }

}
