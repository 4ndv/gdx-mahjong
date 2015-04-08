package ru.andreyviktorov.mahjong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Field {
    private int width;
    private int height;
    public Figure figure;
    public List<Layer> layers = new ArrayList();
    private LinkedList<TileActor> tiles;
    public Field(int w, int h){
        width = w;
        height = h;
    }

    public Field() {
        this.width = 30;
        this.height = 16;
        this.figure = Figure.Pyramid;
        this.generateTiles();
        this.generateFigure(this.figure);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public Layer forkLayer() {
        return new Layer(this.width, this.height);
    }

    public boolean canRemove(int layer, int x, int y) {
        TileActor[][] d = layers.get(layer).data;
        if(d[x][y] == null) {
            System.out.println("Wrong xy passed to canRemove!");
            return false;
        } else {
            boolean leftblock = false;
            boolean rightblock = false;
            boolean topblock = false;

            // Высчитываем, не заблокирован ли сверху
            if(layer < layers.size() - 1) {
                TileActor[][] dtop = layers.get(layer+1).data;

                if(x>0 && y>0 && dtop[x-1][y-1] != null) {
                    topblock = true;
                }

                if(x>0 && dtop[x-1][y] != null) {
                    topblock = true;
                }

                if(x>0 && y<this.getHeight() && dtop[x-1][y+1] != null) {
                    topblock = true;
                }

                if(dtop[x][y] != null) {
                    topblock = true;
                }

                if(y>0 && dtop[x][y-1] != null) {
                    topblock = true;
                }

                if(y<this.getHeight() && dtop[x][y+1] != null) {
                    topblock = true;
                }

                if(x<this.getWidth() && y>0 && dtop[x+1][y-1] != null) {
                    topblock = true;
                }

                if(x<this.getWidth() && dtop[x+1][y] != null) {
                    topblock = true;
                }

                if(x<this.getWidth() && y<this.getHeight() && dtop[x+1][y+1] != null) {
                    topblock = true;
                }
            }

            // Высчитываем, не заблокирован ли слева
            if(x>2) {
                if(y>0 && d[x-2][y-1] != null) {
                    leftblock = true;
                }

                if(d[x-2][y] != null) {
                    leftblock = true;
                }

                if(y<this.getHeight() && d[x-2][y+1] != null) {
                    leftblock = true;
                }
            }

            // Высчитываем, не заблокирован ли справа
            if(x<this.getWidth()-1) {
                if(y>0 && d[x+2][y-1] != null) {
                    rightblock = true;
                }

                if(d[x+2][y] != null) {
                    rightblock = true;
                }

                if(y<this.getHeight() && d[x+2][y+1] != null) {
                    rightblock = true;
                }
            }

            if(topblock) {
                return false;
            } else {
                if(!(leftblock && rightblock)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public void remove(TileActor actor) {
        PlayScreen.gamedata.field.layers.get(actor.tiledata.layer).data[actor.tiledata.datax][actor.tiledata.datay] = null;
        PlayScreen.shadowimgs[actor.tiledata.datax][actor.tiledata.datay][actor.tiledata.layer].remove();
        PlayScreen.shadowimgs[actor.tiledata.datax][actor.tiledata.datay][actor.tiledata.layer] = null;
        actor.remove();
    }

    public enum Figure {
        Pyramid
    }

    public void generateTiles() {
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

    public void shuffleField() {
        LinkedList<TileActor> newlist = new LinkedList();
        for(Layer l : this.layers) {
            for(TileActor[] act_up : l.data) {
                for(TileActor act : act_up) {
                    if(act != null) {
                        newlist.add(act);
                    }
                }
            }
        }

        this.tiles = newlist;
        this.generateFigure(this.figure);

        PlayScreen.rebuildField();
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

    public void generateFigure(Figure fig) {
        switch (fig) {
            case Pyramid:
                Layer first = this.forkLayer();
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
                this.addLayer(first);
                Layer second = this.forkLayer();
                second.addLine(8, 2, getNTileActors(6));
                second.addLine(8, 4, getNTileActors(6));
                second.addLine(8, 6, getNTileActors(6));
                second.addLine(8, 8, getNTileActors(6));
                second.addLine(8, 10, getNTileActors(6));
                second.addLine(8, 12, getNTileActors(6));
                this.addLayer(second);
                Layer third = this.forkLayer();
                third.addLine(10, 4, getNTileActors(4));
                third.addLine(10, 6, getNTileActors(4));
                third.addLine(10, 8, getNTileActors(4));
                third.addLine(10, 10, getNTileActors(4));
                this.addLayer(third);
                Layer fourth = this.forkLayer();
                fourth.addLine(12, 6, getNTileActors(2));
                fourth.addLine(12, 8, getNTileActors(2));
                this.addLayer(fourth);
                Layer fifth = this.forkLayer();
                fifth.setAt(13, 7, getOneTileActor());
                this.addLayer(fifth);
                break;
        }
    }
}
