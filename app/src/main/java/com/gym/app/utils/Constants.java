package com.gym.app.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Constants {
    int DISK_CACHE_SIZE = 10 * 1024;
    String ENDPOINT = "http://192.168.1.122/itec/";
    String COURSES_IMAGES_ENDPOINT = "http://54.70.10.6/uploads/course/";
    SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("EEEE, d.MM.yyyy", Locale.getDefault());

}
