package com.example.seb.alquerquegame;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddPlayer extends AppCompatActivity {

    private EditText playerName;
    private EditText playerDescription;
    private String file= "players";
    public ArrayList<Player> playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_league_table, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loadPlayers()==null){
            playersList= new ArrayList<Player>();
        }
        else{
            playersList= loadPlayers();
        }

        playerName = (EditText) findViewById(R.id.name_entry_box);
        playerDescription = (EditText) findViewById(R.id.description_entry_box);
    }

    private boolean emptyField(EditText textField){
        if(textField.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Text fields are empty", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_add_player) {
            System.out.println(loadPlayers());
            if(!emptyField(playerName)&&!emptyField(playerDescription)){
                String name= playerName.getText().toString();
                String description= playerDescription.getText().toString();
                if(!name.equals("player1")||!name.equals("player2")){
                    playersList.add(new Player(playerName.getText().toString(),
                            playerDescription.getText().toString(),0));
                    loadPlayers();
                    savePlayers();

                    Toast.makeText(getApplicationContext(),"Player Added",Toast.LENGTH_LONG).show();
                    playerName.setText("");
                    playerDescription.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Name Not Allowed",Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void savePlayers(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson= new Gson();
        String json = gson.toJson(playersList);

        editor.putString(file, json);
        editor.commit();
    }

    public ArrayList<Player> loadPlayers(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson= new Gson();
        String json = sharedPreferences.getString(file, null);

        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> players = gson.fromJson(json,type);

        return players;
    }

}
