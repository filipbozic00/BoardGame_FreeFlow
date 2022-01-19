package com.mygdx.game.freeflow.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.freeflow.CellActor;
import com.mygdx.game.freeflow.CellState;
import com.mygdx.game.freeflow.MyGdxGame;
import com.mygdx.game.freeflow.assets.AssetDescriptors;
import com.mygdx.game.freeflow.assets.RegionNames;
import com.mygdx.game.freeflow.common.GameManager;

import java.util.Random;

public class GameScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private GlyphLayout layout;
    private BitmapFont font;
    private Stage gameplayStage;
    private Stage hudStage;
    private TextureAtlas gameplayAtlas;

    private Viewport viewport;
    private Viewport hudViewport;
    private Music music;
    private Sound onClickImg;

    TextureAtlas scene2dAtlas;
    private Image infoImage;
    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);


    private TextureRegion starRegion;
    private final GameManager gameManager = new GameManager();
    private CellState move = GameManager.INSTANCE.getInitMove();
    private int playingFieldSize;
    private int[][] rowValues;
    private int[][] columnValues;
    private int[] columnsFinal;
    private int[] rowsFinal;
    private Array<TextureRegion> numbers;
    private Skin skin;
    private long startTime;
    private float resultTime;
    Random rand = new Random();


    public GameScreen(MyGdxGame game) {
        music = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\filip\\AndroidStudioProjects\\Free_flow\\android\\assets\\sounds\\bass.wav"));
        onClickImg = Gdx.audio.newSound(Gdx.files.internal("C:\\Users\\filip\\AndroidStudioProjects\\Free_flow\\android\\assets\\sounds\\shortDrum.wav"));
        music.setLooping(true);

        if (gameManager.getMusicB()) {
            music.play();
        }
        startTime = System.currentTimeMillis();
        playingFieldSize = gameManager.getPlayingFieldSize();
        rowValues = new int[playingFieldSize][playingFieldSize];
        columnValues = new int[playingFieldSize][playingFieldSize];
        rowsFinal = new int[playingFieldSize];
        columnsFinal = new int[playingFieldSize];
        for (int i = 0; i < playingFieldSize; i++) {
            for (int j = 0; j < playingFieldSize; j++) {
                rowValues[i][j] = 0;
                columnValues[i][j] = 0;
            }
            // rowsFinal[i] = 0;
            //columnsFinal[i] = 0;
        }
        numbers = new Array<TextureRegion>();
        //generateGrid();
        this.game = game;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
    }

    @Override
    public void show() {
        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        scene2dAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        //gameplayStage.addActor(createGrid(25));
        //gameplayStage.addActor(generateGrid());
        gameplayStage.addActor(createGrid(3, 3, 5));
        hudStage = new Stage(hudViewport, game.getBatch());
        // hudStage.addActor(createInfo());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));

        // layout = new GlyphLayout();
        font = new BitmapFont();
        font.getData().markupEnabled = true;

        //layout.setText(font, "Press [GREEN]'space'[] to return to the menu screen");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 0f);

        handleInput();

        gameplayStage.act(delta);
        hudStage.act(delta);
        //hudStage.addActor(createInfo());

        // draw
        gameplayStage.draw();
        hudStage.draw();
        //createGrid(3, 3, 5);
        //generateGrid();
        //renderGameplay();
        //renderHud();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) game.setScreen(new MenuScreen(game));

    }

    private void renderGameplay() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(starRegion,
                GameConfig.WORLD_WIDTH / 2f - GameConfig.STAR_SIZE / 2f, GameConfig.WORLD_HEIGHT / 2f - GameConfig.STAR_SIZE / 2f,
                GameConfig.STAR_SIZE, GameConfig.STAR_SIZE
        );

        batch.end();
    }

    private void renderHud() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        font.draw(batch, layout, GameConfig.WIDTH / 2f - layout.width / 2f, 20f);

        batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        font.dispose();
        gameplayStage.dispose();
        hudStage.dispose();
        music.dispose();
        onClickImg.dispose();
    }


    private Actor createGrid(int rows, int columns, final float cellSize) {
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)

        final Table grid = new Table();


        grid.defaults().size(cellSize, cellSize);   // all cells will be the same size
        grid.setDebug(false);

        numbers.add(scene2dAtlas.findRegion(RegionNames.RED_CIRCLE));
        numbers.add(scene2dAtlas.findRegion(RegionNames.GREEN_CIRCLE));
        numbers.add(scene2dAtlas.findRegion(RegionNames.BLUE_CIRCLE));
        numbers.add(scene2dAtlas.findRegion(RegionNames.YELLOW_CIRCLE));
        //  return table;

        final TextureRegion emptyRegion = scene2dAtlas.findRegion(RegionNames.WHITE_CIRCLE);
        final TextureRegion connection = scene2dAtlas.findRegion(RegionNames.SQUARE);
        final TextureRegion redRegion = scene2dAtlas.findRegion(RegionNames.RED_CIRCLE);
        final TextureRegion blueRegion = scene2dAtlas.findRegion(RegionNames.BLUE_CIRCLE);
        final TextureRegion greenRegion = scene2dAtlas.findRegion(RegionNames.GREEN_CIRCLE);
        final TextureRegion yellowRegion = scene2dAtlas.findRegion(RegionNames.YELLOW_CIRCLE);
        CellActor cell;

        int randomNumx = rand.nextInt((playingFieldSize) - 1);
        int randomNumy = rand.nextInt((playingFieldSize) - 1);
        if(randomNumy == randomNumx) randomNumy--;

        int randomNum2x = rand.nextInt((playingFieldSize) - 1);
        int randomNum2y = rand.nextInt((playingFieldSize) - 1);
        if (randomNum2x == randomNumx) randomNum2x--;
        if(randomNum2y == randomNum2x) randomNum2y--;

        int randomNum3x = rand.nextInt((playingFieldSize) - 1);
        int randomNum3y = rand.nextInt((playingFieldSize) - 1);
        if (randomNum3x == randomNum2x || randomNum3x == randomNumx) randomNum3x--;
        if(randomNum3y == randomNum3x) randomNum3y--;

        int randomNum4x = rand.nextInt((playingFieldSize) - 1);
        int randomNum4y = rand.nextInt((playingFieldSize) - 1);
        if (randomNum4x == randomNum3x || randomNum4x == randomNum2x || randomNum4x == randomNumx) randomNum4x--;
        if(randomNum4y == randomNum4x) randomNum4y--;


        for (int row = 0; row < playingFieldSize; row++) {
            for (int column = 0; column < playingFieldSize; column++) {



                cell = new CellActor(emptyRegion);
                final int clickedColumn = column;
                final int clickedRow = row;

                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final CellActor clickedCell = (CellActor) event.getTarget();
                        final CellActor clickedCell2 = (CellActor) event.getTarget();
                        // it will be an image for sure :-)
                        if (clickedCell.isEmpty()) {
                            clickedCell.setState(CellState.X);
                            clickedCell.setDrawable(connection);
                            // grid.add(new Image(numbers.get(rowsFinal[row])));
                            //columnValues[clickedRow][clickedColumn] = (clickedRow + 1);
                            //rowValues[clickedRow][clickedColumn] = (clickedColumn + 1);
                            if (gameManager.getMusicB()) {
                                onClickImg.play();
                            }

                        }

                        else {
                            clickedCell.setState(CellState.EMPTY);
                            clickedCell.setDrawable(emptyRegion);
                            columnValues[clickedRow][clickedColumn] = 0;
                            rowValues[clickedRow][clickedColumn] = 0;
                            //  if(gameManager.getSoundB()){
                            //    hitBlack.play();
                            //}
                        }
                      // evaluate();
                    }
                });
                if(playingFieldSize == 6) {
                    if (row == randomNumx && column == randomNumy) {
                        cell = new CellActor(yellowRegion);
                    }
                    if (row == randomNum2x && column == randomNum2y) {
                        cell = new CellActor(blueRegion);
                    }
                    if (row == randomNum3x && column == randomNum3y) {
                        cell = new CellActor(greenRegion);
                    }
                    if (row == randomNum4x && column == randomNum4y) {
                        cell = new CellActor(redRegion);
                    }

                    if (column == randomNumx && row == randomNumy) {
                        cell = new CellActor(yellowRegion);
                    }
                    if (column == randomNum2x && row == randomNum2y) {
                        cell = new CellActor(blueRegion);
                    }
                    if (column == randomNum3x && row == randomNum3y) {
                        cell = new CellActor(greenRegion);
                    }
                    if (column == randomNum4x && row == randomNum4y) {
                        cell = new CellActor(redRegion);
                    }
                }
                if(playingFieldSize == 3){
                    if (row == randomNumx+1 && column == randomNumy) {
                        cell = new CellActor(yellowRegion);
                    }
                    if (row == randomNum2x && column == randomNum2y) {
                        cell = new CellActor(blueRegion);
                    }

                    if (column == randomNumx && row == randomNumy+1) {
                        cell = new CellActor(yellowRegion);
                    }
                    if (column == randomNum2x && row == randomNum2y) {
                        cell = new CellActor(blueRegion);
                    }

                }
                  grid.add(cell);

            }
            //grid.add(new Image(numbers.get(rowsFinal[row])));
            // grid.add(new Image(numbers.get(0)));
            grid.row();
        }
