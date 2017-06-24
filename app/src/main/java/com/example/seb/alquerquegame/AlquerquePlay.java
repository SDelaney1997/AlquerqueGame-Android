package com.example.seb.alquerquegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class AlquerquePlay extends Activity {
    private String file= "boardd";
    private TextView p1;
    private TextView p2;
    private int[] board;
    private int[] boardDefault= {1, 1, 1, 1, 1,
                          1, 1, 1, 1, 1,
                          1, 1, 0, 2, 2,
                          2, 2, 2, 2, 2,
                          2, 2, 2, 2, 2};
    private ImageAdapter boardAdapter;
    private boolean multiCapFound= false;
    private boolean mustCap= false;
    private RulesActivity rules;
    private String player1;
    private String player2;
    ///////////////////////////////////////////////////
    public boolean noBackwardsRule= false;
    public boolean manditoryTake= false;
    ///////////////////////////////////////////////////
    public int toTake;

    private GridView boardGrid;
    private int position1;
    private int player= 1;
    private int moveNum = 1;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alquerque_play);
        p1= (TextView) findViewById(R.id.p1_tv);
        p2= (TextView) findViewById(R.id.p2_tv);
        p1.setText(getIntent().getStringExtra("player1"));
        p2.setText(getIntent().getStringExtra("player2"));
        if(loadBoard()!=null && getIntent().getStringExtra("continue").equals("true")){
            board=loadBoard();
        }
        else{
            clearBoard();
            board= boardDefault;
        }
        boardGrid= (GridView) findViewById(R.id.alquerque_grid);

        boardAdapter= new ImageAdapter(this,board);
        boardGrid.setAdapter(boardAdapter);

        decideRules();

        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position, long id){

                if(moveNum == 1){
                    if(player==board[position]){
                        position1 = position;
                        moveNum = 2;
                    }
                    else if(board[position]==0){
                        Toast.makeText(getApplicationContext(),"Position is Empty",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Not Your Piece",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if(position1 == position) {
                        moveNum= 1;
                        if(multiCapFound){
                            multiCapFound=false;
                            Toast.makeText(getApplicationContext(),"Turn Ended",Toast.LENGTH_SHORT).show();
                            if(player==1){
                                player=2;
                            }
                            else{
                                player=1;
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Position Unchanged",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(moveValidate(position1,position)){
                        if(!multiCapFound) {
                            moveNum = 1;
                            if (player == 1) {
                                player = 2;
                            } else {
                                player = 1;
                            }
                        }
                        else if(multiCapFound){
                            position1= position;
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid Move",Toast.LENGTH_SHORT).show();
                        if(!multiCapFound){
                            moveNum=1;
                        }
                    }
                }
                isGameWon();
                boardAdapter.notifyDataSetChanged();
            }
        });
    }

    public boolean moveValidate(int position1, int position2){
        saveBoard();
        if(moveAllowed(position1,position2)){
            board[position2]=board[position1];
            board[position1]=0;
            board[toTake]=0;
            multiCapFound= false;
            if(!radiusCheck(position1).contains(position2)){
                checkForMultiCap(position2);
            }
            return true;
        }
        return false;
    }

    public boolean moveAllowed(int position, int position2){
        ArrayList<Integer> radius= radiusCheck(position);
        for(int i=0; i<radius.size(); i++){
            if(board[radius.get(i)]==board[position]){
            }
            else if(position2 == radius.get(i) && board[radius.get(i)]==0 && !multiCapFound){
                if(!noBackwardsRule && !manditoryTake){
                    toTake= position;

                    return true;
                }
                else if ((board[position]==1 && position2 >= position-1) ||
                        (board[position]==2 && position2 <= position+1)){
                    toTake= position;

                    return true;
                }
            }
            else if (board[radius.get(i)]!=0){
                ArrayList<Integer> secondRadius= radiusCheck(radius.get(i));
                for(int x=0; x<secondRadius.size(); x++){
                    if(board[secondRadius.get(x)]==0){
                        if((secondRadius.get(x)%5 == position%5 && secondRadius.get(x)%5 == radius.get(i)%5)
                                && position2==secondRadius.get(x)){
                            if(!noBackwardsRule && !manditoryTake){
                                toTake= radius.get(i);
                                return true;
                            }
                            else if ((board[position]== 1 && secondRadius.get(x)/5 > position/5)
                                    || (board[position]== 2 && secondRadius.get(x)/5 < position/5)){
                                toTake= radius.get(i);
                                return true;
                            }
                        }
                        else if((secondRadius.get(x)/5 == position/5 && secondRadius.get(x)/5== radius.get(i)/5)
                                && position2==secondRadius.get(x)){
                            toTake= radius.get(i);

                            return true;
                        }
                        else if((secondRadius.get(x)-6==position+6 || secondRadius.get(x)+6==position-6)
                                && position2==secondRadius.get(x)){
                            if(!noBackwardsRule && !manditoryTake){
                                toTake= radius.get(i);
                                return true;
                            }
                            else if((board[position]== 1 && secondRadius.get(x)-6 == position+6)
                                    ||(board[position]== 2 &&secondRadius.get(x)+6 == position -6)){
                                toTake= radius.get(i);
                                return true;
                            }

                        }
                        else if((secondRadius.get(x)-4==position+4 || secondRadius.get(x)+4==position-4)
                                && position2==secondRadius.get(x)){
                            if(!noBackwardsRule && !manditoryTake) {
                                toTake= radius.get(i);
                                return true;
                            }
                            else if((board[position]== 1 && secondRadius.get(x)-4 == position+4)
                                    ||(board[position]==2 && secondRadius.get(x)+4 == position-4)){
                                toTake= radius.get(i);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkForMultiCap(int position){
        ArrayList<Integer> radius= radiusCheck(position);
        for(int i=0;i<radius.size();i++){
            if(board[radius.get(i)]!=0 && board[radius.get(i)] != board[position]){
                ArrayList<Integer> secondRadius= radiusCheck(radius.get(i));
                for(int x= 0;x<secondRadius.size();x++){
                    if((board[secondRadius.get(x)]==0 && !radius.contains(secondRadius.get(x)))
                    && moveAllowed(position,secondRadius.get(x))){
                        multiCapFound= true;
                        return true;
                    }
                }
            }
        }
        multiCapFound= false;
        return false;
    }

    public int getCell(int x, int y){
        return x + (y * 5);
    }

    public ArrayList<Integer> radiusCheck(int position){
        ArrayList<Integer> radius= new ArrayList<Integer>();

        for(int x= (position % 5)-1; x<(position % 5)+2; x++){
            for(int y= (position / 5)-1; y<(position / 5)+2; y++){
                if(!(x < 0 || y < 0)){
                    if(!(x > 4 || y > 4)){
                        radius.add(getCell(x,y));
                    }
                }
            }
        }
        return radius;
    }

    public boolean isGameWon(){
        ArrayList<Integer> testBoard= new ArrayList<>();
        for(int i=0; i<board.length;i++){
            testBoard.add(board[i]);
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                switch (option){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(AlquerquePlay.this, AlquerquePlay.class);
                        finish();
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent intent2 = new Intent(AlquerquePlay.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finish();
                        break;
                }
            }
        };

        if(!testBoard.contains(1)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Start new game?").setTitle("White wins!")
                    .setNegativeButton("No", dialogClickListener)
                    .setPositiveButton("Yes", dialogClickListener).show();
            return true;
        }
        else if(!testBoard.contains(2)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Start new game?").setTitle("Black wins!")
                    .setNegativeButton("No", dialogClickListener)
                    .setPositiveButton("Yes", dialogClickListener).show();
            return true;
        }
        else{
            return false;
        }
    }

    public void decideRules(){
        if(rules.activateRule1==true){
            noBackwardsRule= true;
            System.out.println(noBackwardsRule);
        }
        else{
            noBackwardsRule= false;
            System.out.println(noBackwardsRule);
        }
        if(rules.activateRule2==true){
            manditoryTake= true;
            System.out.println(manditoryTake);
        }
        else{
            manditoryTake= false;
            System.out.println(manditoryTake);
        }
    }

    public void saveBoard(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson= new Gson();
        String json = gson.toJson(board);

        editor.putString(file, json);
        editor.commit();
    }

    public int[] loadBoard(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson= new Gson();
        String json = sharedPreferences.getString(file, null);

        Type type = new TypeToken<int[]>(){}.getType();
        int[] loadedBoard = gson.fromJson(json,type);

        return loadedBoard;
    }

    public void clearBoard(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
