package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;

import java.util.Timer;
import java.util.TimerTask;

public class PlayTempo extends Activity implements SoundPool.OnLoadCompleteListener {
    private static final String TAG = "PlayTempo";

    private TextView textSettings; //shows the settings stated by the user in activity_tempo_settings
    private Button buttonTap; //detects the user tapping for the silent bars
    private Button buttonStart; //starts the exercise
    private SoundPool clickSoundPool; // class that loads the sound clips into a device’s memory
    private int tempo; //tempo stated by the user
    private int meter; //meter stated by the user
    private int duration; //duration stated by the user
    private int loud; //loud bars stated by the user
    private int silent; //silent bars stated by the user
    private int accentCounter; //counts the bars already played
    private int silentClickCounter; //counts the beats in silent bars time
    private boolean running; //true if the exercise is running and false if it is not
    private boolean play; //true for loud bars time and false for silent bars time
    private long[] clickTimes; //saves the time in milliseconds of each click for the silent bars time
    private long[] tappingTimes; //saves the time in milliseconds of each user tap for the silent bars time
    private Timer scheduler; //schedules the sounds playing tasks for execution in background threads
    private int length; //length of the arrays storing the time moments of the tapping and the click in silent periods
    private int t2; //time interval between bits

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tempo);
        textSettings = (TextView) findViewById(R.id.text_tempo);
        buttonTap = (Button) findViewById(R.id.buttonTap);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonTap = (Button) findViewById(R.id.buttonTap);
        accentCounter = 1;
        silentClickCounter = 0;
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

        textSettings.setText(getSettings());
        length = meter * ((silent * (duration / (loud + silent))) + (1 + (loud+silent) - duration%(loud+silent)));
        tappingTimes = new long[length];
        clickTimes = new long[length];

        buttonTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!play) && (silentClickCounter<=length)) {
                    tappingTimes[silentClickCounter-1] = System.currentTimeMillis();
                }
                if(running==false){
                    Toast.makeText(getApplicationContext(), "Press start!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t1;
                if (running == false) {
                    running = true;
                    buttonStart.setText(R.string.stop);
                    scheduler = new Timer();
                    //task1 is responsible of the strong beat, it also controls playing and muting the click
                    TimerTask task1 = new TimerTask() {
                        int loudCounter = 1; //counts the number of loud bars for each cycle
                        int silentBarsCounter = 1; //counts the number of silent bars for each cycle

                        @Override
                        public void run() {
                            if (loudCounter <= loud) {
                                clickSoundPool.play(click1Id, 1, 1, 1, 0, 1);
                                play = true;
                                loudCounter++;
                            } else {
                                play = false;
                                if (silentBarsCounter >= silent) {
                                    loudCounter = 1;
                                    silentBarsCounter = 1;
                                } else {
                                    silentBarsCounter++;
                                }
                            }
                            if (accentCounter > duration) {
                                stop(scheduler);
                            }
                            accentCounter++;
                        }
                    };
                    //task2 plays is responsible of the weak beat
                    TimerTask task2 = new TimerTask() {
                        @Override
                        public void run() {
                            if (play) {
                                clickSoundPool.play(click2Id, 1, 1, 0, 0, 1);
                            } else {
                                silentClickCounter++;
                                clickTimes[silentClickCounter-1] = System.currentTimeMillis();
                            }
                        }
                    };
                    t2 = 1000 * 60 / tempo;
                    t1 = meter * t2;
                    scheduler.scheduleAtFixedRate(task1, 1000, t1);
                    scheduler.scheduleAtFixedRate(task2, 1000, t2);
                } else {
                    stop(scheduler);
                }
            }
        });
    }

    //gets the settings stated by the user in activity_tempo_settings and returns a String with them
    private String getSettings() {
        tempo = Integer.parseInt(getIntent().getStringExtra("tempo"));
        meter = Integer.parseInt(getIntent().getStringExtra("meter"));
        duration = Integer.parseInt(getIntent().getStringExtra("duration"));
        loud = Integer.parseInt(getIntent().getStringExtra("loud"));
        silent = Integer.parseInt(getIntent().getStringExtra("silent"));

        String s = getText(R.string.tempo) + "  " + tempo;
        s += "\n" + getText(R.string.meter) + "  " + meter;
        s += "\n" + getText(R.string.text_duration) + " " + duration;
        s += "\n" + getText(R.string.text_loud) + " " + loud;
        s += "\n" + getText(R.string.text_silent) + " " + silent;
        return s;
    }

    //stops the scheduler and  sets the control variables to the start values
    public void stop(Timer scheduler) {
        scheduler.cancel();
        scheduler.purge();
        running = false;
        accentCounter = 0;
        //Only the original thread that created a view hierarchy can touch its views. So we need this change the buttonStart text
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonStart.setText(R.string.start);
                Toast.makeText(getApplicationContext(), "End of exercise!", Toast.LENGTH_SHORT).show();
            }
        });
        calculateScore();
    }

    private void calculateScore() {
        String s = new String();
        long difference;
        long[] result = new long[length];
        s += "The time moments for the clicks and taps are: \n";
        for(int i = 0; i < length; i++){
            s += "Click:" + i + "  time: " + clickTimes[i] + "\n";
            s += "Tap:" + i + "  time: " + tappingTimes[i] + "\n";
            difference = tappingTimes [i] == 0 ? 0 : (t2 - (clickTimes[i] - tappingTimes[i]));
            difference = difference > 0 ? difference : -difference;
            result[i] = 100*difference/t2;
        }
        Log.e(TAG, s);
        for (int i = 0; i<length; i++){
            Log.e(TAG, "Result for i = " + i +" is " + Long.valueOf(result[i]) + "\n");
        }
    }

    //method used to create the soundPool
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
            switch (sampleId) {
                case 2:
                    //Toast.makeText(getApplicationContext(), "Sounds loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
