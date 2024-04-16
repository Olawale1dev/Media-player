package tso.mediaplayer;

/*import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
*/
/*public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.viewHolder> {
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_AD = 2;
    private static final int VIEW_TYPE_VIDEO = 1;
    private static final int VIEW_TYPE_AD = 2;

    Context context;
    ArrayList<SubjectModel> list;
    private List<ListItem> items;


    public VideoAdapter( List<ListItem> items) {
        this.items = items;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_VIDEO) {
            // Inflate the video item layout and return a VideoViewHolder
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new VideoAdapter.viewHolder(itemView);
        } else if (viewType == VIEW_TYPE_AD) {
            // Inflate the ad item layout and return an AdViewHolder
            View adView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
            return new VideoAdapter.viewHolder(adView);
        }
        // Return null or throw an exception if viewType is invalid
        throw new IllegalArgumentException("Invalid view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ListItem item = items.get(position);
        if (item instanceof VideoItem) {
            ((viewHolder) holder).bind((VideoItem) item);
        } else if (item instanceof AdItem) {
            ((viewHolder) holder).bind((VideoItem) item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type based on the object at the position
        if (items.get(position) instanceof VideoItem) {
            return VIEW_TYPE_VIDEO;
        } else {
            // Handle other view types (e.g., ads) if needed
            return VIEW_TYPE_AD;
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(VideoItem videoItem) {
            // Example using Glide for image loading (make sure to include the Glide dependency in your build.gradle):
            Glide.with(itemView.getContext())
                    .load(videoItem.getThumbnailUrl())
                    .into(thumbnailImageView);

            titleTextView.setText(videoItem.getVideoTitle());

            // Set any other data binding logic based on your VideoItem model
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        // Add your AdViewHolder components here (e.g., AdView)
        AdView mAdView;

        // ImageView adImageView;
        // TextView titleTextView;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your AdViewHolder components here
            //adImageView = itemView.findViewById(R.id.adImageView);
            // titleTextView = itemView.findViewById(R.id.titleTextView);
            mAdView = itemView.findViewById(R.id.adView);
        }

        // Example method to bind ad data
        public void bind(AdItem adItem) {
            // Bind data to views for the ad item
            // Example using Glide for loading an image:
            Glide.with(itemView.getContext()).load(mAdView);

            // Set the title
            // titleTextView.setText(adItem.getTitle());
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnailImageView;
        private TextView titleTextView;

        public viewHolder(View itemView) {
            super(itemView);

            // Initialize your views here
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }


        // Other methods or properties of ViewHolder

        public void bind(VideoItem videoItem) {
            // Bind data to views
            // Example using Glide library:
            Glide.with(itemView.getContext()).load(videoItem.getThumbnailUrl()).into(thumbnailImageView);
            titleTextView.setText(videoItem.getVideoTitle());
        }
    }
}*/
