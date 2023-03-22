package my.edu.utar.individual;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class GameResult extends AppCompatActivity implements player_name_dialog.ExampleDialogListener {

    ImageButton exitButton;
    ImageButton replayButton;
    TextView tvPoints;
    TextView tvHighest;
    static final int maxRow = 25;
    int highest;
    static int scores;
    ImageView ivNewHighest;
    static TableRow ranking[] = new TableRow[maxRow];
    static TextView rankingName[] = new TextView[maxRow];
    static TextView rankingPoint[] = new TextView[maxRow];
    static SharedPreferences sharedPreferences;
    public static Context context;
    public static int ScoreList [] = new int[25];
    public static String NameList [] = new String[25];
    static ArrayList<Integer> ScoreList_finalize = new ArrayList<Integer>();
    static ArrayList<String> NameList_finalize = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        context = this;
        initialize();

        // let player enter their name after game
        openDialog();

        // get the scores data
        scores = getIntent().getExtras().getInt("scores");
        String playerName = "";
        applyTexts(playerName);


        // Initialize the sharedPreferences
        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setFieldsFromSavedData();

        // Update the highest marks
        tvPoints.setText("" + scores);
        if (scores > highest) {
            ivNewHighest.setVisibility(View.VISIBLE);
            highest = scores;

            editor.putInt("highest", highest);
            editor.commit();
        }
        tvHighest.setText("" + highest);

        replayButton.setOnClickListener(replayOnClick);
        exitButton.setOnClickListener(exitOnClick);
    }

    protected View.OnClickListener replayOnClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            GamePattern gamePattern = new GamePattern(GameResult.this);
            setContentView(gamePattern);
        }
    };

    protected View.OnClickListener exitOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            System.exit(0);
        }
    };

    public void openDialog() {
        player_name_dialog playerNameDialog = new player_name_dialog();
        playerNameDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public String applyTexts(String player) {
        return player;
    }

    public void initialize() {
        exitButton = findViewById(R.id.exitButton);
        replayButton = findViewById(R.id.replayButton);
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);



        for (int i = 0; i < 25; i++) {
            Resources res = getResources();
            int rankingID = res.getIdentifier("ranking" + (i+1), "id", getApplicationContext().getPackageName());
            int rankingNameID = res.getIdentifier("ranking" + (i+1) + "Name", "id", getApplicationContext().getPackageName());
            int rankingPointID = res.getIdentifier("ranking" + (i+1) + "Point", "id", getApplicationContext().getPackageName());
            ranking[i] = findViewById(rankingID);
            rankingName[i] = findViewById(rankingNameID);
            rankingPoint[i] = findViewById(rankingPointID);
        }

    }

    public void setFieldsFromSavedData() {
        highest = sharedPreferences.getInt("highest", 0);

        //get the score and name in list
        NameList_finalize.clear();
        ScoreList_finalize.clear();
        for (int i = 0; i < 25; i++) {
            ScoreList[i] = sharedPreferences.getInt("ranking" + (i+1) + "Score", 0);
            NameList[i] = sharedPreferences.getString("ranking" + (i+1) + "Name", "" );

            if (ScoreList[i] != 0) {
                ScoreList_finalize.add(ScoreList[i]);
            }
            if (!NameList[i].equals("")) {
                NameList_finalize.add(NameList[i]);
            }
        }
    }

    public static void sorting() {
        // another initialize
        int Score2;
        String Name2;

        if (ScoreList_finalize.size() != 1) {
            for (int i = 0; i < ScoreList_finalize.size(); i++) {
                for (int j = i + 1; j < ScoreList_finalize.size(); j++) {
                    if ( ScoreList_finalize.get(i) < ScoreList_finalize.get(j)) {

                        Score2 = ScoreList_finalize.get(i);
                        ScoreList_finalize.set(i, ScoreList_finalize.get(j)) ;
                        ScoreList_finalize.set(j, Score2);

                        Name2 = NameList_finalize.get(i);
                        NameList_finalize.set(i, NameList_finalize.get(j));
                        NameList_finalize.set(j, Name2);
                    }
                }
            }
            while (ScoreList_finalize.size() > 25) {
                ScoreList_finalize.remove(ScoreList_finalize.size()-1);
                NameList_finalize.remove(NameList_finalize.size()-1);
            }
        }

    }

    public static void updateRanking(Context context) {

        sharedPreferences = context.getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i <ScoreList_finalize.size(); i++) {
            ranking[i].setVisibility(View.VISIBLE);
            rankingName[i].setText("" + NameList_finalize.get(i));
            rankingPoint[i].setText("" + ScoreList_finalize.get(i));

            editor.putString("ranking" + (i+1) + "Name", NameList_finalize.get(i));
            editor.putInt("ranking" + (i+1) + "Score", ScoreList_finalize.get(i));
        }
        editor.commit();
    }
}