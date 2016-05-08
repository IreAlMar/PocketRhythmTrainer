package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;

public class PlayTempo extends Activity implements SoundPool.OnLoadCompleteListener {
    private TextView textTempo;
    private Button buttonTap;
    private Button buttonStart;
    private SoundPool clickSoundPool;
    private int tempo;
    private int meter;
    private int duration;
    private int loud;
    private int silent;
    private java.util.Timer scheduler;
    private int t1;
    private int t2;
    private int counter;
    private boolean running;
    private boolean play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tempo);
        textTempo = (TextView) findViewById(R.id.text_tempo);
        buttonTap = (Button) findViewById(R.id.buttonTap);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        counter = 1;
        running = false;
        play = true;

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        clickSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).build();
        createNewSoundPool();
        clickSoundPool.setOnLoadCompleteListener(this);
        final int click1Id = clickSoundPool.load(this, R.raw.beep08b, 1);
        final int click2Id = clickSoundPool.load(this, R.raw.beep07, 1);

        tempo = Integer.parseInt(getIntent().getStringExtra("tempo"));
        meter = Integer.parseInt(getIntent().getStringExtra("meter"));
        duration = Integer.parseInt(getIntent().getStringExtra("duration"));
        loud = Integer.parseInt(getIntent().getStringExtra("loud"));
        silent = Integer.parseInt(getIntent().getStringExtra("silent"));

        String s = getText(R.string.tempo) + "  " + tempo;
        s += "\n" + getText(R.string.meter) + "  " + meter;
        s += "\n" + getText(R.string.text_duration) + " " + duration;
        s += "\n" + getText(R.string.text_loud) + " "+ loud;
        s += "\n" + getText(R.string.text_silent) + " "+ silent;
        textTempo.setText(s);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running == false){
                    running = true;
                    buttonStart.setText(R.string.stop);
                    scheduler = new java.util.Timer();
                    TimerTask task1 = new TimerTask() {
                        @Override
                        public void run() {
                            int t = loud + silent;
                            if(loud <= counter-(t)*(counter/(t))){
                                play = false;
                            }else{
                                clickSoundPool.play(click1Id, 1, 1, 1, 0, 1);
                                play = true;
                            }
                            if (counter > duration){
                                stop();//preguntar por qué se queda pillado en buttonStart.setText(R.string.start);
                            }
                            counter ++;
                        }
                    };
                    TimerTask task2 = new TimerTask() {
                        @Override
                        public void run() {
                            if (play){
                                clickSoundPool.play(click2Id, 1, 1, 0, 0, 1);
                            }
                        }
                    };
                    t2 = 1000 * 60 / tempo;
                    t1 =  meter * t2;
                    scheduler.scheduleAtFixedRate(task1, 1000, t1);
                    scheduler.scheduleAtFixedRate(task2, 1000, t2);
                }else{
                    stop();
                }
            }
        });
    }

    public void stop(){
        scheduler.cancel();
        counter = 0;
        running = false;
        buttonStart.setText(R.string.start);
    }
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        clickSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            switch (sampleId){
                case 2:
                    Toast.makeText(getApplicationContext(), "Sounds loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
