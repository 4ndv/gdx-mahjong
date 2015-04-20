package ru.andreyviktorov.mahjong;

import java.io.Serializable;

public class Tile implements Serializable {
    public enum Suit {
        Pin, Bamboo, Man, WindEast, WindSouth, WindWest, WindNorth, DragonRed, DragonGreen, DragonWhite, Flower, Season
    }

    public Suit suit;
    public int number = 0;
}
