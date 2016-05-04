package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PlayTempo extends Activity {
    private TextView textTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tempo);
        textTempo = (TextView) findViewById(R.id.text_tempo);
        String s = getText(R.string.tempo) + "  " + getIntent().getStringExtra("tempo");
        s += "\n" + getText(R.string.meter) + "  " + getIntent().getStringExtra("meter");
        textTempo.setText(s);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



    }

}
