package com.downloaderpro.mrpro.demo;

import android.os.Environment;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;

public class FileDir {

  public static File getFileDir() {
    String dirName = "DownloaderPro";
    File file = new File(Environment.getExternalStorageDirectory(), dirName);
    if (!file.exists()) {
      DocumentFile file1 =
          DocumentFile.fromFile(
              new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
      file1.createDirectory(dirName);
    }
    return file;
  }
}
