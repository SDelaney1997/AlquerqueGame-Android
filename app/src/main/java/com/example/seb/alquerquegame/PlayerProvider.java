package com.example.seb.alquerquegame;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class PlayerProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.seb.alquerquegame";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/player");
    public static final String BASE_PATH= PlayerContract.TABLE_NAME_PLAYER_DETAILS; //"player";

    private static final int PLAYER = 0;
    private static final int PLAYER_ID = 1;
   // public static final UriMatcher uriMatcher = getUriMatcher();
   // private static UriMatcher getUriMatcher(){
     //   UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
       // uriMatcher.addURI(AUTHORITY,BASE_PATH,PLAYER);//"player",PLAYER);
        //uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",PLAYER_ID);//"/player",PLAYER_ID);
        //return uriMatcher;
    //}

    private PlayerDBHelper playerDBHelper;
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        playerDBHelper= new PlayerDBHelper(getContext());
        uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,BASE_PATH,PLAYER);//"player",PLAYER);
        uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",PLAYER_ID);//"/player",PLAYER_ID);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case PLAYER:
                return "vnd.android.cursor.dir/vnd.example.seb.player_details";
                //return "vnd.android.cursor.dir/vnd.com.example.seb.alquerquegame";
            case PLAYER_ID:
                return "vnd.android.cursor.item/vnd.example.player_details";
                //return "vnd.android.cursor.item/vnd.com.example.seb.alquerquegame";
            default:
                throw new IllegalArgumentException("Unsupported URI");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder= new SQLiteQueryBuilder();
        builder.setTables(PlayerContract.TABLE_NAME_PLAYER_DETAILS);
        int uriType= uriMatcher.match(uri);
        switch (uriType){
            case PLAYER:
                break;
            //return "vnd.android.cursor.div/vnd.com.example.seb.alquerquegame";
            case PLAYER_ID:
                builder.appendWhere(PlayerContract._ID+"="+uri.getLastPathSegment());
                break;
            //return "vnd.android.cursor.div/vnd.com.example.seb.alquerquegame";
            default:
                throw new IllegalArgumentException("Unsupported URI");
        }
        SQLiteDatabase db= playerDBHelper.getReadableDatabase();
        Cursor cursor= builder.query(db,projection, selection,
                selectionArgs,null,null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
        //String id= null;
        //if(uriMatcher.match(uri)== PLAYER_ID){
          //  id= uri.getPathSegments().get(1);//*******************
        //}
        //return playerDBHelper.getPlayers(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = playerDBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        Uri resultUri = null;
        if (uriType == PLAYER) {
            long rowId = db.insert(PlayerContract.TABLE_NAME_PLAYER_DETAILS, null, values);
            resultUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
        }
        else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return resultUri;

        /*
        try {
            long id = playerDBHelper.addNewPlayer(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }*/
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = playerDBHelper.getWritableDatabase();
        int rowsDeleted = 0;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLAYER:
                rowsDeleted = db.delete(PlayerContract.TABLE_NAME_PLAYER_DETAILS,
                        selection, selectionArgs);
                break;
            case PLAYER_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsDeleted = db.delete(PlayerContract.TABLE_NAME_PLAYER_DETAILS,
                        newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    private String appendToSelection(Uri uri, String selection) {
        String id = uri.getLastPathSegment();
        StringBuilder newSelection = new StringBuilder(PlayerContract._ID + "=" + id);
        if (selection != null && !selection.isEmpty()) {
            newSelection.append(" AND " + selection);
        }
        return newSelection.toString();
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = playerDBHelper.getWritableDatabase();
        int rowsUpdated = 0;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLAYER:
                rowsUpdated = db.update(PlayerContract.TABLE_NAME_PLAYER_DETAILS,
                        values, selection, selectionArgs);
                break;
            case PLAYER_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsUpdated = db.update(PlayerContract.TABLE_NAME_PLAYER_DETAILS,
                        values, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }
}
