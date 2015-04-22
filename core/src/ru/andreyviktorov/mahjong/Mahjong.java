package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
    Хранилища:
    leveldata - сохраненные игры
    statdata - статистика
    appdata - системная информация о приложении (например, версия)
 */

public class Mahjong extends Game implements Serializable {
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"´`'<>";
    Map<String, BitmapFont> fontsHash = new HashMap<String, BitmapFont>();
    public float tenth;
    public float twentyth;


	@Override
	public void create () {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/gameui/PTN77F.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font;

        this.tenth = Gdx.graphics.getHeight() / 10;
        this.twentyth = Gdx.graphics.getHeight() / 20;

        param.size = Math.round(Gdx.graphics.getHeight() / 20);
        param.characters = FONT_CHARACTERS;
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("big", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 22);
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("semi-big", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 24);
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("medium", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 30);
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("semi-medium", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 35);
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("small", font);

        param.size = Math.round(Gdx.graphics.getHeight() / 10);
        font = generator.generateFont(param);
        font.setColor(Color.DARK_GRAY);

        fontsHash.put("logo", font);

        generator.dispose();
        Static.mahjong = this;
        setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
