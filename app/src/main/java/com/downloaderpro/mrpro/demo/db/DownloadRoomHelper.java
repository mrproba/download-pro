package com.downloaderpro.mrpro.demo.db;

import android.content.Context;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    entities = {DownloadsRoom.class},
    exportSchema = false,
    version = 1)
public abstract class DownloadRoomHelper extends RoomDatabase {
  private static final String DB_NAME = "DownloadRoomDB";
  private static DownloadRoomHelper instance;

  public static synchronized DownloadRoomHelper getDB(Context context) {
    if (instance == null) {
      instance =
          Room.databaseBuilder(context, DownloadRoomHelper.class, DB_NAME)
              .fallbackToDestructiveMigration()
              .allowMainThreadQueries()
              .build();
    }

    return instance;
  }

  public abstract DownloadsRoomDao DownloadsRoomDao();
}
