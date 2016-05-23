package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SaveRoundActivity extends Activity {
    private static final String TAG = SaveRoundActivity.class.getSimpleName();
    public static final String SCORE = "score";
    public static final String GAME = "nameG";

    Long score;
    String game;
    String player;

    EditText editTextPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_round);
        TextView textSave = (TextView) findViewById(R.id.text_save);
        editTextPlayer = (EditText) findViewById(R.id.player_name);

        score = Long.parseLong(getIntent().getStringExtra(SCORE));
        game = getIntent().getStringExtra(GAME);

        textSave.setText(String.format("Your score is  %d%% \n%s", score, getText(R.string.save_message)));
    }

    public void save(View view){
        Log.d(TAG, "Saving in database");

        player = editTextPlayer.getText().toString();

        if(player.isEmpty()){
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            return;
        }
        Round round = new Round(player, game, score);
        round.save();
        showEndOfExercise();
    }

    public void cancel(View view){
        showEndOfExercise();
    }

    private void showEndOfExercise(){
        Intent intent = new Intent(getApplicationContext(), EndOfExerciseActivity.class);
        startActivity(intent);
        finish();
    }
}
