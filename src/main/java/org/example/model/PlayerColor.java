package org.example.model;

import java.util.Random;

public enum PlayerColor {
    RED, GREEN, BLUE, BLACK;

    // created random var
    private static final Random RANDOM = new Random();


    // will choose color randomly for us
    public static PlayerColor random(){
        PlayerColor[] values = values();
        return values[RANDOM.nextInt(values.length)];
    }
}