/*        int counter = 0;
        for (int row = 0; row < 4; row++) {
            int row_index = rand.nextInt((playingFieldSize) - 1);
            int column_index = rand.nextInt((playingFieldSize) - 1);

            if(counter == 0)
            {
                cell = new CellActor(redRegion);
            }


        }*/

        // for (int column = 0; column < playingFieldSize; column++) {
        //      grid.add(new Image(numbers.get(columnsFinal[column] - 1)));
        //grid.add(new Image(numbers.get(0)));

        //   }
        grid.row();
        table.add(grid).row();
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;

    }

    private void generateGrid() {
        int random;
        for (int i = 0; i < playingFieldSize; i++) {
            random = MathUtils.random(playingFieldSize - 1) + 1;
            while (random <= playingFieldSize) {
                rowValues[i][random - 1] = random; // on row i, column random, value for row is random
                rowsFinal[i] += random;
                columnValues[i][random - 1] = i + 1; // on row i, column random, value for column is i
                columnsFinal[random - 1] += i + 1;
                random += MathUtils.random(playingFieldSize - 1) + 1; //fills rowValues with random values
            }
        }
        for (int i = 0; i < playingFieldSize; i++) {
            if (columnsFinal[i] == 0) {
                random = MathUtils.random(playingFieldSize - 1) + 1;
                columnsFinal[i] += random;
                rowsFinal[random - 1] += i + 1;
                rowValues[random - 1][i] = i + 1;
                columnValues[random - 1][i] = random - 1;
            }
        }
        System.out.println("Rows");
        for (int i = 0; i < playingFieldSize; i++) {
            for (int j = 0; j < playingFieldSize; j++) {
                System.out.print(rowValues[i][j]);
                rowValues[i][j] = 0;
                columnValues[i][j] = 0;
            }
            System.out.println();
        }
        for (int i = 0; i < playingFieldSize; i++) {
            if (rowsFinal[i] >= 20 || columnsFinal[i] >= 20) {
                for (int j = 0; j < playingFieldSize; j++) {
                    rowsFinal[j] = 0;
                    columnsFinal[j] = 0;
                }
                generateGrid();
            }
        }
    }


    private Boolean evaluate() {
        int tmpRow = 0;
        int tmpColumn = 0;
        for (int i = 0; i < playingFieldSize; i++) {
            tmpRow = 0;
            tmpColumn = 0;
            for (int j = 0; j < playingFieldSize; j++) {
                tmpRow += rowValues[i][j];
                tmpColumn += columnValues[j][i];
            }
            if (tmpRow != rowsFinal[i] || tmpColumn != columnsFinal[i]) {
                return false;
            }
        }
        gameManager.resultList.results.add(resultTime);
        gameManager.resultList.sort();
        /*try {
            gameManager.saveToJson(gameManager.resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        game.setScreen(new LeaderBoradScreen(game, assetManager));
        return true;

    }


    private Actor createInfo() {
        final Table table = new Table();
        table.add(new Label("Time: ", skin));
        table.add(new TextField(String.valueOf(Float.valueOf(System.currentTimeMillis() - startTime) / 1000), skin)).row();
        resultTime = Float.valueOf(System.currentTimeMillis() - startTime) / 1000;
        table.center();
        table.pack();
        table.setPosition(
                GameConfig.WIDTH / 2f - table.getWidth() / 2f,
                GameConfig.HEIGHT - table.getHeight() - 20f
        );
        return table;
    }
}