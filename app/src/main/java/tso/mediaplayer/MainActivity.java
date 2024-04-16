package tso.mediaplayer;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import com.google.android.gms.ads.AdSize;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final int PICK_VIDEO_REQUEST_CODE = 456;

    AdView mAdView;

    Utils utils;

    private RecyclerView recyclerView;
    private Adapter adapter;

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private static final String TAG = "MainActivity";

    //private WatchVideoSection watchVideoSection;
    Context context;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu;
    ImageView onWatchVideoClick;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInterstitial();
        //List<ListItem> items = getVideoAndAdItems();
        //recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager( new LinearLayoutManager(this));
        //RecyclerView recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //mediaPlayer = new MediaPlayer();

        AdView adView = findViewById(R.id.adView); // Replace with your actual AdView ID



        Intent serviceIntent = new Intent(this, MediaPlayerService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        ImageView onWatchVideoClick  = findViewById(R.id.onWatchVideoClick);
        ImageView menu = findViewById(R.id.menu);
        LinearLayout watchVideoSection = findViewById(R.id.watchVideoSection);

        onWatchVideoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideo();
            }
        });
        /*if (recyclerView != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                // Permission granted
                List<ListItem> items = getVideoAndAdItems(); // Implement this method


                LinearLayoutManager manager = new LinearLayoutManager(this);;
                recyclerView.setLayoutManager(manager);

                adapter = new VideoAdapter( items);
                recyclerView.setAdapter(adapter);
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Cannot access videos.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where recyclerView is not found in your layout
            Log.e("MainActivity", "RecyclerView not found in the layout");
        }*/


        getSupportActionBar().hide();
        // Request permissions
        requestStoragePermissions();
        NavigationView navigationView = findViewById(R.id.navView);
        View header = navigationView.getHeaderView(0);

       // header = navigationView.getHeaderView( 0);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.rate:
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=tso.mediaplayer")));
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.share:
                        String shareBody= "Hey, i am using Best Video Player app App. CLICK THE LINK TO DOWNLOAD:https://play.google.com/store/apps/details?id=tso.mediaplayer ";
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);


                }

                return true;
            }
        });


        //MyGestureListener myGestureListenerMain = new MyGestureListener(MainActivity.this);
        //MyGestureListener myGestureListener = new MyGestureListener(this);

      //  gestureDetector = new GestureDetector(this, myGestureListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                }


            }
        }, 2000);

    }





    public void onWatchVideoClick(View view) {
        // Open the file picker to select a video file
        pickVideo();
    }






    private void requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE
                );
            } else {
                // Permissions already granted
                pickVideo();
            }
        } else {
            // Permissions granted for lower versions
            pickVideo();
        }
    }

   /* private void requestStoragePermissions() {
        // Implement your existing permission request logic here

        // After permission is granted, load video and ad items and initialize the adapter
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Request the READ_EXTERNAL_STORAGE permission
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE
            );
        } else {
            // Permission is already granted, load video and ad items
            loadVideoAndAdItems();
           // pickVideo();
        }
    }*/

       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE
            );
        } else {
            loadVideoAndAdItems();
            // Permission granted
            List<ListItem> items = getVideoAndAdItems();
            if (items == null) {
                throw new NullPointerException("items is null");
            }

            // Implement this method
            adapter = new VideoAdapter(items);

            recyclerView.setAdapter(adapter);
            if (adapter == null) {
                throw new NullPointerException("adapter is null");
            }
        }*/
       /* } else{
            // Permission denied
            Toast.makeText(this, "Permission denied. Cannot access videos.", Toast.LENGTH_SHORT).show();
        }

    }*/

   /* private List<ListItem> getVideoAndAdItems() {
        Context context = this;
        // Replace this with your logic to fetch videos and ads
        List<ListItem> items = new ArrayList<>();

        // Add logic to fetch videos from storage
        List<String> videoPaths = utils.getVideoPathsFromStorage(context);
        if (videoPaths == null) {
            return new ArrayList<>(); // Return an empty list
        }
// Create VideoItem instances for each video found
        for (String videoPath : videoPaths) {
            if (videoPath == null) {
                continue; // Skip null video paths
            }
            items.add(new VideoItem(videoPath, "Video")); // You might want to extract video title using videoPath
        }

// Add an ad item
        items.add(new AdItem("ad_image_url_1", "Ad 1"));//ou might need to create an AdItem class

        return items;
    }*/



    /*private void loadVideoAndAdItems() {
        // Fetch video and ad items
        //pickVideo();
        List<ListItem> items = getVideoAndAdItems();
        if (items != null) {
            // Items fetched successfully, initialize the adapter and set it to the recyclerView
            adapter = new VideoAdapter(items);
            recyclerView.setAdapter(adapter);
        } else {
            // Handle the case where an error occurred while fetching video and ad items
            showErrorMessage("Error loading items. Please try again.");
        }
    }*/




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                pickVideo();
               // loadVideoAndAdItems();
            } else {
                // Permission is denied, show an error message or handle it accordingly
                showErrorMessage("Permission denied. Cannot access videos.");
            }
        }

    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
       /* super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                pickVideo();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Cannot access videos.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/



    private void pickVideo() {
        // Open the file picker to select a video file
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE);
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




    }


   /* private void lockScreen() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(this, YourDeviceAdminReceiver.class);

        // Check if your app is a device administrator
        if (devicePolicyManager.isAdminActive(componentName)) {
            // Lock the screen
            devicePolicyManager.lockNow();
        } else {
            // Your app is not a device administrator, handle accordingly
            Toast.makeText(this, "Device administrator not enabled", Toast.LENGTH_SHORT).show();
        }


    }*/


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onBackPressed() {

        //pickVideo();
            super.onBackPressed();
        pickVideo();


    }







    private void loadInterstitial() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
//ca-app-pub-9163926251753325/6752602808

        //banner  ca-app-pub-9163926251753325/1773121752
        InterstitialAd.load(this, "ca-app-pub-9163926251753325/6752602808", adRequest,
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
