package com.example.jake.LeagueStats;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jake on 10/28/2015.
 *
 * https://www.caveofprogramming.com/guest-posts/custom-listview-with-imageview-and-textview-in-android.html
 */
public class PictureTextSubTextAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    ArrayList<String> text;
    ArrayList<String> subText;
    ArrayList<Bitmap> images;
    //ArrayList<Integer> images;
    Context context;
    public PictureTextSubTextAdapter(Activity activity, ArrayList<String> text, ArrayList<String> subText, ArrayList<Bitmap> images) {
        this.text=text;
        this.subText = subText;
        this.images = images;
        context=activity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //POPULATES VIEW WITH HOLDER CLASS DATA
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.picture_text_subtext_list_item, null);  // USE MY CLASS HERE!
        // Get all views
        holder.text=(TextView) rowView.findViewById(R.id.single_picture_text_subtext_text);
        holder.subText=(TextView) rowView.findViewById(R.id.single_picture_text_subtext_subtext);
        holder.img=(ImageView) rowView.findViewById(R.id.single_picture_text_subtext_pic);

        // Handle 0 values
        if (text.size() > 0)
            holder.text.setText(text.get(position));
        else
            holder.text.setText("");

        if (subText.size() > 0)
            holder.subText.setText(subText.get(position));
        else
            holder.subText.setText("");

        if(images.size() > 0) {
            holder.img.setImageBitmap(images.get(position));
        }
        else {
            //holder.img.setImageBitmap(null);
        }
        return rowView;
    }

    // CLASS THAT HOLDS DATA FOR VIEW
    public class Holder
    {
        ImageView img;
        TextView text;
        TextView subText;
    }

}