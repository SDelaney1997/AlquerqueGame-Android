package com.example.seb.alquerquegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private Player player1= new Player("Player1","Default",0);
    private Player player2= new Player("Player2","Default",0);

    private Button play;
    private Button continueGame;
    private Button addPlayer;
    private Button leagueView;
    private Button rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play_btn);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String p1= player1.getName();
                String p2= player2.getName();
                Intent intent = new Intent(MainActivity.this, AlquerquePlay.class);
                intent.putExtra("player1",p1);
                intent.putExtra("player2",p2);
                intent.putExtra("continue","false");
                startActivity(intent);
            }
        });
        continueGame = (Button) findViewById(R.id.continue_btn);
        continueGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AlquerquePlay.class);
                intent.putExtra("continue","true");
                startActivity(intent);
            }
        });
        addPlayer = (Button) findViewById(R.id.add_player_btn);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AddPlayer.class);
                startActivity(intent);
            }
        });
        leagueView= (Button) findViewById(R.id.league_tbl_btn);
        leagueView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeagueTable.class);
                startActivity(intent);
            }
        });
        rules= (Button) findViewById(R.id.settings_btn);
        rules.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
    }
}
