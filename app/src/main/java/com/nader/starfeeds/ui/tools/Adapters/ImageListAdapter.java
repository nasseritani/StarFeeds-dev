package com.nader.starfeeds.ui.tools.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;

import com.nader.starfeeds.components.FeedFacebookLink;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedInstagramImage;
import com.nader.starfeeds.components.FeedTwitterLink;
import com.nader.starfeeds.components.FeedTwitterImage;
import com.nader.starfeeds.components.Loader;
import com.nader.starfeeds.components.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookItem;
import com.nader.starfeeds.listing.FeedInstagramImageItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.ListingItemType;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
//import com.nader.starfeeds.ui.tools.Activities.CelebrityActivity;
import com.nader.starfeeds.ui.tools.Activities.CelebrityActivity;
import com.nader.starfeeds.ui.tools.Activities.GridDetailActivity;
import com.nader.starfeeds.ui.listeners.OnListListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListingItem> items = new ArrayList<>();
    private Context ctx;
    private OnListListener listener = null;
    MediaController mMediaController;

    public ImageListAdapter(ArrayList<ListingItem> items, OnListListener listener, Context ctx) {
        if (items != null) {
            this.items = items;
        }
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ListingItemType.Loader.ordinal()) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.row_loader, parent, false);
            holder = new LoaderViewHolder(view);
        }
        else if (viewType == ListingItemType.Reloader.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reloader, parent, false);
            holder = new ReloaderViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedFacebookImage.ordinal()||viewType == ListingItemType.FeedInstagramImage.ordinal() || viewType == ListingItemType.FeedTwitter.ordinal()||viewType == ListingItemType.FeedFacebookVideo.ordinal()||viewType == ListingItemType.FeedFacebook.ordinal()||viewType == ListingItemType.FeedTwitterImage.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_list, parent, false);
            holder = new FeedViewHolder(view);
        }

        return holder;
    }
    /**
     * Adds new items to the list.
     */
    public void addNewItems(ArrayList<ListingItem> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }
    /**
     * Adds item to the end of the list.
     */
    public void addNewItem(ListingItem item) {
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
        ListingItem item = items.get(position);
        //sets position to current binded view.
        switch (item.getType()) {

            case Loader:
                LoaderItem loaderItem = (LoaderItem) item;
                Loader loader = loaderItem.getLoader();
                final LoaderViewHolder vhAdvertisement = (LoaderViewHolder) holder;
                Log.i(Configuration.TAG, "loader");
                break;
            case Reloader:
                ReloaderItem reloaderItem = (ReloaderItem) item;
                Reloader reloader = reloaderItem.getReloader();
                final ReloaderViewHolder reloaderViewHolder = (ReloaderViewHolder) holder;
                NetworkErrorType errorType = reloader.getErrorType();
                switch (errorType) {
                    case CONNECTION_FAIL:
                        reloaderViewHolder.tvError.setText("Internet Connection Failed!");
                        break;
                    case API_FAIL:
                        reloaderViewHolder.tvError.setText("An Internal Error Occurred. Please Try Again Later");
                        break;
                    default:
                        reloaderViewHolder.tvError.setText("An Internal Error Occurred. Please Try Again Later");
                }

                break;
            case FeedTwitter:
                FeedTwitterItem feedTwitterItem = (FeedTwitterItem) item;
                FeedTwitterLink feedTwitterLink = (FeedTwitterLink) feedTwitterItem.getFeed();
                String twitterSharedImage = feedTwitterLink.getImageUrl();
                final FeedViewHolder feedViewHolder = (FeedViewHolder) holder;
                Picasso.with(ctx).load(twitterSharedImage).into(feedViewHolder.ivFeed);
                break;
            case FeedFacebook:
                FeedFacebookItem feedFacebookItem = (FeedFacebookItem) item;         // get article from item container
                FeedFacebookLink feedFacebookLink = (FeedFacebookLink) feedFacebookItem.getFeed();                // extract data from article
                String fbSharedImage = feedFacebookLink.getImageUrl();
                // cast holder
                final FeedViewHolder feedViewHolder1 = (FeedViewHolder) holder;
                // set values
                Picasso.with(ctx).load(fbSharedImage).into(feedViewHolder1.ivFeed);
                break;
            case FeedFacebookImage:
                FeedFacebookImageItem feedFacebookImageItem = (FeedFacebookImageItem) item;
                FeedFacebookImage feedFacebookImage = (FeedFacebookImage) feedFacebookImageItem.getFeed();
                String fbImage = feedFacebookImage.getImageUrl();
                // cast holder
                final FeedViewHolder feedViewHolder2 = (FeedViewHolder) holder;
                // set values
                Picasso.with(ctx).load(fbImage).into(feedViewHolder2.ivFeed);
                break;


            case FeedTwitterImage:
                FeedTwitterImageItem feedTwitterImageItem = (FeedTwitterImageItem) item;
                FeedTwitterImage feedTwitterImage = (FeedTwitterImage) feedTwitterImageItem.getFeed();
                String twitterImage = feedTwitterImage.getImageUrl();
                final FeedViewHolder feedViewHolder5 = (FeedViewHolder) holder;
                Picasso.with(ctx).load(twitterImage).into(feedViewHolder5.ivFeed);
                break;

            case FeedInstagramImage:
                FeedInstagramImageItem feedInstagramImageItem= (FeedInstagramImageItem) item;
                FeedInstagramImage feedInstagramImage = (FeedInstagramImage) feedInstagramImageItem.getFeed();

                String urlCoverInstagram = feedInstagramImage.getImageUrl();

                final FeedViewHolder feedViewHolder4 = (FeedViewHolder) holder;

                Picasso.with(ctx).load(urlCoverInstagram).into(feedViewHolder4.ivFeed);
                break;

            default:
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return items != null ? items.get(position).getType().ordinal() : 0;
    }
    public ListingItemType getItemType(int position) {
        return items.get(position).getType();
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
    private class FeedViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFeed;

        FeedViewHolder(final View view) {
            super(view);
            // init views
            ivFeed = (ImageView) view.findViewById(R.id.imageViewList);
            ivFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(),GridDetailActivity.class);
                    for(int i=0;i<items.size();i++){
                    intent.putExtra("FeedType",items.get(getAdapterPosition()).getFeed().getId());}
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


    private class ReloaderViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        Button button;
        TextView tvError;
        public ReloaderViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            button = (Button) itemView.findViewById(R.id.btnRowReloader);
            tvError = (TextView) itemView.findViewById(R.id.tvRowReloader);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onReloaderButtonSelected();
                    }
                }
            });
        }
    }

    private String toFormattedDate(String stringDate) {
        DateFormat formatter;
        Date date = null;
        //
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = (Date) formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTime(date);
        Calendar currentCalendar = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) > givenCalendar.get(Calendar.YEAR)) {
            return (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);
        } else if (currentCalendar.get(Calendar.MONTH) > givenCalendar.get(Calendar.MONTH)) {
            return (String) android.text.format.DateFormat.format("MMM-dd", date);
        } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) > givenCalendar.get(Calendar.DAY_OF_MONTH)) {
            return currentCalendar.get(Calendar.DAY_OF_MONTH) - givenCalendar.get(Calendar.DAY_OF_MONTH) + " days ago";
        } else if (currentCalendar.get(Calendar.HOUR_OF_DAY) > givenCalendar.get(Calendar.HOUR_OF_DAY)) {
            return currentCalendar.get(Calendar.HOUR_OF_DAY) - givenCalendar.get(Calendar.HOUR_OF_DAY) + " h";
        } else if (currentCalendar.get(Calendar.MINUTE) > givenCalendar.get(Calendar.MINUTE)) {
            return currentCalendar.get(Calendar.MINUTE) - givenCalendar.get(Calendar.MINUTE) + " m";
        } else return "Just Now";
    }

}
