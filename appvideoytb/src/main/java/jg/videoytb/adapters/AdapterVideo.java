package jg.videoytb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import jg.videoytb.main.R;
import jg.videoytb.models.VideoInfo;


public class AdapterVideo extends BaseAdapter{

    Context context;
    LayoutInflater lInflater;
    List<VideoInfo> listVideoInfo;

    public AdapterVideo(Context context, List<VideoInfo> listVideoInfo){
        this.context=context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listVideoInfo =listVideoInfo;

    }

    public int getCount() {
        return listVideoInfo.size();
    }

    public Object getItem(int position) {
        return listVideoInfo.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        VideoInfo videoInfo = listVideoInfo.get(position);


        if (view == null) {

            view = lInflater.inflate(R.layout.item_video, parent, false);
        }

       Picasso.with(context)
                .load(videoInfo.getImageUrl())
                .placeholder(R.drawable.video_placeholder)
                .into((ImageView) view.findViewById(R.id.video_thumbnail));

        ((TextView) view.findViewById(R.id.video_title)).setText((videoInfo.getTitle()));
        ((TextView) view.findViewById(R.id.video_description)).setText(videoInfo.getDescription());
        ((TextView) view.findViewById(R.id.video_dutation_text)).setText(videoInfo.getDuration());
        ((TextView) view.findViewById(R.id.video_view_count)).setText(videoInfo.getViewCount());
        ((TextView) view.findViewById(R.id.video_like_count)).setText(videoInfo.getLikeCount());
        ((TextView) view.findViewById(R.id.video_dislike_count)).setText(videoInfo.getDislikeCount());

        return view;
    }

}
