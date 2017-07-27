package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentBulletinDetails;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AllMeetupData;
import com.nationality.model.BulletinListData;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class BulletinListAdapter extends RecyclerView.Adapter<BulletinListAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter, RetrofitListener,Filterable{

    private RestHandler rest;
    private ProgressDialog pDialog;
    Context mContext;
//    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    int itemPos=0;
    RelativeLayout rel_like;
    TextView txtlikeCount;
    int count=0;
    boolean isFirstTime;
    private List<BulletinListData> mDataArray,filter_list=new ArrayList<>();


    private final CustomFilter mFilter;

    public BulletinListAdapter(List<BulletinListData> dataset, Context con) {
        mDataArray = dataset;
        mContext=con;
        mFilter = new CustomFilter(this);
        isFirstTime=true;
    }

    @Override
    public int getItemCount() {
//        if (mDataArray == null)
//            return 10;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bulletin_list, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_title.setText(mDataArray.get(position).getTitle());
        holder.txt_desc.setText(mDataArray.get(position).getContent());

        //////////Setting date and time of each bulletin/////////////////
        String[] datetime=mDataArray.get(position).getCreatedAt().split(" ");
        holder.txt_date.setText(Constants.changeDateFormat(mDataArray.get(position).getCreatedAt(),
                Constants.web_date_format,Constants.app_display_date_format));
        holder.txt_time.setText(Constants.changeDateFormat(mDataArray.get(position).getCreatedAt(),
                Constants.web_date_format,Constants.app_display_time_format));

        //////////////////////Like, View and Reply view text set/////////////////

        if(mDataArray.get(position).getTotalView()>1)
            holder.txt_view_count.setText(mDataArray.get(position).getTotalView()+" Views");
        else
            holder.txt_view_count.setText(mDataArray.get(position).getTotalView()+" View");

        if(mDataArray.get(position).getTotalReply()>1)
            holder.txt_reply_count.setText(mDataArray.get(position).getTotalReply()+" Replies");
        else
            holder.txt_reply_count.setText(mDataArray.get(position).getTotalReply()+" Reply");

        if(mDataArray.get(position).getTotalLike()>1)

            holder.txt_like_count.setText(mDataArray.get(position).getTotalLike()+" Likes");
        else
            holder.txt_like_count.setText(mDataArray.get(position).getTotalLike()+" Like");

        /*if(mDataArray.get(position).getUserId().equals(String.valueOf(Constants.getUserId(mContext))))
        {
            holder.fl_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.fl_like.setEnabled(false);
        }*/
        holder.fl_like.setTag(position);
        /*if(mDataArray.get(position).getIsLike().equalsIgnoreCase(Constants.LIKE_YES))
        {
            holder.fl_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.fl_like.setEnabled(false);
        }*/
        if(mDataArray.get(position).getIsLike().equalsIgnoreCase("yes")) {

            holder.fl_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.fl_like.setEnabled(false);
        }
        else {
            if(mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext))) {
                holder.fl_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                holder.fl_like.setEnabled(false);
            }
            else{
                holder.fl_like.setBackgroundResource(R.drawable.green_rounded_bg);
                holder.fl_like.setEnabled(true);}
        }

        Constants.setImage(holder.img_profile_pic, mDataArray.get(position).getBulletinCreatorPic(), mContext);

        //////////////////Creator name and profile pic////////////////

        holder.txt_creator_name.setText(mDataArray.get(position).getBulletinCreator());

//        notifyItemRemoved(position);
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= mDataArray.size())
            return null;

        String name = mDataArray.get(pos).getContent();
        if (name == null || name.length() < 1)
            return null;

        return mDataArray.get(pos).getContent().substring(0, 1);
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();
        if(response!=null && response.code()==200){

            rel_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            rel_like.setEnabled(false);
            count=count+1;
            /*if(count>1)
                txtlikeCount.setText(count+" Likes");
            else
                txtlikeCount.setText(count+" Like");*/
            mDataArray.get(itemPos).setIsLike(Constants.LIKE_YES);
            mDataArray.get(itemPos).setTotalLike(count);
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

        @Bind(R.id.txt_title)
        TextView txt_title;
        @Bind(R.id.txt_desc)
        TextView txt_desc;
        @Bind(R.id.txt_user_name)
        TextView txt_creator_name;
        @Bind(R.id.txt_date)
        TextView txt_date;
        @Bind(R.id.txt_time)
        TextView txt_time;
        @Bind(R.id.fl_like)
        RelativeLayout fl_like;
        @Bind(R.id.txt_view_count)
        TextView txt_view_count;
        @Bind(R.id.txt_reply_count)
        TextView txt_reply_count;
        @Bind(R.id.txt_like_count)
        TextView txt_like_count;

        @Bind(R.id.profile_pic)
        ImageView img_profile_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle=new Bundle();
                    bundle.putInt("bulletin_id", mDataArray.get(getAdapterPosition()).getId());

                    FragmentBulletinDetails fragment=new FragmentBulletinDetails();

                    fragment.setArguments(bundle);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment,
                            Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                            .addToBackStack(Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                            .commit();
                }
            });

            fl_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(mDataArray.get(getAdapterPosition()).getUserId().equals(Constants.getUserId(mContext))))
                    {

                        itemPos=Integer.parseInt(v.getTag().toString());
                        rel_like=(RelativeLayout) v;
                        LinearLayout parent=(LinearLayout) v.getParent();
                        LinearLayout lin_main=(LinearLayout)parent.getParent();
                        LinearLayout lin_p=(LinearLayout)lin_main.findViewById(R.id.lin_content_parent);
                        LinearLayout lin_c=(LinearLayout)lin_p.findViewById(R.id.lin_view_reply_count);
                        txtlikeCount=(TextView)lin_c.findViewById(R.id.txt_like_count);
                        count=mDataArray.get(itemPos).getTotalLike();
                        likeBulletin(mDataArray.get(itemPos).getId());

                    }
                }
            });

            img_profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BulletinListData col_data=mDataArray.get(getAdapterPosition());
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

            txt_title.setTypeface(Constants.typeFaceOpenSansBold(mContext));
            txt_desc.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_creator_name.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_date.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_time.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_like_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_reply_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_view_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
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

    private void likeBulletin(int comment_id) {
        rest=new RestHandler(mContext,this);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                bulletinLikeRequest(Constants.getUserId(mContext),"Yes",comment_id),"getCommentLikeBulletin");
        pDialog.show();

    }


    public class CustomFilter extends Filter {
        private BulletinListAdapter mAdapter;
        private CustomFilter(BulletinListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;

        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(isFirstTime) {
                filter_list.addAll(mDataArray);
                isFirstTime=false;
            }
            mDataArray.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                mDataArray.addAll(filter_list);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final BulletinListData mWords : filter_list) {
                    if (mWords.getTitle().toLowerCase().contains(filterPattern.toLowerCase())) {
                        mDataArray.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + mDataArray.size());
            results.values = mDataArray;
            results.count = mDataArray.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<BulletinListData>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }




}