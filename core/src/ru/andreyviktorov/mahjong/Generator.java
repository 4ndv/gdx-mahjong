package ru.andreyviktorov.mahjong;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

public class Generator {
    private LinkedList<TileActor> tiles;

    public enum Figure {
        Pyramid
    }

    public Generator(LinkedList<TileActor> list) {
        this.tiles = list;
        Collections.shuffle(this.tiles);
    }

    public Generator() {
        tiles = new LinkedList();
        for (Tile.Suit suit : Tile.Suit.values()) {
            // Создаем тайлы для основных мастей
            if(suit == Tile.Suit.Pin || suit == Tile.Suit.Bamboo || suit == Tile.Suit.Man) {
                for(int i = 1; i<10; i++) {
                    for(int j = 0; j<4; j++) {
                        Tile t = new Tile();
                        t.number = i;
                        t.suit = suit;
                        TileData td = new TileData();
                        TileActor ta = new TileActor(t, td);
                        tiles.add(ta);
                    }
                }
            // Создаем тайлы для цветов и времен года
            } else if(suit == Tile.Suit.Flower || suit == Tile.Suit.Season) {
                for(int i = 1; i<5; i++) {
                    Tile t = new Tile();
                    t.number = i;
                    t.suit = suit;
                    TileData td = new TileData();
                    TileActor ta = new TileActor(t, td);
                    tiles.add(ta);
                }
            // Создаем тайлы для драконов и ветров
            } else {
                for(int i = 1; i<5; i++) {
                    Tile t = new Tile();
                    t.suit = suit;
                    TileData td = new TileData();
                    TileActor ta = new TileActor(t, td);
                    tiles.add(ta);
                }
            }
        }

        Collections.shuffle(tiles);
    }

    public TileActor[] getNTileActors(int n) {
        TileActor[] ta = new TileActor[n+1];
        if(n>tiles.size()) {
            n = tiles.size();
        }
        for (int i = 0; i<n; i++) {
            ta[i] = tiles.poll();
        }
        return ta;
    }

    public TileActor getOneTileActor() {
        if(tiles.size() < 0) {
            return tiles.poll();
        } else {
            return null;
        }
    }

    public static int getFigureHeight(Figure fig) {
        int ret = 0;

        switch (fig) {
            case Pyramid:
                ret = 16;
                break;
            default:
                ret = 0;
                break;
        }

        return ret;
    }

    public Field generateFigure(Figure fig) {
        Field field = new Field(1,1);
        switch (fig) {
            case Pyramid:
                field = new Field(30, 16);
                Layer first = field.forkLayer();
                // СНИЗУ ВВЕРХ!!!11!111!адин
                first.addLine(2, 0, getNTileActors(12));
                first.addLine(6, 2, getNTileActors(8));
                first.addLine(4, 4, getNTileActors(10));
                first.addLine(2, 6, getNTileActors(12));
                first.addLine(2, 8, getNTileActors(12));
                first.addLine(4, 10, getNTileActors(10));
                first.addLine(6, 12, getNTileActors(8));
                first.addLine(2, 14, getNTileActors(12));
                first.setAt(0, 7, getOneTileActor());
                first.setAt(26, 7, getOneTileActor());
                first.setAt(28, 7, getOneTileActor());
                field.addLayer(first);
                Layer second = field.forkLayer();
                second.addLine(8, 2, getNTileActors(6));
                second.addLine(8, 4, getNTileActors(6));
                second.addLine(8, 6, getNTileActors(6));
                second.addLine(8, 8, getNTileActors(6));
                second.addLine(8, 10, getNTileActors(6));
                second.addLine(8, 12, getNTileActors(6));
                field.addLayer(second);
                Layer third = field.forkLayer();
                third.addLine(10, 4, getNTileActors(4));
                third.addLine(10, 6, getNTileActors(4));
                third.addLine(10, 8, getNTileActors(4));
                third.addLine(10, 10, getNTileActors(4));
                field.addLayer(third);
                Layer fourth = field.forkLayer();
                fourth.addLine(12, 6, getNTileActors(2));
                fourth.addLine(12, 8, getNTileActors(2));
                field.addLayer(fourth);
                Layer fifth = field.forkLayer();
                fifth.setAt(13, 7, getOneTileActor());
                field.addLayer(fifth);
                break;
        }
        return field;
    }
}
