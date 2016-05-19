package com.irene.pocketrhythmtrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private void repeat(){

    }

    private void showScore(){
      //TODO
    }

    private void newExercise(){
       //TODO
    }
}
