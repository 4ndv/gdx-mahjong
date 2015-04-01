package ru.andreyviktorov.mahjong;

public class Tile {
    public enum Suit {
        Pin, Bamboo, Man, WindEast, WindSouth, WindWest, WindNorth, DragonRed, DragonGreen, DragonWhite, Flower, Season
    }

    public Suit suit;
    public int number = 0;
}
