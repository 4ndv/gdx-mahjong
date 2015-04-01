package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class Mahjong extends Game {
    public BitmapFont font, levels;
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

	@Override
	public void create () {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ls-reg.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Gdx.graphics.getHeight() / 18;
        param.characters = FONT_CHARACTERS;
        font = generator.generateFont(param);
        param.size = Gdx.graphics.getHeight() / 20;
        levels = generator.generateFont(param);
        font.setColor(Color.WHITE);
        levels.setColor(Color.WHITE);
        generator.dispose();
        setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
