package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlayScreen implements Screen {
    public static Stage stage;
    final Mahjong game;
    public static float SCALE_RATIO = 128 / Gdx.graphics.getWidth();
    public static float TILE_WIDTH;
    public static float TILE_HEIGHT;
    public static GameData gamedata;
    public static List<List<Image>> shadows = new ArrayList();
    public static Texture shadowtexture;
    public static Texture glowtexture;
    public static Image glowimg;
    public static Image[][][] shadowimgs;
    private static List<String> backgrounds;

    public static Group back;
    public static Group fore;

    public static TileActor previousOne;
    public static TileActor previousTwo;

    public static Label remainLabel;
    public static Label availableLabel;

    public PlayScreen (final Mahjong gameref, final Field.Figure figure) {
        game = gameref;

        stage = new Stage(new ScreenViewport());

        // TODO: сделать загрузку геймдаты из сейва
        gamedata = new GameData();
        gamedata.scaleModificator = Field.getFigureHeight(figure) - 6.5F;
        System.out.println("СМ:" + gamedata.scaleModificator);

        gamedata.field = new Field(figure);
        gamedata.remainingTiles = gamedata.field.getMaxTilesCount();

        glowtexture = new Texture(Gdx.files.internal("data/tiles/TileSelected.png"));
        glowimg = new Image(glowtexture);
        glowimg.setPosition(-500, -500);
        shadowimgs = new Image[gamedata.field.getWidth()+1][gamedata.field.getHeight()+1][gamedata.field.layers.size()+1];

        shadowtexture = new Texture(Gdx.files.internal("data/tiles/TileShadow.png"));
        shadowtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        back = new Group();
        back.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        fore = new Group();
        fore.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(back);
        stage.addActor(fore);

        backgrounds = new ArrayList();

        backgrounds.add("data/backgrounds/geometry.png");
        backgrounds.add("data/backgrounds/geometry2.png");
        //backgrounds.add("data/backgrounds/sativa.png");
        backgrounds.add("data/backgrounds/photography.png");

        Collections.shuffle(backgrounds);

        //float koeff_bg = 1920F/1080F;
        //Texture bg_img = new Texture(Gdx.files.internal(backgrounds.get(0)));
        //bg_img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //gamedata.background = new Image(bg_img);
        //gamedata.background.setWidth(Gdx.graphics.getWidth());
        //gamedata.background.setHeight(Gdx.graphics.getWidth() / koeff_bg);
        //if (Gdx.graphics.getWidth() / koeff_bg < Gdx.graphics.getHeight()) {
        //    gamedata.background.setHeight(Gdx.graphics.getHeight());
        //}

        //back.addActor(gamedata.background);

        Texture bg_img = new Texture(Gdx.files.internal(backgrounds.get(0)));
        int w_count = (int)Math.ceil((double)Gdx.graphics.getWidth() / (double)bg_img.getWidth());
        int h_count = (int)Math.ceil((double)Gdx.graphics.getHeight() / (double)bg_img.getHeight());
        int bg_w = bg_img.getWidth();
        int bg_h = bg_img.getHeight();
        for(int i = 0; i<w_count; i++) {
            for(int j = 0; j<h_count; j++) {
                Image tempimg = new Image(bg_img);
                tempimg.setPosition(bg_w * i, bg_h * j);
                back.addActor(tempimg);
            }
        }

        /*Image topbar = new Image(new Texture(Gdx.files.internal("data/ui.png")));
        topbar.setColor(0,0,0,0.3F);
        float barheight = Gdx.graphics.getHeight()/10;
        topbar.setSize(Gdx.graphics.getWidth(), barheight);
        topbar.setPosition(0, Gdx.graphics.getHeight() - barheight);

        back.addActor(topbar);*/

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = game.fontsHash.get("semi-big");
        ls.fontColor = Color.DARK_GRAY;
        remainLabel = new Label("Осталось фишек: " + gamedata.field.getMaxTilesCount(), ls);
        remainLabel.setPosition(game.twentyth/2, Gdx.graphics.getHeight() - remainLabel.getHeight() - game.twentyth/4);
        //remainLabel.setPosition(Gdx.graphics.getWidth()/48,
        //        Gdx.graphics.getHeight() - Utils.getCenteredHeight(barheight, remainLabel.getHeight()));

        availableLabel = new Label("Возможных ходов: " + countAvailablePairs(), ls);
        availableLabel.setPosition(Gdx.graphics.getWidth() - availableLabel.getWidth() - game.twentyth/2, Gdx.graphics.getHeight() - availableLabel.getHeight() - game.twentyth/4);

        //float apos = Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/50 - availableLabel.getWidth();

        //availableLabel.setPosition(apos, Gdx.graphics.getHeight() - Utils.getCenteredHeight(barheight, availableLabel.getHeight()));

        back.addActor(remainLabel);
        back.addActor(availableLabel);

        Table tbl = new Table();
        tbl.setWidth(Gdx.graphics.getWidth());

        /*Texture uitex = new Texture(Gdx.files.internal("data/uihalf.png"));
        uitex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np = new NinePatch(uitex, 8, 8, 8, 8);
        final NinePatchDrawable npd_50 = new NinePatchDrawable(np);

        Texture uitex2 = new Texture(Gdx.files.internal("data/ui75.png"));
        uitex2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np2 = new NinePatch(uitex2, 8, 8, 8, 8);
        final NinePatchDrawable npd_75 = new NinePatchDrawable(np2);

        Texture uitex3 = new Texture(Gdx.files.internal("data/ui90.png"));
        uitex3.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np3 = new NinePatch(uitex2, 8, 8, 8, 8);
        final NinePatchDrawable npd_90 = new NinePatchDrawable(np3);*/

        Texture windowtex = new Texture(Gdx.files.internal("data/gameui/window.png"));
        windowtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        final NinePatchDrawable windownp = new NinePatchDrawable(new NinePatch(windowtex, 30, 30, 50, 20));

        final NinePatchDrawable button_up_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-up.png")), 15, 15, 15, 15));
        final NinePatchDrawable button_down_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-down.png")), 15, 15, 15, 15));


        final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(button_up_npd, button_down_npd, button_up_npd, game.fontsHash.get("small"));

        // TODO: сделать нормальный стиль для диалога
        //final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(npd_50, npd_75, npd_50, game.fontsHash.get("small"));

        // TODO: сделать нормальный стиль для кнопки
        TextButton shuffleButton = new TextButton("Перемешать", tbs);
        shuffleButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayScreen.gamedata.field.shuffleField();
                clearSelection();
                return true;
            }
        });
        TextButton helpButton = new TextButton("Помощь", tbs);
        helpButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dialog dia = new Dialog("Правила игры", new WindowStyle(game.fontsHash.get("semi-big"), Color.DARK_GRAY, windownp));
                dia.pad(90, 40, 20, 40);
                dia.text("Добро пожаловать в пасьянс маджонг!\r\nКраткие правила игры:\r\nНужно убрать с поля все парные фишки.\r\nФишки делятся на два типа: обычные и джокеры.\r\nДжокеры - это фишки с цифрой в левом верхнем углу,\r\nи убираются опираясь на картинку в центре.\r\n\r\nФишки не могут быть убраны если:\r\n1. Над ней есть другая фишка\r\n2. Слева и справа от нее есть другие фишки", new Label.LabelStyle(game.fontsHash.get("small"), Color.DARK_GRAY));
                dia.button("OK", true, tbs);

                dia.show(stage);
                return true;
            }
        });
        TextButton cancelButton = new TextButton("Отменить", tbs);
        cancelButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PlayScreen.previousOne != null && PlayScreen.previousTwo != null) {
                    PlayScreen.gamedata.field.layers.get(PlayScreen.previousOne.tiledata.layer).data[PlayScreen.previousOne.tiledata.datax][PlayScreen.previousOne.tiledata.datay] = PlayScreen.previousOne;
                    PlayScreen.gamedata.field.layers.get(PlayScreen.previousTwo.tiledata.layer).data[PlayScreen.previousTwo.tiledata.datax][PlayScreen.previousTwo.tiledata.datay] = PlayScreen.previousTwo;
                    PlayScreen.previousOne = null;
                    PlayScreen.previousTwo = null;
                    rebuildField();
                    recountMoves();
                    clearSelection();
                }
                return true;
            }
        });
        TextButton menuButton = new TextButton("Меню", tbs);

        //tbl.setDebug(true);
        tbl.align(Align.bottomLeft);
        tbl.add(shuffleButton).pad(5);
        tbl.add(helpButton).pad(5);
        tbl.add().expandX();
        tbl.add(cancelButton).pad(5);
        tbl.add(menuButton).pad(5);
        stage.addActor(tbl);

        rebuildField();

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    public static void recountMoves() {
        PlayScreen.availableLabel.setText("Возможных ходов: " + PlayScreen.countAvailablePairs());
    }

    public static void rebuildField() {
        fore.clear();

        float fieldWidth;
        float fieldHeight;
        float fieldPosX;
        float fieldPosY;

        // z-index
        float g_offset_y = 0;
        int layernumber = 0;
        List<TileActor> layerPool;
        List<Image> shadowPool;
        for(Layer layer : gamedata.field.layers) {
            layerPool = new LinkedList();
            shadowPool = new LinkedList();
            for(int i = 0; i<gamedata.field.getWidth(); i++) {
                for(int j = 0; j<gamedata.field.getHeight(); j++) {
                    if(layer.data[i][j] != null) {
                        // Получаем шириновысоту
                        /*System.out.println(layer.data[i][j].getHeight());
                        TILE_WIDTH = layer.data[i][j].getWidth();
                        TILE_HEIGHT = layer.data[i][j].getHeight();*/

                        // +2 для отступов
                        fieldWidth = TILE_WIDTH * ((gamedata.field.getWidth() + 2) / 2) - TILE_WIDTH;
                        fieldHeight = TILE_HEIGHT * ((gamedata.field.getHeight() + 2) / 2) - TILE_HEIGHT;


                        //System.out.println(layer.data[i][j].tile.suit.name());

                        //System.out.println("TILE_W " + TILE_WIDTH + " TILE_H " + TILE_HEIGHT + " FW " + fieldWidth + " FH " + fieldHeight);

                        fieldPosX = Gdx.graphics.getWidth()/2 - fieldWidth / 2;
                        fieldPosY = Gdx.graphics.getHeight()/2 - fieldHeight / 2;

                        layer.data[i][j].tiledata.datax = i;
                        layer.data[i][j].tiledata.datay = j;
                        layer.data[i][j].tiledata.layer = layernumber;

                        layer.data[i][j].tiledata.x = fieldPosX + TILE_WIDTH * i/2;
                        layer.data[i][j].tiledata.y = fieldPosY + TILE_HEIGHT * j/2 - g_offset_y;

                        Image img = new Image(PlayScreen.shadowtexture);
                        // Еще одна магическая константа: 102/148
                        img.setSize(TILE_WIDTH * 1.3F, TILE_HEIGHT * 1.3F);
                        float offsetx = (img.getWidth() - TILE_WIDTH) / 2;
                        float offsety = (img.getHeight() - TILE_HEIGHT) / 2;

                        img.setPosition(fieldPosX + TILE_WIDTH * i/2 - offsetx, fieldPosY + TILE_HEIGHT * j/2 - g_offset_y - offsety);

                        shadowimgs[i][j][layernumber] = img;

                        shadowPool.add(shadowimgs[i][j][layernumber]);
                        layerPool.add(layer.data[i][j]);
                        //stage.addActor(shadowimgs[i][j][layernumber]);
                        //stage.addActor(layer.data[i][j]);
                    }
                }
            }

            // Flushing pools
            for(Image img : shadowPool) {
                fore.addActor(img);
            }
            for(TileActor act : layerPool) {
                fore.addActor(act);
            }

            System.out.println("Finished layer " + layernumber);
            layernumber++;
            g_offset_y += TILE_HEIGHT/5.8;

            // Must be on top of any else
            fore.addActor(glowimg);
        }
    }

    public static void clearSelection() {
        PlayScreen.glowimg.setPosition(-500, -500);
        PlayScreen.gamedata.selected = null;
    }

    public static int countAvailablePairs() {
        List<TileActor> avalist = new ArrayList();
        Field f = PlayScreen.gamedata.field;
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.stage = null;
    }
}
