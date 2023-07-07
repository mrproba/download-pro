package com.downloaderpro.mrpro;

public interface DownloadListeners {
  public void onConnecting(int downloadId);

  public void onComplete(int downloadId);

  public void onFailed(int downloadId, String message);

  public void onProgress(
      int downloadId, int progress, long downloadedBytes, long totalBytes, long downloadingSpeed);

  public void onPause(int downloadId);

  public void onResume(int downloadId);

  public void onCancel(int downloadId);
}
