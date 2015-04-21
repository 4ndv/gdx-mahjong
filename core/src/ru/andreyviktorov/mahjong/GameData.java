package ru.andreyviktorov.mahjong;

import java.io.Serializable;

public class GameData implements Serializable {
    public Field field;
    // Это поле тоже не надо сериализовать
    public transient TileActor selected;
    public int remainingTiles;
    public boolean declined = false;
}
