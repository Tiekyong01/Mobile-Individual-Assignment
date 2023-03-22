package my.edu.utar.individual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Random;

public class Circle {
    Bitmap circle[] = new Bitmap[3];
    int circleFrame = 0;
    int circleX, circleY;
    boolean light = false;
    Random random;

    // Constructor for circle class
    public Circle (Context context) {
        circle[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        circle[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        circle[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        random = new Random();
        appear();
    }

    public Bitmap getCircle (int circleFrame) {

        return circle[circleFrame];
    }

    public int getCircleWidth () {
        int Highest = 0;
        for (int i = 0; i < 3; i++) {
            if (circle[i].getWidth() > Highest)
                Highest = circle[i].getWidth();
        }
        return Highest;
    }

    public int getCircleHeight () {
        int Highest = 0;
        for (int i = 0; i < 3; i++) {
            if (circle[i].getHeight() > Highest)
                Highest = circle[i].getHeight();
        }
        return Highest;
    }

    //set the red circle
    public void highlight (Context context) {
        circle[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcircle);
        circle[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcircle);
        circle[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcircle);
        light = true;
    }

    //set the yellow circle
    public void unhighlight (Context context) {
        circle[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        circle[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        circle[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcircle);
        light = false;
    }

    //let red and yellow circle appear in the game
    public void appear () {
        circleX = random.nextInt(GamePattern.dWidth - getCircleWidth());
        circleY = random.nextInt(GamePattern.dHeight - getCircleHeight() - 300) + 300;
    }
}