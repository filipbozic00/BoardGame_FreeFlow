package com.mygdx.game.freeflow.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.freeflow.MyGdxGame;
import com.mygdx.game.freeflow.assets.AssetDescriptors;
import com.mygdx.game.freeflow.assets.RegionNames;

public class LeaderBoradScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;

    private float duration = 0f;

    private Stage stage;
    private Skin skin;

    public LeaderBoradScreen(MyGdxGame game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());
        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        Table contentTable = new Table(skin);

        contentTable.add(new Label("Player: Filip, Score: 500", skin)).padBottom(50).colspan(2).row();
        contentTable.add(new Label("Player: Filip, Score: 320", skin)).padBottom(50).colspan(2).row();
        contentTable.add(new Label("Player: Filip, Score: 480", skin)).padBottom(50).colspan(2).row();
        contentTable.add(new Label("Player: Filip, Score: 222", skin)).padBottom(50).colspan(2).row();
        contentTable.add(new Label("Player: Filip, Score: 365", skin)).padBottom(50).colspan(2).row();

        contentTable.add(backButton).width(100).padTop(50).colspan(2);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();
        return table;
    }
}
