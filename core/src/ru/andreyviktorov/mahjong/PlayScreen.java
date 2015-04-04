package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayScreen implements Screen {
    public static Stage stage;
    final Mahjong game;
    public static float SCALE_RATIO = 128 / Gdx.graphics.getWidth();
    public static float TILE_WIDTH;
    public static float TILE_HEIGHT;
    public static Field field;
    public static TileActor selected;
    public static List<List<Image>> shadows = new ArrayList();
    public Texture shadowtexture;
    public Texture glowtexture;
    public static Image glowimg;
    public static Image[][][] shadowimgs;

    public PlayScreen (final Mahjong gameref) {
        game = gameref;

        stage = new Stage(new ScreenViewport());
        Generator gen = new Generator();
        field = gen.generateFigure();

        glowtexture = new Texture(Gdx.files.internal("data/tiles/TileSelected.png"));
        glowimg = new Image(glowtexture);
        glowimg.setPosition(-500, -500);
        shadowimgs = new Image[field.getWidth()+1][field.getHeight()+1][field.layers.size()+1];

        shadowtexture = new Texture(Gdx.files.internal("data/tiles/TileShadow.png"));
        shadowtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        float fieldWidth;
        float fieldHeight;
        float fieldPosX;
        float fieldPosY;

        // z-index
        float g_offset_y = 0;
        int layernumber = 0;
        List<TileActor> layerPool;
        List<Image> shadowPool;
        for(Layer layer : field.layers) {
            layerPool = new LinkedList();
            shadowPool = new LinkedList();
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
                        layer.data[i][j].tiledata.y = fieldPosY + TILE_HEIGHT * j/2 - g_offset_y;

                        Image img = new Image(shadowtexture);
                        // Еще одна магическая константа: 102/148
                        img.setSize(Gdx.graphics.getHeight() * 12 / 100 * 0.68918918918F, Gdx.graphics.getHeight() * 12 / 100);
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
                stage.addActor(img);
            }
            for(TileActor act : layerPool) {
                stage.addActor(act);
            }

            System.out.println("Finished layer " + layernumber);
            layernumber++;
            g_offset_y += TILE_HEIGHT/4.8;
        }

        // Must be on top of any else
        stage.addActor(glowimg);

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
        this.field = null;
        this.selected = null;
        this.stage = null;
    }
}
