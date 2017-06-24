package com.example.seb.alquerquegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class RulesActivity extends AppCompatActivity {

    public static boolean activateRule1 = false;
    public static boolean activateRule2 = false;

    private CheckBox rule1;
    private CheckBox rule2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.rule1_check_box:
                if (checked){
                    activateRule1 =true;
                    System.out.println(activateRule1);
                }
                else{
                    activateRule1 =false;
                    System.out.println(activateRule1);
                }
                break;
            case R.id.rule2_check_box:
                if (checked) {
                    activateRule2 =true;
                    System.out.println(activateRule2);
                }
                else {
                    activateRule1 =false;
                    System.out.println(activateRule2);
                }
                break;
        }
    }
}