package ru.andreyviktorov.mahjong;

import java.io.Serializable;

public class Layer implements Serializable {
    public TileActor[][] data;
    private int width;
    private int height;
    public Layer(int w, int h){
        data = new TileActor[w+1][h+1];
        width = w;
        height = h;
    }

    public void setAt(int x, int y, TileActor actor) {
        data[x][y] = actor;
    }

    public void addLine(int OffsetLeft, int OffsetTop, TileActor[] actors) {
        int i = 0;
        for (TileActor actor : actors) {
            data[OffsetLeft + i][OffsetTop] = actor;
            i += 2;
        }
    }
}
