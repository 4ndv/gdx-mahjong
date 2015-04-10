package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TileActor extends Actor {
    public Tile tile;
    public TileData tiledata;
    private Sprite sprite;
    private Texture img;
    public int randomId = 0;

    public TileActor(Tile t, TileData td) {
        tile = t;
        tiledata = td;
        this.randomId = (int)Math.floor((Math.random() * 99999999) + 10000000);

        // Подгружаем подходящую текстуру
        // Что бы не потерять: однострочник для отличной обрезки:
        // for f in *.png; do convert $f -gravity Center -crop 82x128+0+0 cropped/${f%.png}.png; done

        String s = tile.suit.name();
        if(t.number != 0) {
            s+="-"+t.number;
        }
        img = new Texture("data/tiles/" + s + ".png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(img);

        // Магическая константа: отношение ширины к высоте, 82/102 = 0.640625F
        //sprite.setSize(Gdx.graphics.getHeight() * PlayScreen.gamedata.scaleModificator / 100 * 0.640625F, Gdx.graphics.getHeight() * PlayScreen.gamedata.scaleModificator / 100);
        float baseheight = Gdx.graphics.getHeight();
        float usableheight = baseheight / 10 * 7.5F;
        float tileheight = usableheight / (tiledata.tilesinrow/2);
        sprite.setSize(tileheight * 0.640625F, tileheight);
        PlayScreen.TILE_WIDTH = sprite.getWidth();
        PlayScreen.TILE_HEIGHT = sprite.getHeight();

        final TileActor passthis = this;

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Ткнул на: слой: " + tiledata.layer + " x: " + tiledata.datax + " y: " + tiledata.datay);
                //PlayScreen.field.layers.get(tiledata.layer).data[tiledata.datax][tiledata.datay] = null;
                //passthis.remove();
                //System.out.println("Пынг!");
                if(PlayScreen.gamedata.field.canRemove(tiledata.layer, tiledata.datax, tiledata.datay)) {
                    if(PlayScreen.gamedata.selected == null) {
                        PlayScreen.gamedata.selected = passthis;
                        glowIt();
                    } else {
                        if(PlayScreen.gamedata.selected.randomId == passthis.randomId) {
                        } else if(PlayScreen.gamedata.selected.tile.suit == passthis.tile.suit && PlayScreen.gamedata.selected.tile.number == passthis.tile.number) {
                            removePair();
                        } else if (PlayScreen.gamedata.selected.tile.suit == Tile.Suit.Season && passthis.tile.suit == Tile.Suit.Season) {
                            removePair();
                        } else if (PlayScreen.gamedata.selected.tile.suit == Tile.Suit.Flower && passthis.tile.suit == Tile.Suit.Flower) {
                            removePair();
                        } else {
                            PlayScreen.gamedata.selected = passthis;
                            glowIt();
                        }
                    }
                } else {

                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
        setTouchable(Touchable.enabled);
    }

    public void removePair() {
        PlayScreen.previousOne = this;
        PlayScreen.previousTwo = PlayScreen.gamedata.selected;
        PlayScreen.gamedata.field.remove(this);
        PlayScreen.gamedata.field.remove(PlayScreen.gamedata.selected);
        PlayScreen.clearSelection();
        PlayScreen.gamedata.selected = null;
        PlayScreen.gamedata.remainingTiles -= 2;
        PlayScreen.remainLabel.setText("Осталось фишек: " + PlayScreen.gamedata.remainingTiles);
        PlayScreen.recountMoves();
    }

    public void glowIt() {
        Image img = PlayScreen.glowimg;
        // Еще одна магическая константа: 102/148
        //img.setSize(Gdx.graphics.getHeight() * (PlayScreen.gamedata.scaleModificator + 2) / 100 * 0.68918918918F, Gdx.graphics.getHeight() * (PlayScreen.gamedata.scaleModificator + 2) / 100);
        img.setSize(PlayScreen.TILE_WIDTH * 1.2F, PlayScreen.TILE_HEIGHT * 1.2F);
        float offsetx = (img.getWidth() - PlayScreen.TILE_WIDTH) / 2;
        float offsety = (img.getHeight() - PlayScreen.TILE_HEIGHT) / 2;

        img.setPosition(this.tiledata.x - offsetx, this.tiledata.y - offsety);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, this.sprite.getWidth(), this.sprite.getHeight());
        this.sprite.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.setPosition(tiledata.x, tiledata.y);
        this.setBounds(tiledata.x, tiledata.y, PlayScreen.TILE_WIDTH, PlayScreen.TILE_HEIGHT);
        this.sprite.draw(batch);
    }
}
