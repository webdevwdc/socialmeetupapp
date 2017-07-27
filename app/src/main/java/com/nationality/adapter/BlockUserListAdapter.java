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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AllMeetupData;
import com.nationality.model.BlockUserListdata;
import com.nationality.model.BlockUserRequest;
import com.nationality.model.ConnectionListData;
import com.nationality.model.UnblockUserRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.OnClickCallBack;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class BlockUserListAdapter extends RecyclerView.Adapter<BlockUserListAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,RetrofitListener{

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    private List<BlockUserListdata> mDataArray;

    ProgressDialog pDialog;
    private RestHandler rest;
    OnClickCallBack mCallback;
    int posi_del=-1;

    public BlockUserListAdapter(List<BlockUserListdata> dataset, Context con, OnClickCallBack mCallback) {
        if(con!=null) {
            mDataArray = dataset;
            mContext = con;
            this.mCallback=mCallback;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_block_user_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextName.setText(mDataArray.get(position).getBlockUserName());


        Constants.setImage(holder.profile_pic,mDataArray.get(position).getBlockUserImage(),mContext);

    }

    private void unblockUesr(String UseId) {

        rest=new RestHandler(mContext,this);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                unblockUser(Constants.getUserId(mContext),Integer.parseInt(UseId)),"unblockUser");
        pDialog.show();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= mDataArray.size())
            return null;

        String name = mDataArray.get(pos).getBlockUserName();
        if (name == null || name.length() < 1)
            return null;

        return  mDataArray.get(pos).getBlockUserName().substring(0, 1);
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("unblockUser")) {
                UnblockUserRequest req_Obj = (UnblockUserRequest) response.body();
                if(!req_Obj.getResult().getError()) {

                    if(posi_del!=-1)
                        removeAt(posi_del);

                    Toast.makeText(mContext,"User unblocked successfully.",Toast.LENGTH_LONG).show();
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

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(mContext,errorMessage);

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView mTextName;

        @Bind(R.id.mTextCity)
        TextView mTextCity;

        @Bind(R.id.btn_unblock)
        TextView btn_unblock;

        @Bind(R.id.profile_pic)
        ImageView profile_pic;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            btn_unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Userid", ""+mDataArray.get(getAdapterPosition()).getId());
                /*unblockUesr(mDataArray.get(position).getId());*/
                    posi_del=getAdapterPosition();

                    showConfirmDialog(mDataArray.get(getAdapterPosition()).getToUserid());

                }
            });

            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BlockUserListdata col_data=mDataArray.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getToUserid()));

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

    private void showConfirmDialog(final String toUserid)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to unblock the user ?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
//                        MainActivity.this.finish();
                        unblockUesr(toUserid);
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

        if(mDataArray.size()==0)
            mCallback.onUIupdate();
    }
}
