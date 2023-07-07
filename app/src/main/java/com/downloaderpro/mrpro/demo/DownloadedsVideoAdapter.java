package com.downloaderpro.mrpro.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;

public class DownloadedsVideoAdapter
    extends RecyclerView.Adapter<DownloadedsVideoAdapter.VideoViewHolder> {

  Context mContext;
  List<DownloadedsVideoItem> dVIList;
  OnItemClickListener onItemClickListener;

  public DownloadedsVideoAdapter(Context context, List<DownloadedsVideoItem> vIList) {
    this.dVIList = vIList;
    this.mContext = context;
  }

  public void addNewVideo(DownloadedsVideoItem dVI) {
    //  this.dVIList.add(dVI);
    notifyItemInserted(dVIList.size());
  }

  @Override
  public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
    return new VideoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
    Glide.with(mContext).load(dVIList.get(position).getPath()).into(holder.imageView);
    holder.title.setText(new File(dVIList.get(position).getPath()).getName());
    holder.folderNameTxt.setText(FileDir.getFileDir().getName());
    try {
      MediaPlayer mediaPlayer =
          MediaPlayer.create(mContext, Uri.parse(dVIList.get(position).getPath()));
      holder.duration.setText(getDuration(mediaPlayer.getDuration()));
      int videoWidth = mediaPlayer.getVideoWidth();
      int videoHeight = mediaPlayer.getVideoHeight();
      File file = new File(dVIList.get(position).getPath());
      int videoSize = (int) file.length();
      holder.sizeAndQualityTxt.setText(
          getSize(videoSize) + " | " + getVideoQuality(videoWidth, videoHeight));
    } catch (Exception e) {
      e.printStackTrace();
      holder.duration.setText("--:--");
      holder.sizeAndQualityTxt.setText("-- | --");
    }
    holder.itemView.setOnClickListener(
        v -> onItemClickListener.onClick(v, dVIList.get(position).getPath()));
  }

  @SuppressLint("DefaultLocale")
  private String getDuration(long duration) {
    String videoDuration;

    int dur = (int) duration;
    String hrs = String.valueOf((dur / 3600000));
    String min = String.valueOf((dur / 60000) % 6000);
    String sec = String.valueOf((dur % 60000 / 1000));

    if (sec.length() == 1) {
      sec = "0" + sec;
    }
    if (min.length() == 1) {
      min = "0" + min;
    }
    if (hrs.length() == 1) {
      hrs = "0" + hrs;
    }

    if (Integer.valueOf(hrs) > 0) {
      videoDuration = hrs + ":" + min + ":" + sec;
    } else if (Integer.valueOf(min) > 0) {
      videoDuration = "00:" + min + ":" + sec;
    } else {
      videoDuration = "00:00:" + sec;
    }
    return videoDuration;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemViewType(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    return dVIList.size();
  }

  public static class VideoViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView title, duration, sizeAndQualityTxt,folderNameTxt;

    public VideoViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.list_item_image);
      title = itemView.findViewById(R.id.list_item_title);
      duration = itemView.findViewById(R.id.list_item_duration);
      sizeAndQualityTxt = itemView.findViewById(R.id.downloaded_video_size_and_quality);
      folderNameTxt = itemView.findViewById(R.id.folder_name_textView);
    }
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onClick(View view, String path);
  }

  private String getSize(int size) {
    String s = "";
    double kb = size / 1024.0;
    double mb = kb / 1024;
    double gb = mb / 1024;
    double tb = gb / 1024;
    if (size < 1024) {
      s = size + " B";
    } else if (size >= 1024 && size < 1024 * 1024) {
      s = String.format("%.2f", kb) + " KB";
    } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
      s = String.format("%.2f", mb) + " MB";
    } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
      s = String.format("%.2f", gb) + " GB";
    } else if (size >= 1024 * 1024 * 1024 * 1024) {
      s = String.format("%.2f", tb) + " TB";
    }
    return s;
  }

  private String getVideoQuality(int width, int height) {
    if (width == height) {
      if (width >= 2160) {
        return "4K";
      } else if (width >= 1300) {
        return "1440p";
      } else if (width >= 900) {
        return "1080p";
      } else if (width >= 500) {
        return "720p";
      } else if (width >= 400) {
        return "480p";
      } else if (width >= 300) {
        return "320p";
      } else {
        return "240p";
      }
    }
        
    if (width >= 2160) {
      return "4K";
    } else if (width >= 1440) {
      return "1440p";
    } else if (width == 1440 && height < 1940 && height >= 1080) {
      return "1080p";
    } else if (width == 1280 && height < 1300 && height >= 720) {
      return "720p";
    } else if (width == 480 && height < 900 && height >= 320) {
      return "480p";
    } else if (width <= 640 && width >= 320 && height < 720 && height >= 320) {
      return "360p";
    } else if (width >= 1050 && height <= 1920 && height > 710) {
      return "1080p";
    } else if (width >= 570 && height <= 1280 && height > 400) {
      return "720p";
    } else if (width >= 400 && height <= 854 && height > 300) {
      return "480p";
    } else if (width >= 300 && height <= 640 && height > 220) {
      return "360p";
    } else {
      return "240p";
    }
  }
}
