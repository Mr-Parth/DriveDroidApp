package com.Drive_Droid.androidfacedetection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float PROB_TEXT_SIZE = 50.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;
    private Paint mProbPaint;
    private Paint mProbRect;
    private Paint mProbDRect;

    Context context;

    private volatile Face mFace;
    private int mFaceId;



    public static String eyeProb= String.valueOf(0.0) ;

    String defaultPath = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();








    FaceGraphic(GraphicOverlay overlay, Context check) {
        super(overlay);

        this.context = check;

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);

        mProbPaint = new Paint();
        mProbPaint.setColor(Color.WHITE);
        mProbPaint.setTextSize(PROB_TEXT_SIZE);

        mProbRect = new Paint();
        mProbRect.setColor(Color.BLACK);
        mProbRect.setAlpha(190);

        mProbDRect = new Paint();
        mProbDRect.setColor(Color.RED);
        mProbDRect.setAlpha(190);




    }

    void setId(int id) {
        mFaceId = id;
    }

    public String giveProb(){
        return eyeProb;
    }

    void updateFace(Face face) {
        mFace = face;
        postInvalidate();

    }


    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        float he = (face.getIsLeftEyeOpenProbability() + face.getIsLeftEyeOpenProbability())/2;

        String haye_eye= String.format("%.4f",(face.getIsLeftEyeOpenProbability() + face.getIsLeftEyeOpenProbability())/2!=-1.0?(face.getIsLeftEyeOpenProbability() + face.getIsLeftEyeOpenProbability())/2:1.0);




        eyeProb = haye_eye;

        if(he<0.2 && he!= -1.0){

            canvas.drawRect(0,canvas.getHeight()-200,canvas.getWidth(),canvas.getHeight(),mProbDRect);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();}

        else {

            canvas.drawRect(0,canvas.getHeight()-200,canvas.getWidth(),canvas.getHeight(),mProbRect);

        }

        canvas.drawText( "Eye_Prob", canvas.getWidth()/4-15, canvas.getHeight()-120, mProbPaint);
        canvas.drawText( haye_eye, canvas.getWidth()/4, canvas.getHeight()-50, mProbPaint);













        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);

        // Draws a circle for each face feature detected

    }
}
