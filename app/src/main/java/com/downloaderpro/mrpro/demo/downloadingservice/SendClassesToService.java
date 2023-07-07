package com.downloaderpro.mrpro.demo.downloadingservice;

import com.downloaderpro.mrpro.DownloaderPro;
import com.downloaderpro.mrpro.demo.multi.DownloadingListeners;

public class SendClassesToService {
  private DownloaderPro downloaderPro;
  private DownloadingListeners downloadingListeners;
  private boolean isAlreadCreated = false;

  public void setDownloaderPro(DownloaderPro downloaderPro) {
    this.downloaderPro = downloaderPro;
  }

  public void setDownloadingListeners(DownloadingListeners downloadingListener) {
    this.downloadingListeners = downloadingListener;
  }

  public DownloadingListeners getDownloadingListeners() {
    return this.downloadingListeners;
  }

  public DownloaderPro getDownloaderPro() {
    return this.downloaderPro;
  }

  public void setIsAlreadyCreated(boolean b) {
    this.isAlreadCreated = b;
  }

  public boolean getIsAlreadCreated() {
    return this.isAlreadCreated;
  }
}
