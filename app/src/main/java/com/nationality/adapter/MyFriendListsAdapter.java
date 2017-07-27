package com.nationality.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 16/5/17.
 */

public class MyFriendListsAdapter extends RecyclerView.Adapter<MyFriendListsAdapter.MyViewHolder> {

    MyViewHolder holder;
    Context context;
    List<ConnectionListData> _attediesList;
    Bitmap icon;
    NestedScrollView mScroll;

    public MyFriendListsAdapter(Context context, List<ConnectionListData> content_list )
    {

        if(context!=null) {
            this.context = context;
            this._attediesList = content_list;
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_selected_thumbnail_2, parent, false);
        holder=new MyFriendListsAdapter.MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        Constants.setImage(holder.img,_attediesList.get(position).getProfilePic(),context);
        Constants.setImage(holder.img, _attediesList.get(position).getProfilePic(), context);
        holder.txt_name.setText(_attediesList.get(position).getFirstName()+" "+
                _attediesList.get(position).getLastName());

    }

    @Override
    public int getItemCount() {
        return _attediesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView txt_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.image);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ConnectionListData meetObj=_attediesList.get(getAdapterPosition());


                    Bundle arguments = new Bundle();
                    arguments.putString("tag", "");
                    arguments.putString("interest", "");
                    arguments.putInt("id", meetObj.getId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) context).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack( Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();


                }
            });
        }


    }
}
