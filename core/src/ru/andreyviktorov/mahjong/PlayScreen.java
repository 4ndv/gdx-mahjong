package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PlayScreen implements Screen {
    private Stage stage;
    final Mahjong game;
    public static float SCALE_RATIO = 128 / Gdx.graphics.getWidth();
    public static float TILE_WIDTH;
    public static float TILE_HEIGHT;
    public static Field field;
    public static TileActor selected;

    public PlayScreen (final Mahjong gameref) {
        game = gameref;

        stage = new Stage(new ScreenViewport());
        Generator gen = new Generator();
        field = gen.generateFigure();

        float fieldWidth;
        float fieldHeight;
        float fieldPosX;
        float fieldPosY;

        // z-index
        int offset = 0;
        int layernumber = 0;
        for(Layer layer : field.layers) {
            for(int i = 0; i<field.getWidth(); i++) {
                for(int j = 0; j<field.getHeight(); j++) {
                    if(layer.data[i][j] != null) {
                        // Получаем шириновысоту
                        /*System.out.println(layer.data[i][j].getHeight());
                        TILE_WIDTH = layer.data[i][j].getWidth();
                        TILE_HEIGHT = layer.data[i][j].getHeight();*/

                        // +2 для отступов
                        fieldWidth = TILE_WIDTH * ((field.getWidth() + 2) / 2) - TILE_WIDTH;
                        fieldHeight = TILE_HEIGHT * ((field.getHeight() + 2) / 2) - TILE_HEIGHT;

                        //System.out.println(layer.data[i][j].tile.suit.name());

                        //System.out.println("TILE_W " + TILE_WIDTH + " TILE_H " + TILE_HEIGHT + " FW " + fieldWidth + " FH " + fieldHeight);

                        fieldPosX = Gdx.graphics.getWidth()/2 - fieldWidth / 2;
                        fieldPosY = Gdx.graphics.getHeight()/2 - fieldHeight / 2;

                        layer.data[i][j].tiledata.datax = i;
                        layer.data[i][j].tiledata.datay = j;
                        layer.data[i][j].tiledata.layer = layernumber;
                        
                        layer.data[i][j].tiledata.x = fieldPosX + TILE_WIDTH * i/2;
                        layer.data[i][j].tiledata.y = fieldPosY + TILE_HEIGHT * j/2 - offset;
                        //layer.data[i][j].setPosition(fieldPosX + TILE_WIDTH * i/2, fieldPosY + TILE_HEIGHT * j/2);
                        //System.out.println("Draw at " + (fieldPosX + TILE_WIDTH * i/2) + " " + (fieldPosY + TILE_HEIGHT * j/2));
                        stage.addActor(layer.data[i][j]);
                    }
                }
            }
            System.out.println("Finished layer " + layernumber);
            layernumber++;
            offset += TILE_HEIGHT/4.8;
        }

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
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
