package com.nader.starfeeds.ui.sections.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.ui.sections.celebrity.CelebrityActivity;
import com.nader.starfeeds.ui.sections.search.listing.CelebrityListingItem;
import com.nader.starfeeds.ui.sections.search.listing.ListingSearchType;
import com.nader.starfeeds.ui.sections.search.listing.SearchListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchLoaderItem;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CelebrityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SearchListingItem> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener listener;

    public CelebrityListAdapter(ArrayList<SearchListingItem> items,OnItemClickListener listener, Context ctx) {
        if (items != null) {
            this.items = items;
        }
        this.listener = listener;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ListingSearchType.LOADER.ordinal()) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.row_loader, parent, false);
            holder = new LoaderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.row_celebrity, parent, false);
            holder = new CelebrityViewHolder(view);
        }
        return holder;
    }
    /**
     * Adds new items to the list.
     */
    public void addNewItems(ArrayList<SearchListingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Adds item to the end of the list.
     */
    public void addNewItem(SearchListingItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }
    /**
     * Removes last item of the list.
     */
    public void removeLastItem() {
        items.remove(items.size() - 1);
        notifyItemRemoved(items.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchListingItem item = items.get(position);
        switch (item.getType()) {
            case LOADER:
                bindLoader((LoaderViewHolder) holder, (SearchLoaderItem) item);
                break;
            case CELEBRITY:
                bindCelebrity((CelebrityViewHolder) holder, (CelebrityListingItem) item);
                break;
            default:
        }
    }

    private void bindCelebrity(CelebrityViewHolder holder, CelebrityListingItem item) {
        Celebrity celebrity = item.getCelebrity();
        if (celebrity != null) {
            String celebName = celebrity.getName();
            String profileImage = celebrity.getProfileUrl();

            holder.tvProfileName.setText(celebName);
            Picasso.with(ctx).load(profileImage).placeholder(R.drawable.placeholder).into(holder.ivProfile);
            if (celebrity.isFollowed()){
                holder.btnFollow.setText("Following");
                holder.btnFollow.setBackgroundColor(ContextCompat.getColor(ctx, R.color.follow));
                holder.btnFollow.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            }
            else {
                holder.btnFollow.setText("Follow");
                holder.btnFollow.setBackgroundColor(ContextCompat.getColor(ctx, R.color.transparent));
                holder.btnFollow.setTextColor(ContextCompat.getColor(ctx, R.color.follow));
            }
        }
    }

    private void bindLoader(LoaderViewHolder holder, SearchLoaderItem item) {
        SearchLoaderItem loaderItem = item;
        Loader loader = loaderItem.getLoader();
        final LoaderViewHolder vhAdvertisement = holder;
        Log.i(Configuration.TAG, "loader");
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return items != null ? items.get(position).getType().ordinal() : 0;
    }

    private class LoaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;

        public LoaderViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            }
        }
    }
    private class CelebrityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout llCelebrity;
        private TextView tvProfileName;
        private ImageView ivProfile;
        private Button btnFollow;

        CelebrityViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            llCelebrity = (LinearLayout) view.findViewById(R.id.llCelebrity);
            btnFollow = (Button) view.findViewById(R.id.btnFollow);
            llCelebrity.setOnClickListener(this);
            btnFollow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Celebrity celebrity = items.get(getAdapterPosition()).getCelebrity();
            switch (v.getId()){
                case R.id.btnFollow:
                    if (listener != null) {
                        if (btnFollow.getText().equals("Follow")) listener.onFollowClick(celebrity);
                        else listener.onUnFollowClick(celebrity);
                    }
                    break;
                default:
                    if (listener != null) {
                        listener.onItemClick(celebrity);
                    }
                    //startCelebrityActivity(celebrity.getId());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Celebrity celebrity);
        void onFollowClick(Celebrity celebrity);
        void onUnFollowClick(Celebrity celebrity);
    }

}
