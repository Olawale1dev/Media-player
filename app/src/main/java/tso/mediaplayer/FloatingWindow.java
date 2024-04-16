package tso.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class FloatingWindow {

    private final WindowManager windowManager;
    private final View floatingView;

    public FloatingWindow(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        floatingView = inflater.inflate(R.layout.item_video, null);

        // Initialize your views and set up any interactions
        // Example: Button closeButton = floatingView.findViewById(R.id.closeButton);
        // closeButton.setOnClickListener(v -> closeFloatingWindow());
    }

    public void show() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT
        );

        windowManager.addView(floatingView, params);
    }

    public void close() {
        windowManager.removeView(floatingView);
    }
}

