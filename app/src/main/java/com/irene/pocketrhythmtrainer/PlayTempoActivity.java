package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlayTempoActivity extends Activity implements SoundPool.OnLoadCompleteListener {
    private static final String TAG = PlayTempoActivity.class.getSimpleName();

    private Button buttonStart; //starts the exercise
    private SoundPool clickSoundPool; // class that loads the sound clips into a device’s memory
    private int tempo; //tempo stated by the user
    private int meter; //meter stated by the user number of clicks per bar
    private int duration; //duration stated by the user
    private int loud; //loud bars stated by the user
    private int silent; //silent bars stated by the user
    private int durationCounter; //counts the bars already played
    private int silentClickCounter; //counts the beats in silent bars time
    private boolean running; //true if the exercise is running and false if it is not
    private boolean play; //true for loud bars time and false for silent bars time
    private long[] clickTimes; //saves the time in milliseconds of each click for the silent bars time
    private long[] tappingTimes; //saves the time in milliseconds of each user tap for the silent bars time
    private Timer scheduler; //schedules the sounds playing tasks for execution in background threads
    private int length; //length of the arrays storing the time moments of the tapping and the click in silent periods
    private int timeIntervalBetweenBits;

    private List<TimeSlot> slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tempo);

        TextView textSettings = (TextView) findViewById(R.id.text_tempo);
        Button buttonTap = (Button) findViewById(R.id.buttonTap);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        slots = new ArrayList<>();

        durationCounter = 0;
        running = false;
        play = true;

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            setSettings();
        }
        textSettings.setText(getSettingsText());

        createNewSoundPool();
        final int click1Id = clickSoundPool.load(this, R.raw.beep08b, 1);
        final int click2Id = clickSoundPool.load(this, R.raw.beep07, 1);

        length = calculateLength();
        tappingTimes = new long[length];
        clickTimes = new long[length];

        buttonTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    Toast.makeText(getApplicationContext(), R.string.press_start, Toast.LENGTH_SHORT).show();
                } else {
                    if ((!play) && (silentClickCounter < length) && (silentClickCounter > -1)) {
                        tappingTimes[silentClickCounter] = System.currentTimeMillis();
                    }
                }
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int timeIntervalBetweenBars;
                if (!running) {
                    running = true;
                    silentClickCounter = -1;
                    buttonStart.setText(R.string.stop);
                    scheduler = new Timer();
                    //task1 is responsible of the strong beat, it also controls playing and muting the click
                    TimerTask task1 = new TimerTask() {
                        int loudCounter = 0; //counts the number of loud bars for each cycle
                        int silentBarsCounter = 1; //counts the number of silent bars for each cycle

                        @Override
                        public void run() {
                            if (durationCounter >= duration) {
                                stop(scheduler);
                                //Calculate score and show end of exercise screen
                                endOfExercise(calculateScore());
                                //Save the score in the database
                                //TODO Lanzar dialog en la que se pregunta el nombre
                                //y se guardan los puntos
                            } else {
                                if (loudCounter < loud) {
                                    clickSoundPool.play(click1Id, 1, 1, 1, 0, 1);
                                    play = true;
                                    loudCounter++;
                                } else {
                                    play = false;
                                    if (silentBarsCounter >= silent) {
                                        loudCounter = 0;
                                        silentBarsCounter = 1;
                                    } else {
                                        silentBarsCounter++;
                                    }
                                }
                                durationCounter++;
                            }
                        }
                    };
                    //task2 plays is responsible of the weak beat
                    TimerTask task2 = new TimerTask() {
                        @Override
                        public void run() {
                            if (play) {
                                clickSoundPool.play(click2Id, 1, 1, 0, 0, 1);
                            } else if (silentClickCounter < length - 1) {
                                //clickSoundPool.play(click1Id, 1, 1, 0, 0, 1); //trampa
                                silentClickCounter++;
                                clickTimes[silentClickCounter] = System.currentTimeMillis();
                            }
                        }
                    };
                    timeIntervalBetweenBits = 1000 * 60 / tempo;
                    timeIntervalBetweenBars = meter * timeIntervalBetweenBits;
                    scheduler.scheduleAtFixedRate(task1, 1000, timeIntervalBetweenBars);
                    scheduler.scheduleAtFixedRate(task2, 1000, timeIntervalBetweenBits);
                } else {
                    stop(scheduler);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back pressed");
        stop(scheduler);
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(TempoSettings.STATE_TEMPO, tempo);
        savedInstanceState.putInt(TempoSettings.STATE_METER, meter);
        savedInstanceState.putInt(TempoSettings.STATE_DURATION, duration);
        savedInstanceState.putInt(TempoSettings.STATE_LOUD, loud);
        savedInstanceState.putInt(TempoSettings.STATE_SILENT, silent);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tempo = savedInstanceState.getInt(TempoSettings.STATE_TEMPO);
        meter = savedInstanceState.getInt(TempoSettings.STATE_METER);
        duration = savedInstanceState.getInt(TempoSettings.STATE_DURATION);
        loud = savedInstanceState.getInt(TempoSettings.STATE_LOUD);
        silent = savedInstanceState.getInt(TempoSettings.STATE_SILENT);
    }
    /*@Override
    protected void onStop() {
    //Parar sonido
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("tempo", Integer.toString(tempo));
        editor.putString("meter", Integer.toString(meter));
        editor.putString("duration", Integer.toString(duration));
        editor.putString("loud", Integer.toString(loud));
        editor.putString("silent", Integer.toString(silent));
        editor.apply();
        super.onStop();
    }*/

    //Starts the EndOfExercise activity.
    private void endOfExercise(long score) {
        //show end of exercise options
        Intent intent = new Intent(getApplicationContext(), EndOfExercise.class);
        intent.putExtra("score", Long.toString(score));
        startActivity(intent);
    }

    //Calculates the length for the arrays containing the time instants
    private int calculateLength() {
        int result;
        int cycle = loud + silent;
        int modulo = duration % cycle; //speare bars
        result = meter * silent * (duration / cycle);
        if (modulo > loud) {
            result += meter * (modulo - loud);
        }
        return result;
    }

    //sets the settings stated by the user in activity_tempo_settings
    private void setSettings() {
        tempo = Integer.parseInt(getIntent().getStringExtra(TempoSettings.STATE_TEMPO));
        meter = Integer.parseInt(getIntent().getStringExtra(TempoSettings.STATE_METER));
        duration = Integer.parseInt(getIntent().getStringExtra(TempoSettings.STATE_DURATION));
        loud = Integer.parseInt(getIntent().getStringExtra(TempoSettings.STATE_LOUD));
        silent = Integer.parseInt(getIntent().getStringExtra(TempoSettings.STATE_SILENT));
    }

    //Returns a String with the values por the textSettings TextView
    private String getSettingsText() {
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
        durationCounter = 0;
        //Only the original thread that created a view hierarchy can touch its views. So we need this change the buttonStart text
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonStart.setText(R.string.start);
                Toast.makeText(getApplicationContext(), "End of exercise!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private long calculateScore() {
        //TODO arreglar la matematica
        String s = "";
        long score = 0;
        long at, aux;
        long difference;
        long[] result = new long[length];
        s += "timeIntervalBetweenBits = " + timeIntervalBetweenBits;
        s += "The time instant for the clicks and taps are: \n";
        for (int i = 0; i < length; i++) {
            at = clickTimes[i] - tappingTimes[i];
            at = at > 0 ? at : -at;
            difference = tappingTimes[i] == 0 ? 0 : (timeIntervalBetweenBits - at);
            difference = difference > 0 ? difference : -difference;
            result[i] = 100 * difference / timeIntervalBetweenBits;
            /*s += "i = " + i + " (clickTimes[i] - tappingTimes[i])= " + (clickTimes[i] - tappingTimes[i])+ "\n";
            s += "Click:" + i + "  time: " + clickTimes[i] + "\n";
            s += "Tap:" + i + "  time: " + tappingTimes[i] + "\n";
            s += "index i:" + i + "  result: " + result[i] + "\n";*/
            score += result[i];
        }
        /*for (int i = 1; i<length; i++){
            aux = (clickTimes[i] - clickTimes[i-1]);
            s += "Diferencia entre clickTimes" +i+ " : " + aux + "\n";
        }*/
        //Log.e(TAG, s);
        score = score / length;
        Log.e(TAG, "Final score: " + score + "% \n");
        return score;

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
        clickSoundPool.setOnLoadCompleteListener(this);
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