package com.mygdx.game.freeflow.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.freeflow.MyGdxGame;
import com.mygdx.game.freeflow.assets.AssetDescriptors;
import com.mygdx.game.freeflow.assets.RegionNames;

public class IntroScreen extends ScreenAdapter {
    public static final float INTRO_DURATION_IN_SEC = 3f;   // duration of the (intro) animation

    private final MyGdxGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(MyGdxGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();   // blocks until all assets are loaded

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createUi());
        //stage.addActor(createKeyhole());
        stage.addActor(createAnimationRed());
        stage.addActor(createAnimationGreen());
        stage.addActor(createAnimationBlue());
        stage.addActor(createAnimationYellow());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0 / 255f, 0 / 255f, 0 / 255f, 0f);
       // createUi();
        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
        }

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


    private Actor createAnimationRed() {
        Image red = new Image(gameplayAtlas.findRegion(RegionNames.RED_CIRCLE));
        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_CIRCLE));
        Image blue = new Image(gameplayAtlas.findRegion(RegionNames.BLUE_CIRCLE));
        Image yellow = new Image(gameplayAtlas.findRegion(RegionNames.YELLOW_CIRCLE));
        // set positions x, y to center the image to the center of the window
        float posX = (viewport.getWorldWidth() / 2f) - red.getWidth() / 2f;
        float posY = (viewport.getWorldHeight() / 2f) - red.getHeight() / 2f;
        red.setPosition(viewport.getWorldWidth(), viewport.getWorldHeight());
        red.setOrigin(Align.right);
        red.addAction(
                /* animationDuration = Actions.sequence + Actions.rotateBy + Actions.scaleTo
                                      = 1.5 + 1 + 0.5 = 3 sec */
                Actions.sequence(
                        Actions.parallel(

                                Actions.rotateBy(1080, 1.5f),   // rotate the image three times
                                Actions.moveTo(posX, posY, 1.5f)   // // move image to the center of the window
                        ),
                        Actions.rotateBy(-360, 1),  // rotate the image for 360 degrees to the left side
                        Actions.scaleTo(0, 0, 0.5f),    // "minimize"/"hide" image
                        Actions.removeActor()   // // remove image
                )
        );

        return red;
    }

    private Actor createAnimationGreen() {

        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_CIRCLE));


        // set positions x, y to center the image to the center of the window
        float posX = (viewport.getWorldWidth() / 2f) - green.getWidth() / 2f;
        float posY = (viewport.getWorldHeight() / 2f) - green.getHeight() / 2f;

        green.setOrigin(Align.left);
        green.addAction(
                /* animationDuration = Actions.sequence + Actions.rotateBy + Actions.scaleTo
                                      = 1.5 + 1 + 0.5 = 3 sec */
                Actions.sequence(
                        Actions.parallel(

                                Actions.rotateBy(1080, 1.5f),   // rotate the image three times
                                Actions.moveTo(posX, posY, 1.5f)   // // move image to the center of the window
                        ),
                        Actions.rotateBy(-360, 1),  // rotate the image for 360 degrees to the left side
                        Actions.scaleTo(0, 0, 0.5f),    // "minimize"/"hide" image
                        Actions.removeActor()   // // remove image
                )
        );

        return green;
    }

    private Actor createAnimationBlue() {

        Image blue = new Image(gameplayAtlas.findRegion(RegionNames.BLUE_CIRCLE));


        // set positions x, y to center the image to the center of the window
        float posX = (viewport.getWorldWidth() / 2f) - blue.getWidth() / 2f;
        float posY = (viewport.getWorldHeight() / 2f) - blue.getHeight() / 2f;
        blue.setPosition(viewport.getWorldWidth(), viewport.getWorldHeight() / 50f);
        blue.setOrigin(Align.top);
        blue.addAction(
                /* animationDuration = Actions.sequence + Actions.rotateBy + Actions.scaleTo
                                      = 1.5 + 1 + 0.5 = 3 sec */
                Actions.sequence(
                        Actions.parallel(

                                Actions.rotateBy(1080, 1.5f),   // rotate the image three times
                                Actions.moveTo(posX, posY, 1.5f)   // // move image to the center of the window
                        ),
                        Actions.rotateBy(-360, 1),  // rotate the image for 360 degrees to the left side
                        Actions.scaleTo(0, 0, 0.5f),    // "minimize"/"hide" image
                        Actions.removeActor()   // // remove image
                )
        );

        return blue;
    }

    private Actor createAnimationYellow() {

        Image yellow = new Image(gameplayAtlas.findRegion(RegionNames.YELLOW_CIRCLE));


        // set positions x, y to center the image to the center of the window
        float posX = (viewport.getWorldWidth() / 2f) - yellow.getWidth() / 2f;
        float posY = (viewport.getWorldHeight() / 2f) - yellow.getHeight() / 2f;
        yellow.setPosition(viewport.getWorldWidth() / 50f, viewport.getWorldHeight());
        yellow.setOrigin(Align.bottom);
        yellow.addAction(
                /* animationDuration = Actions.sequence + Actions.rotateBy + Actions.scaleTo
                                      = 1.5 + 1 + 0.5 = 3 sec */
                Actions.sequence(
                        Actions.parallel(

                                Actions.rotateBy(1080, 1.5f),   // rotate the image three times
                                Actions.moveTo(posX, posY, 1.5f)   // // move image to the center of the window
                        ),
                        Actions.rotateBy(-360, 1),  // rotate the image for 360 degrees to the left side
                        Actions.scaleTo(0, 0, 0.5f),    // "minimize"/"hide" image
                        Actions.removeActor()   // // remove image
                )
        );

        return yellow;
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        table.center();
        table.setFillParent(true);
        table.pack();
        return table;
    }
}
