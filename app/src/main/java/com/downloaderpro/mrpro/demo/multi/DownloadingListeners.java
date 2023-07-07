package com.downloaderpro.mrpro.demo.multi;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.downloaderpro.mrpro.DownloadListeners;
import com.downloaderpro.mrpro.demo.DownloadedsVideoItem;
import com.downloaderpro.mrpro.demo.FileDir;
import com.downloaderpro.mrpro.demo.MultiActivity;
import com.downloaderpro.mrpro.demo.SaveDataApp;
import com.downloaderpro.mrpro.demo.downloadingservice.DownloadingService;
import com.downloaderpro.mrpro.demo.downloadingservice.ServiceEventBus;
import java.io.File;
import com.downloaderpro.mrpro.demo.R;
import org.greenrobot.eventbus.EventBus;

public class DownloadingListeners implements DownloadListeners {
  private Context mContext;
  private DownloadingAdapter.DownloadingHolder holder;
  private DownloadingAdapter adapter;
  private static final String CHANNEL_ID = "Download Pro Downloading";
  private static final String CHANNEL_NAME = "Downloading";
  private static final String CHANNEL_DESCRIPTION = "This channel is for downloading notifications";
  private static final int PROGRESS_MAX = 100;
  private static int DOWNLOADED_NOTI_ID = 101;
  private long mDownloadedBytes = 0;
  private NotificationCompat.Builder builder;
  private NotificationManagerCompat notificationManager;
  private boolean isCanceled = false;
  private String fileName;

  public DownloadingListeners(
      DownloadingAdapter.DownloadingHolder holder, DownloadingAdapter adapter) {
    this.holder = holder;
    fileName = holder.fileName.replaceFirst(".", "");
    this.adapter = adapter;
    this.mContext = adapter.mContext;
  }

  public void setDownloadingHolder(DownloadingAdapter.DownloadingHolder holder) {
    this.holder = holder;
  }

  public void setAdapter(DownloadingAdapter adapter) {
    this.adapter = adapter;
  }

  public DownloadListeners getDownloadListeners() {
    return this;
  }

  @Override
  public void onConnecting(int downloadId) {
    holder.mDownloadId = downloadId;
    setNotification(downloadId);
    if (holder.downloadsRoom != null) {
      holder.downloadsRoom.setIsFailed(false);
      holder.downloadsRoom.setDownloadId(downloadId);
      adapter.dRHelper.DownloadsRoomDao().updateDownload(holder.downloadsRoom);
    }
    holder.downloadingSpeedAndStutasTxt.setText("Connecting");
  }

  @Override
  public void onComplete(int downloadId) {
    adapter.TOTAL_DOWNLOADINGS--;

    if (holder.downloadsRoom != null) {
      adapter.dRHelper.DownloadsRoomDao().deleteDownload(holder.downloadsRoom);
      adapter.dRList.remove(holder.position);
      adapter.notifyItemRemoved(holder.position);
    }
    holder.downloadingSpeedAndStutasTxt.setText("Completed");

    notificationManager.cancel(downloadId);

    builder =
        new NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(fileName)
            .setContentText("Completed")
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(getPandingIntent());

    notificationManager = NotificationManagerCompat.from(mContext);
    notificationManager.notify(downloadId, builder.build());

    if (adapter.TOTAL_DOWNLOADINGS == 0) {
      stopService();
    }
    updateDownloadedVideos(FileDir.getFileDir() + "/" + fileName);
  }

  @Override
  public void onFailed(int downloadId, String message) {
    adapter.TOTAL_DOWNLOADINGS--;
    if (adapter.TOTAL_DOWNLOADINGS == 0) {
      stopService();
    }
    holder.downloadsRoom.setIsFailed(true);
    adapter.dRHelper.DownloadsRoomDao().updateDownload(holder.downloadsRoom);
    holder.pauseAndResumeDownloadImgBtn.setImageResource(R.drawable.refresh_24px);
    holder.downloadingSpeedAndStutasTxt.setText("Failed");
    updateNotification(holder, downloadId);
  }

