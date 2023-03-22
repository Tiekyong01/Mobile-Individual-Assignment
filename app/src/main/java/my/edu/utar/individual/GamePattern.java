package my.edu.utar.individual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePattern extends View {
    private Object FragmentManager;

    static enum GameState {
        Running, Paused, Resume;
    }

    static GamePattern.GameState state = GamePattern.GameState.Running;

    Bitmap background;
    Rect rectBackground;
    Rect button;
    static Context context;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 250;
    Paint scoresRecord = new Paint();
    Paint currentStage = new Paint();
    Paint remainingTime = new Paint();
    Paint paint = new Paint();
    float TEXT_SIZE = 100;
    int scores = 0;
    int stage = 1;
    double count = 1;
    int remaining = 5;
    Boolean duplicate = false;
    static int dWidth, dHeight;
    ArrayList<Circle> circles;
    int countdown[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private AtomicBoolean paused;

    // Constructor matching super
    @RequiresApi(api = Build.VERSION_CODES.O)
    public GamePattern (Context context) {
        super(context);
        this.context = context;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background03);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        paused = new AtomicBoolean(false);

        // set the x and y values
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;


        //background design
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        button = new Rect(dWidth-350, 120, dWidth-30, 250);

        // handler and runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                count += 0.25;

                // count the remaining second
                for (int i = 0; i < countdown.length; i++) {
                    if (count == countdown[i]) {
                        remaining--;
                        invalidate();
                    }
                }
                if (count == 25 + 6) {
                    Intent intent = new Intent(context, GameResult.class);
                    intent.putExtra("points", scores);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                else if(count == 5 + 2
                        || count == 10 + 3 || count == 15 + 4 || count == 20 + 5) {
                    stage++;
                    remaining = 5;
                    upLevel(stage);
                }
                invalidate();
            }
        };


        // set the text paint
        scoresRecord.setColor(Color.rgb(255, 225, 53));
        scoresRecord.setTextSize(20);
        scoresRecord.setTextAlign(Paint.Align.LEFT);
        scoresRecord.setTypeface(ResourcesCompat.getFont(context, R.font.shortbaby_mg2w));
        currentStage.setColor(Color.rgb(255, 225, 53));
        currentStage.setTextSize(20 + 10);
        currentStage.setTextAlign(Paint.Align.LEFT);
        currentStage.setTypeface(ResourcesCompat.getFont(context, R.font.shortbaby_mg2w));
        remainingTime.setColor(Color.rgb(255, 26, 0));
        remainingTime.setTextSize(20 * 3);
        remainingTime.setTextAlign(Paint.Align.CENTER);
        remainingTime.setTypeface(ResourcesCompat.getFont(context, R.font.shortbaby_mg2w));
        paint.setColor(Color.parseColor("#0000FF"));
        circles = new ArrayList<>();
        upLevel(stage);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);

        // generate the red cicle randomly
        for (int i = 0; i < circles.size() ; i++) {
            canvas.drawBitmap(circles.get(i).getCircle(circles.get(i).circleFrame),
                    circles.get(i).circleX, circles.get(i).circleY, null);
            circles.get(i).circleFrame++;

            if(circles.get(i).circleFrame > 2) {
                circles.get(i).circleFrame = 0; }
        }

        // draw the text
        canvas.drawRect(0, 0, dWidth,300, paint);
        canvas.drawText("Points: " + scores, 10, TEXT_SIZE + 10, scoresRecord);
        canvas.drawText("Stage: " + stage, 10, (TEXT_SIZE + 10) * 2, currentStage);
        canvas.drawText("" + remaining, dWidth/2, (TEXT_SIZE + 20) * 2 , remainingTime);

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        // consider when touch the orb, user will get the point
        for (int i = 0; i < circles.size() ; i++) {
            if (circles.get(i).circleX <= touchX
                    && circles.get(i).circleX + circles.get(i).getCircleWidth() >= touchX
                    && circles.get(i).circleY <= touchY
                    && circles.get(i).circleY + circles.get(i).getCircleHeight() >= touchY
                    && circles.get(i).light == true) {



                // highlight nextItem
                circles.get(i).unhighlight(context);
                if (i + 1 != circles.size()) {
                    circles.get(i + 1).highlight(context);
                } else {
                    circles.get(0).highlight(context);
                }
                // get the points
                scores = scores + 1;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (button.contains((int)touchX, (int)touchY)) {

            }
        }


        return true;
    }


    public void upLevel (int currentStage) {
        // remain the unselected highlight orb
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).light == false)
                circles.remove(i);}

        switch (currentStage)
        {
            case 1: {
                for (int i = 0; i < 4; i++) {
                    Circle circle = new Circle(context);
                    circles.add(circle); }
                circles.get(0).highlight(context);
            }
            break;
            case 2: {
                for (int i = 0; i < 8; i++) {
                    Circle circle = new Circle(context);
                    circles.add(circle); }
            }
            break;
            case 3: {
                for (int i = 0; i < 15; i++) {
                    Circle circle = new Circle(context);
                    circles.add(circle); }
            }
            break;
            case 4: {
                for (int i = 0; i < 24; i++) {
                    Circle circle = new Circle(context);
                    circles.add(circle); }
            }
            break;
            case 5: {
                for (int i = 0; i < 35; i++) {
                    Circle circle = new Circle(context);
                    circles.add(circle); }
            }
            break;
            default: {
            }
        }
    }

    public static void update () {
        if (state == GameState.Resume) { }
        if (state == GameState.Paused) { }
    }
}


