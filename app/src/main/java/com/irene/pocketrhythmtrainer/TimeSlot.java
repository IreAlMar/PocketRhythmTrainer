package com.irene.pocketrhythmtrainer;

/**
 * Created by Irene on 21/05/2016.
 */
public class TimeSlot {
    private long click;
    private long tapping;

    public TimeSlot() {
        this.click = 0;
        this.tapping = 0;
    }

    public long getClick() {
        return click;
    }

    public long getTapping() {
        return tapping;
    }

    public void setClick(long click) {
        this.click = click;
    }

    public void setTapping(long tapping) {
        this.tapping = tapping;
    }
}
