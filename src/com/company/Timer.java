package com.company;

public class Timer {
    //seconds
    float start;
    float end;
    public static float getInterval(Timer timer){
        float duration = (timer.end - timer.start)/1000000000;
        return duration;
    }
}
