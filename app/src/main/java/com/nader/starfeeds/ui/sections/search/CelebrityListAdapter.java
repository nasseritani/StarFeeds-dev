package com.nader.starfeeds.ui.sections.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nader.starfeeds.configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.ui.sections.search.listing.CelebrityListingItem;
import com.nader.starfeeds.ui.sections.search.listing.ListingSearchType;
import com.nader.starfeeds.ui.sections.search.listing.SearchListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchLoaderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CelebrityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SearchListingItem> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener listener;
    private boolean isSuggestions;

    public CelebrityListAdapter(ArrayList<SearchListingItem> items, Context ctx, boolean isSuggestions ,OnItemClickListener listener) {
        if (items != null) {
            this.items = items;
        }
        this.listener = listener;
        this.ctx = ctx;
        this.isSuggestions = isSuggestions;
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
        if (items.size() > 0) {
            items.remove(items.size() - 1);
            notifyItemRemoved(items.size());
        }
    }


    public void removeItems() {
        if (items != null) items.clear();
        notifyDataSetChanged();
    }

    public void removeCelebrity(Celebrity celebrity){
        for (int i = 0; i < items.size(); i++) {
            String celebId = celebrity.getId();
            if (items.get(i).getCelebrity().getId().equals(celebId)){
                items.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
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
        private CardView cvCelebrity;
        private TextView tvProfileName;
        private ImageView ivProfile;
        private Button btnFollow;
        private Button btnCancel;

        CelebrityViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            cvCelebrity = (CardView) view.findViewById(R.id.llCelebrity);
            btnFollow = (Button) view.findViewById(R.id.btnFollow);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            cvCelebrity.setOnClickListener(this);
            btnFollow.setOnClickListener(this);
            if (isSuggestions)
            btnCancel.setOnClickListener(this);
            else{
                btnCancel.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            Celebrity celebrity = items.get(getAdapterPosition()).getCelebrity();
            if (listener == null) return;
            switch (v.getId()){
                case R.id.btnFollow:
                        if (btnFollow.getText().equals("Follow")) listener.onFollowClick(celebrity);
                        else listener.onUnFollowClick(celebrity);
                    break;
                case R.id.btnCancel:
                    listener.onDislikeClick(celebrity);
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
        void onDislikeClick(Celebrity celebrity);
    }

}
