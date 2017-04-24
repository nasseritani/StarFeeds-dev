package com.nader.starfeeds.ui.sections;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.nader.starfeeds.R;


public class VideoPlayerActivity extends AppCompatActivity {
    public static final String KEY_VIDEO_URL = "video_url";
    // views
    View container;
    ProgressBar pbLoading;
    VideoView vvPlayer;
    View ivRotate;
    // objects
    String videoUrl;
    String fbPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // get the video Id
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(KEY_VIDEO_URL)) {
                videoUrl = bundle.getString(KEY_VIDEO_URL);
            } else {
                closeWithFailure();
            }
        }
        init();
        initializePlayer();
        // check if video url ready
        if (videoUrl != null) {
            // play video with delay
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playVideo();
                }
            }, 1000);
        } else {
            // fetch fb source url
            closeWithFailure();
        }
    }

    /**
     * Initializes the views and tools of this activity.
     */
    void init() {
        // views
        container = findViewById(R.id.container);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        vvPlayer = (VideoView) findViewById(R.id.vvPlayer);
        ivRotate = findViewById(R.id.ivRotate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // revoke ui state
        setPlayerBounds();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // update the UI
        setPlayerBounds();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_alpha_in, R.anim.animation_exit_slide_out_to_bottom);
    }

    @Override
    public void onBackPressed() {
        try {
            // remove views
            ((ViewGroup) findViewById(R.id.container)).removeView(vvPlayer);
            ((ViewGroup) findViewById(R.id.container)).removeView(ivRotate);
            // set portait orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // clear all resources
            if (vvPlayer != null) {
                vvPlayer.pause();
                vvPlayer.stopPlayback();
            }
        } catch (Exception e) {}
        // continue
        super.onBackPressed();
    }

    /**
     * Initializes the player's controller and states callbacks.
     */
    void initializePlayer() {
        // create controlers
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vvPlayer);
        vvPlayer.setMediaController(vidControl);
        //
        vvPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pbLoading.setVisibility(View.GONE);
                vvPlayer.setVisibility(View.VISIBLE);
                // set looping
                if (mp != null) {
                    mp.setLooping(true);
                }
            }
        });
        // register error callback
        vvPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                closeWithFailure();
                return true;
            }
        });
    }

    /**
     * Triggers the playback for the current videoUrl.
     */
    void playVideo() {
        // show loader
        pbLoading.setVisibility(View.VISIBLE);
        vvPlayer.setVisibility(View.VISIBLE);
//        new Handler(Looper.myLooper()).post(new Runnable(){
//            @Override
//            public void run(){
//                videoview.setVideoUri("uri");
//            }
//        });
        final Uri vidUri = Uri.parse(videoUrl);
        vvPlayer.setVideoURI(vidUri);
        vvPlayer.requestFocus();
        vvPlayer.start();
    }

    /**
     * Updates the bounds of the YouTube player according
     * to the screen orientation.
     */
    private void setPlayerBounds() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // remove rotate icon if still there
            hideRotateIcon();
            // request fullscreen
            if (Build.VERSION.SDK_INT > 15) {
                View decorView = getWindow().getDecorView();
//                int uiOptions = /*View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |*/ View.SYSTEM_UI_FLAG_FULLSCREEN;
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
            // update paddins
            updatePlayerPaddings(true);
        } else {
            // remove fullscreen
            if (Build.VERSION.SDK_INT > 15) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
            }
            // update paddins
            updatePlayerPaddings(false);
        }
    }

    /**
     * Hides the rotate icon after cancelling any active animation.
     */
    void hideRotateIcon() {
        ivRotate.clearAnimation();
        ivRotate.setVisibility(View.GONE);
    }

    /**
     * Sets the YouTube player'container paddings according
     * to the screen orientation.
     * @param isFullscreen true if in landscape mode, false otherwise.
     */
    void updatePlayerPaddings(boolean isFullscreen) {
        if (isFullscreen) {
            final int statusBarHeight = getStatusBarHeight();
            container.setPadding(0, statusBarHeight, 0, statusBarHeight);
        } else {
            container.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Returns the height of the device's status bar.
     */
    int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // ERROR HANDLING //


    /**
     * Tries to reload the starting behavior.
     */
    void reloadVideo() {
        // check if streamable url ready
        if (videoUrl != null) {
            playVideo();
        } else {
          // nothing to be done
            closeWithFailure();
        }
    }
    /**
     * Finishes the activity after displaying a failed toast message.
     */
    void closeWithFailure() {
        Toast.makeText(getBaseContext(), "Playback Failed", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

}
