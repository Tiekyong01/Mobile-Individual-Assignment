package my.edu.utar.individual;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Keep the screen open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startGame(View view) {
        GamePattern gamePattern = new GamePattern(this);
        setContentView(gamePattern);
    }

    public void quitGame(View view) {
        ((Activity) this).finish();
    }
}