package com.example.stan.movingaverage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.*;

import static android.R.id.list;


public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{
    SurfaceView cameraDetectorSurfaceView;
    private LinearLayout my_points;
    private CameraDetector cameraDetector;
    private TextView textViewCongrats;
    private int maxProcessingRate = 10;
    private final float threshold = 60;
    private final int numberOfPoint = 15;
    private boolean isFull = false;
    private List<Float> pointsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCongrats = (TextView) findViewById(R.id.textViewCongrats);
//        textViewMean = (TextView) findViewById(R.id.textViewMean);

        cameraDetectorSurfaceView=(SurfaceView)findViewById(R.id.cameraDetectorSurfaceView);
        cameraDetector = new CameraDetector(this,CameraDetector.CameraType.CAMERA_FRONT,cameraDetectorSurfaceView);

        cameraDetector.setMaxProcessRate(maxProcessingRate);
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);

        cameraDetector.setDetectAllEmotions(true);
        cameraDetector.start();

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

        float mean;
        float sum = 0;
        String text = "You have reached high-level joy";
        // check if the frame was processed.
        if(faces == null)
            return;//frame was not processed.

        // check if there is any face recognized.
        if (faces.size() == 0) {
            return;
        }
        Face face = faces.get(0);
        float joy = face.emotions.getJoy();

        pointsList.add(joy);

        if(pointsList.size() == numberOfPoint){
            for(Object i : pointsList){
                sum += (float)i;
            }
            mean = sum/numberOfPoint;
            if(mean > threshold) {
                textViewCongrats.setText(text);
                System.out.println(text);
            } else {
                textViewCongrats.setText("");
            }
            pointsList.remove(0);
        }

        System.out.println("Joy: " + joy);

    }

}
