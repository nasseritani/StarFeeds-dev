package com.nader.starfeeds.ui.sections.user_feeds;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;

import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;
import com.nader.starfeeds.data.componenets.model.FeedInstagramVideo;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.CelebrityProfileItem;
import com.nader.starfeeds.listing.DataEndedItem;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookLinkItem;
import com.nader.starfeeds.listing.FeedFacebookTextItem;
import com.nader.starfeeds.listing.FeedFacebookVideoItem;
import com.nader.starfeeds.listing.FeedInstagramImageItem;
import com.nader.starfeeds.listing.FeedInstagramVideoItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterLinkItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.ListingItemType;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
import com.nader.starfeeds.ui.sections.VideoPlayerActivity;
import com.nader.starfeeds.ui.sections.celebrity.CelebrityActivity;
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
    private MediaController mMediaController;
    private boolean isCelebrity;

    public FeedsListAdapter(ArrayList<ListingItem> items, OnListListener listener, Context ctx, boolean isCelebrity) {
        if (items != null) {
            this.items = items;
        }
        this.ctx = ctx;
        this.listener = listener;
        this.isCelebrity = isCelebrity;
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
        else if (viewType == ListingItemType.ENDED.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data_ended, parent, false);
            holder = new DataEndedViewHolder(view);
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
        else if(viewType == ListingItemType.FeedFacebookLink.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_link, parent, false);
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
        else if(viewType == ListingItemType.FeedTwitterLink.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_twitter_link, parent, false);
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
        else if(viewType == ListingItemType.CELEBRITY_PROFILE.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_celebrity_profile, parent, false);
            holder = new CelebrityProfileViewHolder(view);
        }
        return holder;
    }
    /**
     * Adds new items to the list.
     */
    public void addCelebrityProfileItem(ListingItem item) {
        items.add(0,item);
        notifyItemInserted(0);
    }

    /**
     * Adds new items to the list.
     */
    public void addItems(ArrayList<ListingItem> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void addNewItems(ArrayList<ListingItem> newItems) {
        items.addAll(0, newItems);
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
        notifyItemRemoved(items.size() - 1);
    }

    public ArrayList<ListingItem> getItems() {
        return items;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListingItem item = items.get(position);
        //sets position to current binded view.
        switch (item.getType()) {
            case Loader:
                bindLoader((LoaderViewHolder) holder, (LoaderItem) item);
                break;
            case ENDED:
                bindDataEnded((DataEndedViewHolder) holder, (DataEndedItem) item);
                break;
            case Reloader:
                bindReloader((ReloaderViewHolder) holder, (ReloaderItem) item);
                break;
            case FeedTwitterLink:
                bindTwitterLink((FeedTwitterViewHolder) holder, (FeedTwitterLinkItem) item);
                break;
            case FeedFacebookLink:
                bindFacebookLink((FeedFacebookViewHolder) holder, (FeedFacebookLinkItem) item);
                break;
            case FeedFacebookImage:
                bindFacebookImage((FeedFacebookImageViewHolder) holder, (FeedFacebookImageItem) item);
                break;
            case FeedFaceBookText:
                bindFacebookText((FeedFacebookTextViewHolder) holder, (FeedFacebookTextItem) item);
                break;
            case FeedTwitterImage:
                bindTwitterImage((FeedTwitterImageViewHolder) holder, (FeedTwitterImageItem) item);
                break;
            case FeedTwitterText:
                bindTwitterText((FeedTwitterTextViewHolder) holder, (FeedTwitterTextItem) item);
                break;
            case FeedFacebookVideo:
                bindFacebookVideo((FeedFacebookVideoViewHolder) holder, (FeedFacebookVideoItem) item);
                break;
            case FeedInstagramImage:
                bindInstagramImage((FeedInstagramImageViewHolder) holder, (FeedInstagramImageItem) item);
                break;
            case FeedInstagramVideo:
                bindInstagramVideo((FeedInstagramVideoViewHolder) holder, (FeedInstagramVideoItem) item);
                break;
            case CELEBRITY_PROFILE:
                bindCelebrityProfile((CelebrityProfileViewHolder) holder, (CelebrityProfileItem) item);
                break;

            default:
        }
    }

    private void bindCelebrityProfile(CelebrityProfileViewHolder holder, CelebrityProfileItem item) {
        Celebrity celebrity = item.getCelebrityProfile();
        String celebName = celebrity.getName() != null ? celebrity.getName() : "-";
        holder.tvProfileName.setText(celebName);
        Picasso.with(ctx).load(celebrity.getProfileUrl()).placeholder(R.drawable.placeholder).into(holder.ivProfile);
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

    private void bindFacebookLink(FeedFacebookViewHolder holder, FeedFacebookLinkItem item) {
        FeedFacebookLinkItem feedFacebookLinkItem = item;         // get article from item container
        FeedFacebookLink feedFacebookLink = (FeedFacebookLink) feedFacebookLinkItem.getFeed();                // extract data from article
        String celebNameFb = feedFacebookLink.getCelebName();
        String dateFb = feedFacebookLink.getDate();
        String feedTextFb = feedFacebookLink.getText();
        String linkDescriptionFb = feedFacebookLink.getLinkDescription();
        String linkFb = feedFacebookLink.getLink();
        String urlCoverFb = feedFacebookLink.getImageUrl();
        String imageProfile = feedFacebookLink.getProfileImage();
        // cast holder
        final FeedFacebookViewHolder feedFacebookViewHolder = holder;
        // set values
        dateFb = toFormattedDate(dateFb);
        feedFacebookViewHolder.tvProfileName.setText(celebNameFb);
        feedFacebookViewHolder.tvDate.setText(dateFb);
        feedFacebookViewHolder.tvImageText.setText(linkDescriptionFb);
        feedFacebookViewHolder.tvLink.setText(linkFb);
        feedFacebookViewHolder.tvText.setText(feedTextFb);
        Picasso.with(ctx).load(urlCoverFb).into(feedFacebookViewHolder.ivLink);
        Picasso.with(ctx).load(imageProfile).placeholder(R.drawable.placeholder).into(feedFacebookViewHolder.ivProfile);
    }

    private void bindTwitterLink(FeedTwitterViewHolder holder, FeedTwitterLinkItem item) {
        FeedTwitterLinkItem feedTwitterLinkItem = item;
        FeedTwitterLink feedTwitterLink = (FeedTwitterLink) feedTwitterLinkItem.getFeed();
        String celebName = feedTwitterLink.getCelebName();
        String date = feedTwitterLink.getDate();
        String feedText = feedTwitterLink.getText();
        String urlImage = feedTwitterLink.getImageUrl();
        String linkDescription = feedTwitterLink.getLinkDescription();
        String link = feedTwitterLink.getLink();
        String twitterProifleImage = feedTwitterLink.getProfileImage();
        date = toFormattedDate(date);
        final FeedTwitterViewHolder feedTwitterViewHolder = holder;
        feedTwitterViewHolder.ivProfile.setImageResource(R.drawable.new_logo);
        feedTwitterViewHolder.tvDate.setText(date);
        feedTwitterViewHolder.tvProfileName.setText(celebName);
        feedTwitterViewHolder.tvLinkDescription.setText(linkDescription);
        feedTwitterViewHolder.tvLink.setText(link);
        feedTwitterViewHolder.tvFeed.setText(feedText != "null" ? feedText : "");
        Picasso.with(ctx).load(urlImage).into(feedTwitterViewHolder.ivFeed);
        Picasso.with(ctx).load(twitterProifleImage).placeholder(R.drawable.placeholder).into(feedTwitterViewHolder.ivProfile);
    }

    private void bindInstagramVideo(FeedInstagramVideoViewHolder holder, FeedInstagramVideoItem item) {
        FeedInstagramVideoItem feedInstagramVideoItem = item;
        FeedInstagramVideo feedInstagramVideo = (FeedInstagramVideo) feedInstagramVideoItem.getFeed();
        String celebNameInsta = feedInstagramVideo.getCelebName();
        String dateInsta = feedInstagramVideo.getDate();
        String instaFeedText=feedInstagramVideo.getText();
        String instalikesCount=feedInstagramVideo.getCount();
        String instaImageProfile=feedInstagramVideo.getProfileImage();
        String urlVideoInstagram = feedInstagramVideo.getVideoUrl();
        dateInsta = toFormattedDate(dateInsta);
        final FeedInstagramVideoViewHolder feedInstagramVideoViewHolder = holder;
        feedInstagramVideoViewHolder.tvProfileName.setText(celebNameInsta);
        feedInstagramVideoViewHolder.tvDate.setText(dateInsta);
        feedInstagramVideoViewHolder.tvLikesCount.setText(instalikesCount);
        feedInstagramVideoViewHolder.tvText.setText((Html.fromHtml("<b>" + celebNameInsta + "</b>" + " " + instaFeedText)));
        Picasso.with(ctx).load(instaImageProfile).placeholder(R.drawable.placeholder).into(feedInstagramVideoViewHolder.ivProfile);
    }

    private void bindInstagramImage(FeedInstagramImageViewHolder holder, FeedInstagramImageItem item) {
        FeedInstagramImageItem feedInstagramImageItem= item;
        FeedInstagramImage feedInstagramImage = (FeedInstagramImage) feedInstagramImageItem.getFeed();
        String celebNameInstagram = feedInstagramImage.getCelebName();
        String dateInstagram = feedInstagramImage.getDate();
        String instagramFeedText=feedInstagramImage.getText();
        String likesCount=feedInstagramImage.getCount();
        String urlCoverInstagram = feedInstagramImage.getImageUrl();
        String instagramProfileImage=feedInstagramImage.getProfileImage();
        dateInstagram = toFormattedDate(dateInstagram);
        holder.tvProfileName.setText(celebNameInstagram);
        holder.tvDate.setText(dateInstagram);
        holder.tvLikesCount.setText(likesCount);
        holder.tvText.setText((Html.fromHtml("<b>" + celebNameInstagram + "</b>" + " " + instagramFeedText)));
        Picasso.with(ctx).load(urlCoverInstagram).into(holder.ivFeed);
        Picasso.with(ctx).load(instagramProfileImage).placeholder(R.drawable.placeholder).into(holder.ivProfile);
    }

    private void bindFacebookVideo(FeedFacebookVideoViewHolder holder, FeedFacebookVideoItem item) {
        FeedFacebookVideoItem feedFacebookVideoItem = item;
        FeedFacebookVideo feedFacebookVideo = (FeedFacebookVideo) feedFacebookVideoItem.getFeed();
        String celebNameFbVideo = feedFacebookVideo.getCelebName() != null ? feedFacebookVideo.getCelebName() : "";
        String dateFbVideo = feedFacebookVideo.getDate();
        String videoFbUrl = feedFacebookVideo.getVideoUrl();
        String fbVideoImageProfile = feedFacebookVideo.getProfileImage();
        String fbVideoText=feedFacebookVideo.getText() != null ? feedFacebookVideo.getText() : "";
        dateFbVideo = toFormattedDate(dateFbVideo);
        String image = feedFacebookVideo.getImageUrl();
        final FeedFacebookVideoViewHolder feedFacebookVideoViewHolder = holder;
        feedFacebookVideoViewHolder.tvProfileName.setText(celebNameFbVideo != null ? celebNameFbVideo : "");
        Picasso.with(ctx).load(fbVideoImageProfile).placeholder(R.drawable.placeholder).into(feedFacebookVideoViewHolder.ivProfile);
        Picasso.with(ctx).load(image).placeholder(R.drawable.placeholder).into(feedFacebookVideoViewHolder.ivFeed);
        feedFacebookVideoViewHolder.tvDate.setText(dateFbVideo);
        feedFacebookVideoViewHolder.tvText.setText(fbVideoText);
    }

    private void bindTwitterText(FeedTwitterTextViewHolder holder, FeedTwitterTextItem item) {
        FeedTwitterTextItem feedTwitterTextItem = item;
        FeedTwitterText feedTwitterText = (FeedTwitterText) feedTwitterTextItem.getFeed();
        String celebNameTwitterText = feedTwitterText.getCelebName();
        String dateTwitterText = feedTwitterText.getDate();
        String twitterfeedText2 = feedTwitterText.getText();
        String twitterProfileImage2 = feedTwitterText.getProfileImage();
        dateTwitterText = toFormattedDate(dateTwitterText);
        final FeedTwitterTextViewHolder feedTwitterTextViewHolder = holder;
        feedTwitterTextViewHolder.tvProfileName.setText(celebNameTwitterText != null ? celebNameTwitterText : "");
        feedTwitterTextViewHolder.tvDate.setText(dateTwitterText);
        feedTwitterTextViewHolder.tvFeed.setText(twitterfeedText2);
        Picasso.with(ctx).load(twitterProfileImage2).placeholder(R.drawable.placeholder).into(feedTwitterTextViewHolder.ivProfile);
    }

    private void bindTwitterImage(FeedTwitterImageViewHolder holder, FeedTwitterImageItem item) {
        FeedTwitterImageItem feedTwitterImageItem = item;
        FeedTwitterImage feedTwitterImage = (FeedTwitterImage) feedTwitterImageItem.getFeed();
        String celebNameTwitter = feedTwitterImage.getCelebName();
        String dateTwitter = feedTwitterImage.getDate();
        String twitterFeedText = feedTwitterImage.getText();
        String urlCoverTwitter = feedTwitterImage.getImageUrl();
        String twitterProfileImage = feedTwitterImage.getProfileImage();
        dateTwitter = toFormattedDate(dateTwitter);
        final FeedTwitterImageViewHolder feedTwitterImageViewHolder = holder;
        feedTwitterImageViewHolder.tvProfileName.setText(celebNameTwitter != null ? celebNameTwitter : "");
        feedTwitterImageViewHolder.tvDate.setText(dateTwitter);
        feedTwitterImageViewHolder.tvFeed.setText(twitterFeedText != null ? twitterFeedText : "");
        Picasso.with(ctx).load(urlCoverTwitter).into(feedTwitterImageViewHolder.ivFeed);
        Picasso.with(ctx).load(twitterProfileImage).placeholder(R.drawable.placeholder).into(feedTwitterImageViewHolder.ivProfile);
    }

    private void bindFacebookText(FeedFacebookTextViewHolder holder, FeedFacebookTextItem item) {
        FeedFacebookTextItem feedFacebookTextItem = item;
        FeedFacebookText feedFacebookText = (FeedFacebookText) feedFacebookTextItem.getFeed();
        String celebNameFbText = feedFacebookText.getCelebName();
        String dateFbText = feedFacebookText.getDate();
        String feedFbText = feedFacebookText.getText();
        String fbProfileImage = feedFacebookText.getProfileImage();
        dateFbText = toFormattedDate(dateFbText);
        // cast holder
        final FeedFacebookTextViewHolder feedFacebookTextViewHolder = holder;
        // set values
        feedFacebookTextViewHolder.tvProfileName.setText(celebNameFbText);
        feedFacebookTextViewHolder.tvDate.setText(dateFbText);
        feedFacebookTextViewHolder.tvText.setText(feedFbText);
        Picasso.with(ctx).load(fbProfileImage).placeholder(R.drawable.placeholder).into(feedFacebookTextViewHolder.ivProfile);
    }

    private void bindFacebookImage(FeedFacebookImageViewHolder holder, FeedFacebookImageItem item) {
        FeedFacebookImageItem feedFacebookImageItem = item;
        FeedFacebookImage feedFacebookImage = (FeedFacebookImage) feedFacebookImageItem.getFeed();
        String celebNameFbImage = feedFacebookImage.getCelebName();
        String dateFbImage = feedFacebookImage.getDate();
        String urlCoverFbImage = feedFacebookImage.getImageUrl();
        String feedFbImageText = feedFacebookImage.getText();
        String facebookProfileImage = feedFacebookImage.getProfileImage();
        dateFbImage = toFormattedDate(dateFbImage);
        // cast holder
        final FeedFacebookImageViewHolder feedFacebookImageViewHolder = holder;
        // set values
        feedFacebookImageViewHolder.tvProfileName.setText(celebNameFbImage != null ? celebNameFbImage : "");
        feedFacebookImageViewHolder.tvDate.setText(dateFbImage);
        feedFacebookImageViewHolder.tvFeed.setText(feedFbImageText);
        Picasso.with(ctx).load(urlCoverFbImage).into(feedFacebookImageViewHolder.ivFeed);
        Picasso.with(ctx).load(facebookProfileImage).placeholder(R.drawable.placeholder).into(feedFacebookImageViewHolder.ivProfile);
    }

    private void bindReloader(ReloaderViewHolder holder, ReloaderItem item) {
        ReloaderItem reloaderItem = item;
        Reloader reloader = reloaderItem.getReloader();
        final ReloaderViewHolder reloaderViewHolder = holder;
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
    }

    private void bindLoader(LoaderViewHolder holder, LoaderItem item) {
        LoaderItem loaderItem = item;
        Loader loader = loaderItem.getLoader();
        final LoaderViewHolder vhAdvertisement = holder;
        Log.i(Configuration.TAG, "loader");
    }


    private void bindDataEnded(DataEndedViewHolder holder, DataEndedItem item) {

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return items != null ? items.get(position).getType().ordinal() : 0;
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
            Feed feed = items.get(getAdapterPosition()).getFeed();
            startCelebrityActivity(feed.getCelebId());
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
            Feed feed = items.get(getAdapterPosition()).getFeed();
            startCelebrityActivity(feed.getCelebId());
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
            ivFeed.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            FeedTwitterImage feed = (FeedTwitterImage) items.get(getAdapterPosition()).getFeed();
            switch (view.getId()){
                case R.id.ivTwitterFeed:
                    listener.onImageClicked(feed.getImageUrl());
                    break;
                default:
                    startCelebrityActivity(feed.getCelebId());
            }
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
           Feed feed = items.get(getAdapterPosition()).getFeed();
           startCelebrityActivity(feed.getCelebId());
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
            Feed feed = items.get(getAdapterPosition()).getFeed();
            startCelebrityActivity(feed.getCelebId());
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
            ivFeed.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FeedFacebookImage feed = (FeedFacebookImage) items.get(getAdapterPosition()).getFeed();
            switch (view.getId()){
                case R.id.ivFacebookImageFeed:
                    listener.onImageClicked(feed.getImageUrl());
                    break;
                default:
                    startCelebrityActivity(feed.getCelebId());
            }
        }
    }
    private class FeedFacebookVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProfileName,tvText, tvDate;
        private ImageView ivProfile;
        private ImageView ivFeed;
        private WebView wvVideo;

        FeedFacebookVideoViewHolder(final View view) {
            super(view);
            // init views
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            ivFeed = (ImageView) view.findViewById(R.id.ivFacebookVideoFeed);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvText = (TextView) view.findViewById(R.id.tvFeed);
            tvProfileName.setOnClickListener(this);
            ivFeed.setOnClickListener(this);
            wvVideo = (WebView) view.findViewById(R.id.wvVideo);
            wvVideo.getSettings().setJavaScriptEnabled(true);
            wvVideo.getSettings().setLoadWithOverviewMode(true);
            wvVideo.getSettings().setUseWideViewPort(true);
            wvVideo.setVisibility(View.GONE);
            //wvVideo.setWebViewClient(new WebViewClient());

        }

        @Override
        public void onClick(View view) {

            Feed feed = items.get(getAdapterPosition()).getFeed();
            switch (view.getId()){
                case R.id.ivFacebookVideoFeed:
                    FeedFacebookVideo feedFacebookVideo = (FeedFacebookVideo) feed;
                    wvVideo.loadUrl(feedFacebookVideo.getVideoUrl());
                    break;
                case R.id.tvProfileName:
                    startCelebrityActivity(feed.getCelebId());
            }

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
            ivFeed.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            FeedInstagramImage feed = (FeedInstagramImage) items.get(getAdapterPosition()).getFeed();
            switch (view.getId()){
                case R.id.ivPhoto:
                    listener.onImageClicked(feed.getImageUrl());
                    break;
                default:
                    startCelebrityActivity(feed.getCelebId());
            }
        }

    }

    private class FeedInstagramVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            tvProfileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Feed feed = items.get(getAdapterPosition()).getFeed();
            startCelebrityActivity(feed.getCelebId());
        }
    }

    private class CelebrityProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvProfileName;
        private ImageView ivProfile;
        private Button btnFollow;
        CelebrityProfileViewHolder(final View view) {
            super(view);
            // init views
            tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            ivProfile= (ImageView) view.findViewById(R.id.ivProfilePhoto);
            btnFollow = (Button) view.findViewById(R.id.btnFollow);
            btnFollow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String celebId = ((CelebrityProfileItem)items.get(getAdapterPosition())).getCelebrityProfile().getId();
            switch (v.getId()){
                case R.id.btnFollow:
                    if (listener != null) {
                        if (btnFollow.getText().equals("Follow")) listener.onFollowClick(celebId);
                        else listener.onUnFollowClick(celebId);
                    }
                    break;
            }
        }
    }

    private class LoaderViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public LoaderViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    private class DataEndedViewHolder extends RecyclerView.ViewHolder {

        public DataEndedViewHolder(View view) {
            super(view);
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

    private void startCelebrityActivity(String id){
        if (!isCelebrity) {
            Intent i = new Intent(ctx, CelebrityActivity.class);
            i.putExtra(CelebrityActivity.CELEB_ID, id);
            ctx.startActivity(i);
        }
    }

    void startVideoPlayer(String extra) {
        // check if valid not Id and break
        if (extra == null || extra.isEmpty()) {
            Toast.makeText(ctx, "Playback Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        // start activity
        final Intent intent = new Intent(ctx, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.KEY_VIDEO_URL, extra);
        ctx.startActivity(intent);
    }

    private String toFormattedDate(String stringDate) {
        DateFormat formatter;
        Date date = null;
        //
        formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        try {
            date = (Date) formatter.parse(stringDate);
        } catch (ParseException e) {
            return stringDate;
        }
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTime(date);
        Calendar currentCalendar = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) > givenCalendar.get(Calendar.YEAR)) {
            return (String) android.text.format.DateFormat.format("dd-MM-yyyy", date);
        } else if (currentCalendar.get(Calendar.MONTH) > givenCalendar.get(Calendar.MONTH)) {
            return (String) android.text.format.DateFormat.format("MMM-dd", date);
        } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) > givenCalendar.get(Calendar.DAY_OF_MONTH)) {
            int day = currentCalendar.get(Calendar.DAY_OF_MONTH) - givenCalendar.get(Calendar.DAY_OF_MONTH);
            if (day == 1) return day + " day";
            else return day + " days";
        } else if (currentCalendar.get(Calendar.HOUR_OF_DAY) > givenCalendar.get(Calendar.HOUR_OF_DAY)) {
            int hour = currentCalendar.get(Calendar.HOUR_OF_DAY) - givenCalendar.get(Calendar.HOUR_OF_DAY);
            if (hour == 1) return hour + " hr";
            else return hour + " hrs";
        } else if (currentCalendar.get(Calendar.MINUTE) > givenCalendar.get(Calendar.MINUTE)) {
            int minute = currentCalendar.get(Calendar.MINUTE) - givenCalendar.get(Calendar.MINUTE);
            if (minute == 1) return minute + " min";
            else return minute  + " mins";
        } else return "Just Now";
    }

}
