package com.downloaderpro.mrpro.demo.multi;

import com.downloaderpro.mrpro.DownloaderPro;
import java.util.List;

public class DataEvent {
  private String fileName;
  private String downloadUrl;
  private List<DownloaderPro> downloaderPro;
  private List<DownloadingListeners> downloadingListeners;

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setUrl(String url) {
    this.downloadUrl = url;
  }

  public String getFileName() {
    return fileName;
  }

  public String getDownloadUrl() {
    return downloadUrl;
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
