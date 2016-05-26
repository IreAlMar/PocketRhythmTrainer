package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TempoSettingsActivity extends Activity {

    public static final String STATE_TEMPO = "tempo";
    public static final String STATE_METER = "meter";
    public static final String STATE_DURATION = "duration";
    public static final String STATE_LOUD = "loud";
    public static final String STATE_SILENT = "silent";
    public static final String STATE_ISFIRST = "isFirst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo_settings);
        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTempo = (EditText) findViewById(R.id.value_tempo);
                EditText editTextMeter = (EditText) findViewById(R.id.value_meter);
                EditText editTextDuration = (EditText) findViewById(R.id.value_duration);
                EditText editTextLoud = (EditText) findViewById(R.id.value_loud);
                EditText editTextSilent = (EditText) findViewById(R.id.value_silent);

                Intent intent = new Intent(getApplicationContext(), PlayTempoActivity.class);
                intent.putExtra(STATE_TEMPO, editTextTempo.getText().toString());
                intent.putExtra(STATE_METER, editTextMeter.getText().toString());
                intent.putExtra(STATE_DURATION, editTextDuration.getText().toString());
                intent.putExtra(STATE_LOUD, editTextLoud.getText().toString());
                intent.putExtra(STATE_SILENT, editTextSilent.getText().toString());
                startActivity(intent);
            }
        });
    }
    public void showHelp(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(R.string.help);
        dialog.setMessage(R.string.how_tempo_trainer);

        dialog.setNegativeButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close dialog
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

}
