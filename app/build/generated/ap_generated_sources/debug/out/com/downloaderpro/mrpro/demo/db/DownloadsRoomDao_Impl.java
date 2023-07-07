package com.downloaderpro.mrpro.demo.db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DownloadsRoomDao_Impl implements DownloadsRoomDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DownloadsRoom> __insertionAdapterOfDownloadsRoom;

  private final EntityDeletionOrUpdateAdapter<DownloadsRoom> __deletionAdapterOfDownloadsRoom;

  private final EntityDeletionOrUpdateAdapter<DownloadsRoom> __updateAdapterOfDownloadsRoom;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByUserId;

  private final SharedSQLiteStatement __preparedStmtOfResetAutoIncrement;

  public DownloadsRoomDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDownloadsRoom = new EntityInsertionAdapter<DownloadsRoom>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `DownloadsRoom` (`id`,`downloadUrl`,`downloadFileName`,`downloadedBytes`,`totalBytes`,`progress`,`downloadId`,`isPaused`,`isFailed`,`listeners`,`downloaderPro`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DownloadsRoom value) {
        stmt.bindLong(1, value.getId());
        if (value.getDownloadUrl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDownloadUrl());
        }
        if (value.getDownloadFileName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDownloadFileName());
        }
        stmt.bindLong(4, value.getDownloadedBytes());
        stmt.bindLong(5, value.getTotalBytes());
        stmt.bindLong(6, value.getProgress());
        stmt.bindLong(7, value.getDownloadId());
        final int _tmp = value.getIsPaused() ? 1 : 0;
        stmt.bindLong(8, _tmp);
        final int _tmp_1 = value.getIsFailed() ? 1 : 0;
        stmt.bindLong(9, _tmp_1);
        if (value.getListeners() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getListeners());
        }
        if (value.getDownloaderPro() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDownloaderPro());
        }
      }
    };
    this.__deletionAdapterOfDownloadsRoom = new EntityDeletionOrUpdateAdapter<DownloadsRoom>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `DownloadsRoom` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DownloadsRoom value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfDownloadsRoom = new EntityDeletionOrUpdateAdapter<DownloadsRoom>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `DownloadsRoom` SET `id` = ?,`downloadUrl` = ?,`downloadFileName` = ?,`downloadedBytes` = ?,`totalBytes` = ?,`progress` = ?,`downloadId` = ?,`isPaused` = ?,`isFailed` = ?,`listeners` = ?,`downloaderPro` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DownloadsRoom value) {
        stmt.bindLong(1, value.getId());
        if (value.getDownloadUrl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDownloadUrl());
        }
        if (value.getDownloadFileName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDownloadFileName());
        }
        stmt.bindLong(4, value.getDownloadedBytes());
        stmt.bindLong(5, value.getTotalBytes());
        stmt.bindLong(6, value.getProgress());
        stmt.bindLong(7, value.getDownloadId());
        final int _tmp = value.getIsPaused() ? 1 : 0;
        stmt.bindLong(8, _tmp);
        final int _tmp_1 = value.getIsFailed() ? 1 : 0;
        stmt.bindLong(9, _tmp_1);
        if (value.getListeners() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getListeners());
        }
        if (value.getDownloaderPro() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDownloaderPro());
        }
        stmt.bindLong(12, value.getId());
      }
    };
    this.__preparedStmtOfDeleteByUserId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM DownloadsRoom WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetAutoIncrement = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sqlite_sequence WHERE name='DownloadsRoom'";
        return _query;
      }
    };
  }

  @Override
  public void addDownload(final DownloadsRoom downloadsRoom) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDownloadsRoom.insert(downloadsRoom);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDownload(final DownloadsRoom downloadsRoom) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDownloadsRoom.handle(downloadsRoom);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateDownload(final DownloadsRoom downloadsRoom) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfDownloadsRoom.handle(downloadsRoom);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByUserId(final int userId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByUserId.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, userId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteByUserId.release(_stmt);
    }
  }

  @Override
  public void resetAutoIncrement() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfResetAutoIncrement.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfResetAutoIncrement.release(_stmt);
    }
  }

  @Override
  public List<DownloadsRoom> getAllDownloadsRoom() {
    final String _sql = "SELECT * FROM DownloadsRoom";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final int _cursorIndexOfDownloadId = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadId");
      final int _cursorIndexOfIsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaused");
      final int _cursorIndexOfIsFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "isFailed");
      final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
      final int _cursorIndexOfDownloaderPro = CursorUtil.getColumnIndexOrThrow(_cursor, "downloaderPro");
      final List<DownloadsRoom> _result = new ArrayList<DownloadsRoom>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DownloadsRoom _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpDownloadUrl;
        if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
          _tmpDownloadUrl = null;
        } else {
          _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
        }
        final String _tmpDownloadFileName;
        if (_cursor.isNull(_cursorIndexOfDownloadFileName)) {
          _tmpDownloadFileName = null;
        } else {
          _tmpDownloadFileName = _cursor.getString(_cursorIndexOfDownloadFileName);
        }
        _item = new DownloadsRoom(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _item.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _item.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _item.setProgress(_tmpProgress);
        final int _tmpDownloadId;
        _tmpDownloadId = _cursor.getInt(_cursorIndexOfDownloadId);
        _item.setDownloadId(_tmpDownloadId);
        final boolean _tmpIsPaused;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPaused);
        _tmpIsPaused = _tmp != 0;
        _item.setIsPaused(_tmpIsPaused);
        final boolean _tmpIsFailed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFailed);
        _tmpIsFailed = _tmp_1 != 0;
        _item.setIsFailed(_tmpIsFailed);
        final String _tmpListeners;
        if (_cursor.isNull(_cursorIndexOfListeners)) {
          _tmpListeners = null;
        } else {
          _tmpListeners = _cursor.getString(_cursorIndexOfListeners);
        }
        _item.setListeners(_tmpListeners);
        final String _tmpDownloaderPro;
        if (_cursor.isNull(_cursorIndexOfDownloaderPro)) {
          _tmpDownloaderPro = null;
        } else {
          _tmpDownloaderPro = _cursor.getString(_cursorIndexOfDownloaderPro);
        }
        _item.setDownloaderPro(_tmpDownloaderPro);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public DownloadsRoom getDownloadById(final int downloadId) {
    final String _sql = "SELECT * FROM DownloadsRoom WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, downloadId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final int _cursorIndexOfDownloadId = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadId");
      final int _cursorIndexOfIsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaused");
      final int _cursorIndexOfIsFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "isFailed");
      final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
      final int _cursorIndexOfDownloaderPro = CursorUtil.getColumnIndexOrThrow(_cursor, "downloaderPro");
      final DownloadsRoom _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpDownloadUrl;
        if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
          _tmpDownloadUrl = null;
        } else {
          _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
        }
        final String _tmpDownloadFileName;
        if (_cursor.isNull(_cursorIndexOfDownloadFileName)) {
          _tmpDownloadFileName = null;
        } else {
          _tmpDownloadFileName = _cursor.getString(_cursorIndexOfDownloadFileName);
        }
        _result = new DownloadsRoom(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _result.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _result.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _result.setProgress(_tmpProgress);
        final int _tmpDownloadId;
        _tmpDownloadId = _cursor.getInt(_cursorIndexOfDownloadId);
        _result.setDownloadId(_tmpDownloadId);
        final boolean _tmpIsPaused;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPaused);
        _tmpIsPaused = _tmp != 0;
        _result.setIsPaused(_tmpIsPaused);
        final boolean _tmpIsFailed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFailed);
        _tmpIsFailed = _tmp_1 != 0;
        _result.setIsFailed(_tmpIsFailed);
        final String _tmpListeners;
        if (_cursor.isNull(_cursorIndexOfListeners)) {
          _tmpListeners = null;
        } else {
          _tmpListeners = _cursor.getString(_cursorIndexOfListeners);
        }
        _result.setListeners(_tmpListeners);
        final String _tmpDownloaderPro;
        if (_cursor.isNull(_cursorIndexOfDownloaderPro)) {
          _tmpDownloaderPro = null;
        } else {
          _tmpDownloaderPro = _cursor.getString(_cursorIndexOfDownloaderPro);
        }
        _result.setDownloaderPro(_tmpDownloaderPro);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public DownloadsRoom getRecentDownload() {
    final String _sql = "SELECT * FROM DownloadsRoom ORDER BY id DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final int _cursorIndexOfDownloadId = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadId");
      final int _cursorIndexOfIsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaused");
      final int _cursorIndexOfIsFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "isFailed");
      final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
      final int _cursorIndexOfDownloaderPro = CursorUtil.getColumnIndexOrThrow(_cursor, "downloaderPro");
      final DownloadsRoom _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpDownloadUrl;
        if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
          _tmpDownloadUrl = null;
        } else {
          _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
        }
        final String _tmpDownloadFileName;
        if (_cursor.isNull(_cursorIndexOfDownloadFileName)) {
          _tmpDownloadFileName = null;
        } else {
          _tmpDownloadFileName = _cursor.getString(_cursorIndexOfDownloadFileName);
        }
        _result = new DownloadsRoom(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _result.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _result.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _result.setProgress(_tmpProgress);
        final int _tmpDownloadId;
        _tmpDownloadId = _cursor.getInt(_cursorIndexOfDownloadId);
        _result.setDownloadId(_tmpDownloadId);
        final boolean _tmpIsPaused;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPaused);
        _tmpIsPaused = _tmp != 0;
        _result.setIsPaused(_tmpIsPaused);
        final boolean _tmpIsFailed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFailed);
        _tmpIsFailed = _tmp_1 != 0;
        _result.setIsFailed(_tmpIsFailed);
        final String _tmpListeners;
        if (_cursor.isNull(_cursorIndexOfListeners)) {
          _tmpListeners = null;
        } else {
          _tmpListeners = _cursor.getString(_cursorIndexOfListeners);
        }
        _result.setListeners(_tmpListeners);
        final String _tmpDownloaderPro;
        if (_cursor.isNull(_cursorIndexOfDownloaderPro)) {
          _tmpDownloaderPro = null;
        } else {
          _tmpDownloaderPro = _cursor.getString(_cursorIndexOfDownloaderPro);
        }
        _result.setDownloaderPro(_tmpDownloaderPro);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
