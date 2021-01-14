package com.example.clock.utils;

import java.util.UUID;

public class RandomID {
    public static int generate() {
        long millis = System.currentTimeMillis();
        int id = (int) millis;
        return id;
    }
}
