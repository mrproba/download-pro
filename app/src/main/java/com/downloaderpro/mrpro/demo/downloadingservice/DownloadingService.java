package com.downloaderpro.mrpro.demo.downloadingservice;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import android.os.IBinder;
import android.content.Intent;
import android.app.Service;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.downloaderpro.mrpro.DownloaderPro;
import com.downloaderpro.mrpro.demo.R;
import com.downloaderpro.mrpro.demo.multi.DataEvent;
import com.downloaderpro.mrpro.demo.multi.DownloadingListeners;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownloadingService extends Service {
  private Intent intent;
  private static final String CHANNEL_ID = "Download Pro Downloading Service";
  private static final String CHANNEL_NAME = "Downloading Service";
  private static final int FOREGROUND_ID = 1000;
  private boolean cancelAllNotifications = true;
  private boolean forNotification;
  private List<DownloaderPro> downloaderPro;
  private List<DownloadingListeners> downloadingListeners;

  @Override
  public void onCreate() {
    super.onCreate();
    EventBus.getDefault().register(this);
    downloaderPro = new ArrayList<>();
    downloadingListeners = new ArrayList<>();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
    NotificationCompat.Builder notification =
        new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloader Pro")
            .setContentText("Downloader Pro is downloadings...")
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground);
    startForeground(FOREGROUND_ID, notification.build());
    sendServiceCreated();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    this.intent = intent;
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void sendAdapterBack() {
    DataEvent event = new DataEvent();
    event.setDownloaderPro(downloaderPro);
    event.setDownloadingListeners(downloadingListeners);
    EventBus.getDefault().post(event);
  }

  private void sendServiceCreated() {
    ServiceCreatedBus event = new ServiceCreatedBus(true);
    EventBus.getDefault().post(event);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onDataEvent(ServiceEventBus event) {
    if (event != null) {
      forNotification = event.getForNotification();
      if (forNotification) {
        stopForeground(FOREGROUND_ID);
        cancelAllNotifications = event.getCancelAllNotifications();
      } else {
        downloaderPro = event.getDownloaderPro();
        downloadingListeners = event.getDownloadingListeners();
      }
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void recivedClasses(SendClassesToService event) {
    if (event.getIsAlreadCreated() == true) {
      sendAdapterBack();
    } else {
      downloaderPro.add(event.getDownloaderPro());
      downloadingListeners.add(event.getDownloadingListeners());
    }
  }

  @Override
  public void onDestroy() {
    EventBus.getDefault().unregister(this);
    stopForeground(FOREGROUND_ID);
    if (cancelAllNotifications) {
      for (int i = 0; i < 100; i++) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(i);
      }
    }
    super.onDestroy();
  }
}
