package tso.mediaplayer;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

public class WakeLockHelper {

    private PowerManager.WakeLock wakeLock;

    public void acquireWakeLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager != null) {
            int wakeLockLevel = PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP;

            // If the device is running Android 7.0 (Nougat) or higher, use new wake lock types
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wakeLockLevel |= PowerManager.ON_AFTER_RELEASE;
            }

            wakeLock = powerManager.newWakeLock(wakeLockLevel, "VideoPlayerWakeLockTag");
            wakeLock.acquire();
        }
    }

    public void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}