  @Override
  public void onProgress(
      int downloadId, int progress, long downloadedBytes, long totalBytes, long downloadingSpeed) {
    mDownloadedBytes = downloadedBytes;
    if (holder.downloadsRoom != null) {
      holder.downloadsRoom.setProgress(progress);
      holder.downloadsRoom.setDownloadedBytes(downloadedBytes);
      holder.downloadsRoom.setTotalBytes(totalBytes);
      adapter.dRHelper.DownloadsRoomDao().updateDownload(holder.downloadsRoom);
    }

    holder.downloadProgress.setProgress(progress);
    holder.downloadedAndTotalBytesTxt.setText(
        holder.getHumanReadableBytes(downloadedBytes)
            + " / "
            + holder.getHumanReadableBytes(totalBytes));
    holder.downloadingSpeedAndStutasTxt.setText(
        holder.getHumanReadableBytes(downloadingSpeed) + "/s");

    if (progress < 100) {
      String combinedText =
          String.format(
              "%-20s %s",
              holder.getHumanReadableBytes(downloadedBytes)
                  + " / "
                  + holder.getHumanReadableBytes(totalBytes),
              holder.getHumanReadableBytes(downloadingSpeed) + "/s");
      builder
          .setProgress(PROGRESS_MAX, progress, false)
          .setContentText("Downloading")
          .setStyle(new NotificationCompat.BigTextStyle().bigText(combinedText));
      notificationManager.notify(downloadId, builder.build());
    }
  }

  @Override
  public void onPause(int downloadId) {
    adapter.TOTAL_DOWNLOADINGS--;
    if (adapter.TOTAL_DOWNLOADINGS == 0) {
      stopService();
    }
    holder.downloadsRoom.setIsFailed(false);
    holder.isPaused = true;
    holder.mDownloadId = downloadId;
    if (holder.downloadsRoom != null) {
      holder.downloadsRoom.setIsPaused(true);
      adapter.dRHelper.DownloadsRoomDao().updateDownload(holder.downloadsRoom);
    }
    holder.downloadingSpeedAndStutasTxt.setText("Paused");

    updateNotification(holder, downloadId);
  }

  @Override
  public void onResume(int downloadId) {
    if (adapter.TOTAL_DOWNLOADINGS == 0) {
      if (!isServiceRunning(mContext, DownloadingService.class)) {
        mContext.startService(new Intent(mContext, DownloadingService.class));
        new Handler(Looper.getMainLooper())
            .postDelayed(
                new Runnable() {
                  @Override
                  public void run() {
                    ServiceEventBus event = new ServiceEventBus(true);
                    event.setForNotification(false);
                    event.setDownloaderPro(adapter.downloaderProList2);
                    event.setDownloadingListeners(adapter.downloadingListenersList2);
                    EventBus.getDefault().post(event);
                  }
                },
                5000);
      }
    }
    adapter.TOTAL_DOWNLOADINGS++;
    holder.isPaused = false;
    if (holder.downloadsRoom != null) {
      holder.downloadsRoom.setIsFailed(false);
      holder.downloadsRoom.setIsPaused(false);
      adapter.dRHelper.DownloadsRoomDao().updateDownload(holder.downloadsRoom);
    }
    holder.mDownloadId = downloadId;
    holder.downloadingSpeedAndStutasTxt.setText("Resumed");

    if (holder.isFirstTimeResumed) {
      setNotification(downloadId);
    } else {
      builder
          .setContentText("Resumed")
          .setSmallIcon(android.R.drawable.stat_sys_download)
          .setOngoing(true);
      notificationManager.notify(downloadId, builder.build());
      holder.isFirstTimeResumed = false;
    }
  }

