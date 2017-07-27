package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.nationality.fragment.FragmentChat;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.GetChatTokenRequest;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by webskitters on 3/29/2017.
 */

public class ConnectionLIstAdapter extends RecyclerView.Adapter<ConnectionLIstAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,RetrofitListener {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<ConnectionListData> mDataArray,filter_list=new ArrayList<>();;

    boolean isFirstTime;
    private CustomFilter mFilter;
    RestHandler rest;
    private ProgressDialog pDialog;
    private String receiverId="",to_user_id="";

    public ConnectionLIstAdapter(List<ConnectionListData> dataset,Context con) {
        if(con!=null) {
            //meet_list = new ArrayList<>(meetList);
            mDataArray = new ArrayList<>(dataset);
            mContext = con;
            pDialog=new ProgressDialog(mContext);
            rest = new RestHandler(con, this);
            mFilter = new CustomFilter(this);
            isFirstTime=true;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public ConnectionLIstAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_connection_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
        mDataArray.get(position).getLastName());

        holder.mTextCity.setText(mDataArray.get(position).getHomeCity());

        Constants.setImage(holder.profile_pic,mDataArray.get(position).getProfilePic(),mContext);
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

//    @Override
//    public Filter getFilter() {
//        return mFilter;
//    }



    public class CustomFilter extends Filter {
        private ConnectionLIstAdapter mAdapter;
        private CustomFilter(ConnectionLIstAdapter mAdapter) {
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
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.mTextCity)
        TextView mTextCity;

        //profile_pic
        @Bind(R.id.profile_pic)
        ImageView profile_pic;

        @Bind(R.id.img_msg)
        ImageView img_msg;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            img_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    receiverId=mDataArray.get(getAdapterPosition()).getFacebookId();
                    to_user_id=mDataArray.get(getAdapterPosition()).getId()+"";


                   getChatToken(getAdapterPosition());

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
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
            });
        }


    }

    private void getChatToken(int position) {

//                        pDialog.show();
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String sender_id = prefs.getString(Constants.strShPrefUserFBID, "");
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                getChatToken(sender_id,mDataArray.get(position).getFacebookId(), "Private"),"getChatToken");
    }



    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){
            if(method.equalsIgnoreCase("getChatToken")) {

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
    public void onFailure(String errorMessage) {

    }



    public void animateTo(List<ConnectionListData> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<ConnectionListData> newModels) {
        if(mDataArray!=null) {
            for (int i = mDataArray.size() - 1; i >= 0; i--) {
                final ConnectionListData model = mDataArray.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }
    }

    private void applyAndAnimateAdditions(List<ConnectionListData> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ConnectionListData model = newModels.get(i);

            if (mDataArray!=null&&!mDataArray.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ConnectionListData> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ConnectionListData model = newModels.get(toPosition);
            final int fromPosition = mDataArray.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    public ConnectionListData removeItem(int position) {
        final ConnectionListData model = mDataArray.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ConnectionListData model) {
        mDataArray.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ConnectionListData model = mDataArray.remove(fromPosition);
        mDataArray.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}
