package com.downloaderpro.mrpro.db;

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
public final class DBDao_Impl implements DBDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DB> __insertionAdapterOfDB;

  private final EntityDeletionOrUpdateAdapter<DB> __deletionAdapterOfDB;

  private final EntityDeletionOrUpdateAdapter<DB> __updateAdapterOfDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByUserId;

  private final SharedSQLiteStatement __preparedStmtOfResetAutoIncrement;

  public DBDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDB = new EntityInsertionAdapter<DB>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `DB` (`id`,`downloadUrl`,`downloadFileName`,`fileDirectory`,`downloadedBytes`,`totalBytes`,`progress`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DB value) {
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
        if (value.getFileDirectory() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFileDirectory());
        }
        stmt.bindLong(5, value.getDownloadedBytes());
        stmt.bindLong(6, value.getTotalBytes());
        stmt.bindLong(7, value.getProgress());
      }
    };
    this.__deletionAdapterOfDB = new EntityDeletionOrUpdateAdapter<DB>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `DB` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DB value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfDB = new EntityDeletionOrUpdateAdapter<DB>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `DB` SET `id` = ?,`downloadUrl` = ?,`downloadFileName` = ?,`fileDirectory` = ?,`downloadedBytes` = ?,`totalBytes` = ?,`progress` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DB value) {
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
        if (value.getFileDirectory() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFileDirectory());
        }
        stmt.bindLong(5, value.getDownloadedBytes());
        stmt.bindLong(6, value.getTotalBytes());
        stmt.bindLong(7, value.getProgress());
        stmt.bindLong(8, value.getId());
      }
    };
    this.__preparedStmtOfDeleteByUserId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM DB WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetAutoIncrement = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sqlite_sequence WHERE name='DB'";
        return _query;
      }
    };
  }

  @Override
  public void addDB(final DB DB) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDB.insert(DB);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDB(final DB DB) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDB.handle(DB);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateDB(final DB DB) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfDB.handle(DB);
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
  public List<DB> getAllDB() {
    final String _sql = "SELECT * FROM DB";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfFileDirectory = CursorUtil.getColumnIndexOrThrow(_cursor, "fileDirectory");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final List<DB> _result = new ArrayList<DB>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DB _item;
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
        final String _tmpFileDirectory;
        if (_cursor.isNull(_cursorIndexOfFileDirectory)) {
          _tmpFileDirectory = null;
        } else {
          _tmpFileDirectory = _cursor.getString(_cursorIndexOfFileDirectory);
        }
        _item = new DB(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName,_tmpFileDirectory);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _item.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _item.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _item.setProgress(_tmpProgress);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public DB getDBById(final int dBId) {
    final String _sql = "SELECT * FROM DB WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dBId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfFileDirectory = CursorUtil.getColumnIndexOrThrow(_cursor, "fileDirectory");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final DB _result;
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
        final String _tmpFileDirectory;
        if (_cursor.isNull(_cursorIndexOfFileDirectory)) {
          _tmpFileDirectory = null;
        } else {
          _tmpFileDirectory = _cursor.getString(_cursorIndexOfFileDirectory);
        }
        _result = new DB(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName,_tmpFileDirectory);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _result.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _result.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _result.setProgress(_tmpProgress);
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
  public DB getRecentDB() {
    final String _sql = "SELECT * FROM DB ORDER BY id DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
      final int _cursorIndexOfDownloadFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadFileName");
      final int _cursorIndexOfFileDirectory = CursorUtil.getColumnIndexOrThrow(_cursor, "fileDirectory");
      final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
      final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final DB _result;
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
        final String _tmpFileDirectory;
        if (_cursor.isNull(_cursorIndexOfFileDirectory)) {
          _tmpFileDirectory = null;
        } else {
          _tmpFileDirectory = _cursor.getString(_cursorIndexOfFileDirectory);
        }
        _result = new DB(_tmpId,_tmpDownloadUrl,_tmpDownloadFileName,_tmpFileDirectory);
        final long _tmpDownloadedBytes;
        _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
        _result.setDownloadedBytes(_tmpDownloadedBytes);
        final long _tmpTotalBytes;
        _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
        _result.setTotalBytes(_tmpTotalBytes);
        final int _tmpProgress;
        _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
        _result.setProgress(_tmpProgress);
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
