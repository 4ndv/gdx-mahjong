package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import java.util.Timer;
import java.util.TimerTask;

public class MenuScreen implements Screen{
    Mahjong game;
    Stage stage;

    // Да, более простого способа нет...
    public static Color labelsColor = new Color((1F/255)*8F, (1F/255)*46F, (1F/255)*65F, 1);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    public MenuScreen(Mahjong gam) {
        game = gam;
        stage = new Stage();

        // Подгружаем текстуру фона и устанавливаем её
        Texture bgtex = new Texture(Gdx.files.internal("data/gameui/menubg.png"));
        bgtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image bgimg = new Image(bgtex);
        bgimg.setWidth(Gdx.graphics.getWidth());
        bgimg.setHeight(Gdx.graphics.getHeight());
        stage.addActor(bgimg);

        Table tbl = new Table();
        tbl.setFillParent(true);



        //tbl.row();

        // Таблица и скроллпанель для меню
        Table scrollTable = new Table();
        scrollTable.pad(game.tenth/3);
        ScrollPane sp = new ScrollPane(scrollTable);
        scrollTable.align(Align.topLeft);

        // Текстуры для каждого состояния кнопки
        final NinePatchDrawable button_up_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-up.png")), 15, 15, 15, 15));
        final NinePatchDrawable button_down_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-down.png")), 15, 15, 15, 15));

        // Добавляем заголовки и кнопки карт
        scrollTable.add(new Label("Фигуры:", new Label.LabelStyle(game.fontsHash.get("big"), labelsColor))).padBottom(game.tenth / 10).padLeft(5).left();
        scrollTable.row();

        scrollTable.add(new Label("Простые", new Label.LabelStyle(game.fontsHash.get("semi-medium"), labelsColor))).padBottom(game.tenth / 10).padLeft(5).left();
        scrollTable.row();

        Texture windowtex = new Texture(Gdx.files.internal("data/gameui/window.png"));
        windowtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        final NinePatchDrawable windownp = new NinePatchDrawable(new NinePatch(windowtex, 30, 30, 50, 20));

        // Стиль кнопки
        final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(button_up_npd, button_down_npd, button_up_npd, game.fontsHash.get("medium"));
        final TextButton.TextButtonStyle tbssmall = new TextButton.TextButtonStyle(button_up_npd, button_down_npd, button_up_npd, game.fontsHash.get("small"));


        TextButton tb = new TextButton("Черепаха", tbs);
        tb.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                chooseFigure(Field.Figure.Turtle);
                return true;
            };
        });
        scrollTable.add(tb).padBottom(game.tenth / 10).left();

        scrollTable.row();

        TextButton tb_col = new TextButton("Колизей", tbs);
        tb_col.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                chooseFigure(Field.Figure.Coliseum);
                return true;
            };
        });
        scrollTable.add(tb_col).padBottom(game.tenth / 10).left();

        scrollTable.row();

        scrollTable.add(new Label("Средние", new Label.LabelStyle(game.fontsHash.get("semi-medium"), labelsColor))).padBottom(game.tenth / 10).padLeft(5).left();
        scrollTable.row();

        TextButton tb2 = new TextButton("Три вершины", tbs);
        tb2.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                chooseFigure(Field.Figure.TriPeaks);
                return true;
            };
        });
        scrollTable.add(tb2).padBottom(game.tenth / 10).left();

        scrollTable.row();

        // Занимаем весь экран
        tbl.add(sp).fill().expand();

        // Лого
        Texture logotex = new Texture(Gdx.files.internal("data/gameui/appicon.png"));
        logotex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image img = new Image(logotex);

        Table bottomTable = new Table();
        bottomTable.setWidth(Gdx.graphics.getWidth());
        bottomTable.align(Align.bottomLeft);

        bottomTable.add(img).width(game.tenth*1.5F).height(game.tenth*1.5F).left().top();

        TextButton aboutButton = new TextButton("Об игре", tbssmall);
        aboutButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dialog dia = new Dialog("Об игре", new Window.WindowStyle(game.fontsHash.get("semi-big"), labelsColor, windownp));

                dia.pad(game.tenth * 1.2F, game.tenth / 2F, game.tenth / 2F, game.tenth / 2F);
                dia.text("Mahjong v1.3.1 сборка 151\r\nАвтор: Викторов Андрей\r\nСайт: http://andreyviktorov.github.io\r\n\r\nИсходный код распространяется по\r\nлицензии GNU GPL v2 и доступен по адресу:\r\nhttps://github.com/andreyviktorov/gdx-mahjong\r\n ", new Label.LabelStyle(game.fontsHash.get("small"), labelsColor));
                dia.button("Закрыть", true, tbs);

                dia.show(stage);
                return true;
            };
        });

        TextButton exitButton = new TextButton("Выход", tbssmall);
        exitButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Dialog dia = new Dialog("Выход", new Window.WindowStyle(game.fontsHash.get("semi-big"), labelsColor, windownp)) {
                    public void result(Object obj) {
                        if(obj == (Object)true) {
                            Gdx.app.exit();
                        }
                    }
                };

                dia.pad(game.tenth * 1.2F, game.tenth / 2F, game.tenth / 2F, game.tenth / 2F);
                dia.text("Вы действительно хотите выйти?\r\n ", new Label.LabelStyle(game.fontsHash.get("small"), labelsColor));
                dia.button("Да", true, tbs);
                dia.button("Нет", false, tbs);

                dia.show(stage);
                return true;
            };
        });

        bottomTable.add().expandX().fillX();
        bottomTable.add(aboutButton).bottom().pad(5);
        bottomTable.add(exitButton).bottom().pad(5);

        tbl.row();
        tbl.add(bottomTable).expandX().fillX();

        stage.addActor(tbl);

        Gdx.input.setInputProcessor(stage);
    }

    public void refreshInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public void chooseFigure(final Field.Figure figure) {
        Gdx.audio.newSound(Gdx.files.internal("data/sounds/click.ogg")).play();
        Gdx.input.vibrate(100);
        Preferences prefs = Gdx.app.getPreferences("leveldata");
        String leveldata = prefs.getString(figure.name(), "notfound");
        PlayScreen.stage = null;
        if(!leveldata.equals("notfound")) {
            GameData deserialized = (GameData)Serializer.fromString(leveldata);
            game.setScreen(new PlayScreen(game, figure, deserialized));
        } else {
            game.setScreen(new PlayScreen(game, figure));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }
}
