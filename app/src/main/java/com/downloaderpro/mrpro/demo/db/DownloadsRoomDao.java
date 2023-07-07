package com.downloaderpro.mrpro.demo.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DownloadsRoomDao {
  @Query("SELECT * FROM DownloadsRoom")
  List<DownloadsRoom> getAllDownloadsRoom();

  @Query("SELECT * FROM DownloadsRoom WHERE id = :downloadId")
  DownloadsRoom getDownloadById(int downloadId);

  @Query("SELECT * FROM DownloadsRoom ORDER BY id DESC LIMIT 1")
  DownloadsRoom getRecentDownload();

  @Insert
  void addDownload(DownloadsRoom downloadsRoom);

  @Update
  void updateDownload(DownloadsRoom downloadsRoom);

  @Query("DELETE FROM DownloadsRoom WHERE id = :userId")
  void deleteByUserId(int userId);

  @Query("DELETE FROM sqlite_sequence WHERE name='DownloadsRoom'")
  void resetAutoIncrement();

  @Delete
  void deleteDownload(DownloadsRoom downloadsRoom);
}
