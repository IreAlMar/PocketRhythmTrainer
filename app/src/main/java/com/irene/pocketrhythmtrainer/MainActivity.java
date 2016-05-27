package com.irene.pocketrhythmtrainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textInfo;
    private Button buttonShowInfo;
    private Button buttonRepeatExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInfo = (TextView) findViewById(R.id.text_info_tempo_trainer);
        buttonShowInfo = (Button) findViewById(R.id.buttonShowInfo);
        buttonRepeatExercise = (Button) findViewById(R.id.buttonRepeatExercise);

        textInfo.setVisibility(View.INVISIBLE);
        if(PlayTempoActivity.isFirstRound){
            buttonRepeatExercise.setVisibility(View.INVISIBLE);
        }
    }

    public void repeat(View view) {
        finish();
    }

    public void showScore(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowScoresActivity.class);
        startActivity(intent);
    }

    public void newExercise(View view) {
        Intent intent = new Intent(getApplicationContext(), TempoSettingsActivity.class);
        startActivity(intent);
    }

    public void showInfo(View view) {
        if(textInfo.getVisibility() == View.VISIBLE) {
            textInfo.setVisibility(View.INVISIBLE);
            buttonShowInfo.setText(R.string.what_is_tempo_trainer);
        }else{
            textInfo.setVisibility(View.VISIBLE);
            buttonShowInfo.setText(R.string.hide_information);
        }
    }
}
