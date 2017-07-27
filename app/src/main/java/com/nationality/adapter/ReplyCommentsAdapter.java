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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentBulletinDetails;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AllMeetupData;
import com.nationality.model.BulletinListData;
import com.nationality.model.MeetupCommentDtlsRespondChild;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.CircleTransform;
import com.nationality.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class ReplyCommentsAdapter extends RecyclerView.Adapter<ReplyCommentsAdapter.ViewHolder> implements RetrofitListener
{

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    ProgressDialog pDialog;
    RestHandler rest;

    private List<MeetupCommentDtlsRespondChild> mDataArray;
    private Integer count;
    private TextView txtLikeCount;
    private ImageView imgLike;
    private int posi=0;

    public ReplyCommentsAdapter(List<MeetupCommentDtlsRespondChild> dataset, Context con) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;

            pDialog=new ProgressDialog(con);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            rest=new RestHandler(con,this);

        }
    }

    @Override
    public int getItemCount() {
//        if (mDataArray == null)
//            return 10;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reply_comments, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txt_name.setText(mDataArray.get(position).getName());
        holder.txt_comment.setText(mDataArray.get(position).getComment());
        //holder.tv_likes.setText(String.valueOf(mDataArray.get(position).getRespondLike())+" Likes");

        if(mDataArray.get(position).getRespondLike()>1){
            holder.tv_likes.setText(mDataArray.get(position).getRespondLike()+" Likes");
        }else{
            holder.tv_likes.setText(mDataArray.get(position).getRespondLike()+" Like");
        }

        if(mDataArray.get(position).getRespondReply()>1){
            holder.tv_reply.setText(mDataArray.get(position).getRespondReply()+" Replies");
        }else{
            holder.tv_reply.setText(mDataArray.get(position).getRespondReply()+" Reply");
        }

        holder.tv_reply.setVisibility(View.INVISIBLE);

        if(mDataArray.get(position).getIsLike().equalsIgnoreCase(Constants.LIKE_YES)||mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))){
            holder.like_btn.setImageResource(R.drawable.like_grey);
            holder.like_btn.setEnabled(false);
        }

        holder.tv_date_time.setText(Constants.changeDateFormat(mDataArray.get(position).getCreatedAt(),Constants.web_date_format,Constants.app_display_date_format)
                +" at "+Constants.changeDateFormat(mDataArray.get(position).getCreatedAt(),Constants.web_date_format,Constants.app_display_time_format));

        Constants.setImage(holder.profile_pic,mDataArray.get(position).getProfilePic(),mContext);

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

        @Bind(R.id.profile_pic)
        ImageView profile_pic;

        @Bind(R.id.like_btn)
        ImageView like_btn;

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

            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mDataArray.get(getAdapterPosition()).getIsLike().equalsIgnoreCase("no")
                            &&  !(String.valueOf(mDataArray.get(getAdapterPosition()).getUserId()).equalsIgnoreCase(""+Constants.getUserId(mContext)))){

                        posi=getAdapterPosition();

                        postLiketoServer(mDataArray.get(getAdapterPosition()).getId());
//                        count=mDataArray.get(getAdapterPosition()).getRespondLike();
//                        txtLikeCount=(TextView)v.findViewById(R.id.tv_likes);
//                        imgLike=(ImageView)v.findViewById(R.id.like_btn);
                    }
                }
            });
//            my_recycler_view.setVisibility(View.GONE);

            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MeetupCommentDtlsRespondChild col_data=mDataArray.get(getAdapterPosition());
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

    private void postLiketoServer(int comment_id) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupCommentLike(Constants.getUserId(mContext),Constants.LIKE_YES,
                        comment_id),"likeComment");
    }


    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("likeComment")) {

                MeetupCommentLikeRequest meetup_Obj=(MeetupCommentLikeRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {
                    //
//                    meetup_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                   /* count=count+1;
                    if(count>1){
                        txtLikeCount.setText(count+" Likes");
                    }else{
                        txtLikeCount.setText(count+" Like");
                    }
                    imgLike.setImageResource(R.drawable.like_grey);*/

                    mDataArray.get(posi).setIsLike("yes");
                    mDataArray.get(posi).setRespondLike(mDataArray.get(posi).getRespondLike()+1);

                    notifyDataSetChanged();
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

}
