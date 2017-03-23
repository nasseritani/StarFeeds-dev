package com.nader.starfeeds.ui.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nader.starfeeds.listing.FeedItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Nasse_000 on 3/20/2017.
 */
public class ImageAdapter extends BaseAdapter {
    private String image[]={
        "http://pbs.twimg.com/media/C6LEQ9AWYAM-Wdh.jpg","https://scontent.xx.fbcdn.net/v/t1.0-9/17155591_10155170464197164_6639081427532398965_n.jpg?oh=d618714c1ffe02d92b9415ec23ef6357&oe=5935B731","https://scontent.xx.fbcdn.net/v/t1.0-9/p720x720/15621816_10154213174615975_1819200419086891756_n.jpg?oh=9ea3eb2ca03fd1d0486187f72d3167d7&oe=58FF21D4","http://pbs.twimg.com/ext_tw_video_thumb/838425987381501952/pu/img/bnW4DxshMNEcsXe6.jpg","https://scontent.xx.fbcdn.net/v/t1.0-9/p720x720/15621816_10154213174615975_1819200419086891756_n.jpg?oh=9ea3eb2ca03fd1d0486187f72d3167d7&oe=58FF21D4"
,"https://scontent.xx.fbcdn.net/v/t1.0-9/p720x720/15621816_10154213174615975_1819200419086891756_n.jpg?oh=9ea3eb2ca03fd1d0486187f72d3167d7&oe=58FF21D4","https://scontent.xx.fbcdn.net/v/t1.0-9/p720x720/15621816_10154213174615975_1819200419086891756_n.jpg?oh=9ea3eb2ca03fd1d0486187f72d3167d7&oe=58FF21D4"    };
    int imageTotal = 7;
    private Context ctx;
FeedItem feed;
    public ImageAdapter(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    public int getCount() {
        return imageTotal;
    }

    @Override
    public String getItem(int i) {
        return image[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(ctx);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) view;
        }
        String url = getItem(i);
        Picasso.with(ctx)
                .load(url)
                .fit()
                .into(imageView);
        return imageView;
    }
    }

