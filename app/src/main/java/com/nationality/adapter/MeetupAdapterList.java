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
import android.widget.Toast;

import com.nationality.R;
import com.nationality.fragment.FragmentAddMeetup;
import com.nationality.fragment.FragmentMeetProfileDetails;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.AllMeetupData;
import com.nationality.model.ConnectionListData;
import com.nationality.model.DeleteMeetupRequest;
import com.nationality.model.MeetupLikeRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by webskitters on 4/7/2017.
 */

public class MeetupAdapterList extends RecyclerView.Adapter<MeetupAdapterList.MyViewHolder>
                                                implements RetrofitListener,Filterable{
    private ProgressDialog pDialog;
    public static List<AllMeetupData> meet_list,filter_list=new ArrayList<>();
    MyViewHolder holder;
    Context ctx;
    boolean ismyMeetups=false;
    RestHandler rest;
    View current_btn_like;
    boolean isFirstTime;
    int event_posi=-1;
    TextView txtCountLike;
    private Integer count;
    private final CustomFilter mFilter;
    private int posi_del=0;
    private boolean like_update;


    public MeetupAdapterList(List<AllMeetupData> meetList, Context ctx, boolean ismyMeetups) {
        mFilter = new CustomFilter(this);
        if(ctx!=null) {
            meet_list = new ArrayList<>(meetList);
            this.ctx = ctx;
            this.ismyMeetups = ismyMeetups;
            rest = new RestHandler(ctx, this);

            like_update=false;

            isFirstTime = true;

            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage(ctx.getString(R.string.dialog_msg));
            pDialog.setCancelable(false);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,txt_title,txt_date,txt_time,txt_others_count,tv_total_like;

        RelativeLayout btn_edit,btn_like,btn_delete;

        LinearLayout btn_container;

        ImageView img_attendee3,img_attendee2,img_attendee1,profile_pic;

        public MyViewHolder(View view) {
            super(view);

            tv_name=(TextView)view.findViewById(R.id.tv_name);
            txt_title=(TextView)view.findViewById(R.id.txt_title);
            txt_date=(TextView)view.findViewById(R.id.txt_date);
            txt_time=(TextView)view.findViewById(R.id.txt_time);
            txt_others_count=(TextView)view.findViewById(R.id.txt_others_count);
            tv_total_like=(TextView)view.findViewById(R.id.tv_total_like);

            btn_container=(LinearLayout)view.findViewById(R.id.btn_container);

            btn_edit=(RelativeLayout)view.findViewById(R.id.btn_edit);
            btn_like=(RelativeLayout)view.findViewById(R.id.btn_like);
            btn_delete=(RelativeLayout)view.findViewById(R.id.btn_del) ;

            profile_pic=(ImageView)view.findViewById(R.id.profile_pic);

            img_attendee1=(ImageView)view.findViewById(R.id.img_attendee1);
            img_attendee2=(ImageView)view.findViewById(R.id.img_attendee2);
            img_attendee3=(ImageView)view.findViewById(R.id.img_attendee3);

            tv_name.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_title.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_date.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_time.setTypeface(Constants.typeFaceOpenSansReg(ctx));
            txt_others_count.setTypeface(Constants.typeFaceOpenSansReg(ctx));

            view.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    FragmentMeetProfileDetails mFrag= new FragmentMeetProfileDetails();
                    Bundle b=new Bundle();
                    b.putInt("meetup_id",meet_list.get(getAdapterPosition()).getId());
                    b.putString("fragment","Meetup_list");
                    mFrag.setArguments(b);

                    //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                    ((Activity)ctx).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            mFrag,Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                            .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                            .commit();
                }
            });

            if(ismyMeetups)
                btn_like.setEnabled(false);
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    TextView textView = (TextView) v.findViewById(R.id.tv_total_like);
//                    textView.setText("3232");

                    if(meet_list.get(getAdapterPosition()).getIsLike().equalsIgnoreCase("no")
                            && (!String.valueOf(Constants.getUserId(ctx)).equals(meet_list.get(getAdapterPosition()).getUserId()))) {
                        current_btn_like = v;
                        RelativeLayout rel=(RelativeLayout)v.findViewById(R.id.btn_like);
                        event_posi=getAdapterPosition();
                        txtCountLike=(TextView) rel.findViewById(R.id.tv_total_like);
                        count=meet_list.get(getAdapterPosition()).getTotalLike();
                        postLiketoServer(getAdapterPosition());
                    }

                }
            });


            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AllMeetupData col_data=meet_list.get(getAdapterPosition());
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", col_data.getUserId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) ctx).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();

                }
            });

          /*  btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    posi_del=getAdapterPosition();

                   showConfirmDialog(posi_del);


                }
            });

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FragmentAddMeetup mFrag= new FragmentAddMeetup();
                    Bundle b=new Bundle();
                    b.putString("meetup_id",meet_list.get(getAdapterPosition()).getId().toString());
                    b.putString("title", meet_list.get(getAdapterPosition()).getTitle());
                    b.putString("date",Constants.changeDateFormat(meet_list.get(getAdapterPosition()).getDateTime(),
                            Constants.web_date_format,Constants.app_display_date_format));
                    b.putString("time",Constants.changeDateFormat(meet_list.get(getAdapterPosition()).getDateTime(),
                            Constants.web_date_format,Constants.app_display_time_format));
                    b.putString("address", meet_list.get(getAdapterPosition()).getLocation());
                    b.putString("place", meet_list.get(getAdapterPosition()).getPlace());
                    b.putString("comments", meet_list.get(getAdapterPosition()).getComment());
                    b.putString("type", meet_list.get(getAdapterPosition()).getType());
                    b.putString("position", getAdapterPosition()+"");
                    b.putString("latitude",meet_list.get(getAdapterPosition()).getMeetupLat());
                    b.putString("longitude",meet_list.get(getAdapterPosition()).getMeetupLong());
                    *//*ArrayList<String> arrAttn=new ArrayList<String>();


                    for (int i=0; i<meet_list.get(getAdapterPosition()).getAttendee().size(); i++){
                        arrAttn.add(meet_list.get(getAdapterPosition()).getAttendee().get(i).toString());
                    }

                    b.putStringArrayList("attendees", meet_list.get(getAdapterPosition()).getAttendee());*//*
                    mFrag.setArguments(b);

                    //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                    ((Activity)ctx).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            mFrag,Constants.TAG_FRAGMENT_ADD_MEETUP)
                            .addToBackStack(Constants.TAG_FRAGMENT_ADD_MEETUP)
                            .commit();

                }
            });
*/


        }
    }

    private void postLiketoServer(int adapterPosition) {

        pDialog.show();
        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
                meetupLikeRequest(Constants.getUserId(ctx),Constants.LIKE_YES,
                        meet_list.get(adapterPosition).getId()),"likeMeetup");
    }

    private void deleteMeeetup(int adapterPosition)
    {
        pDialog.show();

        rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class)
                .delMeetup(meet_list.get(adapterPosition).getId()),"del_meetup");

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_meetup_fragment, parent, false);
        holder=new MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

      /*  if(!ismyMeetups) {
            holder.btn_edit.setVisibility(View.INVISIBLE);
        }
        else{
            holder.btn_container.setVisibility(View.VISIBLE);
        }*/

        holder.txt_title.setText(meet_list.get(position).getTitle()+" @ "+
        meet_list.get(position).getPlace());

        holder.tv_name.setText(meet_list.get(position).getMeetupCreator());

        holder.txt_date.setText(Constants.changeDateFormat(meet_list.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_date_format));

        holder.txt_time.setText(Constants.changeDateFormat(meet_list.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_time_format));


        holder.tv_total_like.setText(String.valueOf(meet_list.get(position).getTotalLike()));

        if(meet_list.get(position).getIsLike().equalsIgnoreCase("yes")) {

            holder.btn_like.setBackgroundResource(R.drawable.grey_rounded_bg);
            holder.btn_like.setEnabled(false);
        }
        else {
            if(Constants.getUserId(ctx)==meet_list.get(position).getUserId()) {
                holder.btn_like.setBackgroundResource(R.drawable.grey_rounded_bg);
                holder.btn_like.setEnabled(false);
            }
            else{
            holder.btn_like.setBackgroundResource(R.drawable.green_rounded_bg);
                holder.btn_like.setEnabled(true);}
        }

        Constants.setImage(holder.profile_pic,meet_list.get(position).getMeetupCreatorPic(),ctx);

     /*   if(ismyMeetups)
        {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 10);
            holder.btn_edit.setLayoutParams(params);
        }*/


        if(meet_list.get(position).getAttendee()!=null  )
        {
            handleAttendeelist(holder,position);
        }

    }

    @Override
    public int getItemCount() {
        return meet_list.size();
    }

    private void setImage(ImageView image,String url)
    {
//        if(url!=null && url.length()>0) {
//
////            url= Constants.image_url_aupair+url;
//
//            Picasso.with(ctx)
//                    .load(url).fit().centerInside()
//                    .into(image);
//        }
//        else{
//            Picasso.with(ctx)
////                    .load(Constants.aupair_no_image).fit().centerInside()
//                    .into(image);
//        }
    }


    @Override
    public void onSuccess(Call call, Response response, String method) {

        if(pDialog.isShowing())
            pDialog.dismiss();

        if(response!=null && response.code()==200) {

            if (method.equalsIgnoreCase("likeMeetup")) {

                MeetupLikeRequest meetup_Obj=(MeetupLikeRequest)response.body();


                if(!meetup_Obj.getResult().getError())
                {
                    //

                    //meet_list.get(event_posi).setIsLike("yes");
                    count=count+1;

                    meet_list.get(event_posi).setIsLike("Yes");
                    meet_list.get(event_posi).setTotalLike(count);
                    like_update=true;
                    notifyItemChanged(event_posi);
                    /*
                    txtCountLike.setText(""+count);

                    current_btn_like.setBackgroundResource(R.drawable.grey_rounded_bg);*/
//                    TextView tv=current_btn_like.find
                }
                else
                    Toast.makeText(ctx,"Something went wrong..",Toast.LENGTH_LONG).show();



            }

          else  if (method.equalsIgnoreCase("del_meetup")) {

                DeleteMeetupRequest meetup_Obj=(DeleteMeetupRequest)response.body();

                if(!meetup_Obj.getResult().getError())
                {

                    if(posi_del!=-1)
                        removeAt(posi_del);
                }

                Toast.makeText(ctx,meetup_Obj.getResult().getMessage(),Toast.LENGTH_LONG).show();
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
    }

    private void handleAttendeelist(MyViewHolder h,int position)
    {
        if(meet_list.get(position).getAttendee().size()==0)
        {
            h.txt_others_count.setVisibility(View.INVISIBLE);
            h.img_attendee1.setVisibility(View.INVISIBLE);
            h.img_attendee2.setVisibility(View.INVISIBLE);
            h.img_attendee3.setVisibility(View.INVISIBLE);


        }
        else if(meet_list.get(position).getAttendee().size()==1)
        {
            h.txt_others_count.setVisibility(View.INVISIBLE);
            h.img_attendee2.setVisibility(View.INVISIBLE);
            h.img_attendee3.setVisibility(View.INVISIBLE);
            Constants.setImage(h.img_attendee1,meet_list.get(position).getAttendee().get(0).getAttendeePicture(),ctx);

        }
        else if(meet_list.get(position).getAttendee().size()==2)
        {
            h.txt_others_count.setVisibility(View.INVISIBLE);
            h.img_attendee3.setVisibility(View.INVISIBLE);
            Constants.setImage(h.img_attendee1,meet_list.get(position).getAttendee().get(0).getAttendeePicture(),ctx);
            Constants.setImage(h.img_attendee2,meet_list.get(position).getAttendee().get(1).getAttendeePicture(),ctx);

        }
        else if(meet_list.get(position).getAttendee().size()==3)
        {
            h.txt_others_count.setVisibility(View.INVISIBLE);
//            holder.txt_others_count.setText("and "+meet_list.get(position).getAttendee().size()+" others");

            Constants.setImage(h.img_attendee1,meet_list.get(position).getAttendee().get(0).getAttendeePicture(),ctx);
            Constants.setImage(h.img_attendee2,meet_list.get(position).getAttendee().get(1).getAttendeePicture(),ctx);
            Constants.setImage(h.img_attendee3,meet_list.get(position).getAttendee().get(2).getAttendeePicture(),ctx);
        }
        else {
            h.txt_others_count.setVisibility(View.VISIBLE);
            holder.txt_others_count.setText("and " + (meet_list.get(position).getAttendee().size()-3) + " others");

            Constants.setImage(h.img_attendee1,meet_list.get(position).getAttendee().get(0).getAttendeePicture(),ctx);
            Constants.setImage(h.img_attendee2,meet_list.get(position).getAttendee().get(1).getAttendeePicture(),ctx);
            Constants.setImage(h.img_attendee3,meet_list.get(position).getAttendee().get(2).getAttendeePicture(),ctx);
        }


    }





    public class CustomFilter extends Filter {
        private MeetupAdapterList mAdapter;
        private CustomFilter(MeetupAdapterList mAdapter) {
            super();
            this.mAdapter = mAdapter;

        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(isFirstTime) {
                filter_list.addAll(meet_list);
                isFirstTime=false;
            }
            meet_list.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                meet_list.addAll(filter_list);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final AllMeetupData mWords : filter_list) {
                    if (mWords.getTitle().toLowerCase().contains(filterPattern.toLowerCase())) {
                        meet_list.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + meet_list.size());
            results.values = meet_list;
            results.count = meet_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<AllMeetupData>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    public void animateTo(List<AllMeetupData> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<AllMeetupData> newModels) {
        if(meet_list!=null) {
            for (int i = meet_list.size() - 1; i >= 0; i--) {
                final AllMeetupData model = meet_list.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }
    }

    private void applyAndAnimateAdditions(List<AllMeetupData> newModels) {
        if(meet_list!=null) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final AllMeetupData model = newModels.get(i);
                if (!meet_list.contains(model)) {
                    addItem(i, model);
                }
            }
        }
    }

    private void applyAndAnimateMovedItems(List<AllMeetupData> newModels) {
        if(meet_list!=null) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final AllMeetupData model = newModels.get(toPosition);
                final int fromPosition = meet_list.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }
    }


    public AllMeetupData removeItem(int position) {
            final AllMeetupData model = meet_list.remove(position);
            notifyItemRemoved(position);
            return model;
    }

    public void addItem(int position, AllMeetupData model) {
        if(meet_list!=null) {
            meet_list.add(position, model);
            notifyItemInserted(position);
        }
    }

    public void moveItem(int fromPosition, int toPosition) {
        if(meet_list!=null) {
            final AllMeetupData model = meet_list.remove(fromPosition);
            meet_list.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }
    }


    private void showConfirmDialog(final int posi)
    {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this Meetup?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteMeeetup(posi);
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
