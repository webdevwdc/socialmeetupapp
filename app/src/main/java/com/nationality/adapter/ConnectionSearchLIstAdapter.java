package com.nationality.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AddFriendRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.ConnectionListResult;
import com.nationality.model.ConnectionRequest;
import com.nationality.model.SearchUserConnectionDetails;
import com.nationality.model.SearchUserConnectionRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class ConnectionSearchLIstAdapter extends RecyclerView.Adapter<ConnectionSearchLIstAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter, RetrofitListener,Filterable {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<SearchUserConnectionDetails> mDataArray,filter_list=new ArrayList<>();
    Dialog dialog;
    RestHandler rest;
    ProgressDialog pDialog;
    boolean isFirstTime;
    private CustomFilter mFilter;

    public ConnectionSearchLIstAdapter(List<SearchUserConnectionDetails> dataset, Context con, Dialog dialog) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
            this.dialog = dialog;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection_search, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
        mDataArray.get(position).getLastName());

        holder.txt_subheader.setText(mDataArray.get(position).getHomeCity());

        Glide.with(mContext)
                .load(Constants.image_url+mDataArray.get(position).getProfilePic())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .fitCenter()
                .into(holder.profile_pic);

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add Connection
                addFriend(mDataArray.get(position).getId());
            }
        });

    }

    private void addFriend(Integer to_id) {
        rest = new RestHandler(mContext, this);
        int userId = Constants.getUserId(mContext);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                addFriend(userId, to_id),"addFriend");
        pDialog.show();
//        dialog.dismiss();
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
    public void onSuccess(Call call, Response response, String method) {
        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if(method.equalsIgnoreCase("addFriend"))
            {
                AddFriendRequest request = (AddFriendRequest) response.body();
                if(!request.getResult().getError())
                {
                    Constants.showAlert(mContext, "Invitation Sent");
                    if(dialog!=null)
                    dialog.dismiss();
                }
                else{
//                    Toast.makeText(SignupActivity.this,signup_obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    Constants.showAlert(mContext,request.getResult().getMessage());
                }
            }
        }
        else{
            try {
                response.errorBody().string();
                Constants.showAlert(mContext,response.code()+" "+response.message());
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(mContext,errorMessage);

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.txt_subheader)
        TextView txt_subheader;

        @Bind(R.id.profile_pic)
        CircleImageView profile_pic;

        @Bind(R.id.img_add_connection)
        ImageView img_add_connection;

        @Bind(R.id.btn_add)
        ImageView btn_add;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    dialog.dismiss();
                    SearchUserConnectionDetails col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();
/*
                    Bundle arguments = new Bundle();
                    arguments.putString("tag", col_data.getTag());
                    arguments.putString("interest", col_data.getInterest());
                    arguments.putInt("id", col_data.getId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();*/
                    if(dialog!=null)
                        dialog.dismiss();

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
        private ConnectionSearchLIstAdapter mAdapter;
        private CustomFilter(ConnectionSearchLIstAdapter mAdapter) {
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
                for (final SearchUserConnectionDetails mWords : filter_list) {
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
            System.out.println("Count Number 2 " + ((List<SearchUserConnectionDetails>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

}
