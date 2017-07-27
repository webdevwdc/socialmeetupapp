package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.fragment.FragmentReportPost;
import com.nationality.model.AllMeetupData;
import com.nationality.model.BlockUserRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.DeleteBulletinRequest;
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

public class ConnectionListBlockingLIstAdapter extends RecyclerView.Adapter<ConnectionListBlockingLIstAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,RetrofitListener,Filterable {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<ConnectionListData> mDataArray,filter_list=new ArrayList<>();
    boolean isFirstTime;

    ProgressDialog pDialog;
    private RestHandler rest;

    int posi_del=-1;

    private CustomFilter mFilter;

    public ConnectionListBlockingLIstAdapter(List<ConnectionListData> dataset, Context con) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_connection_for_blocking_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).getFirstName()+" "+
        mDataArray.get(position).getLastName());

        holder.mTextCity.setText(mDataArray.get(position).getHomeCity());

        Constants.setImage(holder.profile_pic,mDataArray.get(position).getProfilePic(),mContext);

    }

    private void blockUesr(int toUseId) {

        rest=new RestHandler(mContext,this);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                blockUser(Constants.getUserId(mContext),toUseId),"blockUser");
        pDialog.show();
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

            if (method.equalsIgnoreCase("blockUser")) {
                BlockUserRequest req_Obj = (BlockUserRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    Toast.makeText(mContext,"User blocked successfully.",Toast.LENGTH_LONG).show();

                    if(posi_del!=-1)
                        removeAt(posi_del);
                }
                else
                    Toast.makeText(mContext,"Server Response Error.. ",Toast.LENGTH_LONG).show();

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

        @Bind(R.id.mTextCity)
        TextView mTextCity;

        @Bind(R.id.btn_block)
        TextView btn_block;

        @Bind(R.id.profile_pic)
        ImageView profile_pic;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            btn_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Userid", ""+mDataArray.get(getAdapterPosition()).getId());

                    posi_del=getAdapterPosition();

                    showConfirmDialog(mDataArray.get(getAdapterPosition()).getId());


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

    private void showConfirmDialog(final Integer toUserid)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to block the user ?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
//                        MainActivity.this.finish();
                        blockUesr(toUserid);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void removeAt(int position) {
        mDataArray.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataArray.size());
    }

    public class CustomFilter extends Filter {
        private ConnectionListBlockingLIstAdapter mAdapter;
        private CustomFilter(ConnectionListBlockingLIstAdapter mAdapter) {
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
