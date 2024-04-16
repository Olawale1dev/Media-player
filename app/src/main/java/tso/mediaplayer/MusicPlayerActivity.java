package tso.mediaplayer;

// MusicPlayerActivity.java

/**import tso.mediaplayer.NotificationHelper;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.session.MediaButtonReceiver;
import android.media.AudioManager;

import android.content.BroadcastReceiver;


import java.io.IOException;**/

/** public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat mediaController;
    private SeekBar seekBar;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private ImageView playPauseButton;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "media_channel";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize UI elements
        seekBar = findViewById(R.id.seekBar);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        totalTimeTextView = findViewById(R.id.totalTimeTextView);
        playPauseButton = findViewById(R.id.playPauseButton);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize MediaSession and MediaController
        mediaSession = new MediaSessionCompat(this, "MusicPlayerSession");
        mediaController = new MediaControllerCompat(this, mediaSession.getSessionToken());

        // Set up notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Set up notification channel
            NotificationHelper.createNotificationChannel(this, CHANNEL_ID, "Media Channel", "Media playback controls");

        }

        // Set up BroadcastReceiver for unplugging headphones
        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        // Set up playback controls
        setUpPlaybackControls();

        // Set up notification
        startForeground(NOTIFICATION_ID, buildNotification());
        // Set up playback controls
        private void setUpPlaybackControls() {
            // Set up play/pause button click listener
            playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        pausePlayback();
                    } else {
                        startPlayback();
                    }
                }
            });

        // Set up notification
       /* startForeground(NOTIFICATION_ID, buildNotification());
    }*/



            /**playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        pausePlayback();
                    } else {
                        startPlayback();
                    }
                }
            });*/

            // Add click listeners for previous and next buttons if available

           /** private void playNext () {
                if (currentAudioIndex < audioFiles.size() - 1) {
                    currentAudioIndex++;
                    startPlayback();
                }
            }*/

            /** private void playPrevious () {
                if (currentAudioIndex > 0) {
                    currentAudioIndex--;
                    startPlayback();
                }
            }*/

// Add click listeners for previous and next buttons
         /**   previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playPrevious();
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNext();
                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Do nothing when user starts tracking
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Do nothing when user stops tracking
                }
            });**/




          /**  private void startPlayback () {
                if (audioFiles != null && audioFiles.size() > 0 && currentAudioIndex < audioFiles.size()) {
                    Uri audioUri = audioFiles.get(currentAudioIndex);
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(getApplicationContext(), audioUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                        updateProgress(); // Start updating progress
                        updateNotification(); // Update the notification for playback controls
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void pausePlayback () {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
                    updateNotification(); // Update the notification for playback controls
                }
            }

// Add methods for handling previous and next playback

            private void updateNotification () {
                Notification notification = buildNotification();
                // Use a NotificationManager to update the existing notification
                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification);
            }


// ... (other existing methods)


            private void updateProgress () {
                // Update the seekBar and time TextViews based on the current playback position
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int totalDuration = mediaPlayer.getDuration();

                    seekBar.setMax(totalDuration);
                    seekBar.setProgress(currentPosition);

                    currentTimeTextView.setText(formatTime(currentPosition));
                    totalTimeTextView.setText(formatTime(totalDuration));

                    // Send a delayed message to update progress every second
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }

            private String formatTime ( int milliseconds){
                // Format time in minutes and seconds
                int seconds = milliseconds / 1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                return String.format("%02d:%02d", minutes, seconds);
            }

            private Notification buildNotification () {
                // Build a media style notification for playback controls
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

                // Set up media style notification
                builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2));

                // Set up actions
                builder.addAction(new NotificationCompat.Action(
                        R.drawable.ic_skip_previous,
                        "Previous",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS))
                );

                if (mediaPlayer.isPlaying()) {
                    builder.addAction(new NotificationCompat.Action(
                            R.drawable.ic_pause,
                            "Pause",
                            MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PAUSE))
                    );
                } else {
                    builder.addAction(new NotificationCompat.Action(
                            R.drawable.ic_play_arrow,
                            "Play",
                            MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY))
                    );
                }

        }



            builder.addAction(new NotificationCompat.Action(
                    R.drawable.ic_skip_next,
                    "Next",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT))
            );

            // Set up notification content
            builder.setContentTitle("Song Title");
            builder.setContentText("Artist Name");
            builder.setSmallIcon(R.drawable.ic_music_note);

            // Set up large icon (optional)
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_large_icon);
            builder.setLargeIcon(largeIcon);

            // Set up pending intent to open the app when notification is clicked
            Intent intent = new Intent(this, MusicPlayerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);

            // Set up notification as ongoing (to keep it in the foreground)
            builder.setOngoing(true);

            return builder.build();
        }


    }*/

   /** @Override
    protected void onDestroy () {
        super.onDestroy();
        // Release resources
        unregisterReceiver(becomingNoisyReceiver);
        handler.removeCallbacksAndMessages(null);
        mediaPlayer.release();
        mediaSession.release();
    }

    // Implement other methods related to media playback control (e.g., play, pause, seek, etc.)
    // ...

    // Update playback state based on MediaPlayer state
    private void updatePlaybackState ( int state){
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_SKIP_TO_NEXT);
        stateBuilder.setState(state, mediaPlayer.getCurrentPosition(), 1.0f);
        mediaSession.setPlaybackState(stateBuilder.build());
    }


    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Pause playback when headphones are unplugged
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
            }
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            updateProgress();
            return true;
        }
    });

}*/


