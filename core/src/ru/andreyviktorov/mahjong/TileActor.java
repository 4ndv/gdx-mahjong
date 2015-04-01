package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TileActor extends Actor {
    public Tile tile;
    public TileData tiledata;
    private Sprite sprite;
    private Texture img;

    public TileActor(Tile t, TileData td) {
        tile = t;
        tiledata = td;

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
        sprite.setSize(Gdx.graphics.getHeight() * 10 / 100 * 0.640625F, Gdx.graphics.getHeight() * 10 / 100);
        PlayScreen.TILE_WIDTH = sprite.getWidth();
        System.out.println(sprite.getWidth());
        PlayScreen.TILE_HEIGHT = sprite.getHeight();

        final TileActor passthis = this;

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //PlayScreen.field.layers.get(tiledata.layer).data[tiledata.datax][tiledata.datay] = null;
                //passthis.remove();
                //System.out.println("Пынг!");
                if(PlayScreen.field.canRemove(tiledata.layer, tiledata.datax, tiledata.datay)) {
                    if(PlayScreen.selected == null) {
                        PlayScreen.selected = passthis;
                        System.out.println("Выбрал");
                    } else {
                        if(PlayScreen.selected.tiledata.randomId == passthis.tiledata.randomId) {
                            System.out.println("Ты только что на него тыкнул");
                        } else if(PlayScreen.selected.tile.suit == passthis.tile.suit && PlayScreen.selected.tile.number == passthis.tile.number) {
                            PlayScreen.field.remove(passthis);
                            PlayScreen.field.remove(PlayScreen.selected);
                            PlayScreen.selected = null;
                        } else {
                            PlayScreen.selected = passthis;
                            System.out.println("Не подошёл, заменил");
                        }
                    }
                } else {
                    System.out.println("Низя убрать");
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
        setTouchable(Touchable.enabled);
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
