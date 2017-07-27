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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nationality.R;
import com.nationality.fragment.FragmentProfile;
import com.nationality.model.ConnectionListData;
import com.nationality.model.MeetupDetailsAttendeesList;
import com.nationality.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private List<ConnectionListData> content_list;
    private String list_type;


    public ImageListAdapter(Context context, List<ConnectionListData> content_list)
    {
        if(context!=null) {
            this.context = context;
            this.content_list = content_list;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(content_list.size()>5)
            return 5;
        return content_list.size();
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

    @SuppressWarnings("unused")
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
            viewholder.txt_name=(TextView)convertView.findViewById(R.id.txt_name);

            convertView.setTag(viewholder);
        }
        else
        {
            //view = convertView;
            viewholder=(ViewHolder) convertView.getTag();
        }

        Constants.setImage(viewholder.img, content_list.get(position).getProfilePic(), context);
        viewholder.txt_name.setText(content_list.get(position).getFirstName()+" "+
        content_list.get(position).getLastName());


        viewholder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectionListData meetObj=content_list.get(position);


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
