package com.example.seb.alquerquegame;

public class Player {
    private String name, description;
    private int score;

    public Player(String name, String description, int score){
        this.name= name;
        this.description= description;
        this.score= score;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getScore(){
        return score;
    }

    public void addScore(){
        score+=1;
    }

    @Override
    public String toString() {
        return "Name: "+ name +  "\nScore: "+score;
    }

}
