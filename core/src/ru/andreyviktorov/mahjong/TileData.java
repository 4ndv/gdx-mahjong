package ru.andreyviktorov.mahjong;

/**
 * Created by andv on 31.03.15.
 */
public class TileData {
    public float x;
    public float y;
    public int datax;
    public int datay;
    public int layer;
    public int randomId;

    public TileData() {
        this.randomId = (int)Math.floor((Math.random() * 99999999) + 10000000);
    }
}
