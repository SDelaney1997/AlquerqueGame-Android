package com.example.seb.alquerquegame;

import android.provider.BaseColumns;

public class PlayerContract implements BaseColumns{
    public static final String TABLE_NAME_PLAYER_DETAILS= "player_details";
    //public static final String COLUMN_NAME_PLAYER_ID= "player_id";
    public static final String COLUMN_NAME_PLAYER= "player_name";
    public static final String COLUMN_NAME_DESCRIPTION= "player_description";
    public static final String COLUMN_NAME_SCORE= "player_score";

    //public static final int COLUMN_INDEX_PLAYER_ID= 0;
    public static final int COLUMN_INDEX_PLAYER = 0;
    public static final int COLUMN_INDEX_DESCRIPTION= 1;
    public static final int COLUMN_INDEX_SCORE= 2;
}
