package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.fragment.FragmentReplyComment;
import com.nationality.model.MeetupCommentDtlsRespondChild;
import com.nationality.model.MeetupCommentLikeRequest;
import com.nationality.model.MeetupDetailsRespond;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class MeetupAllCommentListingAdapter extends RecyclerView.Adapter<MeetupAllCommentListingAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,RetrofitListener {

    Context mContext;
    int meetup_id;

    private List<MeetupDetailsRespond> mDataArray;

    ProgressDialog pDialog;
    RestHandler rest;
    private Integer count=0;
    private TextView txtLikeCount;
    private ImageView imgLike;
    private int posi=0;

    public MeetupAllCommentListingAdapter(List<MeetupDetailsRespond> dataset, Context con,int meetupId) {
        if(con!=null) {

            mDataArray = dataset;
            mContext = con;
            meetup_id = meetupId;

            pDialog=new ProgressDialog(con);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            rest=new RestHandler(con,this);

        }
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 10;
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

        if(mDataArray.get(position).getRespondLike()>1){

            holder.tv_likes.setText(String.valueOf(mDataArray.get(position).getRespondLike())+" Likes");
        }else{

            holder.tv_likes.setText(String.valueOf(mDataArray.get(position).getRespondLike())+" Like");
        }
        //holder.tv_likes.setText(String.valueOf(mDataArray.get(position).getRespondLike())+" Likes");
        if(mDataArray.get(position).getRespondReply()>1){
            holder.tv_reply.setText(String.valueOf(mDataArray.get(position).getRespondReply())+" Replies");
        }else{
            holder.tv_reply.setText(String.valueOf(mDataArray.get(position).getRespondReply())+" Reply");
        }

        holder.tv_date_time.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_date_format)+" at "+
                Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                        Constants.web_date_format,Constants.app_display_time_format)
        );

        if(mDataArray.get(position).getIs_like().equalsIgnoreCase(Constants.LIKE_YES)||mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))){
            holder.like_btn.setImageResource(R.drawable.like_grey);
            holder.like_btn.setEnabled(false);
        }

        Constants.setImage(holder.profile_pic, mDataArray.get(position).getProfilePic(), mContext);



    }


    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= mDataArray.size())
            return null;

        String name = mDataArray.get(pos).getName();
        if (name == null || name.length() < 1)
            return null;

        return mDataArray.get(pos).getName().substring(0, 1);
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
                    count=count+1;

                    mDataArray.get(posi).setIs_like("Yes");
                    mDataArray.get(posi).setRespondLike(mDataArray.get(posi).getRespondLike()+1);

                    notifyDataSetChanged();

                    /*if(count>1){
                        txtLikeCount.setText(count+" Likes");
                    }else{
                        txtLikeCount.setText(count+" Like");
                    }
                    imgLike.setImageResource(R.drawable.like_grey);*/

                    //
//                    meetup_like.setBackgroundResource(R.drawable.grey_rounded_bg);
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

        @Bind(R.id.like_btn)
        ImageView like_btn;

        @Bind(R.id.profile_pic)
        ImageView profile_pic;




//        @Bind(R.id.tv_comment)
//        TextView tv_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            txt_name.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_comment.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_date_time.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_likes.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_reply.setTypeface(Constants.typeFaceOpenSansReg(mContext));

            tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            new FragmentReplyComment().newInstance(mDataArray.get(getAdapterPosition()).getId(),
                                    mDataArray.get(getAdapterPosition()).getName(),
                                    mDataArray.get(getAdapterPosition()).getComment(),
                                    mDataArray.get(getAdapterPosition()).getUpdatedAt(),
                                    meetup_id,mDataArray.get(getAdapterPosition()).getRespondLike(),
                                    mDataArray.get(getAdapterPosition()).getRespondReply(),
                                    mDataArray.get(getAdapterPosition()).getProfilePic(),
                                    mDataArray.get(getAdapterPosition()).getUserId(),mDataArray.get(getAdapterPosition()).getIs_like()),
                            Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                            .addToBackStack( Constants.TAG_FRAGMENT_MEETUP_REPLY_COMMENTS)
                            .commit();

                }
            });

            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    posi=getAdapterPosition();

                   /* if(mDataArray.get(getAdapterPosition()).getIs_like().equalsIgnoreCase("no"))
                        postLiketoServer(mDataArray.get(getAdapterPosition()).getId());
*/
                    if(mDataArray.get(getAdapterPosition()).getIs_like().equalsIgnoreCase("no")&&!mDataArray.get(getAdapterPosition()).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))) {
                        postLiketoServer(mDataArray.get(getAdapterPosition()).getId());
                        count=mDataArray.get(getAdapterPosition()).getRespondLike();
                        txtLikeCount=(TextView)v.findViewById(R.id.tv_likes);
                        imgLike=(ImageView)v.findViewById(R.id.like_btn);
                    }

                }
            });



            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MeetupDetailsRespond col_data=mDataArray.get(getAdapterPosition());
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




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
//                            new FragmentBulletinCommentRespond(), Constants.TAG_FRAGMENT_BULLETIN_COMMENT_RESPOND)
//                            .addToBackStack( Constants.TAG_FRAGMENT_BULLETIN_COMMENT_RESPOND)
//                            .commit();
                }
            });
        }


    }

    private void postLiketoServer(int comment_id) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupCommentLike(Constants.getUserId(mContext),Constants.LIKE_YES,
                        comment_id),"likeComment");
    }

}