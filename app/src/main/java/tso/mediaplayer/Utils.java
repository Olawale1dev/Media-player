package tso.mediaplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class Utils {


    public static List<String> getVideoPathsFromStorage(@NonNull Context context) {
        List<String> videoPaths = new ArrayList<>();

        // Define the columns to query
        String[] projection = {MediaStore.Video.Media.DATA};

        // Query the MediaStore for videos
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
               null,
               null,
                null
        );

        // Iterate through the cursor to retrieve video paths
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                videoPaths.add(videoPath);
            }
            cursor.close();
        }

        return videoPaths;
    }
}

