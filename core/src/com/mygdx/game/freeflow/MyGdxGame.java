package com.mygdx.game.freeflow;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.freeflow.screen.IntroScreen;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;
	private AssetManager assetManager;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();

		setScreen(new IntroScreen(this));
	}

	/*@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}*/
	
	@Override
	public void dispose () {
		assetManager.dispose();
		batch.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
	public SpriteBatch getBatch() {
		return batch;
	}
}
