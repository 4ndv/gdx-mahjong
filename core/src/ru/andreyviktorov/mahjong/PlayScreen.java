package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlayScreen implements Screen, Serializable {
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

        // Генерируем поле с наибольшим числом доступных ходов изначально, снижая этим процент нерешаемых полей
        Field tempfield = null;
        int fieldrecord = 0;
        for(int i = 0; i<20; i++) {
            Field tmp = new Field(figure);
            int tmprecord = tmp.countAvailablePairs();
            if(tmprecord > fieldrecord) {
                tempfield = tmp;
                fieldrecord = tmprecord;
            }
        }
        gamedata.field = tempfield;
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
        backgrounds.add("data/backgrounds/photography.png");

        Collections.shuffle(backgrounds);

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

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = game.fontsHash.get("semi-big");
        ls.fontColor = Color.DARK_GRAY;

        Texture windowtex = new Texture(Gdx.files.internal("data/gameui/window.png"));
        windowtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        final NinePatchDrawable windownp = new NinePatchDrawable(new NinePatch(windowtex, 30, 30, 50, 20));

        final NinePatchDrawable button_up_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-up.png")), 15, 15, 15, 15));
        final NinePatchDrawable button_down_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-down.png")), 15, 15, 15, 15));


        final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(button_up_npd, button_down_npd, button_up_npd, game.fontsHash.get("small"));

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
                dia.pad(game.tenth * 1.2F, game.tenth / 2F, game.tenth / 4F, game.tenth / 2F);
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
        menuButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gotoMenu();
                return true;
            }
        });

        remainLabel = new Label("Осталось фишек: " + gamedata.field.getMaxTilesCount(), ls);
        availableLabel = new Label("Возможных ходов: " + gamedata.field.countAvailablePairs(), ls);

        Table tbl = new Table();
        tbl.setWidth(Gdx.graphics.getWidth());

        //tbl.setDebug(true);
        tbl.align(Align.bottomLeft);
        tbl.add(shuffleButton).pad(5);
        tbl.add(helpButton).pad(5);
        tbl.add().expandX();
        tbl.add(cancelButton).pad(5);
        tbl.add(menuButton).pad(5);
        stage.addActor(tbl);

        Table tbl2 = new Table();
        tbl2.setWidth(Gdx.graphics.getWidth());
        tbl2.align(Align.bottomLeft);
        tbl2.add(remainLabel).padLeft(5);
        tbl2.add().expandX();
        tbl2.add(availableLabel).padRight(5);
        tbl2.setY(Gdx.graphics.getHeight() - game.fontsHash.get("semi-big").getLineHeight());
        stage.addActor(tbl2);

        rebuildField();

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    public void saveField() {
        try {
            Preferences prefs = Gdx.app.getPreferences("leveldata");
            String leveldata = Serializer.toString(this);
            prefs.putString(gamedata.field.figure.name(), leveldata);
            prefs.flush();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void gotoMenu() {
        saveField();
        game.setScreen(new MenuScreen(Static.mahjong));
    }

    public void refreshInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public static void recountMoves() {
        PlayScreen.availableLabel.setText("Возможных ходов: " + gamedata.field.countAvailablePairs());
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
                        // +2 для отступов
                        fieldWidth = TILE_WIDTH * ((gamedata.field.getWidth() + 2) / 2) - TILE_WIDTH;
                        fieldHeight = TILE_HEIGHT * ((gamedata.field.getHeight() + 2) / 2) - TILE_HEIGHT;

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
