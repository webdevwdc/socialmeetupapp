package com.nationality.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AllMeetupData;
import com.nationality.model.MeetupAcceptRejectRequest;
import com.nationality.model.MeetupDetailsRequest;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.model.MeetupModelList;
import com.nationality.R;
import com.nationality.fragment.FragmentMeetProfileDetails;
import com.nationality.model.NewMeetupRequestDatum;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;
import com.nationality.utils.OnClickCallBack;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 4/7/2017.
 */

public class MeetupRequestAdapterList extends RecyclerView.Adapter<MeetupRequestAdapterList.MyViewHolder>
                                                implements RetrofitListener{
    private ProgressDialog pDialog;
    private List<NewMeetupRequestDatum> meet_list;
    private List<AllMeetupData> filter_list=new ArrayList<>();
    MyViewHolder holder;
    Context ctx;
    boolean ismyMeetups=false;
    RestHandler rest;
    View current_btn_like;
    boolean isFirstTime;
    private int posi_del=-1;

    OnClickCallBack mCallback;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,txt_title,txt_date,txt_time,txt_others_count,tv_total_like;

        RelativeLayout btn_edit,btn_like;

        ImageView btn_accept,btn_reject,img_attendee1,profile_pic;

        public MyViewHolder(View view) {
            super(view);

            tv_name=(TextView)view.findViewById(R.id.tv_name);
            txt_title=(TextView)view.findViewById(R.id.txt_title);
            txt_date=(TextView)view.findViewById(R.id.txt_date);
            txt_time=(TextView)view.findViewById(R.id.txt_time);
            txt_others_count=(TextView)view.findViewById(R.id.txt_others_count);
            tv_total_like=(TextView)view.findViewById(R.id.tv_total_like);

            btn_edit=(RelativeLayout)view.findViewById(R.id.btn_edit);
            btn_like=(RelativeLayout)view.findViewById(R.id.btn_like);

            profile_pic=(ImageView)view.findViewById(R.id.profile_pic);

            img_attendee1=(ImageView)view.findViewById(R.id.img_attendee1);
            btn_reject=(ImageView)view.findViewById(R.id.btn_reject);
            btn_accept=(ImageView)view.findViewById(R.id.btn_accept);

            tv_name.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_title.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_date.setTypeface(Constants.typeFaceOpenSansReg(ctx));
//            txt_time.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_others_count.setTypeface(Constants.typeFaceOpenSansReg(ctx));

         btn_accept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 posi_del=getAdapterPosition();
                 sendAcceptReject(getAdapterPosition(),Constants.LIKE_YES);


             }
         });

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    posi_del=getAdapterPosition();
                    sendAcceptReject(getAdapterPosition(),Constants.LIKE_NO);

                }
            });


            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NewMeetupRequestDatum col_data=meet_list.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getUserId()));

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) ctx).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentMeetProfileDetails mFrag= new FragmentMeetProfileDetails();
                    Bundle b=new Bundle();
                    b.putInt("meetup_id",meet_list.get(getAdapterPosition()).getId());
                    b.putString("fragment","Meetup_request");
                    mFrag.setArguments(b);

                    //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                    ((Activity)ctx).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            mFrag,Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                            .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                            .commit();
                }
            });

        }
    }

    private void sendAcceptReject(int adapterPosition,String is_attend) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                sendMeetupAcceptReject(Constants.getUserId(ctx),is_attend,
                        meet_list.get(adapterPosition).getId()),"sendAcceptReject");
    }

    public MeetupRequestAdapterList(List<NewMeetupRequestDatum> meetList, Context ctx, boolean ismyMeetups,OnClickCallBack mCallback) {
        if(ctx!=null) {
            this.meet_list = meetList;
            this.ctx = ctx;
            this.ismyMeetups = ismyMeetups;
            rest = new RestHandler(ctx, this);

            isFirstTime = true;

            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage(ctx.getString(R.string.dialog_msg));
            pDialog.setCancelable(false);

            this.mCallback=mCallback;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_meetup_request, parent, false);
        holder=new MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(!ismyMeetups)
            holder.btn_edit.setVisibility(View.INVISIBLE);

        holder.txt_title.setText(meet_list.get(position).getTitle()+" @ "+
        meet_list.get(position).getLocation());

        holder.txt_date.setText(Constants.changeDateFormat(meet_list.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_date_format)+" at "+Constants.changeDateFormat(meet_list.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_time_format));

        /*holder.txt_time.setText(Constants.changeDateFormat(meet_list.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_time_format));*/

        holder.tv_name.setText(meet_list.get(position).getName());

        Constants.setImage(holder.profile_pic,meet_list.get(position).getImage(),ctx);


    }

    @Override
    public int getItemCount() {
        return meet_list.size();
    }

    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("sendAcceptReject")) {

                MeetupAcceptRejectRequest meetup_Obj=(MeetupAcceptRejectRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {

                    Toast.makeText(ctx,meetup_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
                    if(posi_del!=-1)
                        removeAt(posi_del);

                }
                else
                    Constants.showAlert(ctx,meetup_Obj.getResult().getMessage());
            }
        }
        else {
            try {
                response.errorBody().string();
//                label.setText(response.code()+" "+response.message());
                Constants.showAlert(ctx, response.code() + " " + response.message());
            } catch (IOException e) {
//                showAlertDialog("Alert","Server Response Error..");
                e.printStackTrace();
            } catch (NullPointerException e) {
//                showAlertDialog("Alert","Server Response Error..");
            }
        }

    }

    @Override
    public void onFailure(String errorMessage) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        Constants.showAlert(ctx,errorMessage);

    }

    public void removeAt(int position) {
        meet_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, meet_list.size());

        if(meet_list.size()==0)
            mCallback.onUIupdate();
    }

}
