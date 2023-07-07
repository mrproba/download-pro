package com.downloaderpro.mrpro.demo.downloadingservice;

import com.downloaderpro.mrpro.DownloaderPro;
import com.downloaderpro.mrpro.demo.multi.DownloadingAdapter;
import com.downloaderpro.mrpro.demo.multi.DownloadingListeners;
import java.util.List;

public class ServiceEventBus {
  private boolean cancelAllNotifications;
  private boolean forNotification=false;
  private boolean isServiceClosed=false;
  private List<DownloaderPro> downloaderPro;
  private List<DownloadingListeners> downloadingListeners;

  public ServiceEventBus(boolean b) {
    this.cancelAllNotifications = b;
  }

  public boolean getCancelAllNotifications() {
    return cancelAllNotifications;
  }

  public boolean getForNotification() {
    return this.forNotification;
  }

  public void setForNotification(boolean forNotification) {
    this.forNotification = forNotification;
  }

  public boolean getIsServiceClosed() {
    return this.isServiceClosed;
  }

  public void setIsServiceClosed(boolean isServiceClosed) {
    this.isServiceClosed = isServiceClosed;
  }

  public void setDownloadingListeners(List<DownloadingListeners> downloadingListeners) {
    this.downloadingListeners = downloadingListeners;
  }

  public List<DownloadingListeners> getDownloadingListeners() {
    return this.downloadingListeners;
  }

  public void setDownloaderPro(List<DownloaderPro> downloaderPro) {
    this.downloaderPro = downloaderPro;
  }

  public List<DownloaderPro> getDownloaderPro() {
    return this.downloaderPro;
  }
}
