package com.irene.pocketrhythmtrainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textEndOfExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_exercise);
        textEndOfExercise = (TextView) findViewById(R.id.text_score);
        textEndOfExercise.setText(R.string.text_intro);
    }

    public void repeat(View view) {
        Intent intent = new Intent(getApplicationContext(), PlayTempoActivity.class);
        startActivity(intent);
    }

    public void showScore(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowScoresActivity.class);
        startActivity(intent);
    }

    public void newExercise(View view) {
        Intent intent = new Intent(getApplicationContext(), TempoSettingsActivity.class);
        startActivity(intent);
    }
}
