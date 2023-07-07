package com.downloaderpro.mrpro.demo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "DownloadsRoom")
public class DownloadsRoom {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "downloadUrl")
  private String downloadUrl;

  @ColumnInfo(name = "downloadFileName")
  private String downloadFileName;

  @ColumnInfo(name = "downloadedBytes")
  private long downloadedBytes;

  @ColumnInfo(name = "totalBytes")
  private long totalBytes;

  @ColumnInfo(name = "progress")
  private int progress;

  @ColumnInfo(name = "downloadId")
  private int downloadId;

  @ColumnInfo(name = "isPaused")
  private boolean isPaused;

  @ColumnInfo(name = "isFailed")
  private boolean isFailed=false;

  @ColumnInfo(name = "listeners")
  private String listeners = null;

  @ColumnInfo(name = "downloaderPro")
  private String downloaderPro = null;

  @Ignore
  public DownloadsRoom(String downloadUrl, String downloadFileName) {
    this.downloadUrl = downloadUrl;
    this.downloadFileName = downloadFileName;
    downloadId = 0;
    isPaused = false;
    progress = 0;
    totalBytes = 0;
    downloadedBytes = 0;
  }

  public DownloadsRoom(int id, String downloadUrl, String downloadFileName) {
    this.id = id;
    this.downloadUrl = downloadUrl;
    this.downloadFileName = downloadFileName;
    downloadId = 0;
    isPaused = false;
    progress = 0;
    totalBytes = 0;
    downloadedBytes = 0;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDownloadUrl() {
    return this.downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getDownloadFileName() {
    return this.downloadFileName;
  }

  public void setDownloadFileName(String downloadFileName) {
    this.downloadFileName = downloadFileName;
  }

  public long getDownloadedBytes() {
    return this.downloadedBytes;
  }

  public void setDownloadedBytes(long downloadedBytes) {
    this.downloadedBytes = downloadedBytes;
  }

  public long getTotalBytes() {
    return this.totalBytes;
  }

  public void setTotalBytes(long totalBytes) {
    this.totalBytes = totalBytes;
  }

  public int getProgress() {
    return this.progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public boolean getIsPaused() {
    return this.isPaused;
  }

  public void setIsPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

  public int getDownloadId() {
    return this.downloadId;
  }

  public void setDownloadId(int downloadId) {
    this.downloadId = downloadId;
  }

  public String getListeners() {
    return this.listeners;
  }

  public void setListeners(String listeners) {
    this.listeners = listeners;
  }

  public String getDownloaderPro() {
    return this.downloaderPro;
  }

  public void setDownloaderPro(String downloaderPro) {
    this.downloaderPro = downloaderPro;
  }

  public boolean getIsFailed() {
    return this.isFailed;
  }

  public void setIsFailed(boolean isFailed) {
    this.isFailed = isFailed;
  }
}
