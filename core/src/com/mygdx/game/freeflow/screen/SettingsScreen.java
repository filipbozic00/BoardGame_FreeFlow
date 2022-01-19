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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.freeflow.MyGdxGame;
import com.mygdx.game.freeflow.assets.AssetDescriptors;
import com.mygdx.game.freeflow.assets.RegionNames;
import com.mygdx.game.freeflow.common.GameManager;

public class SettingsScreen extends ScreenAdapter {
    private final MyGdxGame game;
    private final AssetManager assetManager;
    //private final Preferences pref ;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas scene2dAtlas;
    private GameManager gameManager = new GameManager();

    public SettingsScreen(MyGdxGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        scene2dAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        Gdx.input.setInputProcessor(stage);
        stage.addActor(createUi());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0.3f, 0.9f, 0f);

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
        //table.setColor(Color.BLUE);
        TextureRegion backgroundRegion = scene2dAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        final SelectBox<Integer> selectBox = new SelectBox<>(assetManager.get(AssetDescriptors.UI_SKIN));
        selectBox.setItems(3, 2, 6);
        ChangeListener selectListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Integer newValue = selectBox.getSelected();
                valueChanged(newValue);
            }
        };
        selectBox.addListener(selectListener);
        final SelectBox<String> musicBox = new SelectBox<>(assetManager.get(AssetDescriptors.UI_SKIN));
        musicBox.setItems("On", "Off");
        ChangeListener musicListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String newValue = musicBox.getSelected();
                musicChanged(newValue);
            }
        };
        musicBox.addListener(musicListener);
        final SelectBox<String> soundBox = new SelectBox<>(assetManager.get(AssetDescriptors.UI_SKIN));
        soundBox.setItems("On", "Off");
        ChangeListener soundListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String newValue = soundBox.getSelected();
                soundChanged(newValue);
            }
        };
        soundBox.addListener(soundListener);


        Table buttonTable = new Table();

        buttonTable.defaults().padLeft(30).padRight(30).size(70, 80);
        // buttonTable.setBackground(new TextureRegionDrawable(backgroundRegion));
        // TextureRegion menuBackgroundRegion = scene2dAtlas.findRegion(RegionNames.BACKGROUND);
        //  buttonTable.setBackground(new TextureRegionDrawable(menuBackgroundRegion));
        Label sizeOfField = new Label("Field size:\t", assetManager.get(AssetDescriptors.UI_SKIN));

        buttonTable.add(sizeOfField);
        //selectBox.setSelected(pref.getInteger("arraySize",5));
        selectBox.setSelected(gameManager.getPlayingFieldSize());
        buttonTable.add(selectBox);
        Label music = new Label("Game music:\t", assetManager.get(AssetDescriptors.UI_SKIN));
        buttonTable.row();
        buttonTable.add(music);
        musicBox.setSelected(gameManager.getMusic());
        //musicBox.setSelected(pref.getString("music","Off"));
        buttonTable.add(musicBox);
        buttonTable.row();
        Label sound = new Label("Menu music:\t", assetManager.get(AssetDescriptors.UI_SKIN));
        buttonTable.row();
        buttonTable.add(sound);
        soundBox.setSelected(gameManager.getSound());
        //soundBox.setSelected(pref.getString("sound","Off"));
        buttonTable.add(soundBox);
        //buttonTable.sizeBy(5);

        buttonTable.center();
        table.add(buttonTable);
        table.row();
        table.add(backButton).fillX();
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void valueChanged(Integer newValue) {
        gameManager.setPlayingFieldSize(newValue);
    }

    private void musicChanged(String newValue) {
        gameManager.setMusic(newValue);
    }

    private void soundChanged(String newValue) {
        gameManager.setSound(newValue);
    }
}
