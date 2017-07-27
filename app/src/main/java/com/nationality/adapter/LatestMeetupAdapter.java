package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.fragment.FragmentMeetProfileDetails;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.DashboardNextMeetup;
import com.nationality.model.DashboardYourMeetup;
import com.nationality.utils.Constants;

import java.util.ArrayList;

/**
 * Created by webskitters on 3/22/2017.
 */

public class LatestMeetupAdapter extends BaseAdapter {

    Context context;
    ArrayList<DashboardYourMeetup> _data;
    Typeface typeFaceOpenSansBold, typeFaceOpenSansReg;

    public LatestMeetupAdapter(Context context, ArrayList<DashboardYourMeetup> _data){
        if(context!=null) {
            this.context = context;
            this._data = _data;

            typeFaceOpenSansBold = Typeface.createFromAsset(context.getAssets(),
                    "OPENSANS-BOLD.TTF");
            typeFaceOpenSansReg = Typeface.createFromAsset(context.getAssets(), "OPENSANS-REGULAR.TTF");
        }
    }

    @Override
    public int getCount() {
        if(_data==null)
            return  0;
        else if(_data.size()>2)
            return 2;
        else
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView tv_meetup_title, pic_number, text_added,text_date,text_time;
        ImageView img_profile;

       public ViewHolder(View v)
        {
            tv_meetup_title=(TextView)v.findViewById(R.id.tv_meetup_title);
            pic_number=(TextView)v.findViewById(R.id.total_users_added);
            text_added=(TextView)v.findViewById(R.id.text_added);
            text_date=(TextView)v.findViewById(R.id.text_date);
            text_time=(TextView)v.findViewById(R.id.text_time);

            img_profile=(ImageView)v.findViewById(R.id.img_profile);


            tv_meetup_title.setTypeface(typeFaceOpenSansReg);
            pic_number.setTypeface(typeFaceOpenSansReg);
            text_added.setTypeface(typeFaceOpenSansReg);
            text_date.setTypeface(typeFaceOpenSansReg);
            text_time.setTypeface(typeFaceOpenSansReg);

        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        ViewHolder holder = null;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.row_landing_meetups, null);
            holder = new ViewHolder(convertView);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_meetup_title.setText(_data.get(position).getTitle()+" @ "+
        _data.get(position).getMeetupCreator() );
//        holder.pic_number.setText(_data.get(position).getPeople_add());

        holder.text_date.setText(Constants.changeDateFormat(_data.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_date_format));
        holder.text_time.setText(Constants.changeDateFormat(_data.get(position).getDateTime(),
                Constants.web_date_format,Constants.app_display_time_format));

        holder.pic_number.setText(String.valueOf(_data.get(position).getPeople_add()));

        if(_data.get(position).getPeople_add()<=1)
            holder.text_added.setText("Person is added");
        else
            holder.text_added.setText("People are added");

        Constants.setSquareImage(holder.img_profile,_data.get(position).getMeetupCreatorPic(),context);


        holder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DashboardYourMeetup col_data=_data.get(position);
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                Bundle arguments = new Bundle();
                arguments.putString("tag", "");
                arguments.putString("interest", "");
                arguments.putInt("id", Integer.parseInt(col_data.getUserId()));

                FragmentProfile fragment = new FragmentProfile();
                fragment.setArguments(arguments);

                ((Activity) context).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                        .commit();

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardYourMeetup col_data=_data.get(position);
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                FragmentMeetProfileDetails mFrag= new FragmentMeetProfileDetails();
                Bundle b=new Bundle();
                b.putInt("meetup_id", _data.get(position).getId());
                b.putString("fragment","");
                mFrag.setArguments(b);

                //FragmentMeetProfileDetails.newInstance("meet_upid","user_id");

                ((Activity)context).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        mFrag,Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .addToBackStack(Constants.TAG_FRAGMENT_MEETUPS_DETAILS)
                        .commit();
            }
        });


        return convertView;
    }
}
