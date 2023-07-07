package com.downloaderpro.mrpro.demo.multi;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;
import com.downloaderpro.mrpro.DownloaderPro;
import com.downloaderpro.mrpro.demo.FileDir;
import com.downloaderpro.mrpro.demo.R;
import com.downloaderpro.mrpro.demo.SaveDataApp;
import com.downloaderpro.mrpro.demo.db.DownloadRoomHelper;
import com.downloaderpro.mrpro.demo.db.DownloadsRoom;
import com.downloaderpro.mrpro.demo.downloadingservice.DownloadingService;
import com.downloaderpro.mrpro.demo.downloadingservice.SendClassesToService;
import com.downloaderpro.mrpro.demo.downloadingservice.ServiceCreatedBus;
import com.downloaderpro.mrpro.demo.downloadingservice.ServiceEventBus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.DownloadingHolder> {
  public Context mContext;
  public DownloadRoomHelper dRHelper;
  public List<DownloadsRoom> dRList;
  public int TOTAL_DOWNLOADINGS = 0;
  public List<DownloaderPro> downloaderProList, downloaderProList2;
  public List<DownloadingListeners> downloadingListenersList, downloadingListenersList2;
  private boolean isServiceCreated = false;

  public DownloadingAdapter(
      Context context, DownloadRoomHelper downloadRoomHelper, List<DownloadsRoom> dRList) {
    this.mContext = context;
    this.dRHelper = downloadRoomHelper;
    this.dRList = dRList;
    TOTAL_DOWNLOADINGS = this.dRList.size();
    downloaderProList2 = new ArrayList<>();
    downloadingListenersList2 = new ArrayList<>();
  }

  public void setDownloadingListeners(List<DownloadingListeners> downloadingListeners) {
    this.downloadingListenersList = downloadingListeners;
  }

  public void setDownloaderPro(List<DownloaderPro> downloaderPro) {
    this.downloaderProList = downloaderPro;
  }

  public void addDownload(DownloadsRoom room) {
    if (dRList.size() == 0) {
      mContext.startService(new Intent(mContext, DownloadingService.class));
    }
    this.dRList.add(room);
    TOTAL_DOWNLOADINGS = this.dRList.size();
    notifyItemInserted(dRList.size());
  }

  @Override
  public DownloadingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_downloading, parent, false);
    return new DownloadingHolder(view, mContext);
  }

  @Override
  public void onBindViewHolder(@NonNull DownloadingHolder holder, int position) {
    holder.position = position;
    holder.downloadsRoom = dRList.get(position);
    holder.downloadUrl = holder.downloadsRoom.getDownloadUrl();
    holder.fileName = holder.downloadsRoom.getDownloadFileName().replaceAll(" ", "_");
    if (downloaderProList != null && downloaderProList.size() > position) {
      holder.downloaderPro = downloaderProList.get(position);
      holder.listeners = downloadingListenersList.get(position);
      holder.listeners.setAdapter(this);
      holder.listeners.setDownloadingHolder(holder);
      holder.isFirstTimeResumed = false;
    } else {
      holder.downloaderPro = getDownloaderPro(mContext);
      holder.listeners = new DownloadingListeners(holder, this);
      downloaderProList2.add(holder.downloaderPro);
      downloadingListenersList2.add(holder.listeners);
      new Handler()
          .postDelayed(
              new Runnable() {
                @Override
                public void run() {
                  SendClassesToService event = new SendClassesToService();
                  event.setIsAlreadyCreated(false);
                  event.setDownloaderPro(holder.downloaderPro);
                  event.setDownloadingListeners(holder.listeners);
                  EventBus.getDefault().post(event);
                }
              },
              5000);
      holder.isPaused = holder.downloadsRoom.getIsPaused();
      if (holder.downloadsRoom.getDownloadedBytes() != 0) {
        if (holder.isPaused) {
          TOTAL_DOWNLOADINGS--;
          holder.downloadingSpeedAndStutasTxt.setText("Paused");
          holder.pauseAndResumeDownloadImgBtn.setImageResource(R.drawable.play_arrow_24px);
        } else {
          holder.downloaderPro.resumeDownload(holder.downloadsRoom.getDownloadId());
        }
      }
    }

    File isFile = new File(FileDir.getFileDir(), holder.fileName);
    if (isFile.exists() && isFile.isFile()) {
      holder.downloaderPro.setDownloadListeners(holder.listeners.getDownloadListeners());
      holder.downloadingTitleTxt.setText(holder.fileName.replaceFirst(".", ""));
      holder.downloadProgress.setProgress(holder.downloadsRoom.getProgress());
      holder.downloadedAndTotalBytesTxt.setText(
          holder.getHumanReadableBytes(holder.downloadsRoom.getDownloadedBytes())
              + " / "
              + holder.getHumanReadableBytes(holder.downloadsRoom.getTotalBytes()));
    } else {
      holder.fileName = holder.downloadsRoom.getDownloadFileName();
      holder.downloadUrl = holder.downloadsRoom.getDownloadUrl();
      holder.downloaderPro.setDownloadListeners(holder.listeners.getDownloadListeners());
      holder.downloaderPro.setFileName(holder.fileName);
      holder.downloaderPro.setDir(FileDir.getFileDir());
      holder.downloaderPro.setUrl(holder.downloadUrl);
      holder.downloaderPro.startDownloading();
      holder.downloadingTitleTxt.setText(holder.fileName.replaceFirst(".", ""));
    }

    holder.pauseAndResumeDownloadImgBtn.setOnClickListener(
        v -> {
          if (holder.isPaused || holder.downloadsRoom.getIsFailed()) {
            holder.downloaderPro.resumeDownload(holder.downloadsRoom.getDownloadId());
            holder.pauseAndResumeDownloadImgBtn.setImageResource(R.drawable.pause_24px);
          } else {
            holder.pauseAndResumeDownloadImgBtn.setImageResource(R.drawable.play_arrow_24px);
            holder.downloaderPro.pauseDownload();
          }
        });

    holder.cancelDownloadImgBtn.setOnClickListener(
        v -> {
          AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
          builder
              .setTitle("Cancel Download")
              .setMessage("Are you sure you want to cancel download?")
              .setPositiveButton(
                  "Yes",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      // Cancel the download and remove it from the database
                      holder.downloaderPro.cancelDownload(holder.downloadsRoom.getDownloadId());
                    }
                  })
              .setNegativeButton("No", null)
              .show();
        });
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
    return dRList.size();
  }

  public static class DownloadingHolder extends RecyclerView.ViewHolder {
    String downloadUrl, fileName;
    ImageButton cancelDownloadImgBtn, pauseAndResumeDownloadImgBtn;
    TextView downloadingTitleTxt, downloadedAndTotalBytesTxt, downloadingSpeedAndStutasTxt;
    ProgressBar downloadProgress;
    DownloaderPro downloaderPro;
    DownloadsRoom downloadsRoom;
    int position;
    int mDownloadId;
    boolean isPaused = false;
    DownloadingListeners listeners;
    boolean isFirstTimeResumed = true;
    private Context mContext;

    public DownloadingHolder(@NonNull View itemView, Context context) {
      super(itemView);
      this.mContext = context;
      cancelDownloadImgBtn = itemView.findViewById(R.id.cancel_download_image_btn);
      pauseAndResumeDownloadImgBtn = itemView.findViewById(R.id.pause_and_resume_image_btn);
      downloadedAndTotalBytesTxt = itemView.findViewById(R.id.downloaded_and_total_bytes_txt);
      downloadingTitleTxt = itemView.findViewById(R.id.downloading_title_txt);
      downloadingSpeedAndStutasTxt = itemView.findViewById(R.id.downloading_speed_and_status_txt);
      downloadProgress = itemView.findViewById(R.id.download_progress);
    }

    public String getHumanReadableBytes(long size) {
      String s = "";
      double kb = size / 1024.0;
      double mb = kb / 1024;
      double gb = mb / 1024;
      double tb = gb / 1024;
      if (size < 1024) {
        s = size + "B";
      } else if (size >= 1024 && size < 1024 * 1024) {
        s = String.format("%.2f", kb) + "KB";
      } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
        s = String.format("%.2f", mb) + "MB";
      } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
        s = String.format("%.2f", gb) + " GB";
      } else if (size >= 1024 * 1024 * 1024 * 1024) {
        s = String.format("%.2f", tb) + "TB";
      }
      return s;
    }

    public PendingIntent getActionPendingIntent(
        DownloadingHolder holder, String name, int position) {
      String action = "com.downloaderpro.mrpro.demo.multi.ACTION_" + name + position;
      NotificationActionReceiver receiver = new NotificationActionReceiver(holder);
      IntentFilter filter = new IntentFilter();
      filter.addAction(action);
      mContext.registerReceiver(receiver, filter);

      return PendingIntent.getBroadcast(
          mContext, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void stopService() {
      ServiceEventBus event = new ServiceEventBus(false);
      event.setForNotification(true);
      EventBus.getDefault().post(event);
      mContext.stopService(new Intent(mContext, DownloadingService.class));
    }

    public class NotificationActionReceiver extends BroadcastReceiver {
      private DownloadingHolder holder;

      public NotificationActionReceiver(DownloadingHolder holder) {
        this.holder = holder;
      }

      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
          String action = intent.getAction();

          if (action.equals("com.downloaderpro.mrpro.demo.multi.ACTION_CANCEL" + holder.position)) {
            downloaderPro.cancelDownload(downloadsRoom.getDownloadId());
          }
        }
      }
    }
  }

  private static DownloaderPro getDownloaderPro(Context context) {
    DownloaderPro downloaderPro = new DownloaderPro(context);

    return downloaderPro;
  }
}
