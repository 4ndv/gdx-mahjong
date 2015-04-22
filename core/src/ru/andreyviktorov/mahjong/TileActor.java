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

import java.io.Serializable;

public class TileActor extends Actor implements Serializable {
    public Tile tile;
    public TileData tiledata;
    private transient Sprite sprite;
    private transient Texture img;
    public int randomId = 0;

    public TileActor(Tile t, TileData td) {
        tile = t;
        tiledata = td;
        this.randomId = (int)Math.floor((Math.random() * 99999999) + 10000000);

        // Подгружаем подходящую текстуру
        // Что бы не потерять: однострочник для отличной обрезки:
        // for f in *.png; do convert $f -gravity Center -crop 82x128+0+0 cropped/${f%.png}.png; done

        this.refreshTexture();
    }

    public void refreshTexture() {
        String s = tile.suit.name();
        if(tile.number != 0) {
            s+="-"+tile.number;
        }
        img = new Texture("data/tiles/" + s + ".png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(img);

        // Магическая константа: отношение ширины к высоте, 82/102 = 0.640625F
        //sprite.setSize(Gdx.graphics.getHeight() * PlayScreen.gamedata.scaleModificator / 100 * 0.640625F, Gdx.graphics.getHeight() * PlayScreen.gamedata.scaleModificator / 100);
        float baseheight = Gdx.graphics.getHeight();
        float usableheight = baseheight / 10 * 9.7F;
        float tileheight = Math.round(usableheight / (tiledata.tilesinrow/2));
        float tilewidth = Math.round(tileheight * (212F/256F));

        tiledata.offset = tilewidth /(96F/27F);

        sprite.setSize(tilewidth, tileheight);
        PlayScreen.TILE_WIDTH = tilewidth - tiledata.offset;
        PlayScreen.TILE_HEIGHT = tileheight - tiledata.offset;

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
                    Gdx.input.vibrate(100);
                } else {
                    Gdx.input.vibrate(new long[] { 0, 150, 100, 150}, -1);
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
        PlayScreen.recountMoves(false);
        Gdx.audio.newSound(Gdx.files.internal("data/sounds/swosh.ogg")).play();
        PlayScreen.saveField();
    }

    public void glowIt() {
        Image img = PlayScreen.glowimg;
        img.setSize(PlayScreen.TILE_WIDTH + this.tiledata.offset, PlayScreen.TILE_HEIGHT + this.tiledata.offset);
        img.setPosition(this.tiledata.x - this.tiledata.offset/20, this.tiledata.y + this.tiledata.offset/20);
        Gdx.audio.newSound(Gdx.files.internal("data/sounds/click.ogg")).play();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        this.sprite.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        this.setBounds(tiledata.x, tiledata.y, PlayScreen.TILE_WIDTH, PlayScreen.TILE_HEIGHT + this.tiledata.offset);
        this.sprite.setPosition(tiledata.x, tiledata.y);
        this.sprite.draw(batch);
    }
}
