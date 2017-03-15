package com.nader.starfeeds.ui.tools;

import android.content.Context;
import android.media.session.MediaController;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.components.FeedFacebook;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedFacebookText;
import com.nader.starfeeds.components.FeedFacebookVideo;
import com.nader.starfeeds.components.FeedTwitterImage;
import com.nader.starfeeds.components.FeedTwitterText;
import com.nader.starfeeds.components.Loader;
import com.nader.starfeeds.components.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookItem;
import com.nader.starfeeds.listing.FeedFacebookTextItem;
import com.nader.starfeeds.listing.FeedFacebookVideoItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.ListingItemType;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
import com.nader.starfeeds.ui.listeners.OnListListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListingItem> items = new ArrayList<>();
    private Context ctx;
    private OnListListener listener = null;

    public ItemsListAdapter(ArrayList<ListingItem> items, OnListListener listener, Context ctx) {
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
        else if(viewType == ListingItemType.FeedFacebookImage.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_image, parent, false);
            holder = new FeedFacebookImageViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedFacebookVideo.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_video, parent, false);
            holder = new FeedFacebookVideoViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedFaceBookText.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_text, parent, false);
            holder = new FeedTwitterTextViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedFacebook.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook, parent, false);
            holder = new FeedFacebookViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedTwitterImage.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_twitter_image, parent, false);
            holder = new FeedTwitterImageViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedTwitterText.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_twitter_text, parent, false);
            holder = new FeedTwitterTextViewHolder(view);
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
                Log.i(Configuration.TAG,"loader");
                break;
            case Reloader:
                ReloaderItem reloaderItem = (ReloaderItem) item;
                Reloader reloader = reloaderItem.getReloader();
                final ReloaderViewHolder reloaderViewHolder = (ReloaderViewHolder) holder;
                NetworkErrorType errorType = reloader.getErrorType();
                switch (errorType){
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
                break;
            case FeedFacebook:
                FeedFacebookItem feedFacebookItem= (FeedFacebookItem) item;         // get article from item container
                FeedFacebook feed4= (FeedFacebook) feedFacebookItem.getFeed();                // extract data from article
                String celebName4 = feed4.getCelebName();
                String date4 = feed4.getDate();
                String feedText4 = feed4.getText();
                String urlCover4 = feed4.getImageUrl();
                // cast holder
                final FeedFacebookViewHolder feedFacebookViewHolder = (FeedFacebookViewHolder) holder;
                // set values
                //// todo Nasser replace and update commented section with required data

                feedFacebookViewHolder.tvProfileName.setText(celebName4 != null ? celebName4 : "");
                feedFacebookViewHolder.tvDate.setText(date4);
                 feedFacebookViewHolder.tvText.setText(feedText4 != "null" ? feedText4 : "");
                Picasso.with(ctx).load(urlCover4).into(feedFacebookViewHolder.ivFeed);
                break;
            case FeedFacebookImage:
FeedFacebookImageItem feedFacebookImageItem= (FeedFacebookImageItem) item;         // get article from item container
FeedFacebookImage feed= (FeedFacebookImage) feedFacebookImageItem.getFeed();                // extract data from article
                String celebName = feed.getCelebName();
                String date = feed.getDate();
                String feedText = feed.getText();
                String urlCover2 = feed.getImageUrl();
                // cast holder
                final FeedFacebookImageViewHolder feedFacebookImageViewHolder = (FeedFacebookImageViewHolder) holder;
                // set values
                //// todo Nasser replace and update commented section with required data
                feedFacebookImageViewHolder.tvProfileName.setText(celebName != null ? celebName : "");
                feedFacebookImageViewHolder.tvDate.setText(date);
              //  feedFacebookImageViewHolder.tvText.setText(feedText != "null" ? feedText : "");
                Picasso.with(ctx).load(urlCover2).into(feedFacebookImageViewHolder.ivFeed);
                break;
            case FeedFaceBookText:
                FeedFacebookTextItem feedFacebookTextItem= (FeedFacebookTextItem) item;         // get article from item container
                FeedFacebookText feed1= (FeedFacebookText) feedFacebookTextItem.getFeed();                // extract data from article
                String celebName1 = feed1.getCelebName();
                String date1 = feed1.getDate();
                String feedText1 = feed1.getText();
                //String urlCover2 = feed.getImageUrl();
                // cast holder
                final FeedFacebookTextViewHolder feedFacebookTextViewHolder = (FeedFacebookTextViewHolder) holder;
                // set values
                //// todo Nasser replace and update commented section with required data
                feedFacebookTextViewHolder.tvProfileName.setText(celebName1 != null ? celebName1 : "");
                feedFacebookTextViewHolder.tvDate.setText(date1);
                  feedFacebookTextViewHolder.tvText.setText(feedText1 != "null" ? feedText1 : "");
                //Picasso.with(ctx).load(urlCover2).into(feedFacebookImageViewHolder.ivFeed);
                break;
            //todo Nasser add the other cases
            case FeedTwitterImage:
                FeedTwitterImageItem feedTwitterImageItem= (FeedTwitterImageItem) item;
                FeedTwitterImage feed2 = (FeedTwitterImage) feedTwitterImageItem.getFeed();
                String celebNameTwitter = feed2.getCelebName();
                String date2 = feed2.getDate();
                //String twitterfeedText = feed2.getText();
                String urlCover1 = feed2.getImageUrl();
                final FeedTwitterImageViewHolder feedTwitterImageViewHolder = (FeedTwitterImageViewHolder) holder;
                feedTwitterImageViewHolder.tvProfileName.setText(celebNameTwitter != null ? celebNameTwitter : "");
                feedTwitterImageViewHolder.tvDate.setText(date2);
//                feedTwitterImageViewHolder.tvFeed.setText(twitterfeedText != "null" ? twitterfeedText : "");
                Picasso.with(ctx).load(urlCover1).into(feedTwitterImageViewHolder.ivFeed);
                break;
            case FeedTwitterText:
                FeedTwitterTextItem feedTwitterTextItem= (FeedTwitterTextItem) item;
                FeedTwitterText feed3 = (FeedTwitterText) feedTwitterTextItem.getFeed();
                String celebNameTwitter2 = feed3.getCelebName();
                String date3 = feed3.getDate();
                String twitterfeedText2 = feed3.getText();
                //String urlCover = feed2.getImageUrl();
                final FeedTwitterTextViewHolder feedTwitterTextViewHolder = (FeedTwitterTextViewHolder) holder;
                feedTwitterTextViewHolder.tvProfileName.setText(celebNameTwitter2 != null ? celebNameTwitter2 : "");
                feedTwitterTextViewHolder.tvDate.setText(date3);
            feedTwitterTextViewHolder.tvFeed.setText(twitterfeedText2 != "null" ? twitterfeedText2 : "");
                //Picasso.with(ctx).load(urlCover1).into(feedTwitterImageViewHolder.ivFeed);
                break;
       /*     case FeedFacebookVideo:
                FeedFacebookVideoItem feedFacebookVideoItem= (FeedFacebookVideoItem) item;
                FeedFacebookVideo feedFacebookVideo= (FeedFacebookVideo) feedFacebookVideoItem.getFeed();
                String celebName15 = feedFacebookVideo.getCelebName();
                String date15 = feedFacebookVideo.getDate();
                String link = feedFacebookVideo.getLink();
                final FeedFacebookVideoViewHolder feedFacebookVideoViewHolder = (FeedFacebookVideoViewHolder) holder;
                feedFacebookVideoViewHolder.tvProfileName.setText(celebName15 != null ? celebName15 : "");
                feedFacebookVideoViewHolder.tvDate.setText(date15);
               // feedTwitterTextViewHolder.tvFeed.setText(twitterfeedText2 != "null" ? twitterfeedText2 : "");
                feedFacebookVideoViewHolder.vvFeed.setVideoURI(Uri.parse(link));
feedFacebookVideoViewHolder.vvFeed.setMediaController(new android.widget.MediaController(ctx));
                feedFacebookVideoViewHolder.vvFeed.requestFocus();
                feedFacebookVideoViewHolder.vvFeed.start();
                break;
*/            default:
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

//todo Nasser add FeedFacebookImageViewHolder...
private class FeedTwitterTextViewHolder extends RecyclerView.ViewHolder {
    private TextView tvProfileName,tvFeed , tvDate;
    private ImageView ivProfile,ivFeed;

    FeedTwitterTextViewHolder(final View view) {
        super(view);
        // init views
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
//        ivFeed = (ImageView) view.findViewById(R.id.ivTwitterFeed);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvFeed = (TextView) view.findViewById(R.id.tvTwitterTextFeed);
    }
}
    private class FeedTwitterImageViewHolder extends RecyclerView.ViewHolder {
    private TextView tvProfileName,tvFeed , tvDate;
    private ImageView ivProfile,ivFeed;

    FeedTwitterImageViewHolder(final View view) {
        super(view);
        // init views
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        ivFeed = (ImageView) view.findViewById(R.id.ivTwitterFeed);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
     //   tvFeed = (TextView) view.findViewById(R.id.tvTwitterTextFeed);
    }
}

    private class FeedFacebookTextViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProfileName,tvText , tvDate;
        private ImageView ivProfile;

        FeedFacebookTextViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            //ivFeed = (ImageView) view.findViewById(R.id.ivFacebookImageFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvText);
        }
    }
    private class FeedFacebookViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProfileName,tvText , tvDate;
        private ImageView ivProfile,ivFeed;

        FeedFacebookViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            ivFeed = (ImageView) view.findViewById(R.id.ivFacebookImageFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvText);
        }
    }
    private class FeedFacebookImageViewHolder extends RecyclerView.ViewHolder {
    private TextView tvProfileName,tvText , tvDate;
    private ImageView ivProfile,ivFeed;

    FeedFacebookImageViewHolder(final View view) {
        super(view);
        // init views
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        ivFeed = (ImageView) view.findViewById(R.id.ivFacebookImageFeed);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvText = (TextView) view.findViewById(R.id.tvText);
    }
}
    private class FeedFacebookVideoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProfileName,tvText , tvDate;
        private ImageView ivProfile,ivFeed;
        private VideoView vvFeed;

        FeedFacebookVideoViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            vvFeed = (VideoView) view.findViewById(R.id.vvFacebookVideoFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvText);

        }
    }
    //testing
    private class FeedViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvText , tvDate;
        private ImageView ivCover;

        FeedViewHolder(final View view) {
            super(view);
            // init views
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            ivCover = (ImageView) view.findViewById(R.id.ivFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvText);
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
