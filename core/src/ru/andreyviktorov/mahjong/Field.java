package ru.andreyviktorov.mahjong;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private int width;
    private int height;
    public List<Layer> layers = new ArrayList();
    public Field(int w, int h){
        width = w;
        height = h;
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
        PlayScreen.field.layers.get(actor.tiledata.layer).data[actor.tiledata.datax][actor.tiledata.datay] = null;
        PlayScreen.shadowimgs[actor.tiledata.datax][actor.tiledata.datay][actor.tiledata.layer].remove();
        PlayScreen.shadowimgs[actor.tiledata.datax][actor.tiledata.datay][actor.tiledata.layer] = null;
        actor.remove();
    }
}
