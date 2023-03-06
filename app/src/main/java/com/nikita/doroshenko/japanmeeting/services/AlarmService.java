package com.nikita.doroshenko.japanmeeting.services;

import android.util.Log;

public class AlarmService extends Thread {

    @Override
    public void run() {
        while (true) {
            Log.i("AlarmService", "Is interrupted " + isInterrupted());
            if (isInterrupted()) {
                Log.i("AlarmService", "Alarm finished");
                return;
            }
            try {
                Thread.sleep(Long.parseLong("5000"));
            } catch (InterruptedException e) {
                Log.i("AlarmService", "Alarm finished");
                return;
            }
        }
    }
}
