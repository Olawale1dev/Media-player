package tso.mediaplayer;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.pm.ActivityInfo;
import android.app.KeyguardManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.app.PictureInPictureParams;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import android.view.Window;
import android.content.ComponentName;
import androidx.core.content.ContextCompat;
import android.app.admin.DevicePolicyManager;
import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.util.Rational;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.MotionEvent;

import android.view.GestureDetector;
import android.provider.Settings;

public class PopupPlayerActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final int PICK_VIDEO_REQUEST_CODE = 456;

    AdView mAdView;
    private static final String TAG = "PopupPlayerActivity";

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    private GestureDetector gestureDetector;
    private WindowManager.LayoutParams layoutParams;

    private ImageView imgLockUnlock;
    private boolean isLocked = false;

    private boolean isScreenLocked = false;

    private KeyguardManager keyguardManager;
   // private PlayerView exoPlayerView;
    private StyledPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;

    private long playbackPosition = 0;
    private boolean playWhenReady = true;

    private Button btnToggleOrientation;

    private ImageView imgToggleOrientation;

    private ImageView pipButton;
     // To store the playback position
    private boolean userSwitchedOrientation = false;

    private static final int SWIPE_THRESHOLD = 50;
    private static final int SWIPE_VELOCITY_THRESHOLD = 50;
    private MediaPlayer mediaPlayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadInterstitial();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup_player);
        getSupportActionBar().hide();
        exoPlayerView = findViewById(R.id.exoPlayerView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );


        mAdView = findViewById(R.id.adView);
        if (mAdView != null) {
            adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }




// Inside your onCreate or other initialization method
        mediaPlayer = new MediaPlayer();



        // Get WindowManager.LayoutParams to adjust brightness
        layoutParams = getWindow().getAttributes();

        ImageView pipButton = findViewById(R.id.pipButton);
        pipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPiPMode();
            }
        });

        imgLockUnlock = findViewById(R.id.imgLockUnlock);
        imgLockUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocked) {
                    // Unlock the screen
                    isLocked = false;
                    imgLockUnlock.setImageResource(R.drawable.ic_lock_open);// Change to your unlock icon

                    exoPlayerView.setUseController(true);
                    showToast("Screen Unlocked");
                } else {
                    // Lock the screen
                    isLocked = true;

                    imgLockUnlock.setImageResource(R.drawable.ic_lock);// Change to your lock icon

                    exoPlayerView.setUseController(false);
                    showToast("Screen locked");
                }
            }


        });




        gestureDetector = new GestureDetector(this, new GestureListener());
        View rootView = findViewById(R.id.exoPlayerView);  // Use your root layout's ID
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        /*Button btnToggleOrientation = findViewById(R.id.btnToggleOrientation);
        btnToggleOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOrientation();
            }
        });*/

        imgToggleOrientation = findViewById(R.id.imgToggleOrientation);

        imgToggleOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOrientation();
            }
        });

        // Get video path from the intent
        String videoPath = getIntent().getStringExtra("videoPath");
        playbackPosition = savedInstanceState != null ? savedInstanceState.getLong("playbackPosition", 0) : 0;

        // Initialize ExoPlayer
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayerView.setPlayer(exoPlayer);

        // Set media item
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoPath));
        exoPlayer.setMediaItem(mediaItem);


