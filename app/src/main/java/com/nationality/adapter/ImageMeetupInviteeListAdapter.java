package com.nationality.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.fragment.FragmentAddMeetup;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class ImageMeetupInviteeListAdapter extends BaseAdapter {

    private Context context;
    private List<ConnectionListData> content_list;
    //String type;
    //private String list_type;


    public ImageMeetupInviteeListAdapter(Context context,/* List<ConnectionListData> content_list*/String type)
    {
        this.context=context;
        //this.content_list=content_list;
        //this.type = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        /*if (FragmentAddMeetup.meetup_event_type_edited.equalsIgnoreCase("add")) {
            return FragmentAddMeetup.friendArray.size();
        }else {
            String pos = FragmentAddMeetup.position;
            if (!pos.equalsIgnoreCase("")) {
                return MeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().size();
            }
            else {
                return 0;
            }
        }*/
        return FragmentAddMeetup.friendArrayToShow.size();
    }

    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return content_list.get(index);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    //@SuppressWarnings("unused")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        String userId;
        final ViewHolder viewholder;

        if(convertView==null)
        {
            // view = convertView;
            //convertView=l_inflater.inflate(R.layout.layout_addview_friend, null);
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_selected_thumbnail_2, null);

            viewholder=new ViewHolder();

            viewholder.relParent=(RelativeLayout) convertView.findViewById(R.id.rel_parent_grid);
            viewholder.img=(ImageView) convertView.findViewById(R.id.image);
            viewholder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);

            convertView.setTag(viewholder);
        }
        else
        {
            //view = convertView;
            viewholder=(ViewHolder) convertView.getTag();
        }

        /*if (FragmentAddMeetup.meetup_event_type_edited.equalsIgnoreCase("add")) {
            Constants.setImage(viewholder.img, FragmentAddMeetup.friendArray.get(position).getProfilePic(), context);
            viewholder.txt_name.setText(FragmentAddMeetup.friendArray.get(position).getFirstName()+" "
                                            +FragmentAddMeetup.friendArray.get(position).getLastName());
        } else {
            String pos = FragmentAddMeetup.position;
            if (!pos.equalsIgnoreCase("")) {
                Constants.setImage(viewholder.img, MeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().get(position).getAttendeePicture(), context);
                viewholder.txt_name.setText(MeetupAdapterList.meet_list.get(Integer.parseInt(pos)).getAttendee().get(position).getAttendeeName());
            }
        }*/

        Constants.setImage(viewholder.img, FragmentAddMeetup.friendArrayToShow.get(position).get(FragmentAddMeetup.key_pic), context);
        viewholder.txt_name.setText(FragmentAddMeetup.friendArrayToShow.get(position).get(FragmentAddMeetup.key_name));

        viewholder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentAddMeetup.meetup_event_type_edited.equalsIgnoreCase("add") &&
                        FragmentAddMeetup.friendArray.size()>0  ) {
                    Log.d("UserID", FragmentAddMeetup.friendArray.get(position).getId().toString());


                    Bundle arguments = new Bundle();
//                arguments.putString("tag", col_data.getTag());
//                arguments.putString("interest", col_data.getInterest());
                    arguments.putInt("id",  FragmentAddMeetup.friendArray.get(position).getId());

                    FragmentProfile fragment = new FragmentProfile();
                    fragment.setArguments(arguments);

                    ((Activity) context).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                            fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .addToBackStack(Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                            .commit();
                }
                else{
                    if(FragmentAddMeetup.friendArrayToShow.size()>0) {
                        Log.d("UserID", FragmentAddMeetup.friendArrayToShow.get(position).get(FragmentAddMeetup.key_id));


                        Bundle arguments = new Bundle();
//                arguments.putString("tag", col_data.getTag());
//                arguments.putString("interest", col_data.getInterest());
                        arguments.putInt("id", Integer.parseInt(FragmentAddMeetup.friendArrayToShow.get(position).get(FragmentAddMeetup.key_id)));

                        FragmentProfile fragment = new FragmentProfile();
                        fragment.setArguments(arguments);

                        ((Activity) context).getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                                fragment, Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                                .addToBackStack(Constants.TAG_FRAGMENT_CONNECTION_PROF_DTLS)
                                .commit();
                    }
                }
            }
        });

        return convertView;
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);


        return output;
    }

    /**
     * @return the photo URI
     *//*
    public Uri getPhotoUri(int i) {
        try {
            Cursor cur = this.context.getContentResolver().query(
                    ContactsContract.EditProfileData.CONTENT_URI,
                    null,
                    ContactsContract.EditProfileData.CONTACT_ID + "=" + Constants.photoList.get(i) + " AND "
                            + ContactsContract.EditProfileData.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(content_list.get(i)));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }*/
    class ViewHolder
    {

        ImageView img, imgDelete;
        RelativeLayout relParent;
        TextView txt_name;
    }
}
