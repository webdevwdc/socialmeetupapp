package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.fragment.FragmentBulletinMessageRespond;
import com.nationality.fragment.FragmentBulletinReplyList;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.BulletinCommentReplyRequest;
import com.nationality.model.BulletinDetailsRespond;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.R;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by webskitters on 3/29/2017.
 */

public class BulletinDetailsAdapter extends RecyclerView.Adapter<BulletinDetailsAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter, RetrofitListener {

    private RestHandler rest;
    private ProgressDialog pDialog;
    Context mContext;
    String title;
    private List<BulletinDetailsRespond> mDataArray;
    TextView txtlikeCount;
    int count=0;
    LinearLayout linlike;

    int itemPos=0;
    public BulletinDetailsAdapter(List<BulletinDetailsRespond> dataset, String mtitle, Context con) {
        mDataArray = dataset;
        title=mtitle;
        mContext=con;
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 10;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_respond_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mDataArray.get(position).getName().length()>8){
            String[] name=mDataArray.get(position).getName().toString().split(" ");
            if(name.length>1)
            holder.txt_name.setText(name[0]+"\n"+name[1]);
            else holder.txt_name.setText(name[0]);
        }else holder.txt_name.setText(mDataArray.get(position).getName().toString());

        holder.txt_comment.setText(mDataArray.get(position).getComment());
        String[] datetime=mDataArray.get(position).getCreatedAt().split(" ");
        holder.txt_date_time.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_date_format)+" at "+
                Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                        Constants.web_date_format,Constants.app_display_time_format));

        if(mDataArray.get(position).getRespondReply()>1)
            holder.txt_reply_count.setText("("+mDataArray.get(position).getRespondReply()+") Replies");
        else
            holder.txt_reply_count.setText("("+mDataArray.get(position).getRespondReply()+") Reply");

        if(mDataArray.get(position).getRespondLike()>1)
            holder.txt_like_count.setText("("+mDataArray.get(position).getRespondLike()+") Likes");
        else
            holder.txt_like_count.setText("("+mDataArray.get(position).getRespondLike()+") Like");


        if(mDataArray.get(position).getIsLike().equalsIgnoreCase(Constants.LIKE_YES)){
            holder.lin_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.lin_like.setEnabled(false);
        }/*else{
            holder.lin_like.setBackgroundResource(R.drawable.green_rounded_bg);
            holder.lin_like.setEnabled(true);
        }
*/
        if(mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))){
            holder.lin_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.lin_like.setEnabled(false);
        }

        Constants.setImage(holder.img_profile_pic, mDataArray.get(position).getProfilePic(), mContext);
        holder.lin_like.setTag(position);
        holder.txt_like_count.setTag(position);


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
    public void onSuccess(Call call, retrofit2.Response response, String method) {

        if(pDialog.isShowing())
        pDialog.dismiss();
        if(response!=null && response.code()==200){
/*
            linlike.setBackgroundResource(R.drawable.grey_rounded_bg);
            linlike.setEnabled(false);*/
            count=count+1;
           /* if(count>1)
                txtlikeCount.setText("("+count+") Likes");
            else
                txtlikeCount.setText("("+count+") Like");
           */
            mDataArray.get(itemPos).setIsLike(Constants.LIKE_YES);
            mDataArray.get(itemPos).setRespondLike(count);
           notifyDataSetChanged();

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

        Constants.showAlert(mContext,errorMessage);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView txt_name;

        @Bind(R.id.tv_comment)
        TextView txt_comment;

        @Bind(R.id.txt_date_time)
        TextView txt_date_time;

        @Bind(R.id.tv_reply_count)
        TextView txt_reply_count;

        @Bind(R.id.tv_like_count)
        TextView txt_like_count;

        @Bind(R.id.lin_like)
        LinearLayout lin_like;

        @Bind(R.id.lin_reply)
        LinearLayout lin_reply;

        @Bind(R.id.tv_like_btn)
        TextView tv_like_btn;

        @Bind(R.id.tv_reply_btn)
        TextView tv_reply_btn;

        @Bind(R.id.img_profile_pic)
        ImageView img_profile_pic;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            txt_name.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_comment.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_date_time.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_reply_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_like_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_reply_btn.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            tv_like_btn.setTypeface(Constants.typeFaceOpenSansReg(mContext));


            lin_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!mDataArray.get(getAdapterPosition()).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))) {
                        itemPos = Integer.parseInt(v.getTag().toString());
                        linlike = (LinearLayout) v;
                        RelativeLayout parent = (RelativeLayout) v.getParent();
                        txtlikeCount = (TextView) parent.findViewById(R.id.tv_like_count);
                        count = mDataArray.get(itemPos).getRespondLike();
                        commentLikeBulletin(mDataArray.get(itemPos).getId());
                    }
                }
            });

            txt_like_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("comment_id", mDataArray.get(getAdapterPosition()).getId());
                    arguments.putString("bulletin_id", mDataArray.get(getAdapterPosition()).getBulletinId());

                    FragmentBulletinReplyList fragment = new FragmentBulletinReplyList();
                    fragment.setArguments(arguments);
                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment, Constants.TAG_FRAGMENT_BULLETIN_REPLY_LIST)
                            .addToBackStack( Constants.TAG_FRAGMENT_BULLETIN_REPLY_LIST)
                            .commit();
                }
            });

            txt_reply_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("comment_id", mDataArray.get(getAdapterPosition()).getId());
                    arguments.putString("bulletin_id", mDataArray.get(getAdapterPosition()).getBulletinId());

                    FragmentBulletinReplyList fragment = new FragmentBulletinReplyList();
                    fragment.setArguments(arguments);
                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment, Constants.TAG_FRAGMENT_BULLETIN_REPLY_LIST)
                            .addToBackStack( Constants.TAG_FRAGMENT_BULLETIN_REPLY_LIST)
                            .commit();
                }
            });

            lin_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle arguments = new Bundle();
                    arguments.putInt("parent_bulletin_id", mDataArray.get(getAdapterPosition()).getId());
                    arguments.putString("bulletin_id", mDataArray.get(getAdapterPosition()).getBulletinId());
                    arguments.putString("comment", mDataArray.get(getAdapterPosition()).getComment());
                    String[] strDateTime=mDataArray.get(getAdapterPosition()).getCreatedAt().split(" ");
                    arguments.putString("date", strDateTime[0]);
                    arguments.putString("time", strDateTime[1]);
                    arguments.putString("title", title);
                    FragmentBulletinMessageRespond fragment = new FragmentBulletinMessageRespond();
                    fragment.setArguments(arguments);
                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment, Constants.TAG_FRAGMENT_BULLETIN_COMMENT_RESPOND)
                            .addToBackStack( Constants.TAG_FRAGMENT_BULLETIN_COMMENT_RESPOND)
                            .commit();
                }
            });


            img_profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle arguments1 = new Bundle();
                    arguments1.putString("tag", "");
                    arguments1.putString("interest", "");
                    arguments1.putInt("id", Integer.parseInt(mDataArray.get(getAdapterPosition()).getUserId()));

                    FragmentProfile fragment2 = new FragmentProfile();
                    fragment2.setArguments(arguments1);

                    ((Activity)mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment2, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();

                }
            });

        }
    }

    private void commentLikeBulletin(int comment_id) {
        rest=new RestHandler(mContext,this);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinCommentLikeRequest(Constants.getUserId(mContext),"Yes",comment_id),"getCommentLikeBulletin");
        pDialog.show();

    }
}
