package com.nader.starfeeds.ui.tools.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Config;
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
import com.nader.starfeeds.components.FeedFacebookText;
import com.nader.starfeeds.components.FeedFacebookVideo;
import com.nader.starfeeds.components.FeedInstagramImage;
import com.nader.starfeeds.components.FeedInstagramVideo;
import com.nader.starfeeds.components.FeedTwitterLink;
import com.nader.starfeeds.components.FeedTwitterImage;
import com.nader.starfeeds.components.FeedTwitterText;
import com.nader.starfeeds.components.Loader;
import com.nader.starfeeds.components.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookItem;
import com.nader.starfeeds.listing.FeedFacebookTextItem;
import com.nader.starfeeds.listing.FeedFacebookVideoItem;
import com.nader.starfeeds.listing.FeedInstagramImageItem;
import com.nader.starfeeds.listing.FeedInstagramVideoItem;
import com.nader.starfeeds.listing.FeedItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.ListingItemType;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
//import com.nader.starfeeds.ui.tools.Activities.CelebrityActivity;
import com.nader.starfeeds.ui.tools.Activities.CelebrityActivity;
import com.nader.starfeeds.ui.listeners.OnListListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FeedsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ListingItem> items = new ArrayList<>();
    private Context ctx;
    private OnListListener listener = null;
    MediaController mMediaController;

    public FeedsListAdapter(ArrayList<ListingItem> items, OnListListener listener, Context ctx) {
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
            holder = new FeedFacebookTextViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedFacebook.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_shared, parent, false);
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
        else if(viewType == ListingItemType.FeedTwitter.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_twitter_shared, parent, false);
            holder = new FeedTwitterViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedInstagramImage.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_instagram_image, parent, false);
            holder = new FeedInstagramImageViewHolder(view);
        }
        else if(viewType == ListingItemType.FeedInstagramVideo.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_instagram_video, parent, false);
            holder = new FeedInstagramVideoViewHolder(view);
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
    public void addNewItemss(ArrayList<FeedItem> newItems) {
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
                String celebName = feedTwitterLink.getCelebName();
                String date = feedTwitterLink.getDate();
                String feedText = feedTwitterLink.getText();
                String urlImage = feedTwitterLink.getImageUrl();
                String linkDescription = feedTwitterLink.getLinkDescription();
                String link = feedTwitterLink.getLink();
                String twitterProifleImage = feedTwitterLink.getProfileImage();
                final FeedTwitterViewHolder feedTwitterViewHolder = (FeedTwitterViewHolder) holder;
                feedTwitterViewHolder.ivProfile.setImageResource(R.drawable.nancyajramprofile);
                feedTwitterViewHolder.tvDate.setText(date);
                feedTwitterViewHolder.tvProfileName.setText(celebName);
                feedTwitterViewHolder.tvLinkDescription.setText(linkDescription);
                feedTwitterViewHolder.tvLink.setText(link);
                feedTwitterViewHolder.tvFeed.setText(feedText != "null" ? feedText : "");
                Picasso.with(ctx).load(urlImage).into(feedTwitterViewHolder.ivFeed);
                Picasso.with(ctx).load(twitterProifleImage).into(feedTwitterViewHolder.ivProfile);

                break;
            case FeedFacebook:
                FeedFacebookItem feedFacebookItem = (FeedFacebookItem) item;         // get article from item container
                FeedFacebookLink feedFacebookLink = (FeedFacebookLink) feedFacebookItem.getFeed();                // extract data from article
                String celebNameFb = feedFacebookLink.getCelebName();
                String dateFb = feedFacebookLink.getDate();
                String feedTextFb = feedFacebookLink.getText();
                String linkDescriptionFb = feedFacebookLink.getLinkDescription();
                String linkFb = feedFacebookLink.getLink();
                String urlCoverFb = feedFacebookLink.getImageUrl();
                String imageProfile = feedFacebookLink.getProfileImage();
                // cast holder
                final FeedFacebookViewHolder feedFacebookViewHolder = (FeedFacebookViewHolder) holder;
                // set values

                feedFacebookViewHolder.tvProfileName.setText(celebNameFb);
                feedFacebookViewHolder.tvDate.setText(dateFb);
                feedFacebookViewHolder.tvImageText.setText(linkDescriptionFb);
                feedFacebookViewHolder.tvLink.setText(linkFb);
                feedFacebookViewHolder.tvText.setText(feedTextFb);
                Picasso.with(ctx).load(urlCoverFb).into(feedFacebookViewHolder.ivLink);
                Picasso.with(ctx).load(imageProfile).into(feedFacebookViewHolder.ivProfile);
                break;
            case FeedFacebookImage:
                FeedFacebookImageItem feedFacebookImageItem = (FeedFacebookImageItem) item;
                FeedFacebookImage feedFacebookImage = (FeedFacebookImage) feedFacebookImageItem.getFeed();
                String celebNameFbImage = feedFacebookImage.getCelebName();
                String dateFbImage = feedFacebookImage.getDate();
                String urlCoverFbImage = feedFacebookImage.getImageUrl();
                String feedFbImageText = feedFacebookImage.getText();
                String facebookProfileImage = feedFacebookImage.getProfileImage();
                // cast holder
                final FeedFacebookImageViewHolder feedFacebookImageViewHolder = (FeedFacebookImageViewHolder) holder;
                // set values
                feedFacebookImageViewHolder.tvProfileName.setText(celebNameFbImage != null ? celebNameFbImage : "");
                feedFacebookImageViewHolder.tvDate.setText(dateFbImage);
                feedFacebookImageViewHolder.tvFeed.setText(feedFbImageText);
                Picasso.with(ctx).load(urlCoverFbImage).into(feedFacebookImageViewHolder.ivFeed);
                Picasso.with(ctx).load(facebookProfileImage).into(feedFacebookImageViewHolder.ivProfile);
                break;
            case FeedFaceBookText:
                FeedFacebookTextItem feedFacebookTextItem = (FeedFacebookTextItem) item;
                FeedFacebookText feedFacebookText = (FeedFacebookText) feedFacebookTextItem.getFeed();
                String celebNameFbText = feedFacebookText.getCelebName();
                String dateFbText = feedFacebookText.getDate();
                String feedFbText = feedFacebookText.getText();
                String fbProfileImage = feedFacebookText.getProfileImage();
                // cast holder
                final FeedFacebookTextViewHolder feedFacebookTextViewHolder = (FeedFacebookTextViewHolder) holder;
                // set values
                feedFacebookTextViewHolder.tvProfileName.setText(celebNameFbText);
                feedFacebookTextViewHolder.tvDate.setText(dateFbText);
                feedFacebookTextViewHolder.tvText.setText(feedFbText);
                Picasso.with(ctx).load(fbProfileImage).into(feedFacebookTextViewHolder.ivProfile);

                break;

            case FeedTwitterImage:
                FeedTwitterImageItem feedTwitterImageItem = (FeedTwitterImageItem) item;
                FeedTwitterImage feedTwitterImage = (FeedTwitterImage) feedTwitterImageItem.getFeed();
                String celebNameTwitter = feedTwitterImage.getCelebName();
                String dateTwitter = feedTwitterImage.getDate();
                String twitterFeedText = feedTwitterImage.getText();
                String urlCoverTwitter = feedTwitterImage.getImageUrl();
                String twitterProfileImage = feedTwitterImage.getProfileImage();
                final FeedTwitterImageViewHolder feedTwitterImageViewHolder = (FeedTwitterImageViewHolder) holder;
                feedTwitterImageViewHolder.tvProfileName.setText(celebNameTwitter != null ? celebNameTwitter : "");
                feedTwitterImageViewHolder.tvDate.setText(dateTwitter);
                feedTwitterImageViewHolder.tvFeed.setText(twitterFeedText != null ? twitterFeedText : "");
                Picasso.with(ctx).load(urlCoverTwitter).into(feedTwitterImageViewHolder.ivFeed);
                Picasso.with(ctx).load(twitterProfileImage).into(feedTwitterImageViewHolder.ivProfile);
                break;
            case FeedTwitterText:
                FeedTwitterTextItem feedTwitterTextItem = (FeedTwitterTextItem) item;
                FeedTwitterText feedTwitterText = (FeedTwitterText) feedTwitterTextItem.getFeed();
                String celebNameTwitterText = feedTwitterText.getCelebName();
                String dateTwitterText = feedTwitterText.getDate();
                String twitterfeedText2 = feedTwitterText.getText();
                String twitterProfileImage2 = feedTwitterText.getProfileImage();
                final FeedTwitterTextViewHolder feedTwitterTextViewHolder = (FeedTwitterTextViewHolder) holder;
                feedTwitterTextViewHolder.tvProfileName.setText(celebNameTwitterText != null ? celebNameTwitterText : "");
                feedTwitterTextViewHolder.tvDate.setText(dateTwitterText);
                feedTwitterTextViewHolder.tvFeed.setText(twitterfeedText2);
                Picasso.with(ctx).load(twitterProfileImage2).into(feedTwitterTextViewHolder.ivProfile);
                break;
            case FeedFacebookVideo:
                FeedFacebookVideoItem feedFacebookVideoItem = (FeedFacebookVideoItem) item;
                FeedFacebookVideo feedFacebookVideo = (FeedFacebookVideo) feedFacebookVideoItem.getFeed();
                String celebNameFbVideo = feedFacebookVideo.getCelebName();
                String dateFbVideo = feedFacebookVideo.getDate();
                String videoFbUrl = feedFacebookVideo.getVideoUrl();
                String fbVideoImageProfile = feedFacebookVideo.getProfileImage();
                String fbVideoText=feedFacebookVideo.getText();
                final FeedFacebookVideoViewHolder feedFacebookVideoViewHolder = (FeedFacebookVideoViewHolder) holder;
                feedFacebookVideoViewHolder.tvProfileName.setText(celebNameFbVideo != null ? celebNameFbVideo : "");
                Picasso.with(ctx).load(fbVideoImageProfile).into(feedFacebookVideoViewHolder.ivProfile);
                feedFacebookVideoViewHolder.tvDate.setText(dateFbVideo);
                feedFacebookVideoViewHolder.tvText.setText(fbVideoText);
                Uri vidUri = Uri.parse(videoFbUrl);
                feedFacebookVideoViewHolder.vvFeed.setVideoURI(vidUri);
                feedFacebookVideoViewHolder.vvFeed.setMediaController(mMediaController = new MediaController(ctx));
                mMediaController.requestFocus();
                feedFacebookVideoViewHolder.vvFeed.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        feedFacebookVideoViewHolder.vvFeed.start();
                    }
                });
                break;

            case FeedInstagramImage:
                FeedInstagramImageItem feedInstagramImageItem= (FeedInstagramImageItem) item;
                FeedInstagramImage feedInstagramImage = (FeedInstagramImage) feedInstagramImageItem.getFeed();
                String celebNameInstagram = feedInstagramImage.getCelebName();
                String dateInstagram = feedInstagramImage.getDate();
                String instagramFeedText=feedInstagramImage.getText();
                String likesCount=feedInstagramImage.getCount();
                String urlCoverInstagram = feedInstagramImage.getImageUrl();
                String instagramProfileImage=feedInstagramImage.getProfileImage();
                final FeedInstagramImageViewHolder feedInstagramImageViewHolder = (FeedInstagramImageViewHolder) holder;
                feedInstagramImageViewHolder.tvProfileName.setText(celebNameInstagram);
                feedInstagramImageViewHolder.tvDate.setText(dateInstagram);
                feedInstagramImageViewHolder.tvLikesCount.setText(likesCount);
                feedInstagramImageViewHolder.tvText.setText((Html.fromHtml("<b>" + celebNameInstagram + "</b>" + " " + instagramFeedText)));
                Picasso.with(ctx).load(urlCoverInstagram).into(feedInstagramImageViewHolder.ivFeed);
                Picasso.with(ctx).load(instagramProfileImage).into(feedInstagramImageViewHolder.ivProfile);
                break;
            case FeedInstagramVideo:
                FeedInstagramVideoItem feedInstagramVideoItem= (FeedInstagramVideoItem) item;
                FeedInstagramVideo feedInstagramVideo = (FeedInstagramVideo) feedInstagramVideoItem.getFeed();
                String celebNameInsta = feedInstagramVideo.getCelebName();
                String dateInsta = feedInstagramVideo.getDate();
                String instaFeedText=feedInstagramVideo.getText();
                String instalikesCount=feedInstagramVideo.getCount();
                String instaImageProfile=feedInstagramVideo.getProfileImage();
                String urlVideoInstagram = feedInstagramVideo.getVideoUrl();
                final FeedInstagramVideoViewHolder feedInstagramVideoViewHolder = (FeedInstagramVideoViewHolder) holder;
                feedInstagramVideoViewHolder.tvProfileName.setText(celebNameInsta);
                feedInstagramVideoViewHolder.tvDate.setText(dateInsta);
                feedInstagramVideoViewHolder.tvLikesCount.setText(instalikesCount);
                feedInstagramVideoViewHolder.tvText.setText((Html.fromHtml("<b>" + celebNameInsta + "</b>" + " " + instaFeedText)));
                Picasso.with(ctx).load(instaImageProfile).into(feedInstagramVideoViewHolder.ivProfile);
                feedInstagramVideoViewHolder.vvFeed.setVideoPath(urlVideoInstagram);
                MediaController mediaController = new MediaController(ctx);
                mediaController.setAnchorView(feedInstagramVideoViewHolder.vvFeed);
                feedInstagramVideoViewHolder.vvFeed.setMediaController(mediaController);
                feedInstagramVideoViewHolder.vvFeed.requestFocus();
                feedInstagramVideoViewHolder.vvFeed.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        feedInstagramVideoViewHolder.vvFeed.start();
                    }
                });
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
    private class FeedTwitterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvFeed ,tvDate,tvLink,tvLinkDescription;
        private ImageView ivProfile,ivFeed;

        FeedTwitterViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            tvFeed= (TextView) view.findViewById(R.id.tvText);
            tvLinkDescription= (TextView) view.findViewById(R.id.tvImageText);
            tvLink= (TextView) view.findViewById(R.id.tvLink);
            ivFeed = (ImageView) view.findViewById(R.id.ivImageLink);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }

    }

    private class FeedTwitterTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvFeed , tvDate;
        private ImageView ivProfile;

        FeedTwitterTextViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvFeed = (TextView) view.findViewById(R.id.tvFeed);
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
    }

    private class FeedTwitterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvFeed , tvDate;
        private ImageView ivProfile,ivFeed;

        FeedTwitterImageViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            tvFeed= (TextView) view.findViewById(R.id.tvFeed);
            ivFeed = (ImageView) view.findViewById(R.id.ivTwitterFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
           tvProfileName.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
    }

    private class FeedFacebookTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvText , tvDate;
        private ImageView ivProfile;

        FeedFacebookTextViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvText);
            tvProfileName.setOnClickListener(this);
        }

       @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
    }
    private class FeedFacebookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvText,tvDate,tvLink,tvImageText;
        private ImageView ivProfile,ivLink;

        FeedFacebookViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            ivLink = (ImageView) view.findViewById(R.id.ivImageLink);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvFeed);
            tvLink= (TextView) view.findViewById(R.id.tvLink);
            tvImageText= (TextView) view.findViewById(R.id.tvImageText);
            ivProfile= (ImageView) view.findViewById(R.id.ivProfileImage);
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
        }

    private class FeedFacebookImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvDate,tvFeed;
        private ImageView ivProfile,ivFeed;

        FeedFacebookImageViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            ivFeed = (ImageView) view.findViewById(R.id.ivFacebookImageFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvFeed= (TextView) view.findViewById(R.id.tvFeed);
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
    }
    private class FeedFacebookVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvText, tvDate;
        private ImageView ivProfile;
        private VideoView vvFeed;

        FeedFacebookVideoViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            vvFeed = (VideoView) view.findViewById(R.id.vvFacebookVideoFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvFeed);
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }
    }
    private class FeedInstagramImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvProfileName,tvText ,tvDate,tvLikesCount,tvLikes;
        private ImageView ivProfile,ivFeed,ivLike,ivComment,ivSend;
        FeedInstagramImageViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfilename);
            ivFeed = (ImageView) view.findViewById(R.id.ivPhoto);
            tvDate = (TextView) view.findViewById(R.id.tvTime);
            tvText = (TextView) view.findViewById(R.id.tvCaption);
            tvLikesCount= (TextView) view.findViewById(R.id.tvLikesCountNumber);
            tvLikes = (TextView) view.findViewById(R.id.tvLikesCount);
            ivComment= (ImageView) view.findViewById(R.id.ivMakeComment);
            ivLike= (ImageView) view.findViewById(R.id.ivLike);
            ivSend= (ImageView) view.findViewById(R.id.ivSend);
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePhoto);
            tvProfileName.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(view.getContext(),CelebrityActivity.class);
            i.putExtra("Celebname",tvProfileName.getText());
            view.getContext().startActivity(i);
        }

        }

    private class FeedInstagramVideoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProfileName,tvText ,tvDate,tvUsername,tvLikesCount,tvLikes;
        private ImageView ivProfile,ivLike,ivComment,ivSend;
        private VideoView vvFeed;
        FeedInstagramVideoViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfilename);
            vvFeed = (VideoView) view.findViewById(R.id.vvVideo);
            tvDate = (TextView) view.findViewById(R.id.tvTime);
            tvText = (TextView) view.findViewById(R.id.tvCaption);
            tvLikesCount= (TextView) view.findViewById(R.id.tvLikesCountNumber);
            tvLikes = (TextView) view.findViewById(R.id.tvLikesCount);
            ivComment= (ImageView) view.findViewById(R.id.ivMakeComment);
            ivLike= (ImageView) view.findViewById(R.id.ivLike);
            ivSend= (ImageView) view.findViewById(R.id.ivSend);
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePhoto);
            tvProfileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(view.getContext(),CelebrityActivity.class);
                    i.putExtra("Celebname",tvProfileName.getText());
                    view.getContext().startActivity(i);

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
