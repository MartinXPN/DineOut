package com.dineoutmobile.dineout.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.models.SearchInfo;

public class AdapterSearchFilters extends RecyclerView.Adapter<AdapterSearchFilters.ViewHolder> {

    ViewHolder holder;
    private Context context;
    private static SearchInfo searchInfo;
    OnSearchOptionsChangedListener listener;

    public interface OnSearchOptionsChangedListener {
        void onSearchOptionsChanged( SearchInfo searchInfo );
    }


    /// TODO pass search info from fragment
    /// in order to be sure that this adapter and search query
    /// have the same instance of searchInfo object
    public AdapterSearchFilters(Context context) {
        this.context = context;
        if( searchInfo == null )
            searchInfo = new SearchInfo();
        listener = (OnSearchOptionsChangedListener) context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.search_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final SearchInfo.Filters currentItem = SearchInfo.Filters.all.get( position );
        holder.updateFilterBackground( currentItem );
        holder.icon.setImageResource( currentItem.resource );
        holder.icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText( context, currentItem.descriptionResId, Toast.LENGTH_SHORT ).show();
                return true;
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearchOptionsChanged( searchInfo );

                currentItem.flipState();
                holder.updateFilterBackground( currentItem );
            }
        });
    }


    @Override
    public int getItemCount() {
        return SearchInfo.Filters.all.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton icon;

        ViewHolder( View v ) {
            super( v );
            icon = (ImageButton) v.findViewById( R.id.icon );
        }

        public void updateFilterBackground( SearchInfo.Filters currentItem ) {

            if( currentItem.state == SearchInfo.FilterStates.NEUTRAL )
                icon.setBackgroundResource( R.drawable.circle_search_neutral );
            else if( currentItem.state == SearchInfo.FilterStates.SUPPORTED)
                icon.setBackgroundResource( R.drawable.circle_green );
        }
    }
}
