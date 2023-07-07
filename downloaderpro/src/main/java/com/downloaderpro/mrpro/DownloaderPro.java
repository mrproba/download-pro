package com.downloaderpro.mrpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.downloaderpro.mrpro.db.DBHelper;
import com.downloaderpro.mrpro.db.DB;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloaderPro {
  private Context context;
  private ExecutorService executor;
  private DownloadListeners downloadListeners;
  private File file;
  private String fileName;
  private String dUrl;
  private boolean isPuased = false;
  private boolean resume = false;
  private boolean isCancelled = false;
  private long downloadingSpeedStartTime;
  private long downloadStartTimeBytes = 0;
  private long downloadingSpeed = 0;
  private long downloadedBytes = 0;
  private int progress = 0;
  private long totalBytes;
  private int downloadId = 0;
  private InputStream input = null;
  private OutputStream output = null;
  private HttpURLConnection connection = null;
  private DBHelper dBHelper = null;
  private DB dB = null;
  private static final long MIN_PROGRESS_UPDATE_INTERVAL = 300; // milliseconds
  private long lastProgressUpdateTime = 0;
  private boolean isFirstTimeProgress = true;
  private File isAlreadyFile;

  public DownloaderPro(Context context) {
    this.context = context;
  }

  public void setDownloadListeners(DownloadListeners downloadListeners) {
    this.downloadListeners = downloadListeners;
  }

  public void setDir(File file) {
    this.file = file;
  }

  public void setFileName(String name) {
    this.fileName = name.replaceAll(" ", "_");
  }

  public void setUrl(String url) {
    this.dUrl = url;
  }

  public void pauseDownload() {
    isPuased = true;
    resume = false;
    if (executor != null) {
      executor.shutdown();
      try {
        input.close();
      } catch (Exception e) {
      }
      if (connection != null) {
        connection.disconnect();
      }
    }
    new Handler(Looper.getMainLooper())
        .postDelayed(
            new Runnable() {
              @Override
              public void run() {
                downloadListeners.onPause(downloadId);
              }
            },
            100);
  }

  public int getDownloadId() {
    return downloadId;
  }

  public void startDownloading() {
    isPuased = false;
    download();
  }

  public void resumeDownload(final int downloadId) {
    this.downloadId = downloadId;
    isPuased = false;
    resume = true;
    download();
  }

  public void cancelDownload(int downloadId) {
    isCancelled = true;
    if (executor != null) {
      try {
        input.close();
      } catch (Exception e) {
      }
      if (connection != null) connection.disconnect();
      executor.shutdown();
    } else {
      dBHelper = DBHelper.getDB(context);
    }
    dBHelper.DBDao().deleteByUserId(downloadId);
    new Handler(Looper.getMainLooper())
        .post(
            new Runnable() {
              @Override
              public void run() {
                downloadListeners.onCancel(downloadId);
              }
            });
  }

  private void download() {
    executor = Executors.newSingleThreadExecutor();
    executor.execute(
        new Runnable() {
          @Override
          public void run() {
            SharedPreferences sharedPreferences =
                context.getSharedPreferences("DownloadingPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            dBHelper = DBHelper.getDB(context);
            if (resume) {
              dB = dBHelper.DBDao().getDBById(downloadId);
              if (dB != null) {
                file = new File(dB.getFileDirectory());
                File isFile = new File(file, dB.getDownloadFileName());
                if (isFile.exists() && isFile.isFile()) {
                  dUrl = dB.getDownloadUrl();
                  fileName = dB.getDownloadFileName();
                  downloadedBytes = dB.getDownloadedBytes();
                  totalBytes = dB.getTotalBytes();
                  progress = dB.getProgress();
                  dBHelper.DBDao().updateDB(dB);

                  if (downloadedBytes != 0) {
                    new Handler(Looper.getMainLooper())
                        .post(
                            new Runnable() {
                              @Override
                              public void run() {
                                downloadListeners.onResume(downloadId);
                              }
                            });
                  }
                } else {
                  dBHelper.DBDao().deleteDB(dB);
                }
              }
            } else {
              try {
                output = new FileOutputStream(file.getAbsolutePath() + "/" + fileName);
              } catch (FileNotFoundException e) {
              }
            }

            if (!resume && !isCancelled) {
              if (dB == null) {
                dBHelper.DBDao().addDB(new DB(dUrl, fileName, file.getAbsolutePath()));
                dB = dBHelper.DBDao().getRecentDB();
                downloadId = dB.getId();
                new Handler(Looper.getMainLooper())
                    .post(
                        new Runnable() {
                          @Override
                          public void run() {
                            downloadListeners.onConnecting(downloadId);
                          }
                        });
              }
            } else if (dB == null) {
              if (!isPuased && !isCancelled) {
                new Handler(Looper.getMainLooper())
                    .post(
                        new Runnable() {
                          @Override
                          public void run() {
                            downloadListeners.onFailed(downloadId, "File not found for resuming");
                          }
                        });
                return;
              }
            }

            try {
              final URL url = new URL(dUrl);
              try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(60000);
                connection.setConnectTimeout(60000);
                connection.setInstanceFollowRedirects(true);
                connection.setAllowUserInteraction(true);

                if (resume) {
                  connection.setRequestProperty("Range", "bytes=" + downloadedBytes + "-");
                }
                connection.connect();

                // expect HTTP 200 OK or HTTP 206 PARTIAL, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK
                    && connection.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
                  if (dB == null && dB.getDownloadedBytes() == 0) {
                    dBHelper.DBDao().deleteDB(dB);
                  }
                  String error =
                      "Server returned HTTP "
                          + connection.getResponseCode()
                          + " "
                          + connection.getResponseMessage();
                  if (!isPuased && !isCancelled) {
                    new Handler(Looper.getMainLooper())
                        .post(
                            new Runnable() {
                              @Override
                              public void run() {
                                downloadListeners.onFailed(downloadId, error);
                              }
                            });
                    return;
                  }
                }

                if (!resume) {
                  // might be -1: server did not report the length
                  totalBytes = connection.getContentLength();
                } else {
                  resume = false;
                }

                // download the file
                input = connection.getInputStream();
                if (downloadedBytes != 0) {
                  // for resuming
                  output = new FileOutputStream(file.getAbsolutePath() + "/" + fileName, true);
                }

                byte data[] = new byte[4096];
                int count;
                long previousDownloadedBytes = -1;

                // totalBytes saving in db
                dB.setTotalBytes(totalBytes);

                // for downloading speed
                downloadingSpeedStartTime = System.currentTimeMillis();

                while ((count = input.read(data)) != -1) {
                  boolean cancelAllDownloads =
                      sharedPreferences.getBoolean("cancelAllDownloads", false);
                  if (cancelAllDownloads) {
                    int howManyDownloadings = sharedPreferences.getInt("howManyDownloadings", 0);
                    int howManyDownloadingsCannceled =
                        sharedPreferences.getInt("howManyDownloadingsCannceled", 0) + 1;
                    editor
                        .putInt("howManyDownloadingsCannceled", howManyDownloadingsCannceled)
                        .apply();
                    if (howManyDownloadings == howManyDownloadingsCannceled) {
                      editor.clear();
                    }

                    input.close();
                    if (connection != null) connection.disconnect();
                    return;
                  }
                  downloadedBytes += count;
                  downloadStartTimeBytes += count;
                  // publishing the progress....
                  if (totalBytes > 0 && downloadedBytes != previousDownloadedBytes) {
                    long currentTime = System.currentTimeMillis();
                    if (isFirstTimeProgress
                        || currentTime - lastProgressUpdateTime >= MIN_PROGRESS_UPDATE_INTERVAL) {
                      // for downloading speed
                      long endTime = System.currentTimeMillis();
                      long duration = endTime - downloadingSpeedStartTime;
                      if (duration > 0) {
                        downloadingSpeed = (downloadStartTimeBytes * 1000) / duration;
                      }
                      progress = (int) (downloadedBytes * 100 / totalBytes);
                      publishProgress(
                          downloadId, progress, downloadedBytes, totalBytes, downloadingSpeed);
                      lastProgressUpdateTime = currentTime;
                      isFirstTimeProgress = false;
                      downloadingSpeedStartTime = System.currentTimeMillis();
                      downloadStartTimeBytes = 0;
                    }
                    dB.setDownloadedBytes(downloadedBytes);
                    dB.setProgress(progress);
                    dBHelper.DBDao().updateDB(dB);
                    previousDownloadedBytes = downloadedBytes;
                  }
                  output.write(data, 0, count);
                }

                // downloading completed
                if (connection != null) {
                  new Handler(Looper.getMainLooper())
                      .post(
                          new Runnable() {
                            @Override
                            public void run() {
                              if (fileName.startsWith(".", 0)) {
                                // un hiding file
                                File from = new File(file, fileName);
                                fileName = fileName.replaceFirst(".", "");
                                File to = new File(file, fileName); // final file
                                if (from.exists() && from.isFile()) from.renameTo(to);
                                dBHelper.DBDao().deleteByUserId(dB.getId());
                                // for downloading speed
                                long endTime = System.currentTimeMillis();
                                long duration = endTime - downloadingSpeedStartTime;
                                if (duration > 0) {
                                  downloadingSpeed = (downloadStartTimeBytes * 1000) / duration;
                                }
                              }
                              MediaScannerConnection.scanFile(
                                  context,
                                  new String[] {file.getAbsolutePath() + "/" + fileName},
                                  null,
                                  null);
                              if (!isPuased && !isCancelled) {
                                publishProgress(
                                    downloadId, 100, downloadedBytes, totalBytes, downloadingSpeed);
                                downloadListeners.onComplete(downloadId);
                              }
                            }
                          });
                }

              } catch (Exception e) {
                if (dB == null && dB.getDownloadedBytes() == 0) {
                  dBHelper.DBDao().deleteDB(dB);
                }
                if (!isPuased && !isCancelled) {
                  new Handler(Looper.getMainLooper())
                      .post(
                          new Runnable() {
                            @Override
                            public void run() {
                              downloadListeners.onFailed(downloadId, e.getMessage());
                            }
                          });
                  return;
                }
              } finally {
                if (dB == null) {
                  dBHelper.DBDao().deleteDB(dB);
                }
                try {
                  if (output != null) output.close();
                  if (input != null) input.close();
                } catch (IOException ignored) {
                }
                if (connection != null) connection.disconnect();
              }
            } catch (MalformedURLException e) {
              if (!isPuased && !isCancelled) {
                new Handler(Looper.getMainLooper())
                    .post(
                        new Runnable() {
                          @Override
                          public void run() {
                            downloadListeners.onFailed(downloadId, e.getMessage());
                          }
                        });
                if (dB == null) {
                  dBHelper.DBDao().deleteDB(dB);
                }
              }
            }
            return;
          }
        });
  }

  private synchronized void publishProgress(
      int downloadId, int progress, long downloadedBytes, long totalBytes, long speed) {
    new Handler(Looper.getMainLooper())
        .post(
            new Runnable() {
              @Override
              public void run() {
                downloadListeners.onProgress(
                    downloadId, progress, downloadedBytes, totalBytes, speed);
              }
            });
  }
}
