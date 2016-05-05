package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TempoSettings extends Activity {
    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo_settings);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PlayTempo.class);
                EditText editTextTempo = (EditText) findViewById(R.id.value_tempo);
                intent.putExtra("tempo", editTextTempo.getText().toString());
                EditText editTextMeter = (EditText) findViewById(R.id.value_meter);
                intent.putExtra("meter", editTextMeter.getText().toString());
                EditText editTextDuration = (EditText) findViewById(R.id.value_duration);
                intent.putExtra("duration", editTextDuration.getText().toString());
                startActivity(intent);
            }
        });
    }
}
