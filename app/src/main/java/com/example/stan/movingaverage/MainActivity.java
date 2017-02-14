package com.example.stan.movingaverage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{
    SurfaceView cameraDetectorSurfaceView;
//    HorizontalScrollView scrollWindow;
    LinearLayout my_points;
    CameraDetector cameraDetector;
    TextView textViewCongrats;
    TextView textViewMean;
    int maxProcessingRate = 10;
    float threshold = 75;
    boolean isFull = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCongrats = (TextView) findViewById(R.id.textViewCongrats);
        textViewMean = (TextView) findViewById(R.id.textViewMean);

        cameraDetectorSurfaceView=(SurfaceView)findViewById(R.id.cameraDetectorSurfaceView);
        cameraDetector = new CameraDetector(this,CameraDetector.CameraType.CAMERA_FRONT,cameraDetectorSurfaceView);

        cameraDetector.setMaxProcessRate(maxProcessingRate);
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);

        cameraDetector.setDetectAllEmotions(true);
        cameraDetector.start();

        my_points = (LinearLayout)findViewById(R.id.my_points);

    }

    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotate) {
        // Grab Layout Paras from the Surface View.
        ViewGroup.LayoutParams paras = cameraDetectorSurfaceView.getLayoutParams();

        // Change height and width given by Affective SDK.
        paras.height = cameraHeight;
        paras.width = cameraWidth;

        // Set it with new sizing.
        cameraDetectorSurfaceView.setLayoutParams(paras);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        // check if the frame was processed.
        if(faces == null)
            return;//frame was not processed.
        if (faces.size() == 0)
            textViewCongrats.setText("NO FACE");
        // check if there is any face recognized.
//        if(faces.size() == 0)
//            return;//no face
        Face face = faces.get(0);
//        System.out.println("time"+timeStamp);
//        System.out.println("gender: "+genderValue);
        float joy = face.emotions.getJoy();
        if(joy > threshold)
            textViewCongrats.setText("You reached high-level joy");
//        float anger = face.emotions.getAnger();
//        float surprise = face.emotions.getSurprise();

        System.out.println("Joy: " + joy);
//        System.out.println("Anger: " + anger);
//        System.out.println("Surprise: " + surprise);
    }

}
