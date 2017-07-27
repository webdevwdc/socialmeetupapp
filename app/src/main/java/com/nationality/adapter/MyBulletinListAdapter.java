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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentBulletinDetails;
import com.nationality.fragment.FragmentCreateBulletin;
import com.nationality.model.BulletinListData;
import com.nationality.model.BulletinMyListData;
import com.nationality.model.BulletinMyListRequest;
import com.nationality.model.DeleteBulletinRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.OnClickCallBack;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 3/29/2017.
 */

public class MyBulletinListAdapter extends RecyclerView.Adapter<MyBulletinListAdapter.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter,RetrofitListener {

    Context mContext;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;
    RestHandler rest;
    int posi_del=-1;
    ProgressDialog pDialog;

    OnClickCallBack mCallback;

    private List<BulletinMyListData> mDataArray;

    public MyBulletinListAdapter(List<BulletinMyListData> dataset, Context con,OnClickCallBack mCallback) {
        mDataArray = dataset;
        mContext=con;
        if(con!=null) {
            pDialog = new ProgressDialog(con);
            pDialog.setMessage(con.getString(R.string.dialog_msg));
            pDialog.setCancelable(false);
            rest = new RestHandler(con, this);
            this.mCallback=mCallback;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_bulletin_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txt_title.setText(mDataArray.get(position).getTitle());
        holder.txt_desc.setText(mDataArray.get(position).getContent());

        //////////Setting date and time of each bulletin/////////////////
        /*String[] datetime=mDataArray.get(position).getCreatedAt().split(" ");
        holder.txt_date.setText(datetime[0]);
        holder.txt_time.setText(datetime[1]);*/

        holder.txt_date.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
                Constants.web_date_format,Constants.app_display_date_format));
        holder.txt_time.setText(Constants.changeDateFormat(mDataArray.get(position).getUpdatedAt(),
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

    private void delBulletin(int bulletn_id)
    {
        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                delBulletin(bulletn_id),"delBulletin");
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200){

            if (method.equalsIgnoreCase("delBulletin")) {
                DeleteBulletinRequest req_Obj = (DeleteBulletinRequest) response.body();
                if(!req_Obj.getResult().getError()) {
                    Toast.makeText(mContext, req_Obj.getResult().getMessage(), Toast.LENGTH_LONG).show();
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

        @Bind(R.id.txt_date)
        TextView txt_date;
        @Bind(R.id.txt_time)
        TextView txt_time;
        @Bind(R.id.txt_view_count)
        TextView txt_view_count;
        @Bind(R.id.txt_reply_count)
        TextView txt_reply_count;
        @Bind(R.id.txt_like_count)
        TextView txt_like_count;


        @Bind(R.id.btn_delete)
        Button btn_delete;

        @Bind(R.id.btn_edit)
        Button btn_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            typeFaceOpenSansBold = Typeface.createFromAsset(mContext.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg=Typeface.createFromAsset(mContext.getAssets(), "OPENSANS-REGULAR.TTF");

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentBulletinDetails(), Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                            .addToBackStack(Constants.TAG_FRAGMENT_BULLETIN_DETAILS)
                            .commit();
                }
            });*/

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    posi_del=getAdapterPosition();
                   showConfirmDialog(posi_del);
                }
            });


            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentCreateBulletin fragCreate=new FragmentCreateBulletin();

                    Bundle b=new Bundle();
                    b.putInt("bulletin_id",mDataArray.get(getAdapterPosition()).getId());
                    b.putString("title",mDataArray.get(getAdapterPosition()).getTitle());
                    b.putString("content",mDataArray.get(getAdapterPosition()).getContent());
                    b.putString("bulletin_type",mDataArray.get(getAdapterPosition()).getType());

                    fragCreate.setArguments(b);


                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragCreate, Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                            .addToBackStack(Constants.TAG_FRAGMENT_CREATE_BULLETIN)
                            .commit();
                }
            });

            txt_title.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_desc.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_date.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_time.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_like_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_reply_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));
            txt_view_count.setTypeface(Constants.typeFaceOpenSansReg(mContext));

        }

    }

    public void removeAt(int position) {
        mDataArray.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataArray.size());


        if(mDataArray.size()==0)
            mCallback.onUIupdate();
    }
    private void showConfirmDialog(final int posi)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this Bulletin?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        delBulletin(mDataArray.get(posi).getId());
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
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
}
