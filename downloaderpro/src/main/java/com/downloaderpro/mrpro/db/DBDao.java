package com.downloaderpro.mrpro.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DBDao {
  @Query("SELECT * FROM DB")
  List<DB> getAllDB();

  @Query("SELECT * FROM DB WHERE id = :dBId")
  DB getDBById(int dBId);

  @Query("SELECT * FROM DB ORDER BY id DESC LIMIT 1")
  DB getRecentDB();

  @Insert
  void addDB(DB DB);

  @Update
  void updateDB(DB DB);

  @Query("DELETE FROM DB WHERE id = :userId")
  void deleteByUserId(int userId);

  @Query("DELETE FROM sqlite_sequence WHERE name='DB'")
  void resetAutoIncrement();

  @Delete
  void deleteDB(DB DB);
}
