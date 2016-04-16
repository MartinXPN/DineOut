package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;


public class AdapterRestaurantDetailsGrid extends BaseAdapter {

    ViewHolder holder;
    Context context;

    private static final int images[] =  {  R.mipmap.ic_time,
                                            R.mipmap.ic_price,
                                            R.mipmap.ic_wifi,
                                            R.mipmap.ic_vip,
                                            R.mipmap.ic_fourshet,
                                            R.mipmap.ic_shipping,
                                            R.mipmap.ic_credit_card,
                                            R.mipmap.ic_smoking_area,
                                            R.mipmap.ic_no_smoking_area };

    private static final String descriptions[] =
        {   "17:00 - 24:00",
            "Expensive",
            "WiFi",
            "VIP areas",
            "Fourshet",
            "Shipping",
            "Accepts credit-cards",
            "Smoking areas",
            "No-smoking areas" };


    private static final boolean has[] = {  true,
                                            true,
                                            true,
                                            true,
                                            false,
                                            true,
                                            false,
                                            true,
                                            true };


    public AdapterRestaurantDetailsGrid( Context context ) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_details_grid_item, parent, false );

            holder = new ViewHolder( convertView );
            holder.image.setImageResource( images[position] );
            if( has[position] )     holder.image.setBackgroundResource( R.drawable.circle_green );
            else                    holder.image.setBackgroundResource( R.drawable.circle_red );

            holder.description.setText( descriptions[position] );
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageButton image;
        TextView description;

        ViewHolder( View v ) {
            image = (ImageButton) v.findViewById( R.id.restaurant_details_grid_image );
            description = (TextView ) v.findViewById( R.id.restaurant_details_grid_description );
        }
    }
}
