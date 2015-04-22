package ru.andreyviktorov.mahjong;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Field implements Serializable {
    private int width;
    private int height;
    private int tilecount;
    public Figure figure;
    public List<Layer> layers = new ArrayList();
    private LinkedList<TileActor> tiles;
    public Field(int w, int h){
        width = w;
        height = h;
    }

    public Field(Figure figure) {
        this.figure = figure;
        this.doFigure(this.figure);
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
            if(x>1) {
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
        actor.remove();
    }

    public enum Figure {
        Turtle, TriPeaks, Coliseum
    }

    public void generateTiles() {
        tiles = new LinkedList();
        for (Tile.Suit suit : Tile.Suit.values()) {
            // Создаем тайлы для основных мастей
            if(suit == Tile.Suit.Pin || suit == Tile.Suit.Bamboo || suit == Tile.Suit.Man) {
                int max = 0;
                if(this.tilecount != 144) {
                    int a = (144 - this.tilecount)/12;
                    max = 10 - a;
                } else {
                    max = 10;
                }
                for(int i = 1; i<max; i++) {
                    for(int j = 0; j<4; j++) {
                        Tile t = new Tile();
                        t.number = i;
                        t.suit = suit;
                        TileData td = new TileData();
                        td.tilesinrow = getFigureHeight(this.figure);
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
                    td.tilesinrow = getFigureHeight(this.figure);
                    TileActor ta = new TileActor(t, td);
                    tiles.add(ta);
                }
                // Создаем тайлы для драконов и ветров
            } else {
                for(int i = 1; i<5; i++) {
                    Tile t = new Tile();
                    t.suit = suit;
                    TileData td = new TileData();
                    td.tilesinrow = getFigureHeight(this.figure);
                    TileActor ta = new TileActor(t, td);
                    tiles.add(ta);
                }
            }
        }

        System.out.println(tiles.size());

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
        if(tiles.size() > 0) {
            return tiles.poll();
        } else {
            return null;
        }
    }

    public void shuffleField() {
        if(this.countAvailablePairs() == 0 && PlayScreen.gamedata.remainingTiles == 2) {
            PlayScreen.revertMove();
        }
        do {
            LinkedList<TileActor> newlist = new LinkedList();
            LinkedList<TriPoint> points = new LinkedList();
            int maxlayer = this.layers.size();
            int l_c = 0;
            for (Layer l : this.layers) {
                for (TileActor[] act_up : l.data) {
                    for (TileActor act : act_up) {
                        if (act != null) {
                            newlist.add(act);
                            points.add(new TriPoint(act.tiledata.datax, act.tiledata.datay, l_c));
                        }
                    }
                }
                l_c++;
            }

            Collections.shuffle(newlist);

            this.tiles = newlist;
            this.layers = new ArrayList();

            for(int i = 0; i<maxlayer; i++) {
                this.layers.add(this.forkLayer());
            }

            int sz = points.size();
            for(int i = 0; i<sz; i++) {
                TriPoint p = points.poll();
                TileActor ta = this.tiles.poll();
                ta.tiledata.layer = p.z;
                ta.tiledata.datax = p.x;
                ta.tiledata.datay = p.y;
                this.layers.get(p.z).setAt(p.x, p.y, ta);
            }

            PlayScreen.previousOne = null;
            PlayScreen.previousTwo = null;

            PlayScreen.rebuildField();

            PlayScreen.recountMoves(true);
        } while (this.countAvailablePairs() == 0);
    }

    public void doFigure(Figure fig) {
        switch (fig) {
            case Turtle:
                this.width = 30;
                this.height = 16;
                this.tilecount = 144;
                break;
            case TriPeaks:
                this.width = 30;
                this.height = 14;
                this.tilecount = 120;
                break;
            case Coliseum:
                this.width = 20;
                this.height = 16;
                this.tilecount = 136;
                break;
        }

        this.generateTiles();
        this.generateFigure(fig);
    }

    public static int getFigureHeight(Figure fig) {
        int ret = 0;
        switch (fig) {
            case Turtle:
                ret = 16;
                break;
            case TriPeaks:
                ret = 14;
                break;
            case Coliseum:
                ret = 16;
        }

        return ret;
    }

    public int getMaxTilesCount() {
        return this.tilecount;
    }

    public void generateFigure(Figure fig) {
        switch (fig) {
            case Turtle:
                Layer tu_first = this.forkLayer();
                // СНИЗУ ВВЕРХ!!!11!111!адин
                tu_first.addLine(2, 0, getNTileActors(12));
                tu_first.addLine(6, 2, getNTileActors(8));
                tu_first.addLine(4, 4, getNTileActors(10));
                tu_first.addLine(2, 6, getNTileActors(12));
                tu_first.addLine(2, 8, getNTileActors(12));
                tu_first.addLine(4, 10, getNTileActors(10));
                tu_first.addLine(6, 12, getNTileActors(8));
                tu_first.addLine(2, 14, getNTileActors(12));
                tu_first.setAt(0, 7, getOneTileActor());
                tu_first.setAt(26, 7, getOneTileActor());
                tu_first.setAt(28, 7, getOneTileActor());
                this.addLayer(tu_first);
                Layer tu_second = this.forkLayer();
                tu_second.addLine(8, 2, getNTileActors(6));
                tu_second.addLine(8, 4, getNTileActors(6));
                tu_second.addLine(8, 6, getNTileActors(6));
                tu_second.addLine(8, 8, getNTileActors(6));
                tu_second.addLine(8, 10, getNTileActors(6));
                tu_second.addLine(8, 12, getNTileActors(6));
                this.addLayer(tu_second);
                Layer tu_third = this.forkLayer();
                tu_third.addLine(10, 4, getNTileActors(4));
                tu_third.addLine(10, 6, getNTileActors(4));
                tu_third.addLine(10, 8, getNTileActors(4));
                tu_third.addLine(10, 10, getNTileActors(4));
                this.addLayer(tu_third);
                Layer tu_fourth = this.forkLayer();
                tu_fourth.addLine(12, 6, getNTileActors(2));
                tu_fourth.addLine(12, 8, getNTileActors(2));
                this.addLayer(tu_fourth);
                Layer tu_fifth = this.forkLayer();
                tu_fifth.setAt(13, 7, getOneTileActor());
                this.addLayer(tu_fifth);
                break;
            case TriPeaks:
                Layer tp_first = this.forkLayer();
                tp_first.addLine(2, 4, getNTileActors(13));
                tp_first.addLine(2, 8, getNTileActors(13));
                tp_first.addLine(0, 6, getNTileActors(15));
                tp_first.addLine(4, 2, getNTileActors(3));
                tp_first.addLine(12, 2, getNTileActors(3));
                tp_first.addLine(20, 2, getNTileActors(3));
                tp_first.addLine(4, 10, getNTileActors(3));
                tp_first.addLine(12, 10, getNTileActors(3));
                tp_first.addLine(20, 10, getNTileActors(3));
                tp_first.setAt(6, 0, getOneTileActor());
                tp_first.setAt(14, 0, getOneTileActor());
                tp_first.setAt(22, 0, getOneTileActor());
                tp_first.setAt(6, 12, getOneTileActor());
                tp_first.setAt(14, 12, getOneTileActor());
                tp_first.setAt(22, 12, getOneTileActor());
                this.addLayer(tp_first);
                Layer tp_second = this.forkLayer();
                tp_second.addLine(4, 4, getNTileActors(3));
                tp_second.addLine(12, 4, getNTileActors(3));
                tp_second.addLine(20, 4, getNTileActors(3));
                tp_second.addLine(4, 8, getNTileActors(3));
                tp_second.addLine(12, 8, getNTileActors(3));
                tp_second.addLine(20, 8, getNTileActors(3));
                tp_second.addLine(2, 6, getNTileActors(13));
                tp_second.setAt(6, 2, getOneTileActor());
                tp_second.setAt(14, 2, getOneTileActor());
                tp_second.setAt(22, 2, getOneTileActor());
                tp_second.setAt(6, 10, getOneTileActor());
                tp_second.setAt(14, 10, getOneTileActor());
                tp_second.setAt(22, 10, getOneTileActor());
                this.addLayer(tp_second);
                Layer tp_third = this.forkLayer();
                tp_third.addLine(4, 6, getNTileActors(3));
                tp_third.addLine(12, 6, getNTileActors(3));
                tp_third.addLine(20, 6, getNTileActors(3));
                tp_third.setAt(6, 4, getOneTileActor());
                tp_third.setAt(14, 4, getOneTileActor());
                tp_third.setAt(22, 4, getOneTileActor());
                tp_third.setAt(6, 8, getOneTileActor());
                tp_third.setAt(14, 8, getOneTileActor());
                tp_third.setAt(22, 8, getOneTileActor());
                this.addLayer(tp_third);
                Layer tp_four = this.forkLayer();
                tp_four.setAt(6, 6, getOneTileActor());
                tp_four.setAt(14, 6, getOneTileActor());
                tp_four.setAt(22, 6, getOneTileActor());
                this.addLayer(tp_four);
                break;
            case Coliseum:
                Layer co_first = this.forkLayer();
                co_first.addLine(4, 0, getNTileActors(6));
                co_first.addLine(2, 2, getNTileActors(8));
                co_first.addLine(0, 4, getNTileActors(10));
                co_first.addLine(0, 6, getNTileActors(3));
                co_first.addLine(0, 8, getNTileActors(3));
                co_first.addLine(14, 6, getNTileActors(3));
                co_first.addLine(14, 8, getNTileActors(3));
                co_first.addLine(0, 10, getNTileActors(10));
                co_first.addLine(2, 12, getNTileActors(8));
                co_first.addLine(4, 14, getNTileActors(6));
                this.addLayer(co_first);
                Layer co_second = this.forkLayer();
                co_second.addLine(4, 0, getNTileActors(6));
                co_second.addLine(2, 2, getNTileActors(8));
                co_second.addLine(0, 4, getNTileActors(3));
                co_second.addLine(14, 4, getNTileActors(3));
                co_second.addLine(0, 6, getNTileActors(2));
                co_second.addLine(16, 6, getNTileActors(2));
                co_second.addLine(0, 8, getNTileActors(2));
                co_second.addLine(16, 8, getNTileActors(2));
                co_second.addLine(0, 10, getNTileActors(3));
                co_second.addLine(14, 10, getNTileActors(3));
                co_second.addLine(2, 12, getNTileActors(8));
                co_second.addLine(4, 14, getNTileActors(6));
                this.addLayer(co_second);
                Layer co_third = this.forkLayer();
                co_third.addLine(4, 0, getNTileActors(6));
                co_third.addLine(2, 2, getNTileActors(2));
                co_third.addLine(14, 2, getNTileActors(2));
                co_third.setAt(0, 4, getOneTileActor());
                co_third.setAt(0, 6, getOneTileActor());
                co_third.setAt(0, 8, getOneTileActor());
                co_third.setAt(0, 10, getOneTileActor());
                co_third.setAt(18, 4, getOneTileActor());
                co_third.setAt(18, 6, getOneTileActor());
                co_third.setAt(18, 8, getOneTileActor());
                co_third.setAt(18, 10, getOneTileActor());
                co_third.addLine(2, 12, getNTileActors(2));
                co_third.addLine(14, 12, getNTileActors(2));
                co_third.addLine(4, 14, getNTileActors(6));
                this.addLayer(co_third);
        }
    }

    public int countAvailablePairs() {
        List<TileActor> avalist = new ArrayList();
        Field f = this;
        int n = 0;
        for(Layer l : f.layers) {
            for(int i = 0; i<f.getWidth() + 1; i++) {
                for(int j = 0; j<f.getHeight() + 1; j++) {
                    if(l.data[i][j] != null) {
                        if(f.canRemove(n, i, j)) {
                            avalist.add(l.data[i][j]);
                        }
                    }
                }
            }
            n++;
        }

        int c = 0;

        for(TileActor one : avalist) {
            for(TileActor two : avalist) {
                if((one.randomId != two.randomId) && ((one.tile.suit == two.tile.suit && one.tile.number == two.tile.number) || (one.tile.suit == two.tile.suit && one.tile.suit == Tile.Suit.Season) || (one.tile.suit == two.tile.suit && one.tile.suit == Tile.Suit.Flower))) {
                    c++;
                }
            }
        }

        return c/2;
    }
}
