package com.nikita.doroshenko.japanmeeting.utils;

import com.nikita.doroshenko.japanmeeting.BuildConfig;

public interface Constants {

    String TEMI_SPEECH = "Good morning to all the staff\n" +
            "We are making a transition from routine to emergency.\n" +
            "Missile fire is expected in the coming hours.\n" +
            "Everyone should make sure that they know where the nearest protected space is.\n" +
            "When alarming, do not use the elevator.\n" +
            "You must enter the protected space within a minute.\n" +
            "Stay in the protected space for 10 minutes.";
    String userName = "יקי סלומון";
    String userId = "7738b80514a452cd3fe60f9d4c377d76";
    String POSTGRES_PASSWORD = BuildConfig.POSTGRES_PASSWORD;
    String POSTGRES_HOST = "192.168.73.227";  // use 10.0.2.2 for localhost, because 127 it for Android emulator.


}
