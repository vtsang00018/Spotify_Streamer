package com.example.vincent.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vincent on 6/29/2015.
 */
public class ArtistAdapter extends ArrayAdapter<ArtistInfo> {

    Context mContext;

    public ArtistAdapter(Context context, int resource, List<ArtistInfo> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView artistIcon;
        TextView artistName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        ArtistInfo rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_artists, null);
            holder = new ViewHolder();
            holder.artistIcon = (ImageView) convertView.findViewById(R.id.artist_image);
            holder.artistName = (TextView) convertView.findViewById(R.id.artist_name_text);
            convertView.setTag(holder);
        }

        else
            holder = (ViewHolder) convertView.getTag();

            holder.artistName.setText(rowItem.getName());
            Picasso.with(mContext).load(rowItem.getImageUrl()).into(holder.artistIcon);

        return convertView;
    }
}
