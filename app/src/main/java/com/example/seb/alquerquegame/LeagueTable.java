package com.example.seb.alquerquegame;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LeagueTable extends AppCompatActivity{// implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView playerList;
    private ArrayList<Player> list;
    private ArrayAdapter<Player> adapter;
    private String file= "players";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table);
        if(loadPlayers()==null){
            list= new ArrayList<Player>();
        }
        else{
            list= loadPlayers();
        }

        playerList= (ListView) findViewById(R.id.players_list_view);
        adapter = new ArrayAdapter<Player>(this,android.R.layout.simple_expandable_list_item_1, list);
        playerList.setAdapter(adapter);
    }

    public ArrayList<Player> loadPlayers(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson= new Gson();
        String json = sharedPreferences.getString(file, null);

        Type type = new TypeToken<ArrayList<Player>>() {}.getType();
        ArrayList<Player> players = gson.fromJson(json,type);

        return players;
    }
}

    /*
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table);

        String[] from = {PlayerContract.COLUMN_NAME_PLAYER,
        PlayerContract.COLUMN_NAME_DESCRIPTION};
        int[] to = {R.id.common_name_tv, R.id.binomial_name_tv};        //HERE
        adapter = new SimpleCursorAdapter(this, R.layout.player_layout, cursor,
        from, to, 0);
        playerList = (ListView) findViewById(R.id.players_list_view);
        getLoaderManager().initLoader(0, null, this);
        playerList.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new
                CursorLoader(this,PlayerProvider.CONTENT_URI,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


*/