  @Override
  public void onCancel(int downloadId) {
    adapter.TOTAL_DOWNLOADINGS--;
    if (adapter.TOTAL_DOWNLOADINGS == 0) {
      holder.stopService();
    }
    if (holder.downloadsRoom != null && !isCanceled) {
      File isFile = new File(FileDir.getFileDir(), holder.downloadsRoom.getDownloadFileName());
      if (isFile.exists() && isFile.isFile()) {
        holder.downloadingSpeedAndStutasTxt.setText("Canceling");
        if (isFile.delete()) {
          // File deleted successfully
          isCanceled = true;
          holder.downloadingSpeedAndStutasTxt.setText("Cancelled");
          adapter.dRHelper.DownloadsRoomDao().deleteDownload(holder.downloadsRoom);
          if (adapter.dRList.size() > holder.position) {
            adapter.dRList.remove(holder.position);
            adapter.notifyItemRemoved(holder.position);
          }
          if (notificationManager != null) {
            notificationManager.cancel(downloadId);
          }
        } else {
          // Failed to delete file
          holder.downloadingSpeedAndStutasTxt.setText("Failed");
        }
      } else {
        // File does not exist or is not a file
        holder.downloadingSpeedAndStutasTxt.setText("Source file doesn't exist or is not a file ");
      }
    }
  }

  private void updateNotification(DownloadingAdapter.DownloadingHolder holder, int downloadId) {
    if (notificationManager != null) {
      String statusText = holder.downloadingSpeedAndStutasTxt.getText().toString();
      String combinedText =
          String.format(
              "%-50s %s",
              holder.downloadedAndTotalBytesTxt.getText().toString().trim(), statusText);
      builder
          .setStyle(new NotificationCompat.BigTextStyle().bigText(combinedText))
          .setOngoing(false)
          .setSmallIcon(android.R.drawable.stat_sys_download_done);
      if (mDownloadedBytes == 0) {
        builder.setProgress(100, 0, false);
      }
      if (statusText.equalsIgnoreCase("Paused")) {
        builder.setContentText("Paused");
      } else if (statusText.equalsIgnoreCase("Failed")) {
        builder.setContentText("Failed");
      }

      notificationManager.notify(downloadId, builder.build());
    }
  }

  private void setNotification(int downloadId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel =
          new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
      notificationChannel.setDescription(CHANNEL_DESCRIPTION);
      NotificationManager notificationManager =
          mContext.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(notificationChannel);
    }

    builder =
        new NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(fileName)
            .setContentText("Connecting")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(PROGRESS_MAX, 0, true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .addAction(
                R.drawable.cancel_24px,
                "CANCEL",
                holder.getActionPendingIntent(holder, "CANCEL", holder.position))
            .setContentIntent(getPandingIntent());

    // Show notification
    notificationManager = NotificationManagerCompat.from(mContext);
    notificationManager.notify(downloadId, builder.build());
  }

  private void updateDownloadedVideos(String path) {
    DownloadedsVideoItem dVI = new DownloadedsVideoItem(path);
    EventBus.getDefault().post(dVI);
  }

  private PendingIntent getPandingIntent() {
    String action = "com.downloaderpro.mrpro.demo.multi.OPEN_ACTIVITY";
    NotificationReceiver receiver = new NotificationReceiver();
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

  public boolean isServiceRunning(Context context, Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service :
        manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  public class NotificationReceiver extends BroadcastReceiver {
    private boolean isAlreadyOpened = false;

    public NotificationReceiver() {
      // Empty Constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction() != null
          && intent.getAction().equals("com.downloaderpro.mrpro.demo.multi.OPEN_ACTIVITY")) {
        if (SaveDataApp.getActivityName() == null) {
          Intent intentActivity = new Intent(context, MultiActivity.class);
          intentActivity.putExtra("addDownload", "no");
          context.startActivity(intentActivity);
        }
      }
    }
  }
}
