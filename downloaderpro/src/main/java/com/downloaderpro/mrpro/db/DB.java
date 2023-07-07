package com.downloaderpro.mrpro.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "DB")
public class DB {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "downloadUrl")
  private String downloadUrl;

  @ColumnInfo(name = "downloadFileName")
  private String downloadFileName;

  @ColumnInfo(name = "fileDirectory")
  private String fileDirectory;

  @ColumnInfo(name = "downloadedBytes")
  private long downloadedBytes;

  @ColumnInfo(name = "totalBytes")
  private long totalBytes;

  @ColumnInfo(name = "progress")
  private int progress;

  @Ignore
  public DB(String downloadUrl, String downloadFileName, String fileDirectory) {
    this.downloadUrl = downloadUrl;
    this.downloadFileName = downloadFileName;
    this.fileDirectory = fileDirectory;
    progress = 0;
    totalBytes = 0;
    downloadedBytes = 0;
  }

  public DB(int id, String downloadUrl, String downloadFileName, String fileDirectory) {
    this.id = id;
    this.downloadUrl = downloadUrl;
    this.downloadFileName = downloadFileName;
    this.fileDirectory = fileDirectory;
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

  public String getFileDirectory() {
    return this.fileDirectory;
  }

  public void setFileDirectory(String fileDirectory) {
    this.fileDirectory = fileDirectory;
  }
}
