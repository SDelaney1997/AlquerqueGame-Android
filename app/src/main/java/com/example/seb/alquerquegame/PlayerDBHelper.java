package com.example.seb.alquerquegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class PlayerDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String DATABASE_NAME = "players.db";

    private static final String CREATE_ENTRIES =
            "CREATE TABLE " + PlayerContract.TABLE_NAME_PLAYER_DETAILS + " (" +
                    PlayerContract._ID + " INTEGER" + " PRIMARY KEY " +
                    COMMA_SEP +
                    PlayerContract.COLUMN_NAME_PLAYER + TEXT_TYPE +
                    " UNIQUE " + COMMA_SEP +
                    PlayerContract.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                    COMMA_SEP +
                    PlayerContract.COLUMN_NAME_SCORE + TEXT_TYPE + ")";

    private static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS" + PlayerContract.TABLE_NAME_PLAYER_DETAILS;

    PlayerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ENTRIES);
        onCreate(db);
    }

    public Cursor getPlayers(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(PlayerContract.TABLE_NAME_PLAYER_DETAILS);

        if (id != null) {
            sqliteQueryBuilder.appendWhere(PlayerContract._ID + " = " + id);
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = PlayerContract.COLUMN_NAME_PLAYER;
        }
        Cursor cursor = sqliteQueryBuilder.query(getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    public long addNewPlayer(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(PlayerContract.TABLE_NAME_PLAYER_DETAILS, "", values);
        if (id <= 0) {
            throw new SQLException("FAILED TO ADD PLAYER");
        }

        return id;
    }
}
