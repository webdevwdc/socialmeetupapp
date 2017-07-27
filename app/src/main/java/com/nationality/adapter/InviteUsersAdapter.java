package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentAddMeetup;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.BulletinListData;
import com.nationality.model.ConnectionListData;
import com.nationality.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by webskitters on 3/29/2017.
 */

public class InviteUsersAdapter extends RecyclerView.Adapter<InviteUsersAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter, Filterable {

    private boolean isFirstTime;
    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private final CustomFilter mFilter;
    ViewGroup flowContainer;

    private List<ConnectionListData> filter_list=new ArrayList<>();

    private List<ConnectionListData> mDataArray;
    //List<ConnectionListData> friendArray;
    String callFrom;

    public InviteUsersAdapter(List<ConnectionListData> dataset, /*List<ConnectionListData> friendArray,*/ ViewGroup flowContainer, Context con, String type) {
        if(con!=null) {
            mDataArray = dataset;
            //this.friendArray = friendArray;
            this.flowContainer = flowContainer;
            mContext = con;

            isFirstTime = true;
            this.callFrom = type;
        }
        mFilter = new CustomFilter(this);
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_users, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
                        mDataArray.get(position).getLastName());

        holder.mTextCity.setText(mDataArray.get(position).getHomeCity());

        Constants.setImage(holder.profile_pic,mDataArray.get(position).getProfilePic(),mContext);



        if (callFrom.equalsIgnoreCase("add")) {

            if (getStat(position)) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
        }else {

            String posStr = FragmentAddMeetup.position;
            if (!posStr.equalsIgnoreCase("")) {
                if (getStatEdit(Integer.parseInt(posStr), position)) {
                    holder.checkbox.setChecked(true);
                    boolean shallAdd = true;
                    for (int i=0; i< FragmentAddMeetup.friendArray.size(); i++) {
                        if (FragmentAddMeetup.friendArray.get(i).getId()==mDataArray.get(position).getId()){
                            shallAdd = false;
                            break;
                        }
                    }
                    if (shallAdd){
                        FragmentAddMeetup.friendArray.add(mDataArray.get(position));
                    }
                } else {
                    holder.checkbox.setChecked(false);
                }
            }

        }




        /*if (getStat(position)){
            holder.checkbox.setChecked(true);
        }else {
            holder.checkbox.setChecked(false);
        }*/

        FragmentAddMeetup.addToFlowLayout(mContext, flowContainer, FragmentAddMeetup.friendArray);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FragmentAddMeetup.friendArray.size()>0) {
                    boolean shallAdd = true;
                    for (int i = 0; i < FragmentAddMeetup.friendArray.size(); i++){
                        if (FragmentAddMeetup.friendArray.get(i).getId() == mDataArray.get(position).getId()){
                            FragmentAddMeetup.friendArray.remove(FragmentAddMeetup.friendArray.get(i));
                            shallAdd = false;
                            break;
                        }
                    }
                    if (shallAdd){
                        FragmentAddMeetup.friendArray.add(mDataArray.get(position));
                    }
                }else {
                    FragmentAddMeetup.friendArray.add(mDataArray.get(position));
                }
                FragmentAddMeetup.addToFlowLayout(mContext, flowContainer, FragmentAddMeetup.friendArray);


                /*if ( ((CheckBox)v).isChecked() ) {
                    // perform logic
                    for (int i=0; i<mDataArray.size(); i++) {
                        if (friendArray.get(i).getId() == mDataArray.get(position).getId()) {
                            // Already added
                        } else {
                            friendArray.add(mDataArray.get(position));
                        }
                    }
                }else {
                    for (int i=0; i<mDataArray.size(); i++) {
                        if (friendArray.get(i).getId() == mDataArray.get(position).getId()) {
                            friendArray.remove(mDataArray.get(position));
                        } else {
                            // Not there
                        }
                    }
                }*/
            }
        });
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
        TextView mTextCity;

        //profile_pic
        @Bind(R.id.profile_pic)
        ImageView profile_pic;

        @Bind(R.id.checkbox)
        CheckBox checkbox;


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

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConnectionListData col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", col_data.getTag());
                    arguments.putString("interest", col_data.getInterest());
                    arguments.putInt("id", col_data.getId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();
                }
            });*/
        }
    }


    public class CustomFilter extends Filter {
        private InviteUsersAdapter mAdapter;
        private CustomFilter(InviteUsersAdapter mAdapter) {
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

                for (int i=0; i<filter_list.size(); i++){
                    String fullName = filter_list.get(i).getFirstName().toLowerCase()+" "+filter_list.get(i).getLastName().toLowerCase();
                    if (fullName.contains(filterPattern.toLowerCase())){
                        mDataArray.add(filter_list.get(i));
                    }
                }

                /*for (final ConnectionListData mWords : filter_list) {
                    String fullName = mWords.getFirstName().toLowerCase()+" "+mWords.getLastName().toLowerCase();
                    *//*if (mWords.getFirstName().toLowerCase().contains(filterPattern.toLowerCase()) ||
                            mWords.getLastName().toLowerCase().contains(filterPattern.toLowerCase())) {
                        mDataArray.add(mWords);
                    }*//*
                    if (fullName.contains(filterPattern.toLowerCase())) {
                        mDataArray.add(mWords);
                    }
                }*/
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

    public boolean getStat(int pos){
        boolean isCheck = false;

        if (FragmentAddMeetup.friendArray.size()>0){
            for (int i=0; i<FragmentAddMeetup.friendArray.size(); i++) {
                if (FragmentAddMeetup.friendArray.get(i).getId()==mDataArray.get(pos).getId()) {
                    //holder.checkbox.setChecked(true);
                    isCheck = true;
                    break;
                }
            }
            //FragmentAddMeetup.addToFlowLayout(mContext, flowContainer, FragmentAddMeetup.friendArray);
        }
        else {
            //holder.checkbox.setChecked(false);
        }

        return  isCheck;
    }

    public boolean getStatEdit(int posClicked, int pos){
        boolean isCheck = false;

        if (MeetupAdapterList.meet_list.get(posClicked).getAttendee().size()>0){
            for (int i=0; i<MeetupAdapterList.meet_list.get(posClicked).getAttendee().size(); i++) {
                if (Integer.parseInt(MeetupAdapterList.meet_list.get(posClicked).getAttendee().get(i).getUserId())==mDataArray.get(pos).getId()) {
                    //holder.checkbox.setChecked(true);
                    isCheck = true;
                    break;
                }
            }
            //FragmentAddMeetup.addToFlowLayout(mContext, flowContainer, FragmentAddMeetup.friendArray);
        }
        else {
            //holder.checkbox.setChecked(false);
        }

        return  isCheck;
    }

}
