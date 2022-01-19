package com.mygdx.game.freeflow.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.freeflow.CellState;
import com.mygdx.game.freeflow.ListHolder;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    public int playingFieldSize;
    private static Boolean music = Boolean.TRUE;
    private static Boolean sound = Boolean.TRUE;
    private final Preferences PREFS;
    public ListHolder resultList;

    private static final String INIT_MOVE_KEY = "initMove";
    private CellState initMove = CellState.X;

    public GameManager() {
        PREFS = Gdx.app.getPreferences("Settings");
        playingFieldSize = PREFS.getInteger("arraySize", 5);
        music = PREFS.getString("music", "Off").equals("On");
        sound = PREFS.getString("sound", "Off").equals("On");
        /*try {
            //resultList = readFromJson();
        } catch (FileNotFoundException e) {
            resultList = new ListHolder();
            try {
                //saveToJson(resultList);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }*/

    }


    public void setMusic(String newVal) {
        PREFS.putString("music", newVal);
        music = PREFS.getString("music", "Off").equals("On");
        PREFS.flush();
    }

    public void setSound(String newVal) {
        PREFS.putString("sound", newVal);
        sound = PREFS.getString("sound", "Off").equals("On");
        PREFS.flush();
    }

    public void setPlayingFieldSize(int newVal) {
        PREFS.putInteger("arraySize", newVal);
        playingFieldSize = PREFS.getInteger("arraySize", 5);
        PREFS.flush();
    }

    public String getMusic() {
        if (music) {
            return "On";
        }
        return "Off";
    }

    public String getSound() {
        if (sound) {
            return "On";
        }
        return "Off";
    }

    public Boolean getMusicB() {
        return music;
    }

    public Boolean getSoundB() {
        return sound;
    }

    public Integer getPlayingFieldSize() {
        return playingFieldSize;
    }

    /*public static void saveToJson(ListHolder playerList) throws IOException {
        Gson json = new Gson();
        String pl = json.toJson(playerList);

        File jsonFile = new File("desktop", "leaderboard.json");
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(pl);
        writer.close();
    }

    public static ListHolder readFromJson() throws FileNotFoundException {
        Gson json = new Gson();
        ListHolder returnVal = json.fromJson(new FileReader("desktop\\leaderboard.json"), ListHolder.class);
        return returnVal;
    }*/
    public CellState getInitMove() {
        return initMove;
    }

    public void setInitMove(CellState move) {
        initMove = move;

        PREFS.putString(INIT_MOVE_KEY, move.name());
        PREFS.flush();
    }
}
