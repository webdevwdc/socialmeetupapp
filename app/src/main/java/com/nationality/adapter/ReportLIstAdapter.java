package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.fragment.FragmentReportPost;
import com.nationality.model.ConnectionListData;
import com.nationality.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by webskitters on 3/29/2017.
 */

public class ReportLIstAdapter extends RecyclerView.Adapter<ReportLIstAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,Filterable {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<ConnectionListData> mDataArray,filter_list=new ArrayList<>();
    boolean isFirstTime;

    private CustomFilter mFilter;;

    public ReportLIstAdapter(List<ConnectionListData> dataset, Context con) {
        mDataArray = dataset;
        mContext=con;
        mFilter = new CustomFilter(this);
        isFirstTime=true;
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_report_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
                mDataArray.get(position).getLastName());

        Constants.setImage(holder.profile_pic,mDataArray.get(position).getProfilePic(),mContext);



        holder.mTextCity.setText(mDataArray.get(position).getHomeCity());

        if (mDataArray.get(position).getHomeCity().equalsIgnoreCase("")){

            holder.message.setVisibility(View.GONE);
        }
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

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.mTextCity)
        TextView mTextCity;

        @Bind(R.id.profile_pic)
        ImageView profile_pic;

        @Bind(R.id.message)
        ImageView message;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConnectionListData col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    String full_name = mDataArray.get(getAdapterPosition()).getFirstName() + " " +
                            mDataArray.get(getAdapterPosition()).getLastName();

                    Bundle arguments = new Bundle();
                    arguments.putInt("to_user_id", mDataArray.get(getAdapterPosition()).getId());
                    arguments.putString("user_name", full_name);
                    arguments.putString("location", mDataArray.get(getAdapterPosition()).getHomeCity());
                    arguments.putString("image",mDataArray.get(getAdapterPosition()).getProfilePic());

                    FragmentReportPost fragment = new FragmentReportPost();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_REPORT_POST)
                            .addToBackStack( Constants.TAG_FRAGMENT_REPORT_POST)
                            .commit();
                }
            });


            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ConnectionListData col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", col_data.getId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();


                }
            });

        }


    }

    public class CustomFilter extends Filter {
        private ReportLIstAdapter mAdapter;
        private CustomFilter(ReportLIstAdapter mAdapter) {
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
                for (final ConnectionListData mWords : filter_list) {
                    if (mWords.getFirstName().toLowerCase().contains(filterPattern.toLowerCase())) {
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
            System.out.println("Count Number 2 " + ((List<ConnectionListData>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }    }

}