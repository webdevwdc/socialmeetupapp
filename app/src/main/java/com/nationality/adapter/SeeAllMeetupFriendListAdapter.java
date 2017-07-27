package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.model.ProfileDetailsConnectionList;
import com.nationality.utils.Constants;

import java.util.List;

/**
 * Created by android on 5/5/17.
 */

public class SeeAllMeetupFriendListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MeetupDetailsAttendeesList> mDataArray;
    private String list_type;


    public SeeAllMeetupFriendListAdapter(List<MeetupDetailsAttendeesList> content_list, Context context )
    {
        this.mContext=context;
        this.mDataArray=content_list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDataArray.size();
    }

    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return mDataArray.get(index);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressWarnings("unused")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        String userId;
        final ViewHolder viewholder;

        if(convertView==null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_all_user, null);

            viewholder=new ViewHolder();

            viewholder.imgProfile=(ImageView) convertView.findViewById(R.id.imgProfile);
            viewholder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);



            convertView.setTag(viewholder);
        }
        else
        {
            viewholder=(ViewHolder) convertView.getTag();
        }

        Constants.setImage(viewholder.imgProfile,mDataArray.get(position).getProfilePic(),mContext);
        viewholder.txt_name.setText(mDataArray.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDataArray.get(position).getUserId().equalsIgnoreCase(""+Constants.getUserId(mContext)))
                {
                    MeetupDetailsAttendeesList col_data = mDataArray.get(position);
//                    int id=col_data.getId();
//                    String tag=col_data.getTag();
//                    String interest=col_data.getInterest();

                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", Integer.parseInt(col_data.getUserId()));

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) mContext).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack(Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();
                }
            }
        });



        return convertView;
    }
    /**
     * @return the photo URI
     */
    static class ViewHolder
    {
       ImageView imgProfile;
       TextView txt_name;
    }
}