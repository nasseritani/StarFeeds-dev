package com.nader.starfeeds.ui.sections;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.nader.starfeeds.R;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    public static String IMAGE_URL = "image_url";
    private ImageView ivImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initToolbar();
        ivImage = (ImageView) findViewById(R.id.ivImage);
        String imageUrl = null;
        if (getIntent().hasExtra(IMAGE_URL)){
            imageUrl = getIntent().getStringExtra(IMAGE_URL);
        }
        Picasso.with(this).load(imageUrl).placeholder(R.drawable.placeholder).into(ivImage);
    }

    private void initToolbar() {
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
