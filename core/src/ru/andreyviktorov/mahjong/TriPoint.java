package ru.andreyviktorov.mahjong;

/*
    Класс Point с тремя измерениями
 */

import java.io.Serializable;

public class TriPoint implements Serializable {
    public int x;
    public int y;
    public int z;

    public TriPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
