package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.debjit.alphabetscroller.RecyclerViewFastScroller;
import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by android on 5/5/17.
 */

public class AllUserListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ConnectionListData> mDataArray;
    private String list_type;


    public AllUserListAdapter(List<ConnectionListData> content_list,Context context )
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
            viewholder.rel_main = (RelativeLayout)convertView.findViewById(R.id.rel_main);



            convertView.setTag(viewholder);
        }
        else
        {
            viewholder=(ViewHolder) convertView.getTag();
        }

        Constants.setImage(viewholder.imgProfile,mDataArray.get(position).getProfilePic(),mContext);
        viewholder.txt_name.setText(mDataArray.get(position).getFirstName() + " "
                +mDataArray.get(position).getLastName());

        viewholder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDataArray.get(position).getId() != Constants.getUserId(mContext)) {

                    ConnectionListData col_data = mDataArray.get(position);
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
        RelativeLayout rel_main;
    }
}