// Restore playback position and play state
        exoPlayer.seekTo(playbackPosition);
        exoPlayer.setPlayWhenReady(playWhenReady);


        // Prepare the player
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true); // Auto-start playback

        // Handle orientation changes
        exoPlayerView.setControllerShowTimeoutMs(3000);
        exoPlayerView.setControllerHideOnTouch(true);
    }



    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Toggle visibility of ImageViews
            toggleImageViewVisibility();
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void toggleImageViewVisibility() {
        // Toggle visibility of your ImageViews
        int newVisibility = (imgToggleOrientation.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        imgToggleOrientation.setVisibility(newVisibility);
        int newVisibility1 = (imgLockUnlock.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        imgLockUnlock.setVisibility(newVisibility1);
       // int newVisibility2 = (pipButton.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
      //  pipButton.setVisibility(newVisibility2);
       // imageView2.setVisibility(newVisibility);
        // Toggle visibility of other ImageViews as needed
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Check isScreenLocked before allowing any interaction
        if (isScreenLocked) {
            // Handle or block touch events as needed
            return true; // Consume the event
        }
        return super.dispatchTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void lockScreen() {
        if (keyguardManager.isKeyguardSecure()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Show a confirmation dialog before locking the screen (required in Android Oreo and above)
                keyguardManager.requestDismissKeyguard(this, null);
            } else {
                // Directly lock the screen (for versions prior to Android Oreo)
                keyguardManager.requestDismissKeyguard(this, null);
            }
            isLocked = true;
            updateLockButtonImage();
        } else {
            Toast.makeText(this, "Secure lock screen is not set up.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterPiPMode() {
        if (isInPictureInPictureMode()) {
            // Already in PiP mode
        } else {
            // Enter PiP mode
            Rational aspectRatio = new Rational(32, 18); // Set your video's aspect ratio
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build();
            enterPictureInPictureMode(params);
        }
    }


    // Handle PiP mode changes
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        // Handle PiP mode changes here
        if (isInPictureInPictureMode) {
            // PiP mode entered, pause video playback, resize UI, etc.
        } else {
            // PiP mode exited, resume video playback, restore UI, etc.
        }
    }

    private void unlockScreen() {
        // No need to explicitly unlock the screen, as Android system will handle it
        isLocked= false;
        updateLockButtonImage();
        Toast.makeText(this, "Screen unlocked", Toast.LENGTH_SHORT).show();
    }



   private void updateLockButtonImage() {
        imgLockUnlock = findViewById(R.id.imgLockUnlock);
        if (isLocked) {
            Toast.makeText(this, "Screen locked", Toast.LENGTH_SHORT).show();
            imgLockUnlock.setImageResource(R.drawable.ic_lock);}

        else {
            Toast.makeText(this, "Screen unlocked", Toast.LENGTH_SHORT).show();
            imgLockUnlock.setImageResource(R.drawable.ic_lock_open);
           //Toast.makeText(this, "Screen unlocked", Toast.LENGTH_SHORT).show();
        }
    }



   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }*/


    

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("playbackPosition", playbackPosition);
        outState.putLong("playbackPosition", exoPlayer.getCurrentPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the playback position
        playbackPosition = savedInstanceState.getLong("playbackPosition");
        exoPlayer.seekTo(playbackPosition);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String videoPath = videoUri.toString();

            // Now, launch PopupPlayerActivity with the selected video path
            Intent popupIntent = new Intent(this, PopupPlayerActivity.class);
            popupIntent.putExtra("videoPath", videoPath);
            startActivity(popupIntent);
        }

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_WRITE_SETTINGS);
            }
        }*/



    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!userSwitchedOrientation) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Landscape mode
                exoPlayerView.hideController();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Portrait mode
                exoPlayerView.showController();
            }

            // Store current playback position and play state
            playbackPosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
        } else {
            userSwitchedOrientation = false;
        }

    }


    private void toggleLockScreen() {
        isLocked = !isLocked;
        if (isLocked) {
            // Disable controls or take any necessary actions when screen is locked
            // For example, disable play/pause, forward, etc.
            Toast.makeText(this, "Screen locked", Toast.LENGTH_SHORT).show();
            exoPlayerView.setUseController(false);
        } else {
            // Enable controls when screen is unlocked
            Toast.makeText(this, "Screen unlocked", Toast.LENGTH_SHORT).show();
            exoPlayerView.setUseController(true);
        }
    }

    private void toggleOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setFullScreenMode(false);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setFullScreenMode(true);
        }
    }

    private void setFullScreenMode(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }










    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    // Call this method to initialize gesture control
    private void loadInterstitial() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
//ca-app-pub-9163926251753325/6833876743
        InterstitialAd.load(this, "ca-app-pub-9163926251753325/6833876743", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });



    }



}

