package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        Texture logotex = new Texture(Gdx.files.internal("data/menu/logo.png"));
        logotex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image logoimg = new Image(logotex);
        float logoh = logoimg.getHeight();
        logoimg.setHeight(game.tenth * 3);
        logoimg.setWidth(logoimg.getWidth() * (logoimg.getHeight() / logoh));
        logoimg.setX(Gdx.graphics.getWidth() / 2F - logoimg.getWidth() / 2F);
        logoimg.setY(Gdx.graphics.getHeight() - game.tenth * 2 - logoimg.getHeight());
        stage.addActor(logoimg);

        Texture uitex = new Texture(Gdx.files.internal("data/uihalf.png"));
        uitex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch np = new NinePatch(uitex, 8, 8, 8, 8);
        NinePatchDrawable npd = new NinePatchDrawable(np);
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(npd, npd, npd, game.fontsHash.get("big"));
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
