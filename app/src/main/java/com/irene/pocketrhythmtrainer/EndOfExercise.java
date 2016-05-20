package com.irene.pocketrhythmtrainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EndOfExercise extends AppCompatActivity {
    private TextView textEndOfExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_exercise);
        textEndOfExercise = (TextView) findViewById(R.id.text_score);
        textEndOfExercise.setText("End of exercise!\n" + "Your score is: " + getIntent().getStringExtra("score") +"\n");
    }

    public void repeat(View view){
        //Así no vale, porque depende de TempoSettings
        finish();
    }

    public void showScore(View view){
      //TODO
    }

    public void newExercise(View view){
        Intent intent = new Intent(getApplicationContext(), TempoSettings.class);
        startActivity(intent);
    }
}
