package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;


public class Mahjong extends Game {
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"´`'<>";
    Map<String, BitmapFont> fontsHash = new HashMap<String, BitmapFont>();
    public float tenth;
    public float twelveth;


	@Override
	public void create () {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ls-reg.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font;

        this.tenth = Gdx.graphics.getHeight() / 10;
        this.twelveth = Gdx.graphics.getHeight() / 20;

        param.size = Math.round(Gdx.graphics.getHeight() / 22);
        param.characters = FONT_CHARACTERS;
        font = generator.generateFont(param);
        font.setColor(Color.WHITE);

        fontsHash.put("big", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 24);
        font = generator.generateFont(param);
        font.setColor(Color.WHITE);

        fontsHash.put("semi-big", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 30);
        font = generator.generateFont(param);
        font.setColor(Color.WHITE);

        fontsHash.put("medium", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 35);
        font = generator.generateFont(param);
        font.setColor(Color.WHITE);

        fontsHash.put("semi-medium", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 40);
        font = generator.generateFont(param);
        font.setColor(Color.WHITE);

        fontsHash.put("small", font);

        generator.dispose();
        setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
