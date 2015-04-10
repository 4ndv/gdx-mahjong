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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
        Gdx.input.setInputProcessor(stage);

        Texture bgtex = new Texture(Gdx.files.internal("data/menu/bg.png"));
        bgtex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image bgimg = new Image(bgtex);
        bgimg.setWidth(Gdx.graphics.getWidth());
        bgimg.setHeight(Gdx.graphics.getHeight());
        stage.addActor(bgimg);

        /*
        Texture uitex = new Texture(Gdx.files.internal("data/uihalf.png"));
        uitex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np = new NinePatch(uitex, 8, 8, 8, 8);
        final NinePatchDrawable npd_50 = new NinePatchDrawable(np);

        Texture uitex2 = new Texture(Gdx.files.internal("data/ui75.png"));
        uitex2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np2 = new NinePatch(uitex2, 8, 8, 8, 8);
        final NinePatchDrawable npd_75 = new NinePatchDrawable(np2);

        Texture uitex3 = new Texture(Gdx.files.internal("data/ui90.png"));
        uitex3.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np3 = new NinePatch(uitex3, 8, 8, 8, 8);
        final NinePatchDrawable npd_90 = new NinePatchDrawable(np3);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(npd_50, npd_75, npd_50, game.fontsHash.get("semi-big"));
        TextButton tb = new TextButton("Начать игру", tbs);
        tb.setX(Gdx.graphics.getWidth() / 2 - tb.getWidth() / 2);
        tb.setY(game.tenth * 3);
        tb.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen( new PlayScreen(game) );
                return true;
            };
        });
        stage.addActor(tb);
        */


        Table tbl = new Table();
        tbl.setFillParent(true);
        //tbl.add(new Label("Привет, мир", new Label.LabelStyle(game.fontsHash.get("big"), Color.WHITE)));

        Texture logotex = new Texture(Gdx.files.internal("data/menu/logo.png"));
        logotex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image logoimg = new Image(logotex);
        float logoh = logoimg.getHeight();
        //logoimg.setX(Gdx.graphics.getWidth() / 2F - logoimg.getWidth() / 2F);
        //logoimg.setY(Gdx.graphics.getHeight() - game.tenth * 2 - logoimg.getHeight());
        //stage.addActor(logoimg);

        // 751*279
        tbl.add(logoimg).top().left().width(game.tenth * 5).height((game.tenth * 5) * (279F/751F));
        //logoimg.setWidth(logoimg.getWidth() * (logoimg.getHeight() / logoh));

        tbl.row();

        Table scrollTable = new Table();
        ScrollPane sp = new ScrollPane(scrollTable);
        scrollTable.align(Align.topLeft);
        scrollTable.add(new Label("Простые", new Label.LabelStyle(game.fontsHash.get("big"), Color.BLACK)));
        scrollTable.row();

        Texture uitex = new Texture(Gdx.files.internal("data/uihalf.png"));
        uitex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np = new NinePatch(uitex, 8, 8, 8, 8);
        final NinePatchDrawable npd_50 = new NinePatchDrawable(np);

        Texture uitex2 = new Texture(Gdx.files.internal("data/ui75.png"));
        uitex2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np2 = new NinePatch(uitex2, 8, 8, 8, 8);
        final NinePatchDrawable npd_75 = new NinePatchDrawable(np2);

        Texture uitex3 = new Texture(Gdx.files.internal("data/ui90.png"));
        uitex3.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np3 = new NinePatch(uitex3, 8, 8, 8, 8);
        final NinePatchDrawable npd_90 = new NinePatchDrawable(np3);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(npd_50, npd_75, npd_50, game.fontsHash.get("semi-big"));
        TextButton tb = new TextButton("Черепаха", tbs);
        tb.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen( new PlayScreen(game, Field.Figure.Turtle) );
                return true;
            };
        });
        scrollTable.add(tb);

        TextButton tb2 = new TextButton("Три вершины", tbs);
        tb2.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen( new PlayScreen(game, Field.Figure.TriPeaks) );
                return true;
            };
        });
        scrollTable.add(tb2);

        scrollTable.row();

        scrollTable.add(new Label("Средние", new Label.LabelStyle(game.fontsHash.get("big"), Color.BLACK)));
        scrollTable.row();
        scrollTable.add(new Label("Сложные", new Label.LabelStyle(game.fontsHash.get("big"), Color.BLACK)));
        scrollTable.row();
        scrollTable.add(new Label("Эксперт", new Label.LabelStyle(game.fontsHash.get("big"), Color.BLACK)));
        scrollTable.row();

        tbl.add(sp).fill().expand();

        //tbl.setDebug(true);

        stage.addActor(tbl);
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
