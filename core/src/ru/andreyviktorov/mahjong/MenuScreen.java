package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class MenuScreen implements Screen{
    Mahjong game;
    Stage stage;

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
        Texture bgtex = new Texture(Gdx.files.internal("data/menu/bg.png"));
        bgtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image bgimg = new Image(bgtex);
        bgimg.setWidth(Gdx.graphics.getWidth());
        bgimg.setHeight(Gdx.graphics.getHeight());
        stage.addActor(bgimg);

        Table tbl = new Table();
        tbl.setFillParent(true);

        // Лого
        Label logo = new Label("Mahjong", new Label.LabelStyle(game.fontsHash.get("logo"), Color.BLACK));
        tbl.add(logo).top().left().padLeft(game.tenth/3).padTop(game.tenth/3);

        tbl.row();

        // Таблица и скроллпанель для меню
        Table scrollTable = new Table();
        scrollTable.pad(game.tenth/3);
        ScrollPane sp = new ScrollPane(scrollTable);
        scrollTable.align(Align.topLeft);

        // Текстуры для каждого состояния кнопки
        final NinePatchDrawable button_up_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-up.png")), 15, 15, 15, 15));
        final NinePatchDrawable button_down_npd = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("data/gameui/button-down.png")), 15, 15, 15, 15));

        // Добавляем заголовки и кнопки карт
        scrollTable.add(new Label("Простые", new Label.LabelStyle(game.fontsHash.get("small"), Color.DARK_GRAY))).padBottom(game.tenth / 10).padLeft(5).left();
        scrollTable.row();

        // Стиль кнопки
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(button_up_npd, button_down_npd, button_up_npd, game.fontsHash.get("small"));

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

        scrollTable.add(new Label("Средние", new Label.LabelStyle(game.fontsHash.get("small"), Color.DARK_GRAY))).padBottom(game.tenth / 10).padLeft(5).left();
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

        stage.addActor(tbl);

        Gdx.input.setInputProcessor(stage);
    }

    public void refreshInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public void chooseFigure(Field.Figure figure) {
        // Если фигура сохранена, сделать refreshInput
        game.setScreen(new PlayScreen(game, figure));
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